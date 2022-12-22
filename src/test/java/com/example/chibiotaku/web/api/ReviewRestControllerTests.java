package com.example.chibiotaku.web.api;


import com.example.chibiotaku.domain.api.ReviewApiDto;
import com.example.chibiotaku.services.ReviewAnimeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ReviewRestControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewAnimeService reviewAnimeService;

    private final ReviewApiDto REVIEW_API_DTO_TEST_FIRST = new ReviewApiDto()
            .setAnimeTitle("anime test tile1")
            .setContent("some content1")
            .setId(UUID.randomUUID())
            .setImageUrl("some image url1")
            .setPublished(LocalDate.of(2009,1,3));

    private final ReviewApiDto REVIEW_API_DTO_TEST_SECOND = new ReviewApiDto()
            .setAnimeTitle("anime test tile2")
            .setContent("some content2")
            .setId(UUID.randomUUID())
            .setImageUrl("some image url2")
            .setPublished(LocalDate.of(2001,9,9));

    private final ReviewApiDto REVIEW_API_DTO_TEST_THIRD = new ReviewApiDto()
            .setAnimeTitle("anime test tile3")
            .setContent("some content3")
            .setId(UUID.randomUUID())
            .setImageUrl("some image url3")
            .setPublished(LocalDate.of(2012,10,12));


    @Test
    void getLatestAnimeReviews_ShouldReturnCorrectly() throws Exception {

        List<ReviewApiDto> reviewApiDtoList = new ArrayList<>();
        Collections.addAll(
                reviewApiDtoList,
                REVIEW_API_DTO_TEST_FIRST,
                REVIEW_API_DTO_TEST_SECOND,
                REVIEW_API_DTO_TEST_THIRD);

        Mockito.when(this.reviewAnimeService.exportReviewsApiDtoLatestCreated())
                .thenReturn(reviewApiDtoList);

        mockMvc.perform(get("/api/v1/app/reviews/animes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].id",is(REVIEW_API_DTO_TEST_FIRST.getId().toString())))
                .andExpect(jsonPath("$[0].content",is(REVIEW_API_DTO_TEST_FIRST.getContent())))
                .andExpect(jsonPath("$[0].animeTitle",is(REVIEW_API_DTO_TEST_FIRST.getAnimeTitle())))
                .andExpect(jsonPath("$[0].username",is(REVIEW_API_DTO_TEST_FIRST.getUsername())))

                .andExpect(jsonPath("$[1].id",is(REVIEW_API_DTO_TEST_SECOND.getId().toString())))
                .andExpect(jsonPath("$[1].content",is(REVIEW_API_DTO_TEST_SECOND.getContent())))
                .andExpect(jsonPath("$[1].animeTitle",is(REVIEW_API_DTO_TEST_SECOND.getAnimeTitle())))
                .andExpect(jsonPath("$[1].username",is(REVIEW_API_DTO_TEST_SECOND.getUsername())))

                .andExpect(jsonPath("$[2].id",is(REVIEW_API_DTO_TEST_THIRD.getId().toString())))
                .andExpect(jsonPath("$[2].content",is(REVIEW_API_DTO_TEST_THIRD.getContent())))
                .andExpect(jsonPath("$[2].animeTitle",is(REVIEW_API_DTO_TEST_THIRD.getAnimeTitle())))
                .andExpect(jsonPath("$[2].username",is(REVIEW_API_DTO_TEST_THIRD.getUsername())))
                .andExpect(status().isOk());
    }


    @Test
    void getLatestAnimeReviews_ShouldReturnSizeZero() throws Exception {{
        Mockito.when(this.reviewAnimeService.exportReviewsApiDtoLatestCreated())
                .thenReturn(new ArrayList<>());


        mockMvc.perform(get("/api/v1/app/reviews/animes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(status().isOk());
    }}
}