package com.app.vasyBus.controller;

import com.app.vasyBus.dtos.AuthResponseDTO;
import com.app.vasyBus.dtos.LoginRequestDTO;
import com.app.vasyBus.dtos.RegisterRequestDTO;
import com.app.vasyBus.model.ApiResponse;
import com.app.vasyBus.security.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> userLogin(@RequestBody @Valid LoginRequestDTO request){
        return ResponseEntity.ok(new ApiResponse<>(true,"success" ,authService.login(request)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> userRegister(@RequestBody @Valid RegisterRequestDTO request){
        return ResponseEntity.ok(new ApiResponse<>(true ,"success" , authService.register(request)));
    }
}
