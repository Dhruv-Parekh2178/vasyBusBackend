package com.app.vasyBus.dto.user;

public interface ProfileResponseDTO {
    Long getUserId();
    String getName();
    String getEmail();
    String getPhone();
    Integer getAge();
    String getRole();
    java.time.Instant getCreatedAt();
}