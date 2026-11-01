package com.ecommerce_event_management.service;

import com.ecommerce_event_management.common.dto.CommonFilterDTO;
import com.ecommerce_event_management.common.dto.PageResponse;
import com.ecommerce_event_management.dto.request.ModuleRequestDTO;
import com.ecommerce_event_management.dto.response.ModuleResponseDTO;

public interface ModuleService {

    PageResponse<ModuleResponseDTO> getModules(CommonFilterDTO filter);

    ModuleResponseDTO getModuleById(Long id);

    ModuleResponseDTO createModule(ModuleRequestDTO request);

    ModuleResponseDTO updateModule(Long id, ModuleRequestDTO request);

    void softDeleteModule(Long id);

    void hardDeleteModule(Long id);
}
