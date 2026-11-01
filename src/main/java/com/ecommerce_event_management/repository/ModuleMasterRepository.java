package com.ecommerce_event_management.repository;

import com.ecommerce_event_management.entity.ModuleMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleMasterRepository extends JpaRepository<ModuleMaster, Long>, JpaSpecificationExecutor<ModuleMaster> {

    Optional<ModuleMaster> findByModuleCode(String moduleCode);

    boolean existsByModuleCode(String moduleCode);

    List<ModuleMaster> findByIsActiveTrueOrderBySequenceNoAsc();
}
