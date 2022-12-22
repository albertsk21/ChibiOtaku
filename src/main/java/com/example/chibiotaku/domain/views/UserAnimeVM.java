package com.example.chibiotaku.domain.views;

import com.example.chibiotaku.domain.enums.AnimeTypeEnum;
import com.example.chibiotaku.domain.enums.WatchStatusEnum;

import java.time.LocalDate;
import java.util.UUID;

public class UserAnimeVM {
    private UUID id;
    private double score;
    private LocalDate created;
    private WatchStatusEnum watchStatus;
    private String content;
    private String title;
    private AnimeTypeEnum animeType;
    private int eps;
    private String imageUrl;


    public UserAnimeVM() {
    }

    public UUID getId() {
        return id;
    }

    public double getScore() {
        return score;
    }

    public LocalDate getCreated() {
        return created;
    }

    public WatchStatusEnum getWatchStatus() {
        return watchStatus;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public AnimeTypeEnum getAnimeType() {
        return animeType;
    }

    public int getEps() {
        return eps;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public UserAnimeVM setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public UserAnimeVM setId(UUID id) {
        this.id = id;
        return this;
    }

    public UserAnimeVM setScore(double score) {
        this.score = score;
        return this;
    }

    public UserAnimeVM setCreated(LocalDate created) {
        this.created = created;
        return this;
    }

    public UserAnimeVM setWatchStatus(WatchStatusEnum watchStatus) {
        this.watchStatus = watchStatus;
        return this;
    }

    public UserAnimeVM setContent(String content) {
        this.content = content;
        return this;
    }

    public UserAnimeVM setTitle(String title) {
        this.title = title;
        return this;
    }

    public UserAnimeVM setAnimeType(AnimeTypeEnum animeType) {
        this.animeType = animeType;
        return this;
    }

    public UserAnimeVM setEps(int eps) {
        this.eps = eps;
        return this;
    }
}
