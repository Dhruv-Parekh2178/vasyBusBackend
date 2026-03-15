package com.app.vasyBus.controller.publicuser;

import com.app.vasyBus.config.security.JwtUtil;
import com.app.vasyBus.dto.payment.PaymentIntentResponse;
import com.app.vasyBus.dto.payment.PaymentRequestDTO;
import com.app.vasyBus.service.payment.PaymentService;
import com.app.vasyBus.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create-intent")
    public ResponseEntity<ApiResponse<PaymentIntentResponse>>
            createPaymentIntent(
                    @Valid @RequestBody PaymentRequestDTO paymentRequestDTO,
                    HttpServletRequest request) {

        Long userId = extractUserId(request);
        PaymentIntentResponse response =
                paymentService.createPaymentIntent(paymentRequestDTO, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            HttpServletRequest request,
            @RequestHeader("Stripe-Signature") String sigHeader)
            throws IOException {

        byte[] bodyBytes = request.getInputStream().readAllBytes();
        String payload = new String(bodyBytes, StandardCharsets.UTF_8);

        String result = paymentService.handleWebhook(payload, sigHeader);
        return ResponseEntity.ok(result);
    }

    private Long extractUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return jwtUtil.extractUserId(token);
    }
}