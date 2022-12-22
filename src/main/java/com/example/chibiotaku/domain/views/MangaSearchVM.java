package com.example.chibiotaku.domain.views;

import com.example.chibiotaku.domain.enums.AnimeTypeEnum;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;

import java.util.UUID;

public class MangaSearchVM {

    private UUID id;
    private String title;

    private int volumes;
    private int popularity;
    private String imageUrl;
    private String content;
    private int chapters;
    private StatusObjectEnum statusObject;

    public MangaSearchVM() {
    }

    public StatusObjectEnum getStatusObject() {
        return statusObject;
    }

    public MangaSearchVM setStatusObject(StatusObjectEnum statusObject) {
        this.statusObject = statusObject;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getContent() {
        return content;
    }

    public MangaSearchVM setContent(String content) {
        this.content = content;
        return this;
    }

    public MangaSearchVM setTitle(String title) {
        this.title = title;
        return this;
    }


    public int getVolumes() {
        return volumes;
    }

    public MangaSearchVM setVolumes(int volumes) {
        this.volumes = volumes;
        return this;
    }

    public MangaSearchVM setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public int getPopularity() {
        return popularity;
    }

    public MangaSearchVM setPopularity(int popularity) {
        this.popularity = popularity;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public MangaSearchVM setId(UUID id) {
        this.id = id;
        return this;
    }

    public int getChapters() {
        return chapters;
    }

    public MangaSearchVM setChapters(int chapters) {
        this.chapters = chapters;
        return this;
    }
}
