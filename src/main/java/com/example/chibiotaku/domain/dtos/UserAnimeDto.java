package com.example.chibiotaku.domain.dtos;

import com.example.chibiotaku.domain.enums.AnimeTypeEnum;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import com.example.chibiotaku.domain.enums.WatchStatusEnum;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * A DTO for the {@link com.example.chibiotaku.domain.entities.UserAnimeEntity} entity
 */
public class UserAnimeDto implements Serializable {
    private UUID id;
    private double score;
    private LocalDate created;
    private WatchStatusEnum watchStatus;
    private AnimeTypeEnum type;
    private int eps;
    private String content;
    private String imageUrl;
    private String title;
    private LocalDate aired;
    private int popularity;
    private StatusObjectEnum statusObject;


    public UserAnimeDto() {
    }

    public StatusObjectEnum getStatusObject() {
        return statusObject;
    }

    public UserAnimeDto setStatusObject(StatusObjectEnum statusObject) {
        this.statusObject = statusObject;
        return this;
    }

    public LocalDate getAired() {
        return aired;
    }

    public int getPopularity() {
        return popularity;
    }

    public UserAnimeDto setPopularity(int popularity) {
        this.popularity = popularity;
        return this;
    }

    public UserAnimeDto setAired(LocalDate aired) {
        this.aired = aired;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public UserAnimeDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public UserAnimeDto setImageUrl(String imageUrl) {
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

    public UserAnimeDto setScore(double score) {
        this.score = score;
        return this;
    }

    public UserAnimeDto setCreated(LocalDate created) {
        this.created = created;
        return this;
    }

    public UserAnimeDto setWatchStatus(WatchStatusEnum watchStatus) {
        this.watchStatus = watchStatus;
        return this;
    }


    public AnimeTypeEnum getType() {
        return type;
    }

    public UserAnimeDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public UserAnimeDto setType(AnimeTypeEnum type) {
        this.type = type;
        return this;
    }

    public int getEps() {
        return eps;
    }

    public UserAnimeDto setEps(int eps) {
        this.eps = eps;
        return this;
    }

    public String getContent() {
        return content;
    }

    public UserAnimeDto setContent(String content) {
        this.content = content;
        return this;
    }
}