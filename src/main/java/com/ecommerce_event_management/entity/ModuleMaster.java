package com.ecommerce_event_management.entity;

import com.ecommerce_event_management.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "ModuleMaster")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleMaster extends BaseEntity {

    @Column(name = "ModuleCode", length = 50, nullable = false, unique = true)
    private String moduleCode;

    @Column(name = "ModuleName", length = 100, nullable = false)
    private String moduleName;

    @Column(name = "ModuleUrl", length = 255, nullable = false)
    private String moduleUrl;
}

