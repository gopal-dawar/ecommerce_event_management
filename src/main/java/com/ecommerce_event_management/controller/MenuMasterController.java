package com.ecommerce_event_management.controller;

import com.ecommerce_event_management.common.dto.ApiResponse;
import com.ecommerce_event_management.common.dto.CommonFilterDTO;
import com.ecommerce_event_management.common.dto.PageResponse;
import com.ecommerce_event_management.dto.request.MenuRequestDTO;
import com.ecommerce_event_management.dto.response.MenuResponseDTO;
import com.ecommerce_event_management.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuMasterController {

    private final MenuService menuService;

    // 1. GET (List with SearchTerm, Id, Pagination, PageSize=-1 for all, SortBy, SortOrder)
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<MenuResponseDTO>>> getMenus(
            @ModelAttribute CommonFilterDTO filter) {
        PageResponse<MenuResponseDTO> menus = menuService.getMenus(filter);
        return ResponseEntity.ok(ApiResponse.success(menus));
    }

    // 1.1 GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuResponseDTO>> getMenuById(@PathVariable Long id) {
        MenuResponseDTO menu = menuService.getMenuById(id);
        return ResponseEntity.ok(ApiResponse.success(menu));
    }

    // 2. POST (Create)
    @PostMapping
    public ResponseEntity<ApiResponse<MenuResponseDTO>> createMenu(
            @Valid @RequestBody MenuRequestDTO request) {
        MenuResponseDTO menu = menuService.createMenu(request);
        return new ResponseEntity<>(ApiResponse.created(menu, "Menu created successfully"), HttpStatus.CREATED);
    }

    // 3. PUT (Update)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuResponseDTO>> updateMenu(
            @PathVariable Long id,
            @Valid @RequestBody MenuRequestDTO request) {
        MenuResponseDTO menu = menuService.updateMenu(id, request);
        return ResponseEntity.ok(ApiResponse.success(menu, "Menu updated successfully"));
    }

    // 4. DELETE (Soft delete by default, supports ?hard=true as query param)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMenu(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean hard) {
        if (hard) {
            menuService.hardDeleteMenu(id);
            return ResponseEntity.ok(ApiResponse.success(null, "Menu permanently deleted (hard delete)"));
        } else {
            menuService.softDeleteMenu(id);
            return ResponseEntity.ok(ApiResponse.success(null, "Menu deactivated successfully (soft delete)"));
        }
    }

    // 5. DELETE (Dedicated Hard Delete endpoint)
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<ApiResponse<Void>> hardDeleteMenu(@PathVariable Long id) {
        menuService.hardDeleteMenu(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Menu permanently deleted (hard delete)"));
    }
}
