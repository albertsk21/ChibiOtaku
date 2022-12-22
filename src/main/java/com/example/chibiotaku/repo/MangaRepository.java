package com.example.chibiotaku.repo;

import com.example.chibiotaku.domain.entities.MangaEntity;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface MangaRepository extends JpaRepository<MangaEntity, UUID> {

    @Query("FROM MangaEntity AS a WHERE a.id = ?1")
    Optional<MangaEntity> findMangaEntityById(UUID id);

    @Query("SELECT m.id ,m.title, m.chapters , m.volumes, m.popularity, m.imageUrl, m.content, m.statusObject FROM MangaEntity AS m " +
            "WHERE LOWER(m.title) LIKE CONCAT('%', LOWER(?1) , '%')")
    List<Object[]> findMangaEntityContainsTitle(String title);


    @Query("FROM MangaEntity AS m " +
            "JOIN m.user AS u " +
            "WHERE u.username = ?1 " +
            "ORDER BY m.created DESC ")
    List<MangaEntity> findMangaEntitiesByUsername(String username);


    @Query("FROM MangaEntity AS m " +
            "WHERE m.statusObject = ?1")
    List<MangaEntity> findMangaEntitiesByStatusObject(StatusObjectEnum status);

    @Query("FROM MangaEntity AS m " +
            "ORDER BY m.popularity ASC")
    List<MangaEntity> findMangaEntitiesOrderByPopularityASC();

}