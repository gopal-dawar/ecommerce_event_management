package com.ecommerce_event_management.controller;

import com.ecommerce_event_management.common.dto.ApiResponse;
import com.ecommerce_event_management.common.dto.CommonFilterDTO;
import com.ecommerce_event_management.common.dto.PageResponse;
import com.ecommerce_event_management.dto.request.PasswordChangeRequestDTO;
import com.ecommerce_event_management.dto.request.UserCreateRequestDTO;
import com.ecommerce_event_management.dto.request.UserUpdateRequestDTO;
import com.ecommerce_event_management.dto.response.UserResponseDTO;
import com.ecommerce_event_management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserMasterController {

    private final UserService userService;

    // 1. GET (List with SearchTerm, Id, Pagination, PageSize=-1 for all, SortBy, SortOrder)
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserResponseDTO>>> getUsers(
            @ModelAttribute CommonFilterDTO filter) {
        PageResponse<UserResponseDTO> users = userService.getUsers(filter);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    // 1.1 GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    // 2. POST (Create)
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(
            @Valid @RequestBody UserCreateRequestDTO request) {
        UserResponseDTO user = userService.createUser(request);
        return new ResponseEntity<>(ApiResponse.created(user, "User created successfully"), HttpStatus.CREATED);
    }

    // 3. PUT (Update)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequestDTO request) {
        UserResponseDTO user = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success(user, "User updated successfully"));
    }

    // 3.1 PATCH (Change Password)
    @PatchMapping("/{id}/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody PasswordChangeRequestDTO request) {
        userService.changePassword(id, request);
        return ResponseEntity.ok(ApiResponse.success(null, "Password changed successfully"));
    }

    // 4. DELETE (Soft delete by default, supports ?hard=true as query param)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean hard) {
        if (hard) {
            userService.hardDeleteUser(id);
            return ResponseEntity.ok(ApiResponse.success(null, "User permanently deleted (hard delete)"));
        } else {
            userService.softDeleteUser(id);
            return ResponseEntity.ok(ApiResponse.success(null, "User deactivated successfully (soft delete)"));
        }
    }

    // 5. DELETE (Dedicated Hard Delete endpoint)
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<ApiResponse<Void>> hardDeleteUser(@PathVariable Long id) {
        userService.hardDeleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User permanently deleted (hard delete)"));
    }
}
