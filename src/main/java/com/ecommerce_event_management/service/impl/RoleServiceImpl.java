package com.ecommerce_event_management.service.impl;

import com.ecommerce_event_management.common.dto.CommonFilterDTO;
import com.ecommerce_event_management.common.dto.PageResponse;
import com.ecommerce_event_management.common.exception.DuplicateResourceException;
import com.ecommerce_event_management.common.exception.ResourceNotFoundException;
import com.ecommerce_event_management.common.specification.SearchSpecification;
import com.ecommerce_event_management.dto.request.RoleRequestDTO;
import com.ecommerce_event_management.dto.response.RoleResponseDTO;
import com.ecommerce_event_management.entity.RoleMaster;
import com.ecommerce_event_management.repository.RoleMasterRepository;
import com.ecommerce_event_management.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleMasterRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<RoleResponseDTO> getRoles(CommonFilterDTO filter) {
        Specification<RoleMaster> spec = SearchSpecification.byFilter(
                filter, "roleCode", "roleName", "description"
        );
        Pageable pageable = filter.toPageable();
        Page<RoleMaster> page = roleRepository.findAll(spec, pageable);
        return PageResponse.from(page.map(this::mapToDTO), filter.isFetchAll());
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponseDTO getRoleById(Long id) {
        RoleMaster role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
        return mapToDTO(role);
    }

    @Override
    @Transactional
    public RoleResponseDTO createRole(RoleRequestDTO request) {
        if (roleRepository.existsByRoleCode(request.getRoleCode())) {
            throw new DuplicateResourceException("Role", "roleCode", request.getRoleCode());
        }
        if (roleRepository.existsByRoleName(request.getRoleName())) {
            throw new DuplicateResourceException("Role", "roleName", request.getRoleName());
        }

        RoleMaster role = RoleMaster.builder()
                .roleCode(request.getRoleCode())
                .roleName(request.getRoleName())
                .description(request.getDescription())
                .build();
        role.setSequenceNo(request.getSequenceNo() != null ? request.getSequenceNo() : 0);
        role.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        RoleMaster saved = roleRepository.save(role);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public RoleResponseDTO updateRole(Long id, RoleRequestDTO request) {
        RoleMaster role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));

        // Check unique constraints if changed
        if (!role.getRoleCode().equalsIgnoreCase(request.getRoleCode()) &&
                roleRepository.existsByRoleCode(request.getRoleCode())) {
            throw new DuplicateResourceException("Role", "roleCode", request.getRoleCode());
        }
        if (!role.getRoleName().equalsIgnoreCase(request.getRoleName()) &&
                roleRepository.existsByRoleName(request.getRoleName())) {
            throw new DuplicateResourceException("Role", "roleName", request.getRoleName());
        }

        role.setRoleCode(request.getRoleCode());
        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());
        if (request.getSequenceNo() != null) {
            role.setSequenceNo(request.getSequenceNo());
        }
        if (request.getIsActive() != null) {
            role.setIsActive(request.getIsActive());
        }

        RoleMaster updated = roleRepository.save(role);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void softDeleteRole(Long id) {
        RoleMaster role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
        role.setIsActive(false);
        roleRepository.save(role);
    }

    @Override
    @Transactional
    public void hardDeleteRole(Long id) {
        RoleMaster role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
        roleRepository.delete(role);
    }

    private RoleResponseDTO mapToDTO(RoleMaster role) {
        return RoleResponseDTO.builder()
                .id(role.getId())
                .roleCode(role.getRoleCode())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .sequenceNo(role.getSequenceNo())
                .isActive(role.getIsActive())
                .createdBy(role.getCreatedBy())
                .createdDate(role.getCreatedDate())
                .updatedBy(role.getUpdatedBy())
                .updatedDate(role.getUpdatedDate())
                .build();
    }
}
