package com.example.chibiotaku.domain.views;

import com.example.chibiotaku.domain.enums.StatusObjectEnum;

import java.time.LocalDate;
import java.util.UUID;

public class TopMangaVM {

    private UUID id;
    private int counter;
    private String title;
    private String volumes;
    private LocalDate published;
    private int chapters;
    private double score;
    private String imageUrl;
    private StatusObjectEnum statusObject;

    public TopMangaVM() {
    }

    public StatusObjectEnum getStatusObject() {
        return statusObject;
    }

    public TopMangaVM setStatusObject(StatusObjectEnum statusObject) {
        this.statusObject = statusObject;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public int getCounter() {
        return counter;
    }

    public String getTitle() {
        return title;
    }

    public String getVolumes() {
        return volumes;
    }

    public LocalDate getPublished() {
        return published;
    }

    public int getChapters() {
        return chapters;
    }

    public double getScore() {
        return score;
    }

    public TopMangaVM setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public TopMangaVM setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public TopMangaVM setCounter(int counter) {
        this.counter = counter;
        return this;
    }

    public TopMangaVM setTitle(String title) {
        this.title = title;
        return this;
    }

    public TopMangaVM setVolumes(String volumes) {
        this.volumes = volumes;
        return this;
    }

    public TopMangaVM setPublished(LocalDate published) {
        this.published = published;
        return this;
    }

    public TopMangaVM setChapters(int chapters) {
        this.chapters = chapters;
        return this;
    }

    public TopMangaVM setScore(double score) {
        this.score = score;
        return this;
    }
}
