package com.ecommerce_event_management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Role ID is required")
    private Long roleId;

    @NotNull(message = "Menu ID is required")
    private Long menuId;

    @Builder.Default
    private Integer sequenceNo = 0;

    @Builder.Default
    private Boolean isActive = true;
}
