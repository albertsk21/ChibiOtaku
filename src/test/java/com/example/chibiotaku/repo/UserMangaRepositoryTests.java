package com.example.chibiotaku.repo;

import com.example.chibiotaku.domain.entities.MangaEntity;
import com.example.chibiotaku.domain.entities.UserEntity;
import com.example.chibiotaku.domain.entities.UserMangaEntity;
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
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
class UserMangaRepositoryTests {



    @Autowired
    private UserMangaRepository underTest;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MangaRepository mangaRepository;

    private final String USERNAME_FIRST = "tester1";
    private final String USERNAME_SECOND = "tester2";
    private final LocalDate CREATED_FIRST_USER_MANGA = LocalDate.of(2012,1,2);
    private final LocalDate CREATED_SECOND_USER_MANGA = LocalDate.of(2021,2,4);
    private final String MANGA_TITLE_FIRST = "test1";
    private final String MANGA_TITLE_SECOND = "test2";
    private final String MANGA_IMAGE_URL_FIRST = "image1";
    private final String MANGA_IMAGE_URL_SECOND = "image2";
    private final int SCORE_FIRST_USER_MANGA = 10;
    private final int SCORE_SECOND_USER_MANGA = 5;


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
            .setVolumes("12")
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
            .setChapters(1)
            .setContent("lorem ispum1");
    private MangaEntity mangaTest2 = new MangaEntity()
            .setTitle(MANGA_TITLE_SECOND)
            .setVolumes("12")
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
            .setChapters(3)
            .setContent("lorem ispum2");



    @BeforeEach
    void setUp(){
        user1 = this.userRepository.save(user1);
        user2 = this.userRepository.save(user2);

       mangaTest1 = this.mangaRepository.save(mangaTest1);
       mangaTest2 = this.mangaRepository.save(mangaTest2);

        UserMangaEntity userMangaTest1 = new UserMangaEntity()
                .setUser(user1)
                .setScore(SCORE_FIRST_USER_MANGA)
                .setManga(mangaTest1)
                .setCreated(CREATED_FIRST_USER_MANGA)
                .setWatchStatus(WatchStatusEnum.NOT_FINISHED);
        UserMangaEntity userMangaTest2 = new UserMangaEntity()
                .setUser(user2)
                .setScore(SCORE_SECOND_USER_MANGA)
                .setManga(mangaTest2)
                .setCreated(CREATED_SECOND_USER_MANGA)
                .setWatchStatus(WatchStatusEnum.NOT_FINISHED);
        this.underTest.saveAll(List.of(userMangaTest1,userMangaTest2));
    }

    @AfterEach
    void tearDown(){
        this.underTest.deleteAll();
        this.mangaRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    void countByWatchStatusAndUsername_ShouldReturnCorrectNumber(){
        int result = this.underTest.countByWatchStatusAndUsername(USERNAME_FIRST,WatchStatusEnum.NOT_FINISHED);
        int expectedResult = 1;
        Assertions.assertEquals(expectedResult,result);
    }
    @Test
    void countByWatchStatusAndUsername_ShouldReturnZERONumber(){
        int result = this.underTest.countByWatchStatusAndUsername(USERNAME_FIRST,WatchStatusEnum.DROPPED);
        int expectedResult = 0;
        Assertions.assertEquals(expectedResult,result);
    }


    @Test
    void findUserMangaFormatByUsername_ShouldReturnCorrectList(){
        List<Object[]> mangaList = this.underTest.findUserMangaFormatByUsername(USERNAME_FIRST);
        int expectedSizeList = 1;
        Assertions.assertEquals(expectedSizeList,mangaList.size());
        Assertions.assertEquals(MANGA_IMAGE_URL_FIRST,mangaList.get(0)[0]);
    }

    @Test
    void findUserMangaFormatByUsername_ShouldReturnEmptyList(){
        String unknownUsername = "tester";
        List<Object[]> mangaList = this.underTest.findUserMangaFormatByUsername(unknownUsername);
        Assertions.assertTrue(mangaList.isEmpty());
    }

    @Test
    void findUserMangaEntityByUserUsernameAndAndMangaId_ShouldReturnCorrectEntity(){
        List<MangaEntity> mangaEntities  = this.mangaRepository.findAll();
        UUID firstMangaId = mangaEntities.get(0).getId();

        Optional<UserMangaEntity> userMangaOpt = this.underTest
                .findUserMangaEntityByUserUsernameAndAndMangaId(USERNAME_FIRST,firstMangaId);
        Assertions.assertTrue(userMangaOpt.isPresent());

        Assertions.assertEquals(MANGA_TITLE_FIRST,userMangaOpt.get().getManga().getTitle());
        Assertions.assertEquals(USERNAME_FIRST,userMangaOpt.get().getUser().getUsername());
        Assertions.assertEquals(CREATED_FIRST_USER_MANGA,userMangaOpt.get().getCreated());
    }


    @Test
    void findUserMangaEntityByUserUsernameAndAndMangaId_ShouldReturnEmptyObject(){
        UUID randomId = UUID.randomUUID();
        String unknownUsername = "tester";

        var userMangaOpt = this.underTest
                .findUserMangaEntityByUserUsernameAndAndMangaId(unknownUsername,randomId);
        Assertions.assertTrue(userMangaOpt.isEmpty());
    }

    @Test
    void findMangaEntityByUsernameAndWatchStatus_ShouldReturnCorrectList(){
        List<UserMangaEntity> userMangaEntityList = this.underTest
                .findMangaEntityByUsernameAndWatchStatus(USERNAME_FIRST,WatchStatusEnum.NOT_FINISHED);

        int expectedSizeList = 1;
        Assertions.assertEquals(expectedSizeList,userMangaEntityList.size());

        Assertions.assertEquals(MANGA_TITLE_FIRST,userMangaEntityList.get(0).getManga().getTitle());
        Assertions.assertEquals(USERNAME_FIRST,userMangaEntityList.get(0).getUser().getUsername());
        Assertions.assertEquals(CREATED_FIRST_USER_MANGA,userMangaEntityList.get(0).getCreated());
    }

    @Test
    void findMangaEntityByUsernameAndWatchStatus_ShouldReturnEmptyList(){
        String unknownUsername = "tester";
        List<UserMangaEntity> userMangaEntityList = this.underTest
                .findMangaEntityByUsernameAndWatchStatus(unknownUsername,WatchStatusEnum.DROPPED);
        Assertions.assertTrue(userMangaEntityList.isEmpty());
    }
    @Test
    void getAverageScoreByMangaId_ShouldReturnNullValue(){
        UUID randomId = UUID.randomUUID();

        Double result = this.underTest.getAverageScoreByMangaId(randomId);
        Assertions.assertNull(result);
    }

    @Test
    void getAverageScoreByMangaId_ShouldReturnCorrectValue() {
        List<MangaEntity> mangaEntities = this.mangaRepository.findAll();
        UUID secondMangaId = mangaEntities.get(1).getId();

        UserEntity userTest = new UserEntity()
                .setEmail("test3@example.com")
                .setLastName("test3")
                .setFirstName("test3")
                .setUsername("tester3")
                .setPassword("none");
       userTest = this.userRepository.save(userTest);


       mangaTest2 = this.mangaRepository.findById(secondMangaId).get();

      int currentTestScore = 10;
      UserMangaEntity userMangaTest2 = new UserMangaEntity()
                .setUser(userTest)
                .setScore(currentTestScore)
                .setManga(mangaTest2)
                .setCreated(LocalDate.of(2002,3,5))
                .setWatchStatus(WatchStatusEnum.DROPPED);
      this.underTest.save(userMangaTest2);

        Double actual = this.underTest.getAverageScoreByMangaId(secondMangaId);
        Double expectedResult  = 7.5;

        Assertions.assertEquals(expectedResult,actual);
    }

    @Test
    void countUserMangaEntityByMangaId_ShouldReturnZeroCount(){
        UUID randomId = UUID.randomUUID();
        int actual = this.underTest.countUserMangaEntityByMangaId(randomId);
        int expected = 0;
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void countUserMangaEntityByMangaId_ShouldReturnCorrectCount(){
        List<MangaEntity> mangaEntities = this.mangaRepository.findAll();
        UUID secondMangaId = mangaEntities.get(1).getId();

        int actual = this.underTest.countUserMangaEntityByMangaId(secondMangaId);
        int expected = 1;
        Assertions.assertEquals(expected,actual);

    }
}


