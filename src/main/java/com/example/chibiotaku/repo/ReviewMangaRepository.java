package com.example.chibiotaku.repo;

import com.example.chibiotaku.domain.entities.ReviewMangaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewMangaRepository extends JpaRepository<ReviewMangaEntity, UUID> {

    @Query("SELECT u.username , r.created, r.statusReview, r.content  FROM ReviewMangaEntity AS r " +
            "JOIN r.manga AS a " +
            "JOIN r.user AS u " +
            "WHERE a.id = ?1 " +
            "ORDER BY r.created DESC ")
    List<Object[]> findReviewMangaEntitiesByMangaId(UUID id);


    @Query("FROM ReviewMangaEntity AS ra " +
            "WHERE ra.verified = ?1")
    List<ReviewMangaEntity> findReviewMangaEntitiesByVerified(boolean verified);
}