package com.example.chibiotaku.repo;

import com.example.chibiotaku.domain.entities.MangaEntity;
import com.example.chibiotaku.domain.entities.ReviewMangaEntity;
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
class ReviewMangaRepositoryTests {

    @Autowired
    private ReviewMangaRepository underTest;
    @Autowired
    private MangaRepository mangaRepository;
    @Autowired
    private UserRepository userRepository;


    private final String USERNAME_FIRST = "tester1";
    private final String USERNAME_SECOND = "tester2";
    private final String CONTENT_REVIEW_ONE = "some text one";
    private final String CONTENT_REVIEW_TWO = "some text two";
    private final LocalDate CREATED_DATE_FIRST_REVIEW = LocalDate.of(2022,2,2);
    private final LocalDate CREATED_DATE_SECOND_REVIEW = LocalDate.of(2022,12,4);
    private final String MANGA_TITLE_FIRST = "test1";
    private final String MANGA_TITLE_SECOND = "test2";
    private final String MANGA_IMAGE_URL_FIRST = "image1";
    private final String MANGA_IMAGE_URL_SECOND = "image2";
    private final int MANGA_CHAPTERS_FIRST = 2;
    private final int MANGA_CHAPTERS_SECOND = 2;
    private final String MANGA_VOLUMES_FIRST = "1";
    private final String MANGA_VOLUMES_SECOND = "4";

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

    private MangaEntity mangaTest1 = new MangaEntity()
            .setTitle(MANGA_TITLE_FIRST)
            .setVolumes(MANGA_VOLUMES_FIRST)
            .setAuthors("tester1")
            .setGenres("13+")
            .setRanked(1)
            .setMembers(123)
            .setPopularity(1)
            .setPublished(LocalDate.of(2002,12,5))
            .setImageUrl(MANGA_IMAGE_URL_FIRST)
            .setThemes("horror")
            .setStatusObject(StatusObjectEnum.ACCEPTED)
            .setCreated(LocalDate.of(2022,12,3))
            .setChapters(MANGA_CHAPTERS_FIRST)
            .setContent("lorem ispum1");
    private MangaEntity mangaTest2 = new MangaEntity()
            .setTitle(MANGA_TITLE_SECOND)
            .setVolumes(MANGA_VOLUMES_SECOND)
            .setAuthors("tester2")
            .setGenres("13+")
            .setRanked(3)
            .setMembers(13)
            .setPopularity(2)
            .setPublished(LocalDate.of(2002,12,5))
            .setImageUrl(MANGA_IMAGE_URL_SECOND)
            .setThemes("comedy")
            .setStatusObject(StatusObjectEnum.DECLINED)
            .setCreated(LocalDate.of(2022,2,3))
            .setChapters(MANGA_CHAPTERS_SECOND)
            .setContent("lorem ispum2");

    @BeforeEach
    void setUp(){
        user1 = this.userRepository.save(user1);
        user2 = this.userRepository.save(user2);


        mangaTest1 = this.mangaRepository.save(mangaTest1);
        mangaTest2 = this.mangaRepository.save(mangaTest2);

        ReviewMangaEntity reviewMangaTest = new ReviewMangaEntity()
                .setManga(mangaTest1)
                .setCreated(CREATED_DATE_FIRST_REVIEW)
                .setUser(user1)
                .setContent(CONTENT_REVIEW_ONE)
                .setStatusReview(StatusReviewEnum.RECOMMENDED)
                .setVerified(true);

        ReviewMangaEntity reviewMangaTest2 = new ReviewMangaEntity()
                .setManga(mangaTest2)
                .setCreated(CREATED_DATE_SECOND_REVIEW)
                .setUser(user2)
                .setContent(CONTENT_REVIEW_TWO)
                .setStatusReview(StatusReviewEnum.RECOMMENDED)
                .setVerified(true);


        this.underTest.save(reviewMangaTest);
        this.underTest.save(reviewMangaTest2);
    }

    @AfterEach
    void tearDown(){
        this.underTest.deleteAll();
        this.mangaRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    void findReviewMangaEntitiesByMangaId_ShouldReturnCorrectEntity(){
        List<MangaEntity> mangas = this.mangaRepository.findAll();
        List<Object[]> reviews = this.underTest.findReviewMangaEntitiesByMangaId(mangas.get(0).getId());

        int expectedSize = 1;
        Assertions.assertEquals(expectedSize,reviews.size());
        Assertions.assertEquals(USERNAME_FIRST,reviews.get(0)[0]);
        Assertions.assertEquals(CREATED_DATE_FIRST_REVIEW,reviews.get(0)[1]);
        Assertions.assertEquals(StatusReviewEnum.RECOMMENDED,reviews.get(0)[2]);
        Assertions.assertEquals(CONTENT_REVIEW_ONE,reviews.get(0)[3]);
    }

    @Test
    void findReviewMangaEntitiesByMangaId_ShouldReturnEmptyList() {
        UUID randomId = UUID.randomUUID();

        List<Object[]> reviews = this.underTest.findReviewMangaEntitiesByMangaId(randomId);
        Assertions.assertTrue(reviews.isEmpty());

    }


    @Test
    void findReviewMangaEntitiesByVerified_ShouldReturnOneReview(){
        List<ReviewMangaEntity> reviews = this.underTest.findReviewMangaEntitiesByVerified(true);

        int expectedSizeList = 2;
        Assertions.assertEquals(expectedSizeList,reviews.size());
        Assertions.assertEquals(CONTENT_REVIEW_ONE,reviews.get(0).getContent());
        Assertions.assertEquals(CREATED_DATE_FIRST_REVIEW,reviews.get(0).getCreated());
        Assertions.assertEquals(CONTENT_REVIEW_TWO,reviews.get(1).getContent());
        Assertions.assertEquals(CREATED_DATE_SECOND_REVIEW,reviews.get(1).getCreated());
    }
    @Test
    void findReviewMangaEntitiesByVerified_ShouldReturnEmptyList(){

        List<ReviewMangaEntity> reviews = this.underTest.findReviewMangaEntitiesByVerified(false);
        Assertions.assertTrue(reviews.isEmpty());

    }
}
