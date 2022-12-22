package com.example.chibiotaku.domain.views;

import com.example.chibiotaku.domain.enums.WatchStatusEnum;

import java.time.LocalDate;
import java.util.UUID;

public class UserMangaVM {
    private UUID id;
    private double score;
    private LocalDate created;
    private WatchStatusEnum watchStatus;
    private String content;
    private String title;
    private int volumes;
    private String imageUrl;


    public UserMangaVM() {
    }

    public UUID getId() {
        return id;
    }

    public UserMangaVM setId(UUID id) {
        this.id = id;
        return this;
    }

    public double getScore() {
        return score;
    }

    public UserMangaVM setScore(double score) {
        this.score = score;
        return this;
    }

    public LocalDate getCreated() {
        return created;
    }

    public UserMangaVM setCreated(LocalDate created) {
        this.created = created;
        return this;
    }

    public WatchStatusEnum getWatchStatus() {
        return watchStatus;
    }

    public UserMangaVM setWatchStatus(WatchStatusEnum watchStatus) {
        this.watchStatus = watchStatus;
        return this;
    }

    public String getContent() {
        return content;
    }

    public UserMangaVM setContent(String content) {
        this.content = content;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public UserMangaVM setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getVolumes() {
        return volumes;
    }

    public UserMangaVM setVolumes(int volumes) {
        this.volumes = volumes;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public UserMangaVM setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
