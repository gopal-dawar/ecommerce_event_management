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
public class MenuResponseDTO extends BaseDTO {

    private Long moduleId;
    private String moduleCode;
    private String moduleName;
    private String menuCode;
    private String menuName;
    private String menuUrl;
}

