package com.ecommerce_event_management.service;

import com.ecommerce_event_management.common.dto.CommonFilterDTO;
import com.ecommerce_event_management.common.dto.PageResponse;
import com.ecommerce_event_management.dto.request.PermissionRequestDTO;
import com.ecommerce_event_management.dto.response.PermissionResponseDTO;

public interface UserMenuPermissionService {

    PageResponse<PermissionResponseDTO> getPermissions(CommonFilterDTO filter);

    PermissionResponseDTO getPermissionById(Long id);

    PermissionResponseDTO createPermission(PermissionRequestDTO request);

    PermissionResponseDTO updatePermission(Long id, PermissionRequestDTO request);

    void softDeletePermission(Long id);

    void hardDeletePermission(Long id);
}
