package com.example.chibiotaku.domain.views;

import com.example.chibiotaku.domain.enums.AnimeTypeEnum;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import com.example.chibiotaku.domain.enums.WatchStatusEnum;

import java.time.LocalDate;
import java.util.UUID;

public class TopAnimeVM {

    private UUID id;
    private int counter;
    private double score;
    private LocalDate aired;
    private AnimeTypeEnum type;
    private int eps;
    private WatchStatusEnum watchStatus;
    private String imageUrl;
    private String title;
    private StatusObjectEnum statusObject;

    public TopAnimeVM() {
    }

    public StatusObjectEnum getStatusObject() {
        return statusObject;
    }

    public TopAnimeVM setStatusObject(StatusObjectEnum statusObject) {
        this.statusObject = statusObject;
        return this;
    }

    public int getEps() {
        return eps;
    }

    public TopAnimeVM setEps(int eps) {
        this.eps = eps;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TopAnimeVM setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public TopAnimeVM setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public int getCounter() {
        return counter;
    }

    public double getScore() {
        return score;
    }

    public LocalDate getAired() {
        return aired;
    }

    public AnimeTypeEnum getType() {
        return type;
    }



    public WatchStatusEnum getWatchStatus() {
        return watchStatus;
    }

    public TopAnimeVM setId(UUID id) {
        this.id = id;
        return this;
    }

    public TopAnimeVM setCounter(int counter) {
        this.counter = counter;
        return this;
    }

    public TopAnimeVM setScore(double score) {
        this.score = score;
        return this;
    }

    public TopAnimeVM setAired(LocalDate aired) {
        this.aired = aired;
        return this;
    }

    public TopAnimeVM setType(AnimeTypeEnum type) {
        this.type = type;
        return this;
    }



    public TopAnimeVM setWatchStatus(WatchStatusEnum watchStatus) {
        this.watchStatus = watchStatus;
        return this;
    }
}
