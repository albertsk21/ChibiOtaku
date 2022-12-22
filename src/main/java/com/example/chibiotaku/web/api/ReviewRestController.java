package com.example.chibiotaku.web.api;

import com.example.chibiotaku.domain.api.ReviewApiDto;
import com.example.chibiotaku.services.ReviewAnimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/app/reviews")
public class ReviewRestController {

    private ReviewAnimeService reviewAnimeService;

    public ReviewRestController(ReviewAnimeService reviewAnimeService) {
        this.reviewAnimeService = reviewAnimeService;
    }

    @GetMapping("/animes")
    public ResponseEntity<List<ReviewApiDto>> getLatestAnimeReviews(){
       return ResponseEntity.ok(this.reviewAnimeService.exportReviewsApiDtoLatestCreated());
    }

}
