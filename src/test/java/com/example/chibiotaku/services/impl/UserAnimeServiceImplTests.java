package com.example.chibiotaku.services.impl;


import com.example.chibiotaku.domain.dtos.UserAnimeDto;
import com.example.chibiotaku.domain.entities.AnimeEntity;
import com.example.chibiotaku.domain.entities.UserAnimeEntity;
import com.example.chibiotaku.domain.entities.UserEntity;
import com.example.chibiotaku.domain.enums.AnimeTypeEnum;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import com.example.chibiotaku.domain.enums.WatchStatusEnum;
import com.example.chibiotaku.domain.views.UserItemUpdateVM;
import com.example.chibiotaku.repo.AnimeRepository;
import com.example.chibiotaku.repo.UserAnimeRepository;
import com.example.chibiotaku.repo.UserRepository;
import com.example.chibiotaku.services.UserAnimeService;
import com.example.chibiotaku.util.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class UserAnimeServiceImplTests {

    private UserAnimeService underTest;

    @Mock
    private UserAnimeRepository mockUserAnimeRepository;
    @Mock
    private AnimeRepository mockAnimeRepository;
    @Mock
    private UserRepository mockUserRepository;


    private final String ANIME_ID = "2dee34f8-8bf2-4b3b-975e-cf1457e20aad";
    private final String USERNAME = "username for test";
    private final UUID RANDOM_UUID = UUID.randomUUID();

    private final UserEntity USER = new UserEntity()
            .setUsername("albert")
            .setEmail("albert@example.com")
            .setPassword("security");

    private  final AnimeEntity ANIME_ENTITY_TEST = new AnimeEntity()
            .setAired(LocalDate.now())
            .setDuration(24)
            .setEpisodes(0)
            .setGenres("some genres")
            .setImageUrl("testIamgeUrl")
            .setProducers("Test")
            .setRank(1)
            .setRating("rating test")
            .setStatusObject(StatusObjectEnum.ACCEPTED)
            .setTitle("title test")
            .setPopularity(1);
    private  final AnimeEntity ANIME_ENTITY_TEST_2 = new AnimeEntity()
            .setAired(LocalDate.now())
            .setDuration(25)
            .setEpisodes(0)
            .setGenres("some genres2")
            .setImageUrl("testIamgeUrl2")
            .setProducers("Test2")
            .setRank(1)
            .setRating("rating test2")
            .setStatusObject(StatusObjectEnum.ACCEPTED)
            .setTitle("title test2")
            .setPopularity(2)
            .setType(AnimeTypeEnum.MOVIE);

    private final UserAnimeDto USER_ANIME_TEST = new UserAnimeDto()
            .setAired(LocalDate.now())
            .setEps(0)
            .setScore(10.0)
            .setImageUrl("testIamgeUrl")
            .setStatusObject(StatusObjectEnum.ACCEPTED)
            .setTitle("test1")
            .setPopularity(1);


    @BeforeEach
    void setUp() {
        this.underTest = new UserAnimeServiceImpl(
                mockUserAnimeRepository,
                new ModelMapper(),
                mockAnimeRepository,
                mockUserRepository
        );
    }

    @Test
    void findMyAnimeByAnimeIdAndUsername_ShouldReturnNull(){

        UserAnimeDto actual = this.underTest.findMyAnimeByAnimeIdAndUsername(ANIME_ID,USERNAME);
        Assertions.assertNull(actual);

    }

    @Test
    void findMyAnimeByAnimeIdAndUsername_ShouldReturnUserAnimeDto(){

        UserAnimeEntity expected = new UserAnimeEntity()
                .setScore(10)
                .setWatchStatus(WatchStatusEnum.NOT_FINISHED)
                .setCreated(LocalDate.of(2022,1,3))
                .setAnime(ANIME_ENTITY_TEST);


        Mockito.when(this.mockUserAnimeRepository.findAnimeByAnimeIdAndUserId(UUID.fromString(ANIME_ID),
                        USERNAME))
                .thenReturn(Optional.of(expected));
        UserAnimeDto actual = this.underTest.findMyAnimeByAnimeIdAndUsername(ANIME_ID,USERNAME);


        Assertions.assertEquals(expected.getScore(),actual.getScore());
        Assertions.assertEquals(expected.getWatchStatus(),actual.getWatchStatus());
        Assertions.assertEquals(ANIME_ENTITY_TEST.getType(),actual.getType());
        Assertions.assertEquals(ANIME_ENTITY_TEST.getEpisodes(),actual.getEps());
        Assertions.assertEquals(ANIME_ENTITY_TEST.getContent(),actual.getContent());
        Assertions.assertEquals(ANIME_ENTITY_TEST.getImageUrl(),actual.getImageUrl());
        Assertions.assertEquals(ANIME_ENTITY_TEST.getId(),actual.getId());
        Assertions.assertEquals(ANIME_ENTITY_TEST.getTitle(),actual.getTitle());
        Assertions.assertEquals(ANIME_ENTITY_TEST.getAired(),actual.getAired());
        Assertions.assertEquals(ANIME_ENTITY_TEST.getPopularity(),actual.getPopularity());
        Assertions.assertEquals(ANIME_ENTITY_TEST.getStatusObject(),actual.getStatusObject());
    }


    @Test
    void addAnimeToWatchList_ShouldCreateUserAnimeEntityWhenIsEmpty(){




       Mockito.when(this.mockAnimeRepository.findAnimeEntityById(RANDOM_UUID))
                .thenReturn(Optional.of(ANIME_ENTITY_TEST));
       Mockito.when(this.mockUserRepository.findUserEntityByUsername(USERNAME))
               .thenReturn(Optional.of(USER));

       ArgumentCaptor<UserAnimeEntity> argumentCaptor = ArgumentCaptor.forClass(UserAnimeEntity.class);
       this.underTest.addAnimeToWatchList(USER_ANIME_TEST,RANDOM_UUID,USERNAME);

        verify(this.mockUserAnimeRepository).save(argumentCaptor.capture());

        Assertions.assertEquals(USER_ANIME_TEST.getWatchStatus(),argumentCaptor.getValue().getWatchStatus());
        Assertions.assertEquals(ANIME_ENTITY_TEST,argumentCaptor.getValue().getAnime());
        Assertions.assertEquals(USER,argumentCaptor.getValue().getUser());
        Assertions.assertEquals(USER_ANIME_TEST.getScore(),argumentCaptor.getValue().getScore());
    }

    @Test
    void addAnimeToWatchList_ObjectNotFound(){
        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> this.underTest.addAnimeToWatchList(new UserAnimeDto(),RANDOM_UUID,USERNAME)
        );
    }

    @Test
    void addAnimeToWatchList_UserNotFound() {
        AnimeEntity anime = new AnimeEntity();
        Mockito.when(this.mockAnimeRepository.findAnimeEntityById(RANDOM_UUID))
                .thenReturn(Optional.of(anime));
        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> this.underTest.addAnimeToWatchList(new UserAnimeDto(),RANDOM_UUID,USERNAME)
        );
    }

    @Test
    void addAnimeToWatchList_OptionalIsPresent(){
       // I copied this code because I needed just two times this entities :)

        UserAnimeEntity userAnimeEntityTest = new UserAnimeEntity()
                .setScore(10)
                .setAnime(ANIME_ENTITY_TEST)
                .setUser(USER);

        Mockito.when(this.mockUserAnimeRepository.findUserAnimeEntityByUserUsernameAndAndAnimeId(
                        USERNAME,RANDOM_UUID))
                .thenReturn(Optional.of(userAnimeEntityTest));

        ArgumentCaptor<UserAnimeEntity> argumentCaptor = ArgumentCaptor.forClass(UserAnimeEntity.class);
        this.underTest.addAnimeToWatchList(USER_ANIME_TEST,RANDOM_UUID,USERNAME);

        verify(this.mockUserAnimeRepository).save(argumentCaptor.capture());

        Assertions.assertEquals(USER_ANIME_TEST.getScore(),argumentCaptor.getValue().getScore());
        Assertions.assertEquals(USER_ANIME_TEST.getWatchStatus(),argumentCaptor.getValue().getWatchStatus());

    }


    @Test
    void deleteMyAnimeByIdAndUsername_ObjectNotFound(){
        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> this.underTest.deleteMyAnimeByIdAndUsername(RANDOM_UUID,USERNAME)
        );
    }

    @Test
    void deleteMyAnimeByIdAndUsername_ShouldWorkCorrectly(){


        UserAnimeEntity userAnimeTest = new UserAnimeEntity()
                .setUser(USER)
                .setAnime(ANIME_ENTITY_TEST)
                .setScore(10)
                .setWatchStatus(WatchStatusEnum.NOT_FINISHED)
                .setCreated(LocalDate.of(2022,1,2));

        UUID animeUUID = UUID.fromString(ANIME_ID);
        Mockito.when(this.mockUserAnimeRepository.findUserAnimeEntityByUserUsernameAndAndAnimeId(USERNAME,animeUUID))
                .thenReturn(Optional.of(userAnimeTest));


        this.underTest.deleteMyAnimeByIdAndUsername(animeUUID,USERNAME);
        verify(this.mockUserAnimeRepository).delete(userAnimeTest);
    }

    @Test
    void countUserAnimeByWatchStatusAndUsername_ShouldReturnZERO(){
        int actual = this.underTest.countUserAnimeByWatchStatusAndUsername(USERNAME,WatchStatusEnum.NOT_FINISHED);
        Assertions.assertEquals(0,actual);
    }

    @Test
    void countUserAnimeByWatchStatusAndUsername_ShouldReturnPositiveNumber(){

        Mockito.when(this.mockUserAnimeRepository.countByWatchStatusAndUsername(USERNAME,WatchStatusEnum.COMPLETE))
                .thenReturn(10);

        int expectedCounter = 10;
        int actual = this.underTest.countUserAnimeByWatchStatusAndUsername(USERNAME,WatchStatusEnum.COMPLETE);
        Assertions.assertEquals(expectedCounter,actual);
    }

    @Test
    void extractAnimesVMByUsername_ReturnUserItemUpdateVMList(){


        Object[] animeFormat1 = new Object[2];
                 animeFormat1[1] = UUID.randomUUID();
                 animeFormat1[0] = "some image url1";

        Object[] animeFormat2 = new Object[2];
                 animeFormat2[1] = UUID.randomUUID();
                 animeFormat2[0] = "some image url1";

        Object[] animeFormat3 = new Object[2];
                 animeFormat3[1] = UUID.randomUUID();
                 animeFormat3[0] = "some image url1";
        List<Object[]> expected = new ArrayList<>(Arrays.asList(animeFormat1,animeFormat2,animeFormat3));


        String  username = "test username";
        Mockito.when(this.mockUserAnimeRepository.findUserAnimeFormatByUsername(username))
                .thenReturn(expected);
        List<UserItemUpdateVM> actual = this.underTest.extractAnimesVMByUsername(username);

        Assertions.assertEquals(animeFormat1[0],actual.get(0).getImageUrl());
        Assertions.assertEquals(animeFormat1[1],actual.get(0).getId());

        Assertions.assertEquals(animeFormat2[0],actual.get(1).getImageUrl());
        Assertions.assertEquals(animeFormat2[1],actual.get(1).getId());

        Assertions.assertEquals(animeFormat3[0],actual.get(2).getImageUrl());
        Assertions.assertEquals(animeFormat3[1],actual.get(2).getId());
    }

    @Test
    void findUserAnimeDtoByUsernameAndAnimeId_ShouldReturnNull(){
        UserAnimeDto actual = this.underTest.findUserAnimeDtoByUsernameAndAnimeId(USERNAME,RANDOM_UUID);
        Assertions.assertNull(actual);
    }

    @Test
    void findUserAnimeDtoByUsernameAndAnimeId_ShouldReturnUserAnimeDto_WithNullValues(){
        UserAnimeEntity expected = new UserAnimeEntity()
                .setUser(USER)
                .setAnime(ANIME_ENTITY_TEST)
                .setScore(10)
                .setWatchStatus(WatchStatusEnum.NOT_FINISHED)
                .setCreated(LocalDate.of(2022,1,2));

        Mockito.when(this.mockUserAnimeRepository.findUserAnimeEntityByUserUsernameAndAndAnimeId(USERNAME,RANDOM_UUID))
                .thenReturn(Optional.of(expected));

        UserAnimeDto actual = this.underTest.findUserAnimeDtoByUsernameAndAnimeId(USERNAME,RANDOM_UUID);

        Assertions.assertEquals(expected.getScore(),actual.getScore());
        Assertions.assertEquals(expected.getWatchStatus(),actual.getWatchStatus());
    }

    @Test
    void extractAnimesByUsernameAndWatchEnum_ShouldReturnUserAnimeDtoList_WtihoutNullValues(){
        UserAnimeEntity expected = new UserAnimeEntity()
                .setUser(USER)
                .setAnime(ANIME_ENTITY_TEST)
                .setScore(10)
                .setWatchStatus(WatchStatusEnum.NOT_FINISHED)
                .setCreated(LocalDate.of(2022,1,2));


        Mockito.when(this.mockUserAnimeRepository.findAnimeEntityByUsernameAndWatchStatus(USERNAME,WatchStatusEnum.NOT_FINISHED))
                .thenReturn(new ArrayList<>(Arrays.asList(expected)));
        List<UserAnimeDto> actual = this.underTest.extractAnimesByUsernameAndWatchEnum(USERNAME,WatchStatusEnum.NOT_FINISHED);

        verify(this.mockUserAnimeRepository).findAnimeEntityByUsernameAndWatchStatus(USERNAME,WatchStatusEnum.NOT_FINISHED);


        Assertions.assertEquals(expected.getScore(),actual.get(0).getScore());
        Assertions.assertEquals(expected.getWatchStatus(),actual.get(0).getWatchStatus());
        Assertions.assertEquals(ANIME_ENTITY_TEST.getType(),actual.get(0).getType());
        Assertions.assertEquals(ANIME_ENTITY_TEST.getEpisodes(),actual.get(0).getEps());
        Assertions.assertEquals(ANIME_ENTITY_TEST.getContent(),actual.get(0).getContent());
        Assertions.assertEquals(ANIME_ENTITY_TEST.getImageUrl(),actual.get(0).getImageUrl());
        Assertions.assertEquals(ANIME_ENTITY_TEST.getId(),actual.get(0).getId());
        Assertions.assertEquals(ANIME_ENTITY_TEST.getTitle(),actual.get(0).getTitle());
        Assertions.assertEquals(ANIME_ENTITY_TEST.getAired(),actual.get(0).getAired());
        Assertions.assertEquals(ANIME_ENTITY_TEST.getPopularity(),actual.get(0).getPopularity());
        Assertions.assertEquals(ANIME_ENTITY_TEST.getStatusObject(),actual.get(0).getStatusObject());

    }

    @Test
    void getAverageScoreByAnimeTitle_ShouldReturnZero(){
        Double actual = this.mockUserAnimeRepository.getAverageScoreByAnimeTitle(ANIME_ENTITY_TEST.getTitle());
        Assertions.assertEquals(0,actual);
    }
    @Test
    void getAverageScoreByAnimeTitle_ShouldReturnDoubleNumber(){
        Mockito.when(this.mockUserAnimeRepository.getAverageScoreByAnimeTitle(ANIME_ENTITY_TEST.getTitle()))
                .thenReturn(9.0);
        Double actual = this.underTest.getAverageScoreByAnimeTitle(ANIME_ENTITY_TEST.getTitle());

        Double expected = 9.0;
        Assertions.assertEquals(expected,actual);
    }


    @Test
    void getCounterByAnimeTitle_ShouldReturnIntNumber(){

        Mockito.when(this.mockUserAnimeRepository.countUserAnimeEntityByAnimeTitle(ANIME_ENTITY_TEST.getTitle()))
                .thenReturn(10);
        int actual = this.underTest.getCounterByAnimeTitle(ANIME_ENTITY_TEST.getTitle());


        int expected = 10;
        Assertions.assertEquals(expected,actual);

    }

    @Test
    void getCounterByAnimeTitle_ShouldReturnZero(){
        int actual = this.underTest.getCounterByAnimeTitle(ANIME_ENTITY_TEST.getTitle());
        int expected = 0;
        Assertions.assertEquals(expected,actual);
    }
}