package com.ecommerce_event_management.service.impl;

import com.ecommerce_event_management.common.dto.CommonFilterDTO;
import com.ecommerce_event_management.common.dto.PageResponse;
import com.ecommerce_event_management.common.exception.BadRequestException;
import com.ecommerce_event_management.common.exception.DuplicateResourceException;
import com.ecommerce_event_management.common.exception.ResourceNotFoundException;
import com.ecommerce_event_management.common.exception.UnauthorizedException;
import com.ecommerce_event_management.common.specification.SearchSpecification;
import com.ecommerce_event_management.dto.request.LoginRequestDTO;
import com.ecommerce_event_management.dto.request.PasswordChangeRequestDTO;
import com.ecommerce_event_management.dto.request.UserCreateRequestDTO;
import com.ecommerce_event_management.dto.request.UserUpdateRequestDTO;
import com.ecommerce_event_management.dto.response.AuthResponseDTO;
import com.ecommerce_event_management.dto.response.MenuTreeResponseDTO;
import com.ecommerce_event_management.dto.response.UserResponseDTO;
import com.ecommerce_event_management.entity.RoleMaster;
import com.ecommerce_event_management.entity.UserMaster;
import com.ecommerce_event_management.repository.RoleMasterRepository;
import com.ecommerce_event_management.repository.UserMasterRepository;
import com.ecommerce_event_management.service.NavigationService;
import com.ecommerce_event_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMasterRepository userRepository;
    private final RoleMasterRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final NavigationService navigationService;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponseDTO> getUsers(CommonFilterDTO filter) {
        Specification<UserMaster> spec = SearchSpecification.byFilter(
                filter, "userCode", "userName", "email", "role.roleName"
        );
        Pageable pageable = filter.toPageable();
        Page<UserMaster> page = userRepository.findAll(spec, pageable);
        return PageResponse.from(page.map(this::mapToDTO), filter.isFetchAll());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        UserMaster user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return mapToDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserCreateRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }
        if (userRepository.existsByUserCode(request.getUserCode())) {
            throw new DuplicateResourceException("User", "userCode", request.getUserCode());
        }

        RoleMaster role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", request.getRoleId()));

        UserMaster user = UserMaster.builder()
                .userCode(request.getUserCode())
                .userName(request.getUserName())
                .email(request.getEmail())
                .role(role)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        user.setSequenceNo(request.getSequenceNo() != null ? request.getSequenceNo() : 0);
        user.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        UserMaster saved = userRepository.save(user);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateRequestDTO request) {
        UserMaster user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (request.getRoleId() != null && !request.getRoleId().equals(user.getRole().getId())) {
            RoleMaster newRole = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "id", request.getRoleId()));
            user.setRole(newRole);
        }

        user.setUserName(request.getUserName());
        if (request.getSequenceNo() != null) {
            user.setSequenceNo(request.getSequenceNo());
        }
        if (request.getIsActive() != null) {
            user.setIsActive(request.getIsActive());
        }

        UserMaster updated = userRepository.save(user);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void softDeleteUser(Long id) {
        UserMaster user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void hardDeleteUser(Long id) {
        UserMaster user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void changePassword(Long id, PasswordChangeRequestDTO request) {
        UserMaster user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password does not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO authenticate(LoginRequestDTO request) {
        UserMaster user = userRepository.findByEmailWithRole(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!user.getIsActive()) {
            throw new BadRequestException("User account is disabled");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        List<MenuTreeResponseDTO> navigationTree = navigationService.getUserNavigationTree(user.getId());

        String sessionToken = UUID.randomUUID().toString();

        return AuthResponseDTO.builder()
                .token(sessionToken)
                .tokenType("Bearer")
                .user(mapToDTO(user))
                .navigationTree(navigationTree)
                .build();
    }

    private UserResponseDTO mapToDTO(UserMaster user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .userCode(user.getUserCode())
                .userName(user.getUserName())
                .email(user.getEmail())
                .roleId(user.getRole() != null ? user.getRole().getId() : null)
                .roleCode(user.getRole() != null ? user.getRole().getRoleCode() : null)
                .roleName(user.getRole() != null ? user.getRole().getRoleName() : null)
                .sequenceNo(user.getSequenceNo())
                .isActive(user.getIsActive())
                .createdBy(user.getCreatedBy())
                .createdDate(user.getCreatedDate())
                .updatedBy(user.getUpdatedBy())
                .updatedDate(user.getUpdatedDate())
                .build();
    }
}
