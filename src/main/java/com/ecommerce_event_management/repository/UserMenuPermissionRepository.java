package com.ecommerce_event_management.repository;

import com.ecommerce_event_management.entity.MenuMaster;
import com.ecommerce_event_management.entity.UserMenuPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMenuPermissionRepository extends JpaRepository<UserMenuPermission, Long>, JpaSpecificationExecutor<UserMenuPermission> {

    Optional<UserMenuPermission> findByUser_IdAndMenu_Id(Long userId, Long menuId);

    boolean existsByUser_IdAndMenu_Id(Long userId, Long menuId);

    @Query("SELECT p.menu FROM UserMenuPermission p WHERE p.user.id = :userId AND p.isActive = true ORDER BY p.menu.sequenceNo ASC")
    List<MenuMaster> findPermittedMenusByUserId(@Param("userId") Long userId);

    @Query("SELECT p.menu FROM UserMenuPermission p WHERE p.role.id = :roleId AND p.isActive = true ORDER BY p.menu.sequenceNo ASC")
    List<MenuMaster> findPermittedMenusByRoleId(@Param("roleId") Long roleId);

    void deleteByUser_Id(Long userId);
}
