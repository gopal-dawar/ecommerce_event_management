package com.ecommerce_event_management.dto.request;

import jakarta.validation.constraints.Email;
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
public class UserCreateRequestDTO {

    @NotBlank(message = "User code is required")
    @Size(max = 100, message = "User code cannot exceed 100 characters")
    private String userCode;

    @NotBlank(message = "User name is required")
    @Size(max = 100, message = "User name cannot exceed 100 characters")
    private String userName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Role ID is required")
    private Long roleId;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Builder.Default
    private Integer sequenceNo = 0;

    @Builder.Default
    private Boolean isActive = true;
}

