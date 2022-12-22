package com.example.chibiotaku.repo;

import com.example.chibiotaku.domain.entities.AnimeEntity;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnimeRepository extends JpaRepository<AnimeEntity, UUID> {

    @Query("FROM AnimeEntity AS a WHERE a.id = ?1")
    Optional<AnimeEntity> findAnimeEntityById(UUID id);




    @Query("SELECT a.id ,a.title, a.type, a.episodes, a.popularity, a.imageUrl, a.content, a.statusObject FROM AnimeEntity AS a " +
           "WHERE LOWER(a.title) LIKE CONCAT('%', LOWER(?1) , '%')")
    List<Object[]> findAnimeEntityContainsTitle(String title);



    @Query("FROM AnimeEntity AS a " +
            "JOIN a.user AS u " +
            "WHERE u.username = ?1 " +
            "ORDER BY a.created DESC")
    List<AnimeEntity> findAnimeEntitiesByUserUsername(String username);
    // AND a.statusObject = 'ACCEPTED'


    @Query("FROM AnimeEntity AS a " +
            "WHERE a.statusObject = 'ACCEPTED'" +
            "ORDER BY a.popularity, a.ranked ")
    List<AnimeEntity> findAllOrderByPopularityAndRanked();


    @Query("FROM AnimeEntity AS a " +
            "WHERE a.statusObject = ?1")
    List<AnimeEntity> findAnimeEntitiesByStatusObject(StatusObjectEnum statusObject);

    @Query("FROM AnimeEntity AS a " +
            "ORDER BY a.popularity ASC")
    List<AnimeEntity> findAnimeEntitiesOrderByPopularityASC();
}