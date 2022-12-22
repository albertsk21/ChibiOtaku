package com.example.chibiotaku.repo;

import com.example.chibiotaku.domain.entities.AnimeEntity;
import com.example.chibiotaku.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Query("FROM UserEntity AS u WHERE u.username = ?1")
    Optional<UserEntity> findUserEntityByUsername(String username);

    @Query("FROM UserEntity AS u WHERE u.email = ?1")
    Optional<UserEntity> findUserEntityByEmail(String email);



}