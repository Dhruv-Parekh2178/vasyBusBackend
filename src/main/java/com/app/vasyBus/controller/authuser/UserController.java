package com.app.vasyBus.controller.authuser;

import com.app.vasyBus.config.security.JwtUtil;
import com.app.vasyBus.dto.user.ProfileResponseDTO;
import com.app.vasyBus.dto.user.UpdateProfileRequestDTO;
import com.app.vasyBus.exception.ResourceNotFoundException;
import com.app.vasyBus.repository.UserRepository;
import com.app.vasyBus.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> getProfile(HttpServletRequest request) {
        Long userId = extractUserId(request);
        ProfileResponseDTO profile = userRepository.findProfileByUserId(userId);
        if (profile == null) throw new ResourceNotFoundException("User not found");
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> updateProfile(
            @Valid @RequestBody UpdateProfileRequestDTO dto,
            HttpServletRequest request) {

        Long userId = extractUserId(request);


        if (userRepository.existsByPhoneAndNotUserId(dto.getPhone(), userId)) {
            throw new IllegalStateException("Phone number already in use by another account");
        }

        userRepository.updateProfile(userId, dto.getName().trim(), dto.getPhone());

        ProfileResponseDTO updated = userRepository.findProfileByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    private Long extractUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return jwtUtil.extractUserId(token);
    }
}