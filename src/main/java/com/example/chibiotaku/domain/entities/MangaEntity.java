package com.example.chibiotaku.domain.entities;

import com.example.chibiotaku.domain.enums.StatusObjectEnum;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "mangas")
public class MangaEntity extends BaseEntity {
    private String title;
    private String volumes;
    private String authors;
    private String genres;
    private String rating;
    private int ranked;
    private int members;
    private int popularity;
    private LocalDate published;
    private String imageUrl;
    private int chapters;
    private String themes;
    private String content;
    private StatusObjectEnum statusObject;
    private LocalDate created;

    private UserEntity user;
    private List<ReviewMangaEntity> reviewMangas;

    public MangaEntity() {
    }

    public String getTitle() {
        return title;
    }
    public String getVolumes() {
        return volumes;
    }
    public String getAuthors() {
        return authors;
    }
    public String getGenres() {
        return genres;
    }
    public String getRating() {
        return rating;
    }
    public int getRanked() {
        return ranked;
    }
    public int getMembers() {
        return members;
    }
    public int getPopularity() {
        return popularity;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public LocalDate getPublished() {
        return published;
    }
    @Enumerated(EnumType.STRING)
    public StatusObjectEnum getStatusObject() {
        return statusObject;
    }
    public LocalDate getCreated() {
        return created;
    }

    @ManyToOne
    @JoinColumn(name = "user")
    public UserEntity getUser() {
        return user;
    }
    @OneToMany(mappedBy = "manga")
    public List<ReviewMangaEntity> getReviewMangas() {
        return reviewMangas;
    }


    public int getChapters() {
        return chapters;
    }

    public String getThemes() {
        return themes;
    }

    @Column(columnDefinition = "TEXT")
    public String getContent() {
        return content;
    }

    public MangaEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public MangaEntity setReviewMangas(List<ReviewMangaEntity> reviewMangas) {
        this.reviewMangas = reviewMangas;
        return this;
    }
    public MangaEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }
    public MangaEntity setStatusObject(StatusObjectEnum statusObject) {
        this.statusObject = statusObject;
        return this;
    }
    public MangaEntity setRanked(int ranked) {
        this.ranked = ranked;
        return this;
    }
    public MangaEntity setTitle(String title) {
        this.title = title;
        return this;
    }
    public MangaEntity setVolumes(String volumes) {
        this.volumes = volumes;
        return this;
    }
    public MangaEntity setAuthors(String authors) {
        this.authors = authors;
        return this;
    }

    public MangaEntity setGenres(String genres) {
        this.genres = genres;
        return this;
    }
    public MangaEntity setRating(String rating) {
        this.rating = rating;
        return this;
    }
    public MangaEntity setMembers(int members) {
        this.members = members;
        return this;
    }
    public MangaEntity setPopularity(int popularity) {
        this.popularity = popularity;
        return this;
    }
    public MangaEntity setPublished(LocalDate published) {
        this.published = published;
        return this;
    }
    public MangaEntity setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public MangaEntity setChapters(int chapters) {
        this.chapters = chapters;
        return this;
    }

    public MangaEntity setThemes(String themes) {
        this.themes = themes;
        return this;
    }

    public MangaEntity setCreated(LocalDate created) {
        this.created = created;
        return this;
    }
}
