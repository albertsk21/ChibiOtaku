package com.example.chibiotaku.domain.views;

import com.example.chibiotaku.domain.enums.AnimeTypeEnum;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;

import java.util.UUID;

public class AnimeSearchVM {

    private UUID id;
    private String title;
    private AnimeTypeEnum type;
    private int episodes;
    private int popularity;
    private String imageUrl;
    private String content;
    private StatusObjectEnum statusObject;

    public AnimeSearchVM() {
    }

    public StatusObjectEnum getStatusObject() {
        return statusObject;
    }

    public AnimeSearchVM setStatusObject(StatusObjectEnum statusObject) {
        this.statusObject = statusObject;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public AnimeTypeEnum getType() {
        return type;
    }

    public int getEpisodes() {
        return episodes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getContent() {
        return content;
    }

    public AnimeSearchVM setContent(String content) {
        this.content = content;
        return this;
    }

    public AnimeSearchVM setTitle(String title) {
        this.title = title;
        return this;
    }

    public AnimeSearchVM setType(AnimeTypeEnum type) {
        this.type = type;
        return this;
    }

    public AnimeSearchVM setEpisodes(int episodes) {
        this.episodes = episodes;
        return this;
    }

    public AnimeSearchVM setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public int getPopularity() {
        return popularity;
    }

    public AnimeSearchVM setPopularity(int popularity) {
        this.popularity = popularity;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public AnimeSearchVM setId(UUID id) {
        this.id = id;
        return this;
    }
}
