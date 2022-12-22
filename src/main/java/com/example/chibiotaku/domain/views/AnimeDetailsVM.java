package com.example.chibiotaku.domain.views;

import com.example.chibiotaku.domain.enums.AnimeTypeEnum;

import java.time.LocalDate;
import java.util.UUID;

public class AnimeDetailsVM {
    private UUID id;
    private String title;
    private AnimeTypeEnum type;
    private int episodes;
    private LocalDate aired;
    private String producers;
    private String studios;
    private String source;
    private String genres;
    private int duration;
    private String rating;
    private int ranked;
    private int members;
    private String imageUrl;
    private int popularity;
    private String content;

    public String getTitle() {
        return title;
    }

    public AnimeTypeEnum getType() {
        return type;
    }

    public int getEpisodes() {
        return episodes;
    }

    public LocalDate getAired() {
        return aired;
    }

    public String getProducers() {
        return producers;
    }

    public String getStudios() {
        return studios;
    }

    public String getSource() {
        return source;
    }

    public String getGenres() {
        return genres;
    }

    public int getDuration() {
        return duration;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public int getPopularity() {
        return popularity;
    }

    public String getContent() {
        return content;
    }

    public AnimeDetailsVM setTitle(String title) {
        this.title = title;
        return this;
    }

    public AnimeDetailsVM setType(AnimeTypeEnum type) {
        this.type = type;
        return this;
    }

    public AnimeDetailsVM setEpisodes(int episodes) {
        this.episodes = episodes;
        return this;
    }

    public AnimeDetailsVM setAired(LocalDate aired) {
        this.aired = aired;
        return this;
    }

    public AnimeDetailsVM setProducers(String producers) {
        this.producers = producers;
        return this;
    }

    public AnimeDetailsVM setStudios(String studios) {
        this.studios = studios;
        return this;
    }

    public AnimeDetailsVM setSource(String source) {
        this.source = source;
        return this;
    }

    public AnimeDetailsVM setGenres(String genres) {
        this.genres = genres;
        return this;
    }

    public AnimeDetailsVM setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public AnimeDetailsVM setRating(String rating) {
        this.rating = rating;
        return this;
    }

    public AnimeDetailsVM setRanked(int ranked) {
        this.ranked = ranked;
        return this;
    }

    public AnimeDetailsVM setMembers(int members) {
        this.members = members;
        return this;
    }

    public AnimeDetailsVM setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public AnimeDetailsVM setPopularity(int popularity) {
        this.popularity = popularity;
        return this;
    }

    public AnimeDetailsVM setContent(String content) {
        this.content = content;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public AnimeDetailsVM setId(UUID id) {
        this.id = id;
        return this;
    }
}
