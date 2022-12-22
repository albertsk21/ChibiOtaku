package com.example.chibiotaku.domain.entities;

import com.example.chibiotaku.domain.enums.WatchStatusEnum;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "user_mangas")
public class UserMangaEntity extends BaseEntity {

    private int score;
    private WatchStatusEnum watchStatus;
    private UserEntity user;
    private MangaEntity manga;
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
    @JoinColumn(name = "manga_id")
    public MangaEntity getManga() {
        return manga;
    }
    public LocalDate getCreated() {
        return created;
    }
    public LocalDate getModified() {
        return modified;
    }
    public int getScore() {
        return score;
    }



    public UserMangaEntity setScore(int score) {
        this.score = score;
        return this;
    }
    public UserMangaEntity setCreated(LocalDate created) {
        this.created = created;
        return this;
    }
    public UserMangaEntity setModified(LocalDate modified) {
        this.modified = modified;
        return this;
    }
    public UserMangaEntity setWatchStatus(WatchStatusEnum watchStatus) {
        this.watchStatus = watchStatus;
        return this;
    }
    public UserMangaEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }
    public UserMangaEntity setManga(MangaEntity manga) {
        this.manga = manga;
        return this;
    }
}
