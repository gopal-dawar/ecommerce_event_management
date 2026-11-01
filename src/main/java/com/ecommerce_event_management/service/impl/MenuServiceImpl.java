package com.ecommerce_event_management.service.impl;

import com.ecommerce_event_management.common.dto.CommonFilterDTO;
import com.ecommerce_event_management.common.dto.PageResponse;
import com.ecommerce_event_management.common.exception.DuplicateResourceException;
import com.ecommerce_event_management.common.exception.ResourceNotFoundException;
import com.ecommerce_event_management.common.specification.SearchSpecification;
import com.ecommerce_event_management.dto.request.MenuRequestDTO;
import com.ecommerce_event_management.dto.response.MenuResponseDTO;
import com.ecommerce_event_management.entity.MenuMaster;
import com.ecommerce_event_management.entity.ModuleMaster;
import com.ecommerce_event_management.repository.MenuMasterRepository;
import com.ecommerce_event_management.repository.ModuleMasterRepository;
import com.ecommerce_event_management.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuMasterRepository menuRepository;
    private final ModuleMasterRepository moduleRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MenuResponseDTO> getMenus(CommonFilterDTO filter) {
        Specification<MenuMaster> spec = SearchSpecification.byFilter(
                filter, "menuCode", "menuName", "menuUrl", "module.moduleName", "module.moduleCode"
        );
        Pageable pageable = filter.toPageable();
        Page<MenuMaster> page = menuRepository.findAll(spec, pageable);
        return PageResponse.from(page.map(this::mapToDTO), filter.isFetchAll());
    }

    @Override
    @Transactional(readOnly = true)
    public MenuResponseDTO getMenuById(Long id) {
        MenuMaster menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu", "id", id));
        return mapToDTO(menu);
    }

    @Override
    @Transactional
    public MenuResponseDTO createMenu(MenuRequestDTO request) {
        if (menuRepository.existsByMenuCode(request.getMenuCode())) {
            throw new DuplicateResourceException("Menu", "menuCode", request.getMenuCode());
        }

        ModuleMaster module = moduleRepository.findById(request.getModuleId())
                .orElseThrow(() -> new ResourceNotFoundException("Module", "id", request.getModuleId()));

        MenuMaster menu = MenuMaster.builder()
                .module(module)
                .menuCode(request.getMenuCode())
                .menuName(request.getMenuName())
                .menuUrl(request.getMenuUrl())
                .build();
        menu.setSequenceNo(request.getSequenceNo() != null ? request.getSequenceNo() : 0);
        menu.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        MenuMaster saved = menuRepository.save(menu);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public MenuResponseDTO updateMenu(Long id, MenuRequestDTO request) {
        MenuMaster menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu", "id", id));

        if (!menu.getMenuCode().equalsIgnoreCase(request.getMenuCode()) &&
                menuRepository.existsByMenuCode(request.getMenuCode())) {
            throw new DuplicateResourceException("Menu", "menuCode", request.getMenuCode());
        }

        if (!menu.getModule().getId().equals(request.getModuleId())) {
            ModuleMaster newModule = moduleRepository.findById(request.getModuleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Module", "id", request.getModuleId()));
            menu.setModule(newModule);
        }

        menu.setMenuCode(request.getMenuCode());
        menu.setMenuName(request.getMenuName());
        menu.setMenuUrl(request.getMenuUrl());
        if (request.getSequenceNo() != null) {
            menu.setSequenceNo(request.getSequenceNo());
        }
        if (request.getIsActive() != null) {
            menu.setIsActive(request.getIsActive());
        }

        MenuMaster updated = menuRepository.save(menu);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void softDeleteMenu(Long id) {
        MenuMaster menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu", "id", id));
        menu.setIsActive(false);
        menuRepository.save(menu);
    }

    @Override
    @Transactional
    public void hardDeleteMenu(Long id) {
        MenuMaster menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu", "id", id));
        menuRepository.delete(menu);
    }

    private MenuResponseDTO mapToDTO(MenuMaster menu) {
        return MenuResponseDTO.builder()
                .id(menu.getId())
                .moduleId(menu.getModule() != null ? menu.getModule().getId() : null)
                .moduleCode(menu.getModule() != null ? menu.getModule().getModuleCode() : null)
                .moduleName(menu.getModule() != null ? menu.getModule().getModuleName() : null)
                .menuCode(menu.getMenuCode())
                .menuName(menu.getMenuName())
                .menuUrl(menu.getMenuUrl())
                .sequenceNo(menu.getSequenceNo())
                .isActive(menu.getIsActive())
                .createdBy(menu.getCreatedBy())
                .createdDate(menu.getCreatedDate())
                .updatedBy(menu.getUpdatedBy())
                .updatedDate(menu.getUpdatedDate())
                .build();
    }
}
