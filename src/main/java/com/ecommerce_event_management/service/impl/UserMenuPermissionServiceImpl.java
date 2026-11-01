package com.ecommerce_event_management.service.impl;

import com.ecommerce_event_management.common.dto.CommonFilterDTO;
import com.ecommerce_event_management.common.dto.PageResponse;
import com.ecommerce_event_management.common.exception.DuplicateResourceException;
import com.ecommerce_event_management.common.exception.ResourceNotFoundException;
import com.ecommerce_event_management.common.specification.SearchSpecification;
import com.ecommerce_event_management.dto.request.PermissionRequestDTO;
import com.ecommerce_event_management.dto.response.PermissionResponseDTO;
import com.ecommerce_event_management.entity.MenuMaster;
import com.ecommerce_event_management.entity.RoleMaster;
import com.ecommerce_event_management.entity.UserMaster;
import com.ecommerce_event_management.entity.UserMenuPermission;
import com.ecommerce_event_management.repository.MenuMasterRepository;
import com.ecommerce_event_management.repository.RoleMasterRepository;
import com.ecommerce_event_management.repository.UserMasterRepository;
import com.ecommerce_event_management.repository.UserMenuPermissionRepository;
import com.ecommerce_event_management.service.UserMenuPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserMenuPermissionServiceImpl implements UserMenuPermissionService {

    private final UserMenuPermissionRepository permissionRepository;
    private final UserMasterRepository userRepository;
    private final RoleMasterRepository roleRepository;
    private final MenuMasterRepository menuRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PermissionResponseDTO> getPermissions(CommonFilterDTO filter) {
        Specification<UserMenuPermission> spec = SearchSpecification.byFilter(
                filter,
                "user.userName", "user.userCode", "user.email",
                "role.roleName", "role.roleCode",
                "menu.menuName", "menu.menuCode"
        );
        Pageable pageable = filter.toPageable();
        Page<UserMenuPermission> page = permissionRepository.findAll(spec, pageable);
        return PageResponse.from(page.map(this::mapToDTO), filter.isFetchAll());
    }

    @Override
    @Transactional(readOnly = true)
    public PermissionResponseDTO getPermissionById(Long id) {
        UserMenuPermission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission", "id", id));
        return mapToDTO(permission);
    }

    @Override
    @Transactional
    public PermissionResponseDTO createPermission(PermissionRequestDTO request) {
        if (permissionRepository.existsByUser_IdAndMenu_Id(request.getUserId(), request.getMenuId())) {
            throw new DuplicateResourceException("Permission mapping already exists for user and menu");
        }

        UserMaster user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

        RoleMaster role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", request.getRoleId()));

        MenuMaster menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu", "id", request.getMenuId()));

        UserMenuPermission permission = UserMenuPermission.builder()
                .user(user)
                .role(role)
                .menu(menu)
                .build();
        permission.setSequenceNo(request.getSequenceNo() != null ? request.getSequenceNo() : 0);
        permission.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        UserMenuPermission saved = permissionRepository.save(permission);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public PermissionResponseDTO updatePermission(Long id, PermissionRequestDTO request) {
        UserMenuPermission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission", "id", id));

        // Validate duplicates if mapping changed
        if ((!permission.getUser().getId().equals(request.getUserId()) ||
                !permission.getMenu().getId().equals(request.getMenuId())) &&
                permissionRepository.existsByUser_IdAndMenu_Id(request.getUserId(), request.getMenuId())) {
            throw new DuplicateResourceException("Permission mapping already exists for user and menu");
        }

        UserMaster user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

        RoleMaster role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", request.getRoleId()));

        MenuMaster menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu", "id", request.getMenuId()));

        permission.setUser(user);
        permission.setRole(role);
        permission.setMenu(menu);
        if (request.getSequenceNo() != null) {
            permission.setSequenceNo(request.getSequenceNo());
        }
        if (request.getIsActive() != null) {
            permission.setIsActive(request.getIsActive());
        }

        UserMenuPermission updated = permissionRepository.save(permission);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void softDeletePermission(Long id) {
        UserMenuPermission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission", "id", id));
        permission.setIsActive(false);
        permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public void hardDeletePermission(Long id) {
        UserMenuPermission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission", "id", id));
        permissionRepository.delete(permission);
    }

    private PermissionResponseDTO mapToDTO(UserMenuPermission p) {
        return PermissionResponseDTO.builder()
                .id(p.getId())
                .userId(p.getUser() != null ? p.getUser().getId() : null)
                .userCode(p.getUser() != null ? p.getUser().getUserCode() : null)
                .userName(p.getUser() != null ? p.getUser().getUserName() : null)
                .userEmail(p.getUser() != null ? p.getUser().getEmail() : null)
                .roleId(p.getRole() != null ? p.getRole().getId() : null)
                .roleCode(p.getRole() != null ? p.getRole().getRoleCode() : null)
                .roleName(p.getRole() != null ? p.getRole().getRoleName() : null)
                .menuId(p.getMenu() != null ? p.getMenu().getId() : null)
                .menuCode(p.getMenu() != null ? p.getMenu().getMenuCode() : null)
                .menuName(p.getMenu() != null ? p.getMenu().getMenuName() : null)
                .menuUrl(p.getMenu() != null ? p.getMenu().getMenuUrl() : null)
                .moduleId(p.getMenu() != null && p.getMenu().getModule() != null ? p.getMenu().getModule().getId() : null)
                .moduleCode(p.getMenu() != null && p.getMenu().getModule() != null ? p.getMenu().getModule().getModuleCode() : null)
                .moduleName(p.getMenu() != null && p.getMenu().getModule() != null ? p.getMenu().getModule().getModuleName() : null)
                .sequenceNo(p.getSequenceNo())
                .isActive(p.getIsActive())
                .createdBy(p.getCreatedBy())
                .createdDate(p.getCreatedDate())
                .updatedBy(p.getUpdatedBy())
                .updatedDate(p.getUpdatedDate())
                .build();
    }
}
