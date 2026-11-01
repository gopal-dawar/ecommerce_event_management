package com.ecommerce_event_management.service;

import com.ecommerce_event_management.dto.response.MenuTreeResponseDTO;

import java.util.List;

public interface NavigationService {

    List<MenuTreeResponseDTO> getUserNavigationTree(Long userId);
}

