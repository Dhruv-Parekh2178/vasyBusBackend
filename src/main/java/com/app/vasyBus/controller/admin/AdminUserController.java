package com.app.vasyBus.controller.admin;

import com.app.vasyBus.dto.admin.UserManagementDTO;
import com.app.vasyBus.repository.UserRepository;
import com.app.vasyBus.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminUserController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserManagementDTO>>> getAllUsers() {
        List<UserManagementDTO> users = userRepository.findAllUsersWithStats();
        return ResponseEntity.ok(ApiResponse.success(users));
    }
}