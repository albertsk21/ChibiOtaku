package com.example.chibiotaku.repo;

import com.example.chibiotaku.domain.entities.AnimeEntity;
import com.example.chibiotaku.domain.entities.UserAnimeEntity;
import com.example.chibiotaku.domain.entities.UserEntity;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import com.example.chibiotaku.domain.enums.WatchStatusEnum;
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
class UserAnimeRepositoryTests {

    @Autowired
    private UserAnimeRepository underTest;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AnimeRepository animeRepository;
    private final String USERNAME_FIRST = "tester1";
    private final String USERNAME_SECOND = "tester2";
    private final LocalDate CREATED_FIRST_USER_ANIME = LocalDate.of(2012,1,2);
    private final LocalDate CREATED_SECOND_USER_ANIME = LocalDate.of(2021,2,4);
    private final String ANIME_TITLE_FIRST = "test1";
    private final String ANIME_TITLE_SECOND = "test2";
    private final String ANIME_IMAGE_URL_FIRST = "image1";
    private final String ANIME_IMAGE_URL_SECOND = "image2";
    private final int SCORE_FIRST_USER_ANIME = 10;
    private final int SCORE_SECOND_USER_ANIME = 5;

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

        UserAnimeEntity userAnimeTest1 = new UserAnimeEntity()
                .setAnime(animeTest1)
                .setCreated(CREATED_FIRST_USER_ANIME)
                .setScore(SCORE_FIRST_USER_ANIME)
                .setWatchStatus(WatchStatusEnum.NOT_FINISHED)
                .setUser(user1);

        UserAnimeEntity userAnimeTest2 = new UserAnimeEntity()
                .setAnime(animeTest2)
                .setUser(user2)
                .setCreated(CREATED_SECOND_USER_ANIME)
                .setScore(SCORE_SECOND_USER_ANIME)
                .setWatchStatus(WatchStatusEnum.NOT_FINISHED);
        this.underTest.saveAll(List.of(userAnimeTest1,userAnimeTest2));
    }

    @AfterEach
    void tearDown(){
        this.underTest.deleteAll();
        this.animeRepository.deleteAll();
        this.userRepository.deleteAll();
    }
    @Test
    void findAnimeByAnimeIdAndUserId_ShouldReturnCorrectEntity(){
        List<AnimeEntity> animeEntities = this.animeRepository.findAll();
        UUID firstAnimeId = animeEntities.get(0).getId();

        var userAnimeEntityOp = this.underTest.findAnimeByAnimeIdAndUserId(firstAnimeId,USERNAME_FIRST);

        Assertions.assertTrue(userAnimeEntityOp.isPresent());
        Assertions.assertEquals(ANIME_TITLE_FIRST,userAnimeEntityOp.get().getAnime().getTitle());
        Assertions.assertEquals(USERNAME_FIRST,userAnimeEntityOp.get().getUser().getUsername());
        Assertions.assertEquals(CREATED_FIRST_USER_ANIME,userAnimeEntityOp.get().getCreated());
    }

    @Test
    void findAnimeByAnimeIdAndUserId_ShouldReturnEmptyObject(){
        String unknownUsername = "tester";
        UUID randomId = UUID.randomUUID();
        var userAnimeEntityOp = this.underTest.findAnimeByAnimeIdAndUserId(randomId,unknownUsername);
        Assertions.assertTrue(userAnimeEntityOp.isEmpty());
    }
    @Test
    void countByWatchStatusAndUsername_ShouldReturnCorrectNumber(){
        int countResult = this.underTest.countByWatchStatusAndUsername(USERNAME_FIRST,WatchStatusEnum.NOT_FINISHED);
        int expectedResult = 1;
        Assertions.assertEquals(expectedResult,countResult);
    }
    @Test
    void countByWatchStatusAndUsername_ShouldReturnZero(){
        String unknownUsername = "tester";
        int countResult = this.underTest.countByWatchStatusAndUsername(unknownUsername,WatchStatusEnum.NOT_FINISHED);
        int expectedResult = 0;
        Assertions.assertEquals(expectedResult,countResult);
    }

    @Test
    void findUserAnimeFormatByUsername_ShouldReturnCorrectObjects(){
        List<Object[]> userAnimeList = this.underTest.findUserAnimeFormatByUsername(USERNAME_FIRST);
        int expectedSize = 1;

        Assertions.assertEquals(expectedSize,userAnimeList.size());
        Assertions.assertEquals(ANIME_IMAGE_URL_FIRST,userAnimeList.get(0)[0]);
    }
    @Test
    void findUserAnimeFormatByUsername_ShouldReturnEmptyList(){
        String unknownUsername = "tester";
        List<Object[]> userAnimeList = this.underTest.findUserAnimeFormatByUsername(unknownUsername);
        Assertions.assertTrue(userAnimeList.isEmpty());
    }

    @Test
    void findUserAnimeEntityByUserUsernameAndAndAnimeId_ShouldReturnCorrectList(){
        List<UserAnimeEntity> userAnimeEntityList = this.underTest.findAnimeEntityByUsernameAndWatchStatus(USERNAME_FIRST,WatchStatusEnum.NOT_FINISHED);

        int expectedSize = 1;
        Assertions.assertEquals(expectedSize,userAnimeEntityList.size());
        Assertions.assertEquals(ANIME_TITLE_FIRST,userAnimeEntityList.get(0).getAnime().getTitle());
        Assertions.assertEquals(USERNAME_FIRST,userAnimeEntityList.get(0).getUser().getUsername());
        Assertions.assertEquals(WatchStatusEnum.NOT_FINISHED,userAnimeEntityList.get(0).getWatchStatus());
        Assertions.assertEquals(CREATED_FIRST_USER_ANIME,userAnimeEntityList.get(0).getCreated());
    }
    @Test
    void findUserAnimeEntityByUserUsernameAndAndAnimeId_ShouldReturnEmptyList(){
        String unknownUsername = "tester";
        List<UserAnimeEntity> userAnimeEntityList = this.underTest.findAnimeEntityByUsernameAndWatchStatus(unknownUsername,WatchStatusEnum.COMPLETE);
        Assertions.assertTrue(userAnimeEntityList.isEmpty());
    }

    @Test
    void getAverageScoreByAnimeTitle_ShouldReturnCorrectResult(){
        Double result = this.underTest.getAverageScoreByAnimeTitle(ANIME_TITLE_FIRST);
        Double expectedResult = 10.0;
        Assertions.assertEquals(expectedResult,result);
    }
    @Test
    void getAverageScoreByAnimeTitle_ShouldReturnNullValue(){
        String unknownAnimeTitle = "anime title test";
        Double result = this.underTest.getAverageScoreByAnimeTitle(unknownAnimeTitle);
        Assertions.assertNull(result);
    }

    @Test
    void countUserAnimeEntityByAnimeTitle_ShouldReturnOneNumberUser(){
        int countResult = this.underTest.countUserAnimeEntityByAnimeTitle(ANIME_TITLE_FIRST);
        int expectedResult = 1;
        Assertions.assertEquals(expectedResult,countResult);
    }

    @Test
    void countUserAnimeEntityByAnimeTitle_ShouldReturnZero(){
        String unknownAnimeTitle = "anime title testing";
        int countResult = this.underTest.countUserAnimeEntityByAnimeTitle(unknownAnimeTitle);
        int expectedResult = 0;
        Assertions.assertEquals(expectedResult,countResult);
    }
}