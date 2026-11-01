package com.ecommerce_event_management.controller;

import com.ecommerce_event_management.common.dto.ApiResponse;
import com.ecommerce_event_management.dto.request.LoginRequestDTO;
import com.ecommerce_event_management.dto.response.AuthResponseDTO;
import com.ecommerce_event_management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO authResponse = userService.authenticate(request);
        return ResponseEntity.ok(ApiResponse.success(authResponse, "User logged in successfully"));
    }
}

