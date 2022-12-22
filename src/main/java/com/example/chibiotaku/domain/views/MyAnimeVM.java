package com.example.chibiotaku.domain.views;

import com.example.chibiotaku.domain.enums.AnimeTypeEnum;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;

import java.util.UUID;

public class MyAnimeVM {

    private UUID id;
    private String title;
    private String content;
    private StatusObjectEnum statusObject;
    private int episodes;
    private AnimeTypeEnum type;
    private String imageUrl;


    public MyAnimeVM() {
    }


    public UUID getId() {
        return id;
    }

    public MyAnimeVM setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public StatusObjectEnum getStatusObject() {
        return statusObject;
    }

    public int getEpisodes() {
        return episodes;
    }

    public AnimeTypeEnum getType() {
        return type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public MyAnimeVM setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public MyAnimeVM setTitle(String title) {
        this.title = title;
        return this;
    }

    public MyAnimeVM setContent(String content) {
        this.content = content;
        return this;
    }

    public MyAnimeVM setStatusObject(StatusObjectEnum statusObject) {
        this.statusObject = statusObject;
        return this;
    }

    public MyAnimeVM setEpisodes(int episodes) {
        this.episodes = episodes;
        return this;
    }

    public MyAnimeVM setType(AnimeTypeEnum type) {
        this.type = type;
        return this;
    }
}
