package com.example.chibiotaku.services;

import com.example.chibiotaku.domain.api.ReviewApiDto;
import com.example.chibiotaku.domain.dtos.ReviewDto;
import com.example.chibiotaku.domain.views.ReviewVM;

import java.util.List;
import java.util.UUID;

public interface ReviewAnimeService {


    void addReviewAnime(UUID animeId, String username, ReviewDto reviewAnimeDto);
    List<ReviewVM> exportReviewsByAnimeId(UUID id);
    List<ReviewApiDto> exportReviewsApiDtoLatestCreated();

}
