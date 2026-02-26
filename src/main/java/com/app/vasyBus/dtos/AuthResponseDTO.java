package com.app.vasyBus.dtos;

import com.app.vasyBus.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String name;
    private String email;
    private String role;


}
