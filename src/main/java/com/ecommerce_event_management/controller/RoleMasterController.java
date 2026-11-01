package com.ecommerce_event_management.controller;

import com.ecommerce_event_management.common.dto.ApiResponse;
import com.ecommerce_event_management.common.dto.CommonFilterDTO;
import com.ecommerce_event_management.common.dto.PageResponse;
import com.ecommerce_event_management.dto.request.RoleRequestDTO;
import com.ecommerce_event_management.dto.response.RoleResponseDTO;
import com.ecommerce_event_management.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleMasterController {

    private final RoleService roleService;

    // 1. GET (List with SearchTerm, Id, Pagination, PageSize=-1 for all, SortBy, SortOrder)
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<RoleResponseDTO>>> getRoles(
            @ModelAttribute CommonFilterDTO filter) {
        PageResponse<RoleResponseDTO> roles = roleService.getRoles(filter);
        return ResponseEntity.ok(ApiResponse.success(roles));
    }

    // 1.1 GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponseDTO>> getRoleById(@PathVariable Long id) {
        RoleResponseDTO role = roleService.getRoleById(id);
        return ResponseEntity.ok(ApiResponse.success(role));
    }

    // 2. POST (Create)
    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponseDTO>> createRole(
            @Valid @RequestBody RoleRequestDTO request) {
        RoleResponseDTO role = roleService.createRole(request);
        return new ResponseEntity<>(ApiResponse.created(role, "Role created successfully"), HttpStatus.CREATED);
    }

    // 3. PUT (Update)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponseDTO>> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleRequestDTO request) {
        RoleResponseDTO role = roleService.updateRole(id, request);
        return ResponseEntity.ok(ApiResponse.success(role, "Role updated successfully"));
    }

    // 4. DELETE (Soft delete by default, supports ?hard=true as query param)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean hard) {
        if (hard) {
            roleService.hardDeleteRole(id);
            return ResponseEntity.ok(ApiResponse.success(null, "Role permanently deleted (hard delete)"));
        } else {
            roleService.softDeleteRole(id);
            return ResponseEntity.ok(ApiResponse.success(null, "Role deactivated successfully (soft delete)"));
        }
    }

    // 5. DELETE (Dedicated Hard Delete endpoint)
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<ApiResponse<Void>> hardDeleteRole(@PathVariable Long id) {
        roleService.hardDeleteRole(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Role permanently deleted (hard delete)"));
    }
}
