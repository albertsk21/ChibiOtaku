package com.example.chibiotaku.domain.entities;

import com.example.chibiotaku.domain.enums.AnimeTypeEnum;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "animes")
public class AnimeEntity extends BaseEntity {

    private String title;
    private AnimeTypeEnum type;
    private int episodes;
    private LocalDate aired;
    private String producers;
    private String studios;
    private String source;
    private String genres;
    private int duration;
    private String rating;
    private int ranked;
    private int members;
    private String imageUrl;
    private int popularity;
    private String content;
    private StatusObjectEnum statusObject;
    private LocalDate created;
    private UserEntity user;
    private List<UserAnimeEntity> watchAnimes;
    private List<ReviewAnimeEntity> reviewAnimes;

    public AnimeEntity() {
    }

    public String getTitle() {
        return title;
    }

    @Enumerated(EnumType.STRING)
    public AnimeTypeEnum getType() {
        return type;
    }

    public int getEpisodes() {
        return episodes;
    }
    public LocalDate getAired() {
        return aired;
    }
    public String getProducers() {
        return producers;
    }
    public String getStudios() {
        return studios;
    }
    public String getSource() {
        return source;
    }
    public String getGenres() {
        return genres;
    }
    public int getDuration() {
        return duration;
    }
    public String getRating() {
        return rating;
    }
    @Enumerated(EnumType.STRING)
    public StatusObjectEnum getStatusObject() {
        return statusObject;
    }
    public int getMembers() {
        return members;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public int getRanked() {
        return ranked;
    }
    public int getPopularity() {
        return popularity;
    }
    @Column(columnDefinition = "TEXT")
    public String getContent() {
        return content;
    }
    public LocalDate getCreated() {
        return created;
    }

    @ManyToOne
    @JoinColumn(name = "user")
    public UserEntity getUser() {
        return user;
    }

    @OneToMany(mappedBy = "anime")
    public List<UserAnimeEntity> getWatchAnimes() {
        return watchAnimes;
    }
    @OneToMany(mappedBy = "anime")
    public List<ReviewAnimeEntity> getReviewAnimes() {
        return reviewAnimes;
    }









    public AnimeEntity setReviewAnimes(List<ReviewAnimeEntity> reviewAnimes) {
        this.reviewAnimes = reviewAnimes;
        return this;
    }
    public AnimeEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }
    public AnimeEntity setRanked(int ranked) {
        this.ranked = ranked;
        return this;
    }
    public AnimeEntity setWatchAnimes(List<UserAnimeEntity> watchAnimes) {
        this.watchAnimes = watchAnimes;
        return this;
    }
    public AnimeEntity setStatusObject(StatusObjectEnum statusObject) {
        this.statusObject = statusObject;
        return this;
    }
    public AnimeEntity setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
    public AnimeEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public AnimeEntity setType(AnimeTypeEnum type) {
        this.type = type;
        return this;
    }

    public AnimeEntity setEpisodes(int episodes) {
        this.episodes = episodes;
        return this;
    }
    public AnimeEntity setAired(LocalDate aired) {
        this.aired = aired;
        return this;
    }
    public AnimeEntity setProducers(String producers) {
        this.producers = producers;
        return this;
    }
    public AnimeEntity setStudios(String studios) {
        this.studios = studios;
        return this;
    }
    public AnimeEntity setSource(String source) {
        this.source = source;
        return this;
    }
    public AnimeEntity setGenres(String genres) {
        this.genres = genres;
        return this;
    }
    public AnimeEntity setDuration(int duration) {
        this.duration = duration;
        return this;
    }
    public AnimeEntity setRating(String rating) {
        this.rating = rating;
        return this;
    }
    public AnimeEntity setRank(int ranked) {
        this.ranked = ranked;
        return this;
    }
    public AnimeEntity setMembers(int members) {
        this.members = members;
        return this;
    }
    public AnimeEntity setPopularity(int popularity) {
        this.popularity = popularity;
        return this;
    }

    public AnimeEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public AnimeEntity setCreated(LocalDate created) {
        this.created = created;
        return this;
    }
}
