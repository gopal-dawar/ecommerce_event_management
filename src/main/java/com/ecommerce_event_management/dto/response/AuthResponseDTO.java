package com.ecommerce_event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

    private String token;
    @Builder.Default
    private String tokenType = "Bearer";
    private UserResponseDTO user;
    private List<MenuTreeResponseDTO> navigationTree;
}

