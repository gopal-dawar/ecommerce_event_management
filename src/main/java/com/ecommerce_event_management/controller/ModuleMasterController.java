package com.ecommerce_event_management.controller;

import com.ecommerce_event_management.common.dto.ApiResponse;
import com.ecommerce_event_management.common.dto.CommonFilterDTO;
import com.ecommerce_event_management.common.dto.PageResponse;
import com.ecommerce_event_management.dto.request.ModuleRequestDTO;
import com.ecommerce_event_management.dto.response.ModuleResponseDTO;
import com.ecommerce_event_management.service.ModuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/modules")
@RequiredArgsConstructor
public class ModuleMasterController {

    private final ModuleService moduleService;

    // 1. GET (List with SearchTerm, Id, Pagination, PageSize=-1 for all, SortBy, SortOrder)
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ModuleResponseDTO>>> getModules(
            @ModelAttribute CommonFilterDTO filter) {
        PageResponse<ModuleResponseDTO> modules = moduleService.getModules(filter);
        return ResponseEntity.ok(ApiResponse.success(modules));
    }

    // 1.1 GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ModuleResponseDTO>> getModuleById(@PathVariable Long id) {
        ModuleResponseDTO module = moduleService.getModuleById(id);
        return ResponseEntity.ok(ApiResponse.success(module));
    }

    // 2. POST (Create)
    @PostMapping
    public ResponseEntity<ApiResponse<ModuleResponseDTO>> createModule(
            @Valid @RequestBody ModuleRequestDTO request) {
        ModuleResponseDTO module = moduleService.createModule(request);
        return new ResponseEntity<>(ApiResponse.created(module, "Module created successfully"), HttpStatus.CREATED);
    }

    // 3. PUT (Update)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ModuleResponseDTO>> updateModule(
            @PathVariable Long id,
            @Valid @RequestBody ModuleRequestDTO request) {
        ModuleResponseDTO module = moduleService.updateModule(id, request);
        return ResponseEntity.ok(ApiResponse.success(module, "Module updated successfully"));
    }

    // 4. DELETE (Soft delete by default, supports ?hard=true as query param)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteModule(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean hard) {
        if (hard) {
            moduleService.hardDeleteModule(id);
            return ResponseEntity.ok(ApiResponse.success(null, "Module permanently deleted (hard delete)"));
        } else {
            moduleService.softDeleteModule(id);
            return ResponseEntity.ok(ApiResponse.success(null, "Module deactivated successfully (soft delete)"));
        }
    }

    // 5. DELETE (Dedicated Hard Delete endpoint)
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<ApiResponse<Void>> hardDeleteModule(@PathVariable Long id) {
        moduleService.hardDeleteModule(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Module permanently deleted (hard delete)"));
    }
}
