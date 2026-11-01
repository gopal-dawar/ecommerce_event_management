package com.ecommerce_event_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleRequestDTO {

    @NotBlank(message = "Module code is required")
    @Size(max = 50, message = "Module code cannot exceed 50 characters")
    private String moduleCode;

    @NotBlank(message = "Module name is required")
    @Size(max = 100, message = "Module name cannot exceed 100 characters")
    private String moduleName;

    @NotBlank(message = "Module URL is required")
    @Size(max = 255, message = "Module URL cannot exceed 255 characters")
    private String moduleUrl;

    @Builder.Default
    private Integer sequenceNo = 0;

    @Builder.Default
    private Boolean isActive = true;
}
