package com.example.chibiotaku.domain.biding;

import com.example.chibiotaku.util.annotations.NotNegativeOrZero;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class MangaBM {

    private String title;
    private String volumes;
    private String authors;
    private String genres;
    private String rating;
    private Integer ranked;
    private Integer members;
    private Integer popularity;
    private LocalDate published;
    private String imageUrl;
    private Integer chapters;
    private String themes;
    private String content;

    public MangaBM() {
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
    @NotEmpty
    public String getRating() {
        return rating;
    }
    @NotNegativeOrZero
    public Integer getRanked() {
        return ranked;
    }
    @NotNegativeOrZero
    public Integer getMembers() {
        return members;
    }
    @NotNegativeOrZero
    public Integer getPopularity() {
        return popularity;
    }
    @DateTimeFormat(pattern="yyyy-MM-dd")
    public LocalDate getPublished() {
        return published;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    @NotNegativeOrZero
    public Integer getChapters() {
        return chapters;
    }
    public String getThemes() {
        return themes;
    }
    @Size(min = 200, message = "content must have minimum 200 characters!")
    public String getContent() {
        return content;
    }

    public MangaBM setTitle(String title) {
        this.title = title;
        return this;
    }
    public MangaBM setVolumes(String volumes) {
        this.volumes = volumes;
        return this;
    }
    public MangaBM setAuthors(String authors) {
        this.authors = authors;
        return this;
    }
    public MangaBM setGenres(String genres) {
        this.genres = genres;
        return this;
    }
    public MangaBM setRating(String rating) {
        this.rating = rating;
        return this;
    }
    public MangaBM setRanked(Integer ranked) {
        this.ranked = ranked;
        return this;
    }
    public MangaBM setContent(String content) {
        this.content = content;
        return this;
    }
    public MangaBM setMembers(Integer members) {
        this.members = members;
        return this;
    }
    public MangaBM setPopularity(Integer popularity) {
        this.popularity = popularity;
        return this;
    }
    public MangaBM setPublished(LocalDate published) {
        this.published = published;
        return this;
    }
    public MangaBM setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
    public MangaBM setChapters(Integer chapters) {
        this.chapters = chapters;
        return this;
    }
    public MangaBM setThemes(String themes) {
        this.themes = themes;
        return this;
    }
}
