package com.app.vasyBus.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @Email(message = "Enter valid email")
    @NotBlank(message = "Email is required")
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "Password can't be null")
    @Size(min = 8, message = "Password minimum length is 8")
    @JsonProperty("password")
    private String password;
}
