package com.ecommerce_event_management.entity;

import com.ecommerce_event_management.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "UserMaster")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMaster extends BaseEntity {

    @Column(name = "UserCode", length = 100, nullable = false, unique = true)
    private String userCode;

    @Column(name = "UserName", length = 100, nullable = false)
    private String userName;

    @Column(name = "Email", length = 255, nullable = false, unique = true)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RoleId", nullable = false)
    private RoleMaster role;

    @Column(name = "Password", length = 255, nullable = false)
    private String password;
}

