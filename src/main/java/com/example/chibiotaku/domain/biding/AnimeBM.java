package com.example.chibiotaku.domain.biding;

import com.example.chibiotaku.util.annotations.NotNegativeOrZero;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

public class AnimeBM {

    private String title;
    private Integer episodes;
    private LocalDate aired;
    private String producers;
    private String studios;
    private String source;
    private String genres;


    private Integer duration;
    @NotEmpty
    private String rating;
    @NotNegativeOrZero

    private Integer ranked;
    @NotNegativeOrZero
    private Integer members;
    @NotEmpty
    private String imageUrl;
    private Integer popularity;
    private String type;
    private String content;
    @NotEmpty
    public String getTitle() {
        return title;
    }
    @NotNegativeOrZero
    public Integer getEpisodes() {
        return episodes;
    }
    @DateTimeFormat(pattern="yyyy-MM-dd")
    public LocalDate getAired() {
        return aired;
    }

    @NotEmpty
    public String getProducers() {
        return producers;
    }
    @NotEmpty
    public String getStudios() {
        return studios;
    }
    @NotEmpty
    public String getSource() {
        return source;
    }
    @NotEmpty
    public String getGenres() {
        return genres;
    }
    @NotNegativeOrZero
    public Integer getDuration() {
        return duration;
    }

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
    @NotEmpty
    public String getImageUrl() {
        return imageUrl;
    }
    @NotNegativeOrZero
    public Integer getPopularity() {
        return popularity;
    }
    public String getType(){
        return type;
    }
    @Size(min = 200, message = "content must have minimum 200 characters!")
    public String getContent() {
        return content;
    }

    
    public AnimeBM setType(String type) {
        this.type = type;
        return this;
    }

    public AnimeBM setTitle(String title) {
        this.title = title;
        return this;
    }

    public AnimeBM setContent(String content) {
        this.content = content;
        return this;
    }
    public AnimeBM setEpisodes(Integer episodes) {
        this.episodes = episodes;
        return this;
    }

    public AnimeBM setAired(LocalDate aired) {
        this.aired = aired;
        return this;
    }

    public AnimeBM setProducers(String producers) {
        this.producers = producers;
        return this;
    }

    public AnimeBM setStudios(String studios) {
        this.studios = studios;
        return this;
    }

    public AnimeBM setSource(String source) {
        this.source = source;
        return this;
    }

    public AnimeBM setGenres(String genres) {
        this.genres = genres;
        return this;
    }

    public AnimeBM setDuration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public AnimeBM setRating(String rating) {
        this.rating = rating;
        return this;
    }

    public AnimeBM setRanked(Integer ranked) {
        this.ranked = ranked;
        return this;
    }

    public AnimeBM setMembers(Integer members) {
        this.members = members;
        return this;
    }

    public AnimeBM setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public AnimeBM setPopularity(Integer popularity) {
        this.popularity = popularity;
        return this;
    }
}
