package com.example.chibiotaku.domain.biding;

import com.example.chibiotaku.domain.enums.StatusReviewEnum;

import javax.validation.constraints.NotEmpty;

public class ReviewBM {



    private String content;
    private StatusReviewEnum statusReview;


    public ReviewBM() {
    }


    @NotEmpty
    public String getContent() {
        return content;
    }

    public ReviewBM setContent(String content) {
        this.content = content;
        return this;
    }

    public StatusReviewEnum getStatusReview() {
        return statusReview;
    }

    public ReviewBM setStatusReview(StatusReviewEnum statusReview) {
        this.statusReview = statusReview;
        return this;
    }
}
