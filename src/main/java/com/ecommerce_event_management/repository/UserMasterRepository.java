package com.ecommerce_event_management.repository;

import com.ecommerce_event_management.entity.UserMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMasterRepository extends JpaRepository<UserMaster, Long>, JpaSpecificationExecutor<UserMaster> {

    Optional<UserMaster> findByEmail(String email);

    Optional<UserMaster> findByUserCode(String userCode);

    boolean existsByEmail(String email);

    boolean existsByUserCode(String userCode);

    @Query("SELECT u FROM UserMaster u JOIN FETCH u.role WHERE u.email = :email")
    Optional<UserMaster> findByEmailWithRole(@Param("email") String email);

    Page<UserMaster> findByIsActiveTrue(Pageable pageable);
}
