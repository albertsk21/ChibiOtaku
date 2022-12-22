package com.example.chibiotaku.domain.entities;

import com.example.chibiotaku.domain.enums.StatusReviewEnum;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reviews_anime")
public class ReviewAnimeEntity extends BaseEntity {

    private String content;
    private StatusReviewEnum statusReview;
    private AnimeEntity anime;
    private UserEntity user;
    private LocalDate created;
    private boolean verified;

    @ManyToOne
    @JoinColumn(name = "anime_id")
    public AnimeEntity getAnime() {
        return anime;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    public UserEntity getUser() {
        return user;
    }

    @Column(columnDefinition = "TEXT")
    public String getContent() {
        return content;
    }

    @Enumerated(EnumType.STRING)
    public StatusReviewEnum getStatusReview() {
        return statusReview;
    }

    public ReviewAnimeEntity setAnime(AnimeEntity anime) {
        this.anime = anime;
        return this;
    }

    public ReviewAnimeEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public ReviewAnimeEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public ReviewAnimeEntity setStatusReview(StatusReviewEnum statusReview) {
        this.statusReview = statusReview;
        return this;
    }

    public LocalDate getCreated() {
        return created;
    }

    public ReviewAnimeEntity setCreated(LocalDate created) {
        this.created = created;
        return this;
    }

    public boolean isVerified() {
        return verified;
    }

    public ReviewAnimeEntity setVerified(boolean verified) {
        this.verified = verified;
        return this;
    }
}
