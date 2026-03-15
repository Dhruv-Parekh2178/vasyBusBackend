package com.app.vasyBus.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateProfileRequestDTO {

    @NotBlank(message = "Name can't be blank")
    @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 10, message = "Phone number must be exactly 10 digits")
    @Digits(integer = 10, fraction = 0, message = "Phone must be numeric")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone must start with 6,7,8,9 and be 10 digits")
    @JsonProperty("phone")
    private String phone;
}