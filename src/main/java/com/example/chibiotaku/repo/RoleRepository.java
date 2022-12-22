package com.example.chibiotaku.repo;

import com.example.chibiotaku.domain.entities.RoleEntity;
import com.example.chibiotaku.domain.enums.RoleUserEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {

    @Query("FROM RoleEntity AS r WHERE  r.role = ?1 ")
    Optional<RoleEntity> findRoleEntityByRole(RoleUserEnum role);
}