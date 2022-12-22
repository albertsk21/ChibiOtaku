package com.example.chibiotaku.domain.views;

import com.example.chibiotaku.domain.enums.AnimeTypeEnum;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;

import java.time.LocalDate;
import java.util.UUID;

public class MangaDetailsVM {
    private UUID id;
    private String title;
    private String volumes;
    private String authors;
    private String genres;
    private String rating;
    private int ranked;
    private int members;
    private int popularity;
    private LocalDate published;
    private String imageUrl;
    private int chapters;
    private String themes;
    private String content;
    private AnimeTypeEnum type;
    private StatusObjectEnum statusObject;

    public MangaDetailsVM() {
    }

    public StatusObjectEnum getStatusObject() {
        return statusObject;
    }

    public MangaDetailsVM setStatusObject(StatusObjectEnum statusObject) {
        this.statusObject = statusObject;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public MangaDetailsVM setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getVolumes() {
        return volumes;
    }

    public String getAuthors() {
        return authors;
    }

    public String getGenres() {
        return genres;
    }

    public String getRating() {
        return rating;
    }

    public int getRanked() {
        return ranked;
    }

    public int getMembers() {
        return members;
    }

    public int getPopularity() {
        return popularity;
    }

    public LocalDate getPublished() {
        return published;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getChapters() {
        return chapters;
    }

    public String getThemes() {
        return themes;
    }

    public String getContent() {
        return content;
    }

    public AnimeTypeEnum getType() {
        return type;
    }

    public MangaDetailsVM setType(AnimeTypeEnum type) {
        this.type = type;
        return this;
    }

    public MangaDetailsVM setTitle(String title) {
        this.title = title;
        return this;
    }

    public MangaDetailsVM setVolumes(String volumes) {
        this.volumes = volumes;
        return this;
    }

    public MangaDetailsVM setAuthors(String authors) {
        this.authors = authors;
        return this;
    }

    public MangaDetailsVM setGenres(String genres) {
        this.genres = genres;
        return this;
    }

    public MangaDetailsVM setRating(String rating) {
        this.rating = rating;
        return this;
    }

    public MangaDetailsVM setRanked(int ranked) {
        this.ranked = ranked;
        return this;
    }

    public MangaDetailsVM setMembers(int members) {
        this.members = members;
        return this;
    }

    public MangaDetailsVM setPopularity(int popularity) {
        this.popularity = popularity;
        return this;
    }

    public MangaDetailsVM setPublished(LocalDate published) {
        this.published = published;
        return this;
    }

    public MangaDetailsVM setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public MangaDetailsVM setChapters(int chapters) {
        this.chapters = chapters;
        return this;
    }

    public MangaDetailsVM setThemes(String themes) {
        this.themes = themes;
        return this;
    }

    public MangaDetailsVM setContent(String content) {
        this.content = content;
        return this;
    }
}
