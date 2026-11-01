package com.ecommerce_event_management.repository;

import com.ecommerce_event_management.entity.MenuMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuMasterRepository extends JpaRepository<MenuMaster, Long>, JpaSpecificationExecutor<MenuMaster> {

    Optional<MenuMaster> findByMenuCode(String menuCode);

    boolean existsByMenuCode(String menuCode);

    List<MenuMaster> findByModule_IdAndIsActiveTrueOrderBySequenceNoAsc(Long moduleId);
}
