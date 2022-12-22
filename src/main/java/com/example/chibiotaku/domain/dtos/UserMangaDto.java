package com.example.chibiotaku.domain.dtos;

import com.example.chibiotaku.domain.enums.WatchStatusEnum;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * A DTO for the {@link com.example.chibiotaku.domain.entities.UserAnimeEntity} entity
 */
public class UserMangaDto implements Serializable {
    private UUID id;
    private double score;
    private LocalDate created;
    private WatchStatusEnum watchStatus;
    private int volumes;
    private String content;
    private String imageUrl;
    private String title;





    public UserMangaDto() {
    }

    public String getTitle() {
        return title;
    }

    public UserMangaDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public UserMangaDto setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
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

    public UserMangaDto setScore(double score) {
        this.score = score;
        return this;
    }

    public UserMangaDto setCreated(LocalDate created) {
        this.created = created;
        return this;
    }

    public UserMangaDto setWatchStatus(WatchStatusEnum watchStatus) {
        this.watchStatus = watchStatus;
        return this;
    }


    public int getVolumes() {
        return volumes;
    }

    public UserMangaDto setVolumes(int volumes) {
        this.volumes = volumes;
        return this;
    }

    public UserMangaDto setId(UUID id) {
        this.id = id;
        return this;
    }



    public String getContent() {
        return content;
    }

    public UserMangaDto setContent(String content) {
        this.content = content;
        return this;
    }
}