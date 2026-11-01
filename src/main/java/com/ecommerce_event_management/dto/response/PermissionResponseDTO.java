package com.ecommerce_event_management.dto.response;

import com.ecommerce_event_management.common.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponseDTO extends BaseDTO {

    private Long userId;
    private String userCode;
    private String userName;
    private String userEmail;

    private Long roleId;
    private String roleCode;
    private String roleName;

    private Long menuId;
    private String menuCode;
    private String menuName;
    private String menuUrl;

    private Long moduleId;
    private String moduleCode;
    private String moduleName;
}
