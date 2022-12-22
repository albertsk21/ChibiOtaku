package com.example.chibiotaku.services.impl;

import com.example.chibiotaku.domain.dtos.AnimeDto;
import com.example.chibiotaku.domain.dtos.UserAnimeDto;
import com.example.chibiotaku.domain.entities.AnimeEntity;
import com.example.chibiotaku.domain.entities.UserAnimeEntity;
import com.example.chibiotaku.domain.entities.UserEntity;
import com.example.chibiotaku.domain.enums.AnimeTypeEnum;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import com.example.chibiotaku.domain.enums.WatchStatusEnum;
import com.example.chibiotaku.domain.views.AnimeSearchVM;
import com.example.chibiotaku.repo.AnimeRepository;
import com.example.chibiotaku.repo.UserAnimeRepository;
import com.example.chibiotaku.repo.UserRepository;
import com.example.chibiotaku.services.AnimeService;
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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.swing.plaf.ListUI;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AnimeServiceImplTests {


    @Mock
    private AnimeRepository animeRepository;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private UserAnimeRepository mockUserAnimeRepository;


    private AnimeService underTest;
    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        underTest = new AnimeServiceImpl(
                animeRepository
                , mockUserRepository,
                modelMapper,
                mockUserAnimeRepository);

        testUser = new UserEntity()
                .setFirstName("albert")
                .setLastName("jack")
                .setPassword("security")
                .setEmail("albert@example.com");
    }


    @Test
    void saveAnime_userNotFound() {
        String unknownUsername = "invalid username";
        AnimeDto animeDto = new AnimeDto();
        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> underTest.saveAnime(animeDto, unknownUsername)
        );
    }


    @Test
    void saveAnime_correctWay() {
        //given
        String testUsername = "albert";

        AnimeDto animeDto = new AnimeDto();


        //when
        Mockito.when(this.mockUserRepository.findUserEntityByUsername(testUsername))
                .thenReturn(Optional.of(testUser));

        //then
        this.underTest.saveAnime(animeDto, testUsername);


        ArgumentCaptor<AnimeEntity> animeEntityArgumentCaptor = ArgumentCaptor.forClass(AnimeEntity.class);

        verify(this.animeRepository).save(animeEntityArgumentCaptor.capture());

        AnimeEntity anime = animeEntityArgumentCaptor.getValue();

        Assertions.assertEquals(StatusObjectEnum.PENDING, anime.getStatusObject());
        Assertions.assertEquals(testUser.getUsername(), anime.getUser().getUsername());
        Assertions.assertEquals(testUser.getEmail(), anime.getUser().getEmail());
        Assertions.assertEquals(testUser.getFirstName(), anime.getUser().getFirstName());
        Assertions.assertEquals(testUser.getLastName(), anime.getUser().getLastName());

    }

    @Test
    void getAnimesByTitle_ShouldReturnEmptyList() {
        String animeTitle = "invalid anime title";
        Mockito.when(this.animeRepository.findAnimeEntityContainsTitle(animeTitle))
                .thenReturn(new ArrayList<>());

        List<AnimeSearchVM> actual = this.underTest.getAnimesByTitle(animeTitle);
        verify(this.animeRepository).findAnimeEntityContainsTitle(animeTitle);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void getAnimesByTitle_ShouldReturnCorrectList() {

        // given

        String animeTitle = "anime Title";
        Object[] arrayObj = new Object[8];
        arrayObj[0] = UUID.fromString("7a4cd640-e61e-4de0-b242-2c8d589968bc");
        arrayObj[1] = animeTitle;
        arrayObj[2] = AnimeTypeEnum.MOVIE;
        arrayObj[3] = 12;
        arrayObj[4] = 1;
        arrayObj[5] = "some image url";
        arrayObj[6] = "some content";
        arrayObj[7] = StatusObjectEnum.PENDING;


        List<Object[]> objectsList = new ArrayList<>();
        objectsList.add(arrayObj);


        Mockito.when(this.animeRepository.findAnimeEntityContainsTitle(animeTitle))
                .thenReturn(objectsList);
        List<AnimeSearchVM> actual = this.underTest.getAnimesByTitle(animeTitle);
        //when
        verify(this.animeRepository).findAnimeEntityContainsTitle(animeTitle);

        int expectedSizeList = 1;
        Assertions.assertEquals(expectedSizeList, actual.size());
        Assertions.assertEquals(animeTitle, actual.get(0).getTitle());
        Assertions.assertEquals(arrayObj[0], actual.get(0).getId());
        Assertions.assertEquals(arrayObj[1], actual.get(0).getTitle());
        Assertions.assertEquals(arrayObj[2], actual.get(0).getType());
        Assertions.assertEquals(arrayObj[3], actual.get(0).getEpisodes());
        Assertions.assertEquals(arrayObj[4], actual.get(0).getPopularity());
        Assertions.assertEquals(arrayObj[5], actual.get(0).getImageUrl());
        Assertions.assertEquals(arrayObj[6], actual.get(0).getContent());
        Assertions.assertEquals(arrayObj[7], actual.get(0).getStatusObject());
    }

    @Test
    void getAnimeById_ObjectNotFound() {
        UUID testId = UUID.fromString("7a4cd640-e61e-4de0-b242-2c8d589968bc");
        Mockito.when(this.animeRepository.findAnimeEntityById(testId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> underTest.getAnimeById(testId)
        );
    }

    @Test
    void getAnimeById_ShouldReturnCorrectAnime() {
        AnimeEntity animeTest = new AnimeEntity()
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
                .setPopularity(1);


        UUID randomId = UUID.randomUUID();
        Mockito.when(this.animeRepository.findAnimeEntityById(randomId))
                .thenReturn(Optional.of(animeTest));


        AnimeDto actual = this.underTest.getAnimeById(randomId);



        this.assertAnimeDtoIsEqualWithEntity(actual,animeTest);
    }

    @Test
    void extractAnimesByUsername_ShouldReturnEmptyList() {
        String unknownUsername = "invalid username";
        Mockito.when(this.animeRepository.findAnimeEntitiesByUserUsername(unknownUsername))
                .thenReturn(new ArrayList<>());
        List<AnimeDto> animeDtos = this.underTest.extractAnimesByUsername(unknownUsername);
        verify(this.animeRepository).findAnimeEntitiesByUserUsername(unknownUsername);
        Assertions.assertTrue(animeDtos.isEmpty());

    }


    @Test
    void extractAnimesByUsername_ShouldReturnCorrectList() {
        AnimeEntity animeTest = new AnimeEntity()
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
                .setPopularity(1);

        String username = "albert";

        List<AnimeEntity> animeEntities = new ArrayList<>(Collections.singletonList(animeTest));
        Mockito.when(this.animeRepository.findAnimeEntitiesByUserUsername(username))
                .thenReturn(animeEntities);


        List<AnimeDto> actual = this.underTest.extractAnimesByUsername(username);

        verify(this.animeRepository).findAnimeEntitiesByUserUsername(username);

        Assertions.assertEquals(1, actual.size());

        this.assertAnimeDtoIsEqualWithEntity(actual.get(0),animeTest);
    }


    @Test
    void extractAllAnimeDtoByStatusObject_ShouldReturnCorrectList() {
        AnimeEntity animeTest = new AnimeEntity()
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
                .setPopularity(1);

        List<AnimeEntity> animeEntities = new ArrayList<>(Collections.singletonList(animeTest));


        Mockito.when(this.animeRepository.findAnimeEntitiesByStatusObject(StatusObjectEnum.ACCEPTED))
                .thenReturn(animeEntities);


        List<AnimeDto> actual = this.underTest.extractAllAnimeDtoByStatusObject(StatusObjectEnum.ACCEPTED);



        this.assertAnimeDtoIsEqualWithEntity(actual.get(0),animeTest);
    }

    @Test
    void extractAllAnimeDtoByStatusObject_ShouldReturnEmptyList() {

        Mockito.when(this.animeRepository.findAnimeEntitiesByStatusObject(StatusObjectEnum.ACCEPTED))
                .thenReturn(new ArrayList<>());

        List<AnimeDto> actual = this.underTest.extractAllAnimeDtoByStatusObject(StatusObjectEnum.ACCEPTED);
        verify(this.animeRepository).findAnimeEntitiesByStatusObject(StatusObjectEnum.ACCEPTED);


        Assertions.assertTrue(actual.isEmpty());
    }


    @Test
    void getAllAnimes_ShouldReturnEmptyList() {
        Mockito.when(this.animeRepository.findAllOrderByPopularityAndRanked())
                .thenReturn(new ArrayList<>());
        List<UserAnimeDto> actual = this.underTest.getAllAnimes();
        verify(this.animeRepository).findAllOrderByPopularityAndRanked();
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void getAllAnimes_ShouldReturnASCList() {
        AnimeEntity animeTest = new AnimeEntity()
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
                .setPopularity(1);
  AnimeEntity animeTest2 = new AnimeEntity()
                .setAired(LocalDate.now())
                .setDuration(24)
                .setEpisodes(0)
                .setGenres("test2")
                .setImageUrl("testIamgeUrl2")
                .setProducers("Test2")
                .setRank(1)
                .setRating("test2")
                .setStatusObject(StatusObjectEnum.DECLINED)
                .setTitle("test2")
                .setPopularity(2);

        List<AnimeEntity> animeEntities = new ArrayList<>(Arrays.asList(animeTest,animeTest2));


        Mockito.when(this.animeRepository.findAllOrderByPopularityAndRanked())
                .thenReturn(animeEntities);
        Mockito.when(this.mockUserAnimeRepository.getAverageScoreByAnimeTitle("test1"))
                .thenReturn(10.0);
        Mockito.when(this.mockUserAnimeRepository.getAverageScoreByAnimeTitle("test2"))
                .thenReturn(5.0);

        List<UserAnimeDto> actual = this.underTest.getAllAnimes();
        verify(this.animeRepository).findAllOrderByPopularityAndRanked();

        assertFalse(actual.isEmpty());
        Assertions.assertEquals(animeTest.getPopularity(),actual.get(0).getPopularity());
        Assertions.assertEquals(animeTest2.getPopularity(),actual.get(1).getPopularity());

    }


    @Test
    void editAnimeStatusByAnimeId_ObjectNotFound(){
        Assertions.assertThrows(ObjectNotFoundException.class,
                () -> {
            this.underTest.editAnimeStatusByAnimeId(StatusObjectEnum.ACCEPTED, UUID.randomUUID());
                });

    }

    @Test
    void editAnimeStatusByAnimeId_EditCorrectly(){
        AnimeEntity animeTest = new AnimeEntity()
                .setAired(LocalDate.now())
                .setDuration(24)
                .setEpisodes(0)
                .setGenres("test1")
                .setImageUrl("testIamgeUrl")
                .setProducers("Test")
                .setRank(1)
                .setRating("test")
                .setStatusObject(StatusObjectEnum.PENDING)
                .setTitle("test1")
                .setPopularity(1);

        UUID randomId = UUID.randomUUID();
        Mockito.when(this.animeRepository.findAnimeEntityById(randomId))
                .thenReturn(Optional.of(animeTest));


        ArgumentCaptor<AnimeEntity> animeEntityArgumentCaptor = ArgumentCaptor.forClass(AnimeEntity.class);


        this.underTest.editAnimeStatusByAnimeId(StatusObjectEnum.ACCEPTED,randomId);
        verify(this.animeRepository).save(animeEntityArgumentCaptor.capture());
        Assertions.assertEquals(StatusObjectEnum.ACCEPTED,animeEntityArgumentCaptor.getValue().getStatusObject());

    }


    @Test
    void findMostPopularAnime_ShouldConvertCorrectly(){
        AnimeEntity animeTest = new AnimeEntity()
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
                .setPopularity(1);
        AnimeEntity animeTest2 = new AnimeEntity()
                .setAired(LocalDate.now())
                .setDuration(24)
                .setEpisodes(0)
                .setGenres("test2")
                .setImageUrl("testIamgeUrl2")
                .setProducers("Test2")
                .setRank(1)
                .setRating("test2")
                .setStatusObject(StatusObjectEnum.DECLINED)
                .setTitle("test2")
                .setPopularity(2);

        Mockito.when(this.animeRepository.findAnimeEntitiesOrderByPopularityASC())
                .thenReturn(new ArrayList<>(Arrays.asList(animeTest,animeTest2)));

        List<AnimeDto> animeDtos = this.underTest.findMostPopularAnime();




        this.assertAnimeDtoIsEqualWithEntity(animeDtos.get(0),animeTest);
        this.assertAnimeDtoIsEqualWithEntity(animeDtos.get(1),animeTest2);
    }


    @Test
    void orderAnimesByScore_ShouldOrderCorrectlyWhenPopularityValuesAreEqual() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Class<?> animeServiceClass = this.underTest.getClass();
        String expectedClassName = "AnimeServiceImpl";
        Assertions.assertEquals(animeServiceClass.getSimpleName(),expectedClassName);


        Method[] methods = animeServiceClass.getDeclaredMethods();
        Method currentMethod = null;
        for (Method method : methods) {
            if (method.getName().equals("orderAnimesByScore")){
                currentMethod = method;
                break;
            }
        }
        Assertions.assertNotNull(currentMethod);

        currentMethod.setAccessible(true);


        UserAnimeDto userAnimeDto = new UserAnimeDto()
                .setPopularity(1)
                .setScore(8);
        UserAnimeDto userAnimeDto2 = new UserAnimeDto()
                .setPopularity(1)
                .setScore(10);


        List<UserAnimeDto> userAnimeDtoList = new ArrayList<>(Arrays.asList(userAnimeDto2,userAnimeDto));
        currentMethod.invoke(this.underTest, userAnimeDtoList);



        double expectedScoreFirst = 10.0;
        double expectedScoreSecond = 8.0;
        Assertions.assertEquals(expectedScoreFirst,userAnimeDtoList.get(0).getScore());
        Assertions.assertEquals(expectedScoreSecond,userAnimeDtoList.get(1).getScore());




    }
    private void assertAnimeDtoIsEqualWithEntity(AnimeDto animeDto, AnimeEntity anime){

        Assertions.assertEquals(anime.getTitle(), animeDto.getTitle());
        Assertions.assertEquals(anime.getGenres(), animeDto.getGenres());
        Assertions.assertEquals(anime.getEpisodes(), animeDto.getEpisodes());
        Assertions.assertEquals(anime.getImageUrl(), animeDto.getImageUrl());
        Assertions.assertEquals(anime.getContent(), animeDto.getContent());
        Assertions.assertEquals(anime.getRanked(), animeDto.getRanked());
        Assertions.assertEquals(anime.getRating(), animeDto.getRating());
        Assertions.assertEquals(anime.getPopularity(), animeDto.getPopularity());
        Assertions.assertEquals(anime.getAired(), animeDto.getAired());
        Assertions.assertEquals(anime.getId(), animeDto.getId());
    }
}