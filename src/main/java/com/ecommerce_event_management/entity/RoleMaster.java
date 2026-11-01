package com.ecommerce_event_management.entity;

import com.ecommerce_event_management.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "RoleMaster")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleMaster extends BaseEntity {

    @Column(name = "RoleCode", length = 50, nullable = false, unique = true)
    private String roleCode;

    @Column(name = "RoleName", length = 50, nullable = false, unique = true)
    private String roleName;

    @Column(name = "Description", length = 255)
    private String description;
}

