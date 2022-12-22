package com.example.chibiotaku.domain.dtos;

import com.example.chibiotaku.domain.enums.StatusObjectEnum;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.example.chibiotaku.domain.entities.MangaEntity} entity
 */
public class MangaDto implements Serializable {

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
    private StatusObjectEnum statusObject;
    private double score;

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
    public StatusObjectEnum getStatusObject() {
        return statusObject;
    }

    public double getScore() {
        return score;
    }

    public MangaDto setScore(double score) {
        this.score = score;
        return this;
    }

    public MangaDto setStatusObject(StatusObjectEnum statusObject) {
        this.statusObject = statusObject;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public MangaDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public MangaDto setContent(String content) {
        this.content = content;
        return this;
    }

    public MangaDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public MangaDto setVolumes(String volumes) {
        this.volumes = volumes;
        return this;
    }

    public MangaDto setAuthors(String authors) {
        this.authors = authors;
        return this;
    }


    public MangaDto setGenres(String genres) {
        this.genres = genres;
        return this;
    }

    public MangaDto setRating(String rating) {
        this.rating = rating;
        return this;
    }

    public MangaDto setRanked(int ranked) {
        this.ranked = ranked;
        return this;
    }

    public MangaDto setMembers(int members) {
        this.members = members;
        return this;
    }

    public MangaDto setPopularity(int popularity) {
        this.popularity = popularity;
        return this;
    }

    public MangaDto setPublished(LocalDate published) {
        this.published = published;
        return this;
    }

    public MangaDto setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public MangaDto setChapters(int chapters) {
        this.chapters = chapters;
        return this;
    }

    public MangaDto setThemes(String themes) {
        this.themes = themes;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MangaDto entity = (MangaDto) o;
        return Objects.equals(this.title, entity.title) &&
                Objects.equals(this.volumes, entity.volumes) &&
                Objects.equals(this.authors, entity.authors) &&
                Objects.equals(this.genres, entity.genres) &&
                Objects.equals(this.rating, entity.rating) &&
                Objects.equals(this.ranked, entity.ranked) &&
                Objects.equals(this.members, entity.members) &&
                Objects.equals(this.popularity, entity.popularity) &&
                Objects.equals(this.published, entity.published) &&
                Objects.equals(this.imageUrl, entity.imageUrl) &&
                Objects.equals(this.chapters, entity.chapters) &&
                Objects.equals(this.themes, entity.themes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, volumes, authors,genres, rating, ranked, members, popularity, published, imageUrl, chapters, themes);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "title = " + title + ", " +
                "volumes = " + volumes + ", " +
                "authors = " + authors + ", " +
                "genres = " + genres + ", " +
                "rating = " + rating + ", " +
                "ranked = " + ranked + ", " +
                "members = " + members + ", " +
                "popularity = " + popularity + ", " +
                "published = " + published + ", " +
                "imageUrl = " + imageUrl + ", " +
                "chapters = " + chapters + ", " +
                "themes = " + themes + ")";
    }
}