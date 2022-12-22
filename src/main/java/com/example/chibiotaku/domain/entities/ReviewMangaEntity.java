package com.example.chibiotaku.domain.entities;

import com.example.chibiotaku.domain.enums.StatusReviewEnum;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reviews_mangas" )
public class ReviewMangaEntity extends  BaseEntity {

    private String content;
    private StatusReviewEnum statusReview;
    private UserEntity user;
    private MangaEntity manga;
    private LocalDate created;
    private boolean verified;

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

    @ManyToOne
    @JoinColumn(name = "manga_id")
    public MangaEntity getManga() {
        return manga;
    }

    public ReviewMangaEntity setManga(MangaEntity manga) {
        this.manga = manga;
        return this;
    }
    public ReviewMangaEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }
    public ReviewMangaEntity setContent(String content) {
        this.content = content;
        return this;
    }
    public ReviewMangaEntity setStatusReview(StatusReviewEnum statusReview) {
        this.statusReview = statusReview;
        return this;
    }

    public LocalDate getCreated() {
        return created;
    }

    public ReviewMangaEntity setCreated(LocalDate created) {
        this.created = created;
        return this;
    }

    public boolean isVerified() {
        return verified;
    }

    public ReviewMangaEntity setVerified(boolean verified) {
        this.verified = verified;
        return this;
    }
}
