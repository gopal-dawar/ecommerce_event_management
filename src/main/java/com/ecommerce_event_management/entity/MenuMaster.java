package com.ecommerce_event_management.entity;

import com.ecommerce_event_management.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MenuMaster")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuMaster extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ModuleId", nullable = false)
    private ModuleMaster module;

    @Column(name = "MenuCode", length = 50, nullable = false, unique = true)
    private String menuCode;

    @Column(name = "MenuName", length = 100, nullable = false)
    private String menuName;

    @Column(name = "MenuUrl", length = 255, nullable = false)
    private String menuUrl;
}

