package com.example.chibiotaku.repo;

import com.example.chibiotaku.domain.entities.MangaEntity;
import com.example.chibiotaku.domain.entities.UserEntity;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
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
class MangaRepositoryTests {

    @Autowired
    private MangaRepository underTest;
    @Autowired
    private UserRepository userRepository;


    private final MangaEntity mangaTest1 = new MangaEntity()
            .setTitle("test1")
            .setVolumes("12")
            .setAuthors("tester1")
            .setGenres("13+")
            .setRanked(1)
            .setMembers(123)
            .setPopularity(1)
            .setPublished(LocalDate.of(2002,12,5))
            .setImageUrl("url1")
            .setThemes("horror")
            .setStatusObject(StatusObjectEnum.ACCEPTED)
            .setCreated(LocalDate.of(2022,12,3))
            .setChapters(1)
            .setContent("lorem ispum1");
    private final MangaEntity mangaTest2 = new MangaEntity()
            .setTitle("test2")
            .setVolumes("12")
            .setAuthors("tester2")
            .setGenres("13+")
            .setRanked(3)
            .setMembers(13)
            .setPopularity(2)
            .setPublished(LocalDate.of(2002,12,5))
            .setImageUrl("url2")
            .setThemes("comedy")
            .setStatusObject(StatusObjectEnum.DECLINED)
            .setCreated(LocalDate.of(2022,2,3))
            .setChapters(3)
            .setContent("lorem ispum2");
    private final MangaEntity mangaTest3 = new MangaEntity()
            .setTitle("test3")
            .setVolumes("12")
            .setAuthors("tester3")
            .setGenres("18+")
            .setRanked(3)
            .setMembers(13)
            .setPopularity(2)
            .setPublished(LocalDate.of(2002,1,5))
            .setImageUrl("url3")
            .setThemes("action")
            .setStatusObject(StatusObjectEnum.DECLINED)
            .setCreated(LocalDate.of(2022,12,3))
            .setChapters(12)
            .setContent("lorem ispum3");


    @BeforeEach
    void setUp(){
        this.underTest.save(mangaTest1);
        this.underTest.save(mangaTest2);
        this.underTest.save(mangaTest3);
    }



    @AfterEach
    void tearDown(){
        this.underTest.deleteAll();
    }

    @Test
    void findMangaEntityById_ShouldReturnCorrectEntities(){

        List<MangaEntity> mangaEntities = this.underTest.findAll();
        UUID mangaId1 = mangaEntities.get(0).getId();
        UUID mangaId2 = mangaEntities.get(1).getId();
        UUID mangaId3 = mangaEntities.get(2).getId();

        MangaEntity currentManga = this.underTest.findMangaEntityById(mangaId1).get();
        this.assertMangaIsEqual(mangaTest1,currentManga);

        currentManga = this.underTest.findMangaEntityById(mangaId2).get();
        this.assertMangaIsEqual(mangaTest2,currentManga);

        currentManga = this.underTest.findMangaEntityById(mangaId3).get();
        this.assertMangaIsEqual(mangaTest3,currentManga);
    }

    @Test
    void findMangaEntityById_ShouldNotReturnAnyManga(){
        UUID randomId = UUID.randomUUID();
        var mangaOpt = this.underTest.findMangaEntityById(randomId);
        Assertions.assertTrue(mangaOpt.isEmpty());
    }

    @Test
    void findMangaEntityContainsTitle_ShouldReturnThreeEntities(){
        String searchWord = "test";
        int expectedSizeList = 3;
        List<Object[]> mangaObjList = this.underTest.findMangaEntityContainsTitle(searchWord);

        Assertions.assertEquals(mangaObjList.size(),expectedSizeList);

        Assertions.assertEquals(mangaObjList.get(0)[1],mangaTest1.getTitle());
        Assertions.assertEquals(mangaObjList.get(0)[2],mangaTest1.getChapters());
        Assertions.assertEquals(mangaObjList.get(0)[3],mangaTest1.getVolumes());
        Assertions.assertEquals(mangaObjList.get(0)[4],mangaTest1.getPopularity());
        Assertions.assertEquals(mangaObjList.get(0)[5],mangaTest1.getImageUrl());
        Assertions.assertEquals(mangaObjList.get(0)[6],mangaTest1.getContent());
        Assertions.assertEquals(mangaObjList.get(0)[7],mangaTest1.getStatusObject());

        Assertions.assertEquals(mangaObjList.get(1)[1],mangaTest2.getTitle());
        Assertions.assertEquals(mangaObjList.get(1)[2],mangaTest2.getChapters());
        Assertions.assertEquals(mangaObjList.get(1)[3],mangaTest2.getVolumes());
        Assertions.assertEquals(mangaObjList.get(1)[4],mangaTest2.getPopularity());
        Assertions.assertEquals(mangaObjList.get(1)[5],mangaTest2.getImageUrl());
        Assertions.assertEquals(mangaObjList.get(1)[6],mangaTest2.getContent());
        Assertions.assertEquals(mangaObjList.get(1)[7],mangaTest2.getStatusObject());

        Assertions.assertEquals(mangaObjList.get(2)[1],mangaTest3.getTitle());
        Assertions.assertEquals(mangaObjList.get(2)[2],mangaTest3.getChapters());
        Assertions.assertEquals(mangaObjList.get(2)[3],mangaTest3.getVolumes());
        Assertions.assertEquals(mangaObjList.get(2)[4],mangaTest3.getPopularity());
        Assertions.assertEquals(mangaObjList.get(2)[5],mangaTest3.getImageUrl());
        Assertions.assertEquals(mangaObjList.get(2)[6],mangaTest3.getContent());
        Assertions.assertEquals(mangaObjList.get(2)[7],mangaTest3.getStatusObject());
    }


    @Test
    void findMangaEntityContainsTitle_ShouldReturnOneEntities(){
        String searchWord = "1";
        int expectedSizeList = 1;

        List<Object[]> mangaObjList = this.underTest.findMangaEntityContainsTitle(searchWord);
        Assertions.assertEquals(mangaObjList.size(),expectedSizeList);
        Assertions.assertEquals(mangaObjList.get(0)[1],mangaTest1.getTitle());
        Assertions.assertEquals(mangaObjList.get(0)[2],mangaTest1.getChapters());
        Assertions.assertEquals(mangaObjList.get(0)[3],mangaTest1.getVolumes());
        Assertions.assertEquals(mangaObjList.get(0)[4],mangaTest1.getPopularity());
        Assertions.assertEquals(mangaObjList.get(0)[5],mangaTest1.getImageUrl());
        Assertions.assertEquals(mangaObjList.get(0)[6],mangaTest1.getContent());
        Assertions.assertEquals(mangaObjList.get(0)[7],mangaTest1.getStatusObject());

    }




    @Test
    void findMangaEntitiesByUsername_ShouldReturnOneEntity(){

        UserEntity userTest = new UserEntity()
                .setUsername("test")
                .setFirstName("tester")
                .setLastName("tesst")
                .setEmail("test@example.com")
                .setPassword("test");
        userTest = this.userRepository.save(userTest);

        MangaEntity mangaTest = new MangaEntity()
                .setTitle("test4")
                .setVolumes("11")
                .setAuthors("tester4")
                .setGenres("12+")
                .setRanked(4)
                .setMembers(14)
                .setPopularity(4)
                .setPublished(LocalDate.of(2001,1,5))
                .setImageUrl("url4")
                .setThemes("action")
                .setStatusObject(StatusObjectEnum.ACCEPTED)
                .setCreated(LocalDate.of(2022,12,5))
                .setChapters(10)
                .setContent("lorem ispum4")
                .setUser(userTest);
        this.underTest.save(mangaTest);

        int expectedSize = 1;
        List<MangaEntity> mangaEntities = this.underTest.findMangaEntitiesByUsername(userTest.getUsername());
        Assertions.assertEquals(mangaEntities.size(),expectedSize);
        this.assertMangaIsEqual(mangaTest,mangaEntities.get(0));
    }
    @Test
    void findMangaEntitiesByUsername_ShouldReturnZeroEntities(){
        String usernameTest = "tester";
        List<MangaEntity> mangaEntities = this.underTest.findMangaEntitiesByUsername(usernameTest);
        Assertions.assertTrue(mangaEntities.isEmpty());
    }

    @Test
    void findMangaEntitiesByStatusObject_ShouldReturnTwoEntities(){
        int expectedSize = 2;
        List<MangaEntity> mangaEntities = this.underTest.findMangaEntitiesByStatusObject(StatusObjectEnum.DECLINED);
        Assertions.assertEquals(expectedSize,mangaEntities.size());
        this.assertMangaIsEqual(mangaTest2, mangaEntities.get(0));
        this.assertMangaIsEqual(mangaTest3, mangaEntities.get(1));
    }
    @Test
    void findMangaEntitiesByStatusObject_ShouldReturnOneEntities(){
        int expectedSize = 1;
        List<MangaEntity> mangaEntities = this.underTest.findMangaEntitiesByStatusObject(StatusObjectEnum.ACCEPTED);
        Assertions.assertEquals(expectedSize,mangaEntities.size());
        this.assertMangaIsEqual(mangaTest1, mangaEntities.get(0));

    }
    @Test
    void findMangaEntitiesByStatusObject_ShouldReturnZeroEntities(){
        List<MangaEntity> mangaEntities = this.underTest.findMangaEntitiesByStatusObject(StatusObjectEnum.PENDING);
        Assertions.assertTrue(mangaEntities.isEmpty());
    }

    @Test
    void findMangaEntitiesOrderByPopularityASC_ShouldReturnInCorrectOrder(){
        int expectedSize = 3;
        List<MangaEntity> mangaEntities = this.underTest.findMangaEntitiesOrderByPopularityASC();
        Assertions.assertEquals(mangaEntities.size(),expectedSize);
        this.assertMangaIsEqual(mangaTest1,mangaEntities.get(0));
        this.assertMangaIsEqual(mangaTest2,mangaEntities.get(1));
        this.assertMangaIsEqual(mangaTest3,mangaEntities.get(2));
    }
    void assertMangaIsEqual(MangaEntity m1, MangaEntity m2){
        Assertions.assertEquals(m1.getTitle(),m2.getTitle());
        Assertions.assertEquals(m1.getVolumes(),m2.getVolumes());
        Assertions.assertEquals(m1.getAuthors(),m2.getAuthors());
        Assertions.assertEquals(m1.getGenres(),m2.getGenres());
        Assertions.assertEquals(m1.getRating(),m2.getRating());
        Assertions.assertEquals(m1.getRanked(),m2.getRanked());
        Assertions.assertEquals(m1.getMembers(),m2.getMembers());
        Assertions.assertEquals(m1.getPopularity(),m2.getPopularity());
        Assertions.assertEquals(m1.getPublished(),m2.getPublished());
        Assertions.assertEquals(m1.getImageUrl(),m2.getImageUrl());
        Assertions.assertEquals(m1.getThemes(),m2.getThemes());
        Assertions.assertEquals(m1.getContent(),m2.getContent());
        Assertions.assertEquals(m1.getChapters(),m2.getChapters());
        Assertions.assertEquals(m1.getStatusObject(),m2.getStatusObject());

    }
}