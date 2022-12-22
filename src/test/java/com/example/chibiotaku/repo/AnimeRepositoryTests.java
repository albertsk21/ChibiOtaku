package com.example.chibiotaku.repo;

import com.example.chibiotaku.domain.entities.AnimeEntity;
import com.example.chibiotaku.domain.entities.UserEntity;
import com.example.chibiotaku.domain.enums.AnimeTypeEnum;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@DataJpaTest
class AnimeRepositoryTests {

    @Autowired
    private AnimeRepository underTest;
    @Autowired
    private UserRepository userRepository;

    private  UserEntity user1 = new UserEntity()
            .setEmail("test1@example.com")
            .setLastName("test1")
            .setFirstName("test1")
            .setUsername("tester1")
            .setPassword("none");
    private  UserEntity user2 = new UserEntity()
            .setEmail("test2@example.com")
            .setLastName("test2")
            .setFirstName("test2")
            .setUsername("tester2")
            .setPassword("none");

    private final AnimeEntity animeTest1 = new AnimeEntity()
            .setAired(LocalDate.now())
            .setDuration(24)
            .setEpisodes(0)
            .setGenres("test1")
            .setImageUrl("testIamgeUrl")
            .setProducers("Test")
            .setRank(1)
            .setRating("test")
            .setStatusObject(StatusObjectEnum.ACCEPTED)
            .setTitle("test1")
            .setUser(user1)
            .setPopularity(1);

    private final AnimeEntity animeTest2 = new AnimeEntity()
            .setAired(LocalDate.now())
            .setDuration(24)
            .setEpisodes(0)
            .setGenres("test2")
            .setImageUrl("testIamgeUrl2")
            .setProducers("Test2")
            .setRank(2)
            .setRating("test2")
            .setStatusObject(StatusObjectEnum.ACCEPTED)
            .setTitle("test2")
            .setUser(user2)
            .setPopularity(2);

    private final int expectedSizeList = 2;
    @BeforeEach
    void setUp() {
        user1 = this.userRepository.save(user1);
        user2 = this.userRepository.save(user2);
        this.underTest.saveAll(List.of(animeTest1,animeTest2));

    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void deleteAnime_ShouldDeleteCorrectly(){
        AnimeEntity expected = new AnimeEntity()
                .setAired(LocalDate.now())
                .setDuration(24)
                .setEpisodes(0)
                .setGenres("test")
                .setImageUrl("testIamgeUrl")
                .setProducers("Test")
                .setRank(-1)
                .setRating("test");

        AnimeEntity actual =  underTest.save(expected);
        this.underTest.delete(actual);
        Optional<AnimeEntity> actualOp = this.underTest.findById(actual.getId());
        Assertions.assertTrue(actualOp.isEmpty());

    }


    @Test
    void findAnimeEntityContainsTitle_ShouldReturnCorrectObjects(){
        String expectTitleAnime1 = "test1";
        String expectTitleAnime2 = "test2";
        List<Object[]> animes = this.underTest.findAnimeEntityContainsTitle("te");


        Assertions.assertEquals(animes.size(),expectedSizeList);
        Assertions.assertEquals(expectTitleAnime1,animes.get(0)[1]);
        Assertions.assertEquals(expectTitleAnime2,animes.get(1)[1]);
        System.out.println();
    }


    @Test
    void findAnimeEntitiesByUserUsername_ShouldReturnOneEntity(){
        String username = "tester1";

        int expectedSize = 1;
        List<AnimeEntity> animeEntities = this.underTest.findAnimeEntitiesByUserUsername(username);
        Assertions.assertEquals(animeEntities.size(),expectedSize);
    }
    @Test
    void findAnimeEntitiesByUserUsername_ShouldReturnZeroEntity(){

        String username = "testtt";
        int expectedSize = 0;
        List<AnimeEntity> animeEntities = this.underTest.findAnimeEntitiesByUserUsername(username);
        Assertions.assertEquals(expectedSize,animeEntities.size());
    }

    @Test
    void findAllOrderByPopularityAndRanked_ShouldReturnInCorrectOrder(){


        List<AnimeEntity> animeEntities = this.underTest.findAllOrderByPopularityAndRanked();

        this.assertAnimeIsEqual(animeEntities.get(0),animeTest1);
        this.assertAnimeIsEqual(animeEntities.get(1),animeTest2);
        Assertions.assertEquals(animeEntities.size(),expectedSizeList);
    }


    @Test
    void findAnimeEntitiesByStatusObject_ShouldReturnCorrectEntities(){

        List<AnimeEntity> animeEntities = this.underTest.findAnimeEntitiesByStatusObject(StatusObjectEnum.ACCEPTED);


        Assertions.assertEquals(animeEntities.size(),expectedSizeList);

        this.assertAnimeIsEqual(animeEntities.get(0),animeTest1);
        this.assertAnimeIsEqual(animeEntities.get(1),animeTest2);


    }
    @Test
    void findAnimeEntitiesByStatusObject_ShouldReturnZEROEntities(){
        int zeroSizeExpected = 0;
        List<AnimeEntity> animeEntities = this.underTest.findAnimeEntitiesByStatusObject(StatusObjectEnum.PENDING);
        Assertions.assertEquals(animeEntities.size(),zeroSizeExpected);

    }
    @Test
    void findAnimeEntitiesByStatusObject_ShouldReturnOneEntity(){

        String titleTest = "tesst";
        int popularityTest = 3;
        int episodesTest = 23;
        int durationTest = 24;
        String genresTest = "13+";
        int membersTest = 2345;
        String producersTest = "producers test";
        String imageUrlTest = "image test";
        String contentTest = "lorem ispum dolor";
        int rankTest = 6;
        LocalDate airedTest = LocalDate.of(2002,5,24);
        String studioTest = "studio test";

        String sourceTest = "test";

        AnimeEntity animeTest = new AnimeEntity()
                .setTitle(titleTest)
                .setPopularity(popularityTest)
                .setEpisodes(episodesTest)
                .setDuration(durationTest)
                .setGenres(genresTest)
                .setCreated(LocalDate.now())
                .setMembers(membersTest)
                .setProducers(producersTest)
                .setImageUrl(imageUrlTest)
                .setRank(rankTest)
                .setContent(contentTest)
                .setStatusObject(StatusObjectEnum.DECLINED)
                .setType(AnimeTypeEnum.TV)
                .setAired(airedTest)
                .setStudios(studioTest)
                .setSource(sourceTest);

        this.underTest.save(animeTest);

        int expectedSizeOne = 1;
        List<AnimeEntity> animeEntities = this.underTest.findAnimeEntitiesByStatusObject(StatusObjectEnum.DECLINED);
        Assertions.assertEquals(animeEntities.size(),expectedSizeOne);
        this.assertAnimeIsEqual(animeEntities.get(0),animeTest);

    }

    @Test
    void findAnimeEntitiesOrderByPopularityASC_ShouldReturnInCorrectOrder(){
        List<AnimeEntity> animeEntities = this.underTest.findAnimeEntitiesOrderByPopularityASC();
        Assertions.assertEquals(animeEntities.size(),expectedSizeList);
        this.assertAnimeIsEqual(animeEntities.get(0),animeTest1);
        this.assertAnimeIsEqual(animeEntities.get(1),animeTest2);
    }


    @Test
    void findAnimeEntityById_ShouldReturnCorrectEntity(){
        List<AnimeEntity> animeEntities = this.underTest.findAll();
        Optional<AnimeEntity> animeOpt = this.underTest.findAnimeEntityById(animeEntities.get(0).getId());
        Assertions.assertTrue(animeOpt.isPresent());
        this.assertAnimeIsEqual(animeEntities.get(0),animeOpt.get());

    }
    void assertAnimeIsEqual(AnimeEntity a1, AnimeEntity a2){

        Assertions.assertEquals(a1.getTitle(),a2.getTitle());
        Assertions.assertEquals(a1.getType(),a2.getType());
        Assertions.assertEquals(a1.getEpisodes(),a2.getEpisodes());
        Assertions.assertEquals(a1.getAired(),a2.getAired());
        Assertions.assertEquals(a1.getProducers(),a2.getProducers());
        Assertions.assertEquals(a1.getStudios(), a2.getStudios());
        Assertions.assertEquals(a1.getSource(),a2.getSource());
        Assertions.assertEquals(a1.getGenres(),a2.getGenres());
        Assertions.assertEquals(a1.getDuration(),a2.getDuration());
        Assertions.assertEquals(a1.getMembers(),a2.getMembers());
        Assertions.assertEquals(a1.getImageUrl(),a2.getImageUrl());
        Assertions.assertEquals(a1.getPopularity(),a2.getPopularity());
        Assertions.assertEquals(a1.getContent(),a2.getContent());
    }
}