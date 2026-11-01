package com.ecommerce_event_management.controller;

import com.ecommerce_event_management.common.dto.ApiResponse;
import com.ecommerce_event_management.dto.response.MenuTreeResponseDTO;
import com.ecommerce_event_management.service.NavigationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/navigation")
@RequiredArgsConstructor
public class NavigationController {

    private final NavigationService navigationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<MenuTreeResponseDTO>>> getUserNavigation(
            @PathVariable Long userId) {
        List<MenuTreeResponseDTO> navigationTree = navigationService.getUserNavigationTree(userId);
        return ResponseEntity.ok(ApiResponse.success(navigationTree));
    }
}

