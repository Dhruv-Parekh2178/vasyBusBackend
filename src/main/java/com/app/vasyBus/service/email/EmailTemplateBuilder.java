package com.app.vasyBus.service.email;

import com.app.vasyBus.kafka.event.BookingCancelledEvent;
import com.app.vasyBus.kafka.event.BookingCreatedEvent;
import com.app.vasyBus.kafka.event.PaymentSuccessEvent;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmailTemplateBuilder {

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("hh:mm a").withZone(ZoneId.of("Asia/Kolkata"));

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("EEE, dd MM yyyy").withZone(ZoneId.of("Asia/Kolkata"));

    public static String bookingCreated(BookingCreatedEvent e) {

        String passengerRows = buildPassengerRows(e.getPassengers());

        String body = "<p>Your booking has been created successfully.</p>";
        body += "<p>Please complete the payment to confirm your seat.</p>";

        body += infoCard(
                e.getBookingId(),
                e.getBusName(),
                e.getBusType(),
                e.getSourceCity(),
                e.getDestinationCity(),
                TIME_FMT.format(e.getDepartureTime()),
                TIME_FMT.format(e.getArrivalTime()),
                DATE_FMT.format(e.getTravelDate().atStartOfDay(ZoneId.of("Asia/Kolkata"))),
                "₹" + e.getTotalAmount().toPlainString(),
                "PENDING"
        );

        body += passengerRows;
        body += pendingPaymentNote();

        return wrap("Booking Confirmed — Pending Payment", e.getUserName(), body);
    }

    public static String paymentSuccess(PaymentSuccessEvent e) {

        String passengerRows = buildPassengerRows(e.getPassengers());

        String body = "<p>Payment received successfully.</p>";
        body += "<p>Your booking is confirmed.</p>";

        body += infoCard(
                e.getBookingId(),
                e.getBusName(),
                e.getBusType(),
                e.getSourceCity(),
                e.getDestinationCity(),
                TIME_FMT.format(e.getDepartureTime()),
                TIME_FMT.format(e.getArrivalTime()),
                DATE_FMT.format(e.getTravelDate().atStartOfDay(ZoneId.of("Asia/Kolkata"))),
                "₹" + e.getAmountPaid().toPlainString(),
                "CONFIRMED"
        );

        body += passengerRows;
        body += paymentInfo(e.getStripePaymentId(), e.getCurrency());

        return wrap("Booking Confirmed", e.getUserName(), body);
    }

    public static String bookingCancelled(BookingCancelledEvent e) {

        String cancelledByLabel;

        if ("ADMIN".equals(e.getCancelledBy())) {
            cancelledByLabel = "Admin";
        } else {
            cancelledByLabel = "You";
        }

        String body = "<p>Your booking has been cancelled.</p>";
        body += "<p>Cancelled by: <strong>" + cancelledByLabel + "</strong></p>";

        body += "<p>Reason: " + e.getCancellationReason() + "</p>";

        body += infoCard(
                e.getBookingId(),
                e.getBusName(),
                "",
                e.getSourceCity(),
                e.getDestinationCity(),
                TIME_FMT.format(e.getDepartureTime()),
                "",
                DATE_FMT.format(e.getTravelDate().atStartOfDay(ZoneId.of("Asia/Kolkata"))),
                "₹" + e.getTotalAmount().toPlainString(),
                "CANCELLED"
        );

        body += refundNote();

        return wrap("Booking Cancelled", e.getUserName(), body);
    }

    private static String buildPassengerRows(List<BookingCreatedEvent.PassengerInfo> passengers) {

        if (passengers == null || passengers.isEmpty()) {
            return "";
        }

        StringBuilder rows = new StringBuilder();

        rows.append("<h3>Passengers</h3>");
        rows.append("<table border='1' cellpadding='5'>");

        rows.append("<tr>");
        rows.append("<th>#</th>");
        rows.append("<th>Name</th>");
        rows.append("<th>Age</th>");
        rows.append("<th>Gender</th>");
        rows.append("<th>Seat</th>");
        rows.append("<th>Type</th>");
        rows.append("</tr>");

        int i = 1;

        for (BookingCreatedEvent.PassengerInfo p : passengers) {

            rows.append("<tr>");

            rows.append("<td>").append(i++).append("</td>");
            rows.append("<td>").append(p.getPassengerName()).append("</td>");
            rows.append("<td>").append(p.getPassengerAge()).append("</td>");
            rows.append("<td>").append(p.getPassengerGender()).append("</td>");
            rows.append("<td>").append(p.getSeatNumber()).append("</td>");
            rows.append("<td>").append(p.getSeatType()).append("</td>");

            rows.append("</tr>");
        }

        rows.append("</table>");

        return rows.toString();
    }

    private static String infoCard(Long bookingId, String busName, String busType,
                                   String from, String to,
                                   String depTime, String arrTime,
                                   String date, String amount,
                                   String status) {

        String statusColor;

        if ("CONFIRMED".equals(status)) {
            statusColor = "green";
        } else if ("CANCELLED".equals(status)) {
            statusColor = "red";
        } else {
            statusColor = "orange";
        }

        String card = "";

        card += "<h3>Booking Details</h3>";
        card += "<p><b>Booking ID:</b> #" + bookingId + "</p>";
        card += "<p><b>Bus:</b> " + busName + " " + busType + "</p>";
        card += "<p><b>Route:</b> " + from + " → " + to + "</p>";
        card += "<p><b>Date:</b> " + date + "</p>";
        card += "<p><b>Departure:</b> " + depTime + "</p>";
        card += "<p><b>Amount:</b> " + amount + "</p>";
        card += "<p><b>Status:</b> <span style='color:" + statusColor + "'>" + status + "</span></p>";

        return card;
    }
    private static String pendingPaymentNote() {

        return "<p><b>Note:</b> Seats are reserved for 10 minutes. Please complete payment.</p>";
    }

    private static String paymentInfo(String paymentId, String currency) {

        String info = "";

        info += "<p>Payment ID: " + paymentId + "</p>";
        info += "<p>Currency: " + currency + "</p>";
        info += "<p>Please arrive 15 minutes before departure.</p>";

        return info;
    }

    private static String refundNote() {

        return "<p>If you paid for this booking, refund will be processed as per policy.</p>";
    }


    private static String wrap(String title, String userName, String body) {

        String html = "";

        html += "<html>";
        html += "<body>";

        html += "<h2>VasyBus</h2>";

        html += "<h3>" + title + "</h3>";

        html += "<p>Hi " + userName + ",</p>";

        html += body;

        html += "<hr>";
        html += "<p>This is an automated email from VasyBus.</p>";

        html += "</body>";
        html += "</html>";

        return html;
    }

}
