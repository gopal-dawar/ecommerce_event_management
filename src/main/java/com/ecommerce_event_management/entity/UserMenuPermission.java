package com.ecommerce_event_management.entity;

import com.ecommerce_event_management.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "UserMenuPermission",
    uniqueConstraints = @UniqueConstraint(name = "uq_user_menu", columnNames = {"UserId", "MenuId"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMenuPermission extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", nullable = false)
    private UserMaster user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RoleId", nullable = false)
    private RoleMaster role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MenuId", nullable = false)
    private MenuMaster menu;
}

