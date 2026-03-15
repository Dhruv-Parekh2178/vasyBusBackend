package com.app.vasyBus.service.payment;

import com.app.vasyBus.config.StripeConfig;
import com.app.vasyBus.dto.payment.PaymentIntentResponse;
import com.app.vasyBus.dto.payment.PaymentRequestDTO;
import com.app.vasyBus.enums.BookingStatus;
import com.app.vasyBus.enums.CurrencyType;
import com.app.vasyBus.enums.PaymentStatus;
import com.app.vasyBus.exception.ResourceNotFoundException;
import com.app.vasyBus.model.Booking;
import com.app.vasyBus.model.Payment;
import com.app.vasyBus.repository.BookingRepository;
import com.app.vasyBus.repository.PaymentRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final StripeConfig stripeConfig;

    @Override
    @Transactional
    public PaymentIntentResponse createPaymentIntent(
            PaymentRequestDTO paymentRequestDTO, Long userId) {

        Booking booking = bookingRepository.findById(paymentRequestDTO.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking not found with id: " + paymentRequestDTO.getBookingId()));

        if (booking.isDeleted()) {
            throw new ResourceNotFoundException(
                    "Booking not found with id: " + paymentRequestDTO.getBookingId());
        }

        if (!booking.getUser().getUserId().equals(userId)) {
            throw new IllegalStateException(
                    "You are not authorized to pay for this booking");
        }

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Cannot pay for a cancelled booking");
        }

        if (booking.getPaymentStatus() == PaymentStatus.SUCCESS) {
            throw new IllegalStateException(
                    "This booking is already paid");
        }

        long amountInPaise = booking.getTotalAmount()
                .multiply(java.math.BigDecimal.valueOf(100))
                .longValue();

        com.app.vasyBus.dto.payment.PaymentResponseDTO existingPayment =
                paymentRepository.findPaymentByBookingId(booking.getBookingId());

        if (existingPayment != null
                && existingPayment.getStripePaymentId() != null
                && "PENDING".equals(existingPayment.getPaymentStatus())) {
            try {
                PaymentIntent existingIntent =
                        PaymentIntent.retrieve(existingPayment.getStripePaymentId());
                log.info("Returning existing PaymentIntent {} for booking {}",
                        existingIntent.getId(), booking.getBookingId());
                return new PaymentIntentResponse(
                        existingIntent.getClientSecret(),
                        existingIntent.getId(),
                        booking.getBookingId(),
                        amountInPaise,
                        "INR"
                );
            } catch (Exception e) {
                log.warn("Could not retrieve existing PaymentIntent, creating new one: {}",
                        e.getMessage());
            }
        }

        try {
            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(amountInPaise)
                            .setCurrency("inr")
                            .putMetadata("bookingId",
                                    booking.getBookingId().toString())
                            .putMetadata("userId", userId.toString())
                            .setAutomaticPaymentMethods(
                                    PaymentIntentCreateParams
                                            .AutomaticPaymentMethods
                                            .builder()
                                            .setEnabled(true)
                                            .setAllowRedirects(
                                                    PaymentIntentCreateParams
                                                            .AutomaticPaymentMethods
                                                            .AllowRedirects.NEVER)
                                            .build()
                            )
                            .build();

            PaymentIntent intent = PaymentIntent.create(params);

            Payment payment = Payment.builder()
                    .booking(booking)
                    .stripePaymentId(intent.getId())
                    .amount(booking.getTotalAmount())
                    .currency(CurrencyType.INR)
                    .paymentStatus(PaymentStatus.PENDING)
                    .build();

            paymentRepository.save(payment);

            return new PaymentIntentResponse(
                    intent.getClientSecret(),
                    intent.getId(),
                    booking.getBookingId(),
                    amountInPaise,
                    "INR"
            );

        } catch (Exception e) {
            log.error("Stripe PaymentIntent creation failed: {}", e.getMessage());
            throw new IllegalStateException(
                    "Payment processing failed. Please try again.");
        }
    }

    @Override
    @Transactional
    public String handleWebhook(String payload, String sigHeader) {

        Event event;
        try {
            event = Webhook.constructEvent(
                    payload, sigHeader, stripeConfig.getWebhookSecret());
        } catch (SignatureVerificationException e) {
            log.error("Webhook signature verification failed: {}", e.getMessage());
            throw new IllegalStateException("Invalid webhook signature");
        }

        log.info("Received Stripe event: {}", event.getType());

        switch (event.getType()) {
            case "payment_intent.succeeded" -> {
                try {
                    PaymentIntent intent = (PaymentIntent)
                            event.getDataObjectDeserializer().deserializeUnsafe();
                    handlePaymentSuccess(intent);
                } catch (Exception e) {
                    log.error("Failed to process payment_intent.succeeded: {}", e.getMessage());
                }
            }
            case "payment_intent.payment_failed" -> {
                try {
                    PaymentIntent intent = (PaymentIntent)
                            event.getDataObjectDeserializer().deserializeUnsafe();
                    handlePaymentFailed(intent);
                } catch (Exception e) {
                    log.error("Failed to process payment_intent.payment_failed: {}", e.getMessage());
                }
            }
            default -> log.info("Unhandled Stripe event: {}", event.getType());
        }

        return "Webhook processed";
    }

    private void handlePaymentSuccess(PaymentIntent intent) {
        String stripePaymentId = intent.getId();
        paymentRepository.updatePaymentStatus(stripePaymentId, PaymentStatus.SUCCESS.name());
        paymentRepository.findByStripePaymentId(stripePaymentId)
                .ifPresent(payment -> {
                    bookingRepository.updateBookingStatuses(
                            payment.getBooking().getBookingId(),
                            PaymentStatus.SUCCESS.name(),
                            BookingStatus.CONFIRMED.name()
                    );
                    log.info("Booking {} confirmed after payment success",
                            payment.getBooking().getBookingId());
                });
    }

    private void handlePaymentFailed(PaymentIntent intent) {
        String stripePaymentId = intent.getId();
        paymentRepository.updatePaymentStatus(stripePaymentId, PaymentStatus.FAILED.name());
        log.warn("Payment failed for Stripe intent: {}", stripePaymentId);
    }
}