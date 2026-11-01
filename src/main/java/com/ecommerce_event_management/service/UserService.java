package com.ecommerce_event_management.service;

import com.ecommerce_event_management.common.dto.CommonFilterDTO;
import com.ecommerce_event_management.common.dto.PageResponse;
import com.ecommerce_event_management.dto.request.LoginRequestDTO;
import com.ecommerce_event_management.dto.request.PasswordChangeRequestDTO;
import com.ecommerce_event_management.dto.request.UserCreateRequestDTO;
import com.ecommerce_event_management.dto.request.UserUpdateRequestDTO;
import com.ecommerce_event_management.dto.response.AuthResponseDTO;
import com.ecommerce_event_management.dto.response.UserResponseDTO;

public interface UserService {

    PageResponse<UserResponseDTO> getUsers(CommonFilterDTO filter);

    UserResponseDTO getUserById(Long id);

    UserResponseDTO createUser(UserCreateRequestDTO request);

    UserResponseDTO updateUser(Long id, UserUpdateRequestDTO request);

    void softDeleteUser(Long id);

    void hardDeleteUser(Long id);

    void changePassword(Long id, PasswordChangeRequestDTO request);

    AuthResponseDTO authenticate(LoginRequestDTO request);
}
