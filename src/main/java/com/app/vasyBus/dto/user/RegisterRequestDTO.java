package com.app.vasyBus.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    @NotBlank(message = "Name can't be null")
    @Size(min = 2, max = 30)
    @JsonProperty("name")
    private String name;

    @Email(message = "Enter valid email")
    @NotBlank(message = "Email is required")
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "Password can't be null")
    @Size(min = 8, message = "Password minimum length is 8")
    @JsonProperty("password")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 10)
    @Digits(integer = 10, fraction = 0)
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must start with 6,7,8,9 and be exactly 10 digits")
    @JsonProperty("phone")
    private String phone;

    @Min(value = 10, message = "User minimum age should be 10 years")
    @JsonProperty("age")
    private Integer age;
}
