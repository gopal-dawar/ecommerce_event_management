package com.ecommerce_event_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuRequestDTO {

    @NotNull(message = "Module ID is required")
    private Long moduleId;

    @NotBlank(message = "Menu code is required")
    @Size(max = 50, message = "Menu code cannot exceed 50 characters")
    private String menuCode;

    @NotBlank(message = "Menu name is required")
    @Size(max = 100, message = "Menu name cannot exceed 100 characters")
    private String menuName;

    @NotBlank(message = "Menu URL is required")
    @Size(max = 255, message = "Menu URL cannot exceed 255 characters")
    private String menuUrl;

    @Builder.Default
    private Integer sequenceNo = 0;

    @Builder.Default
    private Boolean isActive = true;
}
