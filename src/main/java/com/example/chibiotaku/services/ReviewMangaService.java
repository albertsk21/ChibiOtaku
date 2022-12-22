package com.example.chibiotaku.services;

import com.example.chibiotaku.domain.dtos.ReviewDto;
import com.example.chibiotaku.domain.views.ReviewVM;

import java.util.List;
import java.util.UUID;

public interface ReviewMangaService {

    void addReview(UUID mangaId, String username, ReviewDto review);
    List<ReviewVM> exportReviewsByMangaId(UUID id);
}
