package com.example.chibiotaku.domain.dtos;

import com.example.chibiotaku.domain.entities.AnimeEntity;
import com.example.chibiotaku.domain.enums.AnimeTypeEnum;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link AnimeEntity} entity
 */
public class AnimeDto implements Serializable {

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
    private StatusObjectEnum statusObject;

    public AnimeTypeEnum getType() {
        return type;
    }

    public AnimeDto setType(AnimeTypeEnum type) {
        this.type = type;
        return this;
    }
    public String getTitle() {
        return title;
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimeDto entity = (AnimeDto) o;
        return Objects.equals(this.title, entity.title) &&
                Objects.equals(this.type, entity.type) &&
                Objects.equals(this.episodes, entity.episodes) &&
                Objects.equals(this.aired, entity.aired) &&
                Objects.equals(this.producers, entity.producers) &&
                Objects.equals(this.studios, entity.studios) &&
                Objects.equals(this.source, entity.source) &&
                Objects.equals(this.genres, entity.genres) &&
                Objects.equals(this.duration, entity.duration) &&
                Objects.equals(this.rating, entity.rating) &&
                Objects.equals(this.ranked, entity.ranked) &&
                Objects.equals(this.members, entity.members) &&
                Objects.equals(this.imageUrl, entity.imageUrl) &&
                Objects.equals(this.popularity, entity.popularity);
    }


    public AnimeDto setContent(String content) {
        this.content = content;
        return this;
    }
    public AnimeDto setTitle(String title) {
        this.title = title;
        return this;
    }


    public AnimeDto setEpisodes(int episodes) {
        this.episodes = episodes;
        return this;
    }

    public AnimeDto setAired(LocalDate aired) {
        this.aired = aired;
        return this;
    }

    public AnimeDto setProducers(String producers) {
        this.producers = producers;
        return this;
    }

    public AnimeDto setStudios(String studios) {
        this.studios = studios;
        return this;
    }

    public AnimeDto setSource(String source) {
        this.source = source;
        return this;
    }

    public AnimeDto setGenres(String genres) {
        this.genres = genres;
        return this;
    }

    public AnimeDto setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public AnimeDto setRating(String rating) {
        this.rating = rating;
        return this;
    }

    public AnimeDto setRanked(int ranked) {
        this.ranked = ranked;
        return this;
    }

    public AnimeDto setMembers(int members) {
        this.members = members;
        return this;
    }

    public AnimeDto setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public AnimeDto setPopularity(int popularity) {
        this.popularity = popularity;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public AnimeDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public StatusObjectEnum getStatusObject() {
        return statusObject;
    }

    public AnimeDto setStatusObject(StatusObjectEnum statusObject) {
        this.statusObject = statusObject;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, type, episodes, aired, producers, studios, source, genres, duration, rating, ranked, members, imageUrl, popularity);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "title = " + title + ", " +
                "type = " + type + ", " +
                "episodes = " + episodes + ", " +
                "aired = " + aired + ", " +
                "producers = " + producers + ", " +
                "studios = " + studios + ", " +
                "source = " + source + ", " +
                "genres = " + genres + ", " +
                "duration = " + duration + ", " +
                "rating = " + rating + ", " +
                "ranked = " + ranked + ", " +
                "members = " + members + ", " +
                "imageUrl = " + imageUrl + ", " +
                "popularity = " + popularity + ")";
    }
}