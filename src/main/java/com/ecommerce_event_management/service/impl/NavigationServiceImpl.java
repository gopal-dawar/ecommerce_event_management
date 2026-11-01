package com.ecommerce_event_management.service.impl;

import com.ecommerce_event_management.dto.response.MenuResponseDTO;
import com.ecommerce_event_management.dto.response.MenuTreeResponseDTO;
import com.ecommerce_event_management.entity.MenuMaster;
import com.ecommerce_event_management.entity.ModuleMaster;
import com.ecommerce_event_management.repository.UserMenuPermissionRepository;
import com.ecommerce_event_management.service.NavigationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NavigationServiceImpl implements NavigationService {

    private final UserMenuPermissionRepository permissionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MenuTreeResponseDTO> getUserNavigationTree(Long userId) {
        List<MenuMaster> permittedMenus = permissionRepository.findPermittedMenusByUserId(userId);

        if (permittedMenus.isEmpty()) {
            return Collections.emptyList();
        }

        // Group permitted menus by Module
        Map<ModuleMaster, List<MenuMaster>> moduleMap = permittedMenus.stream()
                .filter(MenuMaster::getIsActive)
                .filter(m -> m.getModule() != null && m.getModule().getIsActive())
                .collect(Collectors.groupingBy(MenuMaster::getModule));

        // Sort modules by SequenceNo and map to MenuTreeResponseDTO
        return moduleMap.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().getSequenceNo()))
                .map(entry -> {
                    ModuleMaster module = entry.getKey();
                    List<MenuResponseDTO> childMenus = entry.getValue().stream()
                            .sorted(Comparator.comparing(MenuMaster::getSequenceNo))
                            .map(m -> MenuResponseDTO.builder()
                                    .id(m.getId())
                                    .moduleId(module.getId())
                                    .moduleCode(module.getModuleCode())
                                    .moduleName(module.getModuleName())
                                    .menuCode(m.getMenuCode())
                                    .menuName(m.getMenuName())
                                    .menuUrl(m.getMenuUrl())
                                    .sequenceNo(m.getSequenceNo())
                                    .isActive(m.getIsActive())
                                    .build())
                            .collect(Collectors.toList());

                    return MenuTreeResponseDTO.builder()
                            .moduleId(module.getId())
                            .moduleCode(module.getModuleCode())
                            .moduleName(module.getModuleName())
                            .moduleUrl(module.getModuleUrl())
                            .sequenceNo(module.getSequenceNo())
                            .menus(childMenus)
                            .build();
                })
                .collect(Collectors.toList());
    }
}

