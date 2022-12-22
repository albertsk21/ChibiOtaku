package com.example.chibiotaku.repo;

import com.example.chibiotaku.domain.entities.ReviewAnimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewAnimeRepository extends JpaRepository<ReviewAnimeEntity, UUID> {

    @Query("SELECT u.username , r.created, r.statusReview, r.content  FROM ReviewAnimeEntity AS r " +
            "JOIN r.anime AS a " +
            "JOIN r.user AS u " +
            "WHERE a.id = ?1 " +
            "ORDER BY r.created DESC ")
    List<Object[]> findReviewAnimeEntitiesByAnimeId(UUID id);

    @Query("FROM ReviewAnimeEntity AS ra " +
            "WHERE ra.verified = ?1")
    List<ReviewAnimeEntity> findReviewAnimeEntitiesByVerified(boolean verified);

    @Query("SELECT a.id, ra.content, a.title, ra.created, a.imageUrl, u.username FROM ReviewAnimeEntity AS ra " +
            "JOIN ra.anime AS a " +
            "JOIN ra.user AS u " +
            "GROUP BY a.id " +
            "ORDER BY ra.created DESC")
    List<Object[]> findReviewAnimeEntitiesOrderByCreated();
;}