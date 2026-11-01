package com.ecommerce_event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Nested hierarchical module with its sub-menus for frontend sidebar navigation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuTreeResponseDTO {

    private Long moduleId;
    private String moduleCode;
    private String moduleName;
    private String moduleUrl;
    private Integer sequenceNo;
    private List<MenuResponseDTO> menus;
}

