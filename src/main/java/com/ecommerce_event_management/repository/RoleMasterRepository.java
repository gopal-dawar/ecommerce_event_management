package com.ecommerce_event_management.repository;

import com.ecommerce_event_management.entity.RoleMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleMasterRepository extends JpaRepository<RoleMaster, Long>, JpaSpecificationExecutor<RoleMaster> {

    Optional<RoleMaster> findByRoleCode(String roleCode);

    Optional<RoleMaster> findByRoleName(String roleName);

    boolean existsByRoleCode(String roleCode);

    boolean existsByRoleName(String roleName);
}
