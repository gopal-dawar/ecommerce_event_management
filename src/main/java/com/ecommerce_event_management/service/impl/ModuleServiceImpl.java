package com.ecommerce_event_management.service.impl;

import com.ecommerce_event_management.common.dto.CommonFilterDTO;
import com.ecommerce_event_management.common.dto.PageResponse;
import com.ecommerce_event_management.common.exception.DuplicateResourceException;
import com.ecommerce_event_management.common.exception.ResourceNotFoundException;
import com.ecommerce_event_management.common.specification.SearchSpecification;
import com.ecommerce_event_management.dto.request.ModuleRequestDTO;
import com.ecommerce_event_management.dto.response.ModuleResponseDTO;
import com.ecommerce_event_management.entity.ModuleMaster;
import com.ecommerce_event_management.repository.ModuleMasterRepository;
import com.ecommerce_event_management.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleMasterRepository moduleRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ModuleResponseDTO> getModules(CommonFilterDTO filter) {
        Specification<ModuleMaster> spec = SearchSpecification.byFilter(
                filter, "moduleCode", "moduleName", "moduleUrl"
        );
        Pageable pageable = filter.toPageable();
        Page<ModuleMaster> page = moduleRepository.findAll(spec, pageable);
        return PageResponse.from(page.map(this::mapToDTO), filter.isFetchAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ModuleResponseDTO getModuleById(Long id) {
        ModuleMaster module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module", "id", id));
        return mapToDTO(module);
    }

    @Override
    @Transactional
    public ModuleResponseDTO createModule(ModuleRequestDTO request) {
        if (moduleRepository.existsByModuleCode(request.getModuleCode())) {
            throw new DuplicateResourceException("Module", "moduleCode", request.getModuleCode());
        }

        ModuleMaster module = ModuleMaster.builder()
                .moduleCode(request.getModuleCode())
                .moduleName(request.getModuleName())
                .moduleUrl(request.getModuleUrl())
                .build();
        module.setSequenceNo(request.getSequenceNo() != null ? request.getSequenceNo() : 0);
        module.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        ModuleMaster saved = moduleRepository.save(module);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public ModuleResponseDTO updateModule(Long id, ModuleRequestDTO request) {
        ModuleMaster module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module", "id", id));

        if (!module.getModuleCode().equalsIgnoreCase(request.getModuleCode()) &&
                moduleRepository.existsByModuleCode(request.getModuleCode())) {
            throw new DuplicateResourceException("Module", "moduleCode", request.getModuleCode());
        }

        module.setModuleCode(request.getModuleCode());
        module.setModuleName(request.getModuleName());
        module.setModuleUrl(request.getModuleUrl());
        if (request.getSequenceNo() != null) {
            module.setSequenceNo(request.getSequenceNo());
        }
        if (request.getIsActive() != null) {
            module.setIsActive(request.getIsActive());
        }

        ModuleMaster updated = moduleRepository.save(module);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void softDeleteModule(Long id) {
        ModuleMaster module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module", "id", id));
        module.setIsActive(false);
        moduleRepository.save(module);
    }

    @Override
    @Transactional
    public void hardDeleteModule(Long id) {
        ModuleMaster module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module", "id", id));
        moduleRepository.delete(module);
    }

    private ModuleResponseDTO mapToDTO(ModuleMaster module) {
        return ModuleResponseDTO.builder()
                .id(module.getId())
                .moduleCode(module.getModuleCode())
                .moduleName(module.getModuleName())
                .moduleUrl(module.getModuleUrl())
                .sequenceNo(module.getSequenceNo())
                .isActive(module.getIsActive())
                .createdBy(module.getCreatedBy())
                .createdDate(module.getCreatedDate())
                .updatedBy(module.getUpdatedBy())
                .updatedDate(module.getUpdatedDate())
                .build();
    }
}
