package com.example.chibiotaku.domain.api;


import java.time.LocalDate;
import java.util.UUID;

public class ReviewApiDto {

    private UUID id;
    private String content;
    private String animeTitle;
    private LocalDate published;
    private String imageUrl;
    private String username;

    public ReviewApiDto() {
    }


    public ReviewApiDto setContent(String content) {
        this.content = content;
        return this;
    }

    public ReviewApiDto setAnimeTitle(String animeTitle) {
        this.animeTitle = animeTitle;
        return this;
    }

    public ReviewApiDto setPublished(LocalDate published) {
        this.published = published;
        return this;
    }

    public ReviewApiDto setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public ReviewApiDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public ReviewApiDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAnimeTitle() {
        return animeTitle;
    }

    public LocalDate getPublished() {
        return published;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUsername() {
        return username;
    }
}