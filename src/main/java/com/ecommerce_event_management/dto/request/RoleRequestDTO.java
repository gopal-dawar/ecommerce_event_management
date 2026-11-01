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
public class RoleRequestDTO {

    @NotBlank(message = "Role code is required")
    @Size(max = 50, message = "Role code cannot exceed 50 characters")
    private String roleCode;

    @NotBlank(message = "Role name is required")
    @Size(max = 50, message = "Role name cannot exceed 50 characters")
    private String roleName;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @Builder.Default
    private Integer sequenceNo = 0;

    @Builder.Default
    private Boolean isActive = true;
}

