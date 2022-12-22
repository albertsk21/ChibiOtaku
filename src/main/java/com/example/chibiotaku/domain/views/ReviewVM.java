package com.example.chibiotaku.domain.views;

import com.example.chibiotaku.domain.enums.StatusReviewEnum;

import java.time.LocalDate;

public class ReviewVM {


    private String username;
    private LocalDate created;
    private StatusReviewEnum statusReview;
    private String content;

    public ReviewVM() {
    }

    public String getUsername() {
        return username;
    }

    public LocalDate getCreated() {
        return created;
    }

    public StatusReviewEnum getStatusReview() {
        return statusReview;
    }

    public ReviewVM setUsername(String username) {
        this.username = username;
        return this;
    }

    public ReviewVM setCreated(LocalDate created) {
        this.created = created;
        return this;
    }

    public ReviewVM setStatusReview(StatusReviewEnum statusReview) {
        this.statusReview = statusReview;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ReviewVM setContent(String content) {
        this.content = content;
        return this;
    }
}
