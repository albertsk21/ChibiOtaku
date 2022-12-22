package com.example.chibiotaku.domain.dtos;

import com.example.chibiotaku.domain.enums.StatusReviewEnum;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.example.chibiotaku.domain.entities.ReviewAnimeEntity} entity
 */
public class ReviewDto implements Serializable {
    private  String content;
    private  LocalDate created;
    private  StatusReviewEnum statusReview;

    public ReviewDto(String content, LocalDate created, StatusReviewEnum statusReview) {
        this.content = content;
        this.created = created;
        this.statusReview = statusReview;
    }

    public ReviewDto() {
    }

    public String getContent() {
        return content;
    }

    public LocalDate getCreated() {
        return created;
    }

    public StatusReviewEnum getStatusReview() {
        return statusReview;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewDto entity = (ReviewDto) o;
        return Objects.equals(this.content, entity.content) &&
                Objects.equals(this.created, entity.created) &&
                Objects.equals(this.statusReview, entity.statusReview);
    }

    public ReviewDto setContent(String content) {
        this.content = content;
        return this;
    }

    public ReviewDto setCreated(LocalDate created) {
        this.created = created;
        return this;
    }

    public ReviewDto setStatusReview(StatusReviewEnum statusReview) {
        this.statusReview = statusReview;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, created, statusReview);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "content = " + content + ", " +
                "created = " + created + ", " +
                "statusReview = " + statusReview + ")";
    }
}