package com.ecommerce_event_management.controller;

import com.ecommerce_event_management.common.dto.ApiResponse;
import com.ecommerce_event_management.common.dto.CommonFilterDTO;
import com.ecommerce_event_management.common.dto.PageResponse;
import com.ecommerce_event_management.dto.request.PermissionRequestDTO;
import com.ecommerce_event_management.dto.response.PermissionResponseDTO;
import com.ecommerce_event_management.service.UserMenuPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Menu Permission Master", description = "User Menu Permission management APIs (CRUD, soft/hard delete, dynamic search & pagination)")
@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
public class UserMenuPermissionController {

    private final UserMenuPermissionService permissionService;

    // 1. GET (List with SearchTerm, Id, Pagination, PageSize=-1 for all, SortBy, SortOrder)
    @Operation(summary = "Get list of permissions with filtering, pagination, and sorting (PageSize=-1 returns all)")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PermissionResponseDTO>>> getPermissions(
            @ModelAttribute CommonFilterDTO filter) {
        PageResponse<PermissionResponseDTO> permissions = permissionService.getPermissions(filter);
        return ResponseEntity.ok(ApiResponse.success(permissions));
    }

    // 1.1 GET by ID
    @Operation(summary = "Get permission by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionResponseDTO>> getPermissionById(@PathVariable Long id) {
        PermissionResponseDTO permission = permissionService.getPermissionById(id);
        return ResponseEntity.ok(ApiResponse.success(permission));
    }

    // 2. POST (Create)
    @Operation(summary = "Assign permission to user for a menu")
    @PostMapping
    public ResponseEntity<ApiResponse<PermissionResponseDTO>> createPermission(
            @Valid @RequestBody PermissionRequestDTO request) {
        PermissionResponseDTO permission = permissionService.createPermission(request);
        return new ResponseEntity<>(ApiResponse.created(permission, "Permission assigned successfully"), HttpStatus.CREATED);
    }

    // 3. PUT (Update)
    @Operation(summary = "Update an existing user menu permission")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionResponseDTO>> updatePermission(
            @PathVariable Long id,
            @Valid @RequestBody PermissionRequestDTO request) {
        PermissionResponseDTO permission = permissionService.updatePermission(id, request);
        return ResponseEntity.ok(ApiResponse.success(permission, "Permission updated successfully"));
    }

    // 4. DELETE (Soft delete by default, supports ?hard=true as query param)
    @Operation(summary = "Delete permission (Soft delete by default, or hard delete if ?hard=true)")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePermission(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean hard) {
        if (hard) {
            permissionService.hardDeletePermission(id);
            return ResponseEntity.ok(ApiResponse.success(null, "Permission permanently deleted (hard delete)"));
        } else {
            permissionService.softDeletePermission(id);
            return ResponseEntity.ok(ApiResponse.success(null, "Permission deactivated successfully (soft delete)"));
        }
    }

    // 5. DELETE (Dedicated Hard Delete endpoint)
    @Operation(summary = "Permanently hard delete a permission")
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<ApiResponse<Void>> hardDeletePermission(@PathVariable Long id) {
        permissionService.hardDeletePermission(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Permission permanently deleted (hard delete)"));
    }
}
