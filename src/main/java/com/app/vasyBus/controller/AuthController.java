package com.app.vasyBus.controller;

import com.app.vasyBus.dto.auth.AuthResponseDTO;
import com.app.vasyBus.dto.user.LoginRequestDTO;
import com.app.vasyBus.dto.user.RegisterRequestDTO;
import com.app.vasyBus.utils.ApiResponse;
import com.app.vasyBus.config.security.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
//@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> userLogin(@RequestBody @Valid LoginRequestDTO request){
        return ResponseEntity.ok(ApiResponse.success(authService.login(request)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> userRegister(@RequestBody @Valid RegisterRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(authService.register(request)));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().body(ApiResponse.success( "Logged out successfully"));
    }
}
