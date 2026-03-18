package com.app.vasyBus.controller.admin;

public interface UserManagementDTO {
    Long   getUserId();
    String getName();
    String getEmail();
    String getPhone();
    Integer getAge();
    String getRole();
    Long   getTotalBookings();
    Long   getConfirmedBookings();
    Long   getCancelledBookings();
    java.time.Instant getCreatedAt();
}