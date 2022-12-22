package com.example.chibiotaku.repo;

import com.example.chibiotaku.domain.entities.AnimeEntity;
import com.example.chibiotaku.domain.entities.ReviewAnimeEntity;
import com.example.chibiotaku.domain.entities.UserEntity;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import com.example.chibiotaku.domain.enums.StatusReviewEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@DataJpaTest
class ReviewAnimeRepositoryTests {

    @Autowired
    private ReviewAnimeRepository underTest;
    @Autowired
    private AnimeRepository animeRepository;
    @Autowired
    private UserRepository userRepository;


    private final String USERNAME_FIRST = "tester1";
    private final String USERNAME_SECOND = "tester2";
    private final String CONTENT_REVIEW_ONE = "some text one";
    private final String CONTENT_REVIEW_TWO = "some text two";
    private final LocalDate CREATED_DATE_FIRST_REVIEW = LocalDate.of(2022,2,2);
    private final LocalDate CREATED_DATE_SECOND_REVIEW = LocalDate.of(2022,12,4);
    private final String ANIME_TITLE_FIRST = "test1";
    private final String ANIME_TITLE_SECOND = "test2";
    private final String ANIME_IMAGE_URL_FIRST = "image1";
    private final String ANIME_IMAGE_URL_SECOND = "image2";

    private UserEntity user1 = new UserEntity()
            .setUsername(USERNAME_FIRST)
            .setEmail("test@example.com")
            .setFirstName("test")
            .setLastName("test")
            .setPassword("123");

    private UserEntity user2 = new UserEntity()
            .setEmail("test2@example.com")
            .setLastName("test2")
            .setFirstName("test2")
            .setUsername(USERNAME_SECOND)
            .setPassword("none");


    @BeforeEach
    void setUp(){
        user1 = this.userRepository.save(user1);
        user2 = this.userRepository.save(user2);

        AnimeEntity animeTest1 = new AnimeEntity()
                .setAired(LocalDate.now())
                .setDuration(24)
                .setEpisodes(0)
                .setGenres("test1")
                .setImageUrl(ANIME_IMAGE_URL_FIRST)
                .setProducers("Test")
                .setRank(1)
                .setRating("test")
                .setStatusObject(StatusObjectEnum.ACCEPTED)
                .setTitle(ANIME_TITLE_FIRST)
                .setUser(user1)
                .setPopularity(1);
        AnimeEntity animeTest2 = new AnimeEntity()
                .setAired(LocalDate.now())
                .setDuration(24)
                .setEpisodes(0)
                .setGenres("test2")
                .setImageUrl(ANIME_IMAGE_URL_SECOND)
                .setProducers("Test2")
                .setRank(2)
                .setRating("test2")
                .setStatusObject(StatusObjectEnum.ACCEPTED)
                .setTitle(ANIME_TITLE_SECOND)
                .setPopularity(2);
        animeTest1 = this.animeRepository.save(animeTest1);
        animeTest2 = this.animeRepository.save(animeTest2);

        ReviewAnimeEntity reviewAnimeEntityOne = new ReviewAnimeEntity()
                .setStatusReview(StatusReviewEnum.RECOMMENDED)
                .setContent(CONTENT_REVIEW_ONE)
                .setVerified(true)
                .setAnime(animeTest1)
                .setUser(user1)
                .setCreated(CREATED_DATE_FIRST_REVIEW);

        ReviewAnimeEntity reviewAnimeEntityTwo = new ReviewAnimeEntity()
                .setAnime(animeTest2)
                .setStatusReview(StatusReviewEnum.RECOMMENDED)
                .setContent(CONTENT_REVIEW_TWO)
                .setVerified(true)
                .setUser(user2)
                .setCreated(CREATED_DATE_SECOND_REVIEW);
        this.underTest.saveAll(List.of(reviewAnimeEntityOne,reviewAnimeEntityTwo));
    }


    @AfterEach
    void tearDown(){
        this.underTest.deleteAll();
        this.animeRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    void findReviewAnimeEntitiesByAnimeId_ShouldReturnCorrectEntity(){
        List<AnimeEntity> animeEntities = this.animeRepository.findAll();
        List<Object[]> reviews = this.underTest.findReviewAnimeEntitiesByAnimeId(animeEntities.get(0).getId());

        int expectedSize = 1;
        Assertions.assertEquals(expectedSize,reviews.size());
        Assertions.assertEquals(user1.getUsername(),reviews.get(0)[0]);
        Assertions.assertEquals(CREATED_DATE_FIRST_REVIEW,reviews.get(0)[1]);
        Assertions.assertEquals(StatusReviewEnum.RECOMMENDED,reviews.get(0)[2]);
        Assertions.assertEquals(CONTENT_REVIEW_ONE,reviews.get(0)[3]);
    }

    @Test
    void findReviewAnimeEntitiesByAnimeId_ShouldReturnReturnEmptyList(){
        UUID randomId = UUID.randomUUID();
        List<Object[]> reviews = this.underTest.findReviewAnimeEntitiesByAnimeId(randomId);
        Assertions.assertTrue(reviews.isEmpty());
    }

    @Test
    void findReviewAnimeEntitiesByVerifiedFalse_ShouldReturnEmptyList(){
        List<ReviewAnimeEntity> reviews = this.underTest.findReviewAnimeEntitiesByVerified(false);
        Assertions.assertTrue(reviews.isEmpty());
    }

    @Test
    void findReviewAnimeEntitiesByVerifiedTrue_ShouldReturnTwoReviews(){
        List<ReviewAnimeEntity> reviews = this.underTest.findReviewAnimeEntitiesByVerified(true);
        int expectedSize = 2;

        Assertions.assertEquals(expectedSize,reviews.size());


        Assertions.assertEquals(reviews.get(0).getStatusReview(),StatusReviewEnum.RECOMMENDED);
        Assertions.assertEquals(reviews.get(0).getContent(),CONTENT_REVIEW_ONE);
        Assertions.assertEquals(reviews.get(1).getStatusReview(),StatusReviewEnum.RECOMMENDED);
        Assertions.assertEquals(reviews.get(1).getContent(),CONTENT_REVIEW_TWO);
    }


    @Test
    void findReviewAnimeEntitiesOrderByCreated_ShouldReturnCorrectEntity(){
        List<Object[]> reviews = this.underTest.findReviewAnimeEntitiesOrderByCreated();
        int expectedSize = 2;

        Assertions.assertEquals(expectedSize,reviews.size());
        Assertions.assertEquals(reviews.get(0)[1],CONTENT_REVIEW_TWO);
        Assertions.assertEquals(reviews.get(1)[1],CONTENT_REVIEW_ONE);

        Assertions.assertEquals(reviews.get(0)[2],ANIME_TITLE_SECOND);
        Assertions.assertEquals(reviews.get(1)[2],ANIME_TITLE_FIRST);

        Assertions.assertEquals(reviews.get(0)[3],CREATED_DATE_SECOND_REVIEW);
        Assertions.assertEquals(reviews.get(1)[3],CREATED_DATE_FIRST_REVIEW);

        Assertions.assertEquals(reviews.get(0)[4],ANIME_IMAGE_URL_SECOND);
        Assertions.assertEquals(reviews.get(1)[4],ANIME_IMAGE_URL_FIRST);

        Assertions.assertEquals(reviews.get(0)[5],USERNAME_SECOND);
        Assertions.assertEquals(reviews.get(1)[5],USERNAME_FIRST);
    }
}