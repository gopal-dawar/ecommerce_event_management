package com.ecommerce_event_management.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * BaseEntity: Reusable base persistence class containing standard audit and status fields.
 * Extends DRY principles across all database entities.
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "SequenceNo", nullable = false)
    private Integer sequenceNo = 0;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive = true;

    @CreatedBy
    @Column(name = "CreatedBy", nullable = false, updatable = false)
    private Long createdBy = 1L;

    @CreatedDate
    @Column(name = "CreatedDate", nullable = false, updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @LastModifiedBy
    @Column(name = "UpdatedBy")
    private Long updatedBy;

    @LastModifiedDate
    @Column(name = "UpdatedDate")
    private LocalDateTime updatedDate;
}

