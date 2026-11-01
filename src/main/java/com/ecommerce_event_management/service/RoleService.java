package com.ecommerce_event_management.service;

import com.ecommerce_event_management.common.dto.CommonFilterDTO;
import com.ecommerce_event_management.common.dto.PageResponse;
import com.ecommerce_event_management.dto.request.RoleRequestDTO;
import com.ecommerce_event_management.dto.response.RoleResponseDTO;

public interface RoleService {

    PageResponse<RoleResponseDTO> getRoles(CommonFilterDTO filter);

    RoleResponseDTO getRoleById(Long id);

    RoleResponseDTO createRole(RoleRequestDTO request);

    RoleResponseDTO updateRole(Long id, RoleRequestDTO request);

    void softDeleteRole(Long id);

    void hardDeleteRole(Long id);
}
