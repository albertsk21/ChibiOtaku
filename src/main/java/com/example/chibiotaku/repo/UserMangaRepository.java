package com.example.chibiotaku.repo;

import com.example.chibiotaku.domain.entities.UserMangaEntity;
import com.example.chibiotaku.domain.enums.WatchStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserMangaRepository extends JpaRepository<UserMangaEntity, UUID> {

    @Query("SELECT COUNT(um.watchStatus) FROM UserMangaEntity AS um " +
            "JOIN um.manga AS a " +
            "JOIN um.user AS u " +
            "WHERE u.username = ?1 AND um.watchStatus = ?2")
    int countByWatchStatusAndUsername(String username, WatchStatusEnum watchStatusEnum);

    @Query("SELECT m.imageUrl, m.id FROM UserMangaEntity AS um " +
            "JOIN um.manga AS m " +
            "JOIN um.user AS u " +
            "WHERE u.username = ?1 " +
            "ORDER BY um.modified DESC, um.created DESC")
    List<Object[]> findUserMangaFormatByUsername(String username);

    @Query("FROM UserMangaEntity AS um " +
            "JOIN um.manga AS m " +
            "JOIN um.user AS u " +
            "WHERE u.username = ?1 AND m.id = ?2")
    Optional< UserMangaEntity> findUserMangaEntityByUserUsernameAndAndMangaId(String username, UUID mangaId);

    @Query("FROM UserMangaEntity AS um " +
            "JOIN um.user AS u " +
            "JOIN um.manga AS m " +
            "WHERE u.username = ?1 AND um.watchStatus = ?2")
    List<UserMangaEntity>  findMangaEntityByUsernameAndWatchStatus(String username, WatchStatusEnum watchStatusEnum);


    @Query("SELECT AVG(ua.score) FROM UserMangaEntity AS ua " +
            "JOIN ua.manga AS m " +
            "WHERE m.id = ?1")
    Double getAverageScoreByMangaId(UUID id);

    @Query("SELECT COUNT(um.id) FROM UserMangaEntity  AS um " +
            "JOIN um.manga AS m " +
            "WHERE m.id = ?1")
    int countUserMangaEntityByMangaId(UUID id);


}