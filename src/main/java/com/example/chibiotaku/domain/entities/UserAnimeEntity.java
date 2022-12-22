package com.example.chibiotaku.domain.entities;

import com.example.chibiotaku.domain.enums.WatchStatusEnum;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "user_animes")
public class UserAnimeEntity extends BaseEntity{


    private int score;
    private WatchStatusEnum watchStatus;
    private UserEntity user;
    private AnimeEntity anime;

    private LocalDate created;
    private LocalDate modified;

    @Enumerated(EnumType.STRING)
    public WatchStatusEnum getWatchStatus() {
        return watchStatus;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    public UserEntity getUser() {
        return user;
    }

    @ManyToOne
    @JoinColumn(name = "anime_id")
    public AnimeEntity getAnime() {
        return anime;
    }


    public int getScore() {
        return score;
    }

    public UserAnimeEntity setScore(int score) {
        this.score = score;
        return this;
    }

    public LocalDate getCreated() {
        return created;
    }

    public LocalDate getModified() {
        return modified;
    }

    public UserAnimeEntity setCreated(LocalDate created) {
        this.created = created;
        return this;
    }

    public UserAnimeEntity setModified(LocalDate modified) {
        this.modified = modified;
        return this;
    }

    public UserAnimeEntity setWatchStatus(WatchStatusEnum watchStatus) {
        this.watchStatus = watchStatus;
        return this;
    }
    public UserAnimeEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }
    public UserAnimeEntity setAnime(AnimeEntity anime) {
        this.anime = anime;
        return this;
    }
}
