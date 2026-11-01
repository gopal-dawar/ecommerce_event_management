package com.ecommerce_event_management.service;

import com.ecommerce_event_management.common.dto.CommonFilterDTO;
import com.ecommerce_event_management.common.dto.PageResponse;
import com.ecommerce_event_management.dto.request.MenuRequestDTO;
import com.ecommerce_event_management.dto.response.MenuResponseDTO;

public interface MenuService {

    PageResponse<MenuResponseDTO> getMenus(CommonFilterDTO filter);

    MenuResponseDTO getMenuById(Long id);

    MenuResponseDTO createMenu(MenuRequestDTO request);

    MenuResponseDTO updateMenu(Long id, MenuRequestDTO request);

    void softDeleteMenu(Long id);

    void hardDeleteMenu(Long id);
}
