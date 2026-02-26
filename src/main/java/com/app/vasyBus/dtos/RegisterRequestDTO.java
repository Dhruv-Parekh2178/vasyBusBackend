package com.app.vasyBus.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    @NotBlank(message = "Name can't be null")
    @Size(min = 2, max = 30)
    private String name;

    @Email(message = "Enter valid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password can't be null")
    @Size(min = 8, message = "Password minimum length is 8")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 10)
    @Digits(integer = 10, fraction = 0)
    private String phone;

    @Min(value = 10, message = "User minimum age should be 10 years")
    private Integer age;
}
