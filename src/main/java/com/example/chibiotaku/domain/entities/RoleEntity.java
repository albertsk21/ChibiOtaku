package com.example.chibiotaku.domain.entities;

import com.example.chibiotaku.domain.enums.RoleUserEnum;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class RoleEntity extends BaseEntity {


    private RoleUserEnum role;
    @Enumerated(EnumType.STRING)
    public RoleUserEnum getRole() {
        return role;
    }

    public RoleEntity setRole(RoleUserEnum role) {
        this.role = role;
        return this;
    }
}
