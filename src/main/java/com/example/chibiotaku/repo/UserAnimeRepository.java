package com.example.chibiotaku.repo;

import com.example.chibiotaku.domain.entities.UserAnimeEntity;
import com.example.chibiotaku.domain.enums.WatchStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserAnimeRepository extends JpaRepository<UserAnimeEntity, UUID> {


    @Query("FROM UserAnimeEntity AS ua " +
            "JOIN ua.anime AS a " +
            "JOIN ua.user AS u " +
            "WHERE a.id = ?1 AND u.username = ?2")
    Optional<UserAnimeEntity> findAnimeByAnimeIdAndUserId(UUID animeId, String username);

    @Query("SELECT COUNT(ua.watchStatus) FROM UserAnimeEntity AS ua " +
            "JOIN ua.anime AS a " +
            "JOIN ua.user AS u " +
            "WHERE u.username = ?1 AND ua.watchStatus = ?2")
    int countByWatchStatusAndUsername(String username, WatchStatusEnum watchStatusEnum);

    @Query("SELECT a.imageUrl, a.id FROM UserAnimeEntity AS ua " +
            "JOIN ua.anime AS a " +
            "JOIN ua.user AS u " +
            "WHERE u.username = ?1 " +
            "ORDER BY ua.modified DESC, ua.created DESC")
    List<Object[]> findUserAnimeFormatByUsername(String username);

    @Query("FROM UserAnimeEntity AS ua " +
            "JOIN ua.anime AS a " +
            "JOIN ua.user AS u " +
            "WHERE u.username = ?1 AND a.id = ?2")
   Optional< UserAnimeEntity> findUserAnimeEntityByUserUsernameAndAndAnimeId(String username, UUID animeId);

    @Query("FROM UserAnimeEntity AS ua " +
            "JOIN ua.user AS u " +
            "JOIN ua.anime AS a " +
            "WHERE u.username = ?1 AND ua.watchStatus = ?2")
    List<UserAnimeEntity>  findAnimeEntityByUsernameAndWatchStatus(String username, WatchStatusEnum watchStatusEnum);


    @Query("SELECT AVG(ua.score) FROM UserAnimeEntity AS ua " +
            "JOIN ua.anime AS a " +
            "WHERE a.title = ?1")
    Double getAverageScoreByAnimeTitle(String animeTitle);

    @Query("SELECT COUNT(ua.id) FROM UserAnimeEntity  AS ua " +
            "JOIN ua.anime AS a " +
            "WHERE a.title = ?1")
    int countUserAnimeEntityByAnimeTitle(String animeTitle);


}