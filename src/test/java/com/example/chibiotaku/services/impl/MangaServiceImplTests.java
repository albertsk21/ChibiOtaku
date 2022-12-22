package com.example.chibiotaku.services.impl;

import com.example.chibiotaku.domain.dtos.MangaDto;
import com.example.chibiotaku.domain.entities.MangaEntity;
import com.example.chibiotaku.domain.entities.UserEntity;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import com.example.chibiotaku.domain.views.MangaSearchVM;
import com.example.chibiotaku.repo.MangaRepository;
import com.example.chibiotaku.repo.UserMangaRepository;
import com.example.chibiotaku.repo.UserRepository;
import com.example.chibiotaku.services.MangaService;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MangaServiceImplTests {



    private MangaService underTest;
    @Mock
    private UserMangaRepository mockUserMangaRepository;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private MangaRepository mockMangaRepository;

    private UserEntity testUser;

    private  MangaEntity testManga = new MangaEntity()
            .setChapters(2)
            .setContent("some test")
            .setImageUrl("some image")
            .setPopularity(1)
            .setAuthors("authors...")
            .setGenres("some genre")
            .setMembers(454)
            .setRanked(12)
            .setThemes("action")
            .setTitle("some title")
            .setPublished(LocalDate.of(2002,3,11))
            .setRating("some rating")
            .setVolumes("some volumes");
    MangaEntity testManga2 = new MangaEntity()
            .setChapters(3)
            .setContent("some test2")
            .setImageUrl("some image2")
            .setPopularity(2)
            .setAuthors("authors...2")
            .setGenres("some genre2")
            .setMembers(455)
            .setRanked(11)
            .setThemes("action2")
            .setTitle("some title2")
            .setPublished(LocalDate.of(1999,1,1))
            .setRating("some rating2")
            .setVolumes("some volumes2");
    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        this.underTest = new MangaServiceImpl(
                mockMangaRepository,
                mockUserMangaRepository,
                mockUserRepository,
                modelMapper);



        testUser = new UserEntity()
                .setUsername("albert")
                .setEmail("albert@example.com")
                .setPassword("security");
    }

    @Test
    void addManga_UserNotFound(){
        String unknownUsername = "invalid username";
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> this.underTest.addManga(new MangaDto(),unknownUsername));
    }

    @Test
    void addManga_ShouldSaveCorrectly(){
        String username = "albert";
        Mockito.when(this.mockUserRepository.findUserEntityByUsername(username))
                .thenReturn(Optional.of(testUser));

        MangaDto mangaDto = new MangaDto()
                .setChapters(2)
                .setContent("some test")
                .setImageUrl("some image")
                .setPopularity(1)
                .setAuthors("authors...")
                .setGenres("some genre")
                .setMembers(454)
                .setRanked(12)
                .setThemes("action")
                .setTitle("some title")
                .setPublished(LocalDate.of(2002,3,11))
                .setRating("some rating")
                .setVolumes("some volumes");
        ArgumentCaptor<MangaEntity> mangaEntityArgumentCaptor = ArgumentCaptor.forClass(MangaEntity.class);
        this.underTest.addManga(mangaDto,username);

        verify(this.mockMangaRepository).save(mangaEntityArgumentCaptor.capture());

        this.assertMangaDtoAndEntityIsEqual(mangaEntityArgumentCaptor.getValue(),mangaDto);
        Assertions.assertEquals(mangaEntityArgumentCaptor.getValue().getStatusObject(), StatusObjectEnum.PENDING);
    }

    @Test
    void getMangasByTitle_ShouldReturnEmptyList(){
        String title = "some title";

        Mockito.when(this.mockMangaRepository.findMangaEntityContainsTitle(title))
                .thenReturn(new ArrayList<>());
        List<MangaSearchVM> actual = this.underTest.getMangasByTitle(title);
        Assertions.assertTrue(actual.isEmpty());

    }

    @Test
    void getMangasByTitle_ShouldReturnCorrectEmpty(){

        Object[] manga = new Object[8];
                manga[0] = "306b2bba-181f-46e3-85f3-ff09a864a2a4";
                manga[2] = 2;
                manga[3] = "12";
                manga[6] = "some content";
                manga[5] = "some image";
                manga[4] = 1;
                manga[1] = "some title";
                manga[7] =StatusObjectEnum.ACCEPTED;


        Object[] manga2 = new Object[8];
                manga2[0] = "8be1d087-065a-4ee6-b09c-c6baee0077d7";
                manga2[2] = 3;
                manga2[3] = "32";
                manga2[6] = "some content2";
                manga2[5] = "some image2";
                manga2[4] = 2;
                manga2[1] = "some titl2";
                manga2[7] = StatusObjectEnum.ACCEPTED;

        String  mangaTitle = "title";

        Mockito.when(this.mockMangaRepository.findMangaEntityContainsTitle(mangaTitle))
                .thenReturn(new ArrayList<>(Arrays.asList(manga,manga2)));

        List<MangaSearchVM> actual = this.underTest.getMangasByTitle(mangaTitle);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(2,actual.size());

        Assertions.assertEquals(actual.get(0).getId().toString(),manga[0]);
        Assertions.assertEquals(actual.get(0).getTitle(),manga[1]);
        Assertions.assertEquals(actual.get(0).getChapters(),manga[2]);
        Assertions.assertEquals(actual.get(0).getVolumes(),Integer.parseInt(manga[3].toString()));
        Assertions.assertEquals(actual.get(0).getPopularity(),manga[4]);
        Assertions.assertEquals(actual.get(0).getImageUrl(),manga[5]);
        Assertions.assertEquals(actual.get(0).getContent(),manga[6]);
        Assertions.assertEquals(actual.get(0).getStatusObject(),manga[7]);


        Assertions.assertEquals(actual.get(1).getId().toString(),manga2[0]);
        Assertions.assertEquals(actual.get(1).getTitle(),manga2[1]);
        Assertions.assertEquals(actual.get(1).getChapters(),manga2[2]);
        Assertions.assertEquals(actual.get(1).getVolumes(),Integer.parseInt(manga2[3].toString()));
        Assertions.assertEquals(actual.get(1).getPopularity(),manga2[4]);
        Assertions.assertEquals(actual.get(1).getImageUrl(),manga2[5]);
        Assertions.assertEquals(actual.get(1).getContent(),manga2[6]);
        Assertions.assertEquals(actual.get(1).getStatusObject(),manga2[7]);
    }

    void assertMangaDtoAndEntityIsEqual(MangaEntity mangaEntity, MangaDto mangaDto){
        Assertions.assertEquals(mangaEntity.getChapters(),mangaDto.getChapters());
        Assertions.assertEquals(mangaEntity.getTitle(),mangaDto.getTitle());
        Assertions.assertEquals(mangaEntity.getVolumes(), mangaDto.getVolumes());
        Assertions.assertEquals(mangaEntity.getRanked(),mangaDto.getRanked());
        Assertions.assertEquals(mangaEntity.getRating(),mangaDto.getRating());
        Assertions.assertEquals(mangaEntity.getContent(),mangaDto.getContent());
        Assertions.assertEquals(mangaEntity.getImageUrl(),mangaDto.getImageUrl());
        Assertions.assertEquals(mangaEntity.getPublished(),mangaDto.getPublished());
    }

    @Test
    void getMangaById_ObjectNotFound(){

        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () ->{this.underTest.getMangaById(UUID.randomUUID());}
        );

    }
    @Test
    void getMangaById_ShouldReturnMangaDto(){
        MangaEntity manga = new MangaEntity()
                .setChapters(2)
                .setContent("some test")
                .setImageUrl("some image")
                .setPopularity(1)
                .setAuthors("authors...")
                .setGenres("some genre")
                .setMembers(454)
                .setRanked(12)
                .setThemes("action")
                .setTitle("some title")
                .setPublished(LocalDate.of(2002,3,11))
                .setRating("some rating")
                .setVolumes("some volumes");

        UUID randomId = UUID.randomUUID();
        Mockito.when(this.mockMangaRepository.findMangaEntityById(randomId))
                .thenReturn(Optional.of(manga));


        MangaDto actual = this.underTest.getMangaById(randomId);


        this.assertMangaDtoAndEntityIsEqual(manga,actual);
    }

    @Test
    void extractMangaByUsername_ShouldReturnEmptyList(){
        String  username = "some username";

        Mockito.when(this.mockMangaRepository.findMangaEntitiesByUsername(username))
                .thenReturn(new ArrayList<>());

        List<MangaDto> actual = this.underTest.extractMangaByUsername(username);

        verify(this.mockMangaRepository).findMangaEntitiesByUsername(username);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void extractMangaByUsername_ShouldReturnMangaDto(){



        List<MangaEntity> expected = new ArrayList<>(Arrays.asList(testManga, testManga2));

        Mockito.when(this.mockMangaRepository.findMangaEntitiesByUsername(this.testUser.getUsername()))
                .thenReturn(expected);


        List<MangaDto> actual = this.underTest.extractMangaByUsername(this.testUser.getUsername());

        verify(this.mockMangaRepository).findMangaEntitiesByUsername(this.testUser.getUsername());


        this.assertMangaDtoAndEntityIsEqual(testManga,actual.get(0));
        this.assertMangaDtoAndEntityIsEqual(testManga2,actual.get(1));
    }

    @Test
    void getAllManga_ShouldReturnMangaDtoList(){

        List<MangaEntity> expected = new ArrayList<>(Arrays.asList(testManga, testManga2));


        Mockito.when(this.mockMangaRepository.findAll())
                .thenReturn(expected);

        List<MangaDto> actual = this.underTest.getAllManga();

        Assertions.assertEquals(2,actual.size());
        this.assertMangaDtoAndEntityIsEqual(expected.get(0),actual.get(0));
        this.assertMangaDtoAndEntityIsEqual(expected.get(1),actual.get(1));

    }


    @Test
    void editMangaStatusByMangaId_ObjectNotFound(){

        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () ->
                    this.underTest.editMangaStatusByMangaId(StatusObjectEnum.ACCEPTED,UUID.randomUUID())

        );
    }


    @Test
    void editMangaStatusByMangaId_ShouldSaveCorrectly(){
        UUID randomId = UUID.randomUUID();
        Mockito.when(this.mockMangaRepository.findMangaEntityById(randomId))
                .thenReturn(Optional.of(testManga));

        ArgumentCaptor<MangaEntity> argumentCaptorManga = ArgumentCaptor.forClass(MangaEntity.class);

        this.underTest.editMangaStatusByMangaId(StatusObjectEnum.ACCEPTED,randomId);
        verify(this.mockMangaRepository).save(argumentCaptorManga.capture());
        Assertions.assertEquals(StatusObjectEnum.ACCEPTED,argumentCaptorManga.getValue().getStatusObject());
    }


    @Test
    void extractMangaByStatusObject_ShouldReturnMangaDtoList(){
        List<MangaEntity> expected = new ArrayList<>(Arrays.asList(testManga, testManga2));

        Mockito.when(this.mockMangaRepository.findMangaEntitiesByStatusObject(StatusObjectEnum.ACCEPTED))
                .thenReturn(expected);


        List<MangaDto> actual = this.underTest.extractMangaByStatusObject(StatusObjectEnum.ACCEPTED);



        Assertions.assertEquals(2,actual.size());
        this.assertMangaDtoAndEntityIsEqual(expected.get(0),actual.get(0));
        this.assertMangaDtoAndEntityIsEqual(expected.get(1),actual.get(1));

    }

    @Test
    void findMostPopularManga_ShouldConvertIntoMangaDtoList(){
        List<MangaEntity> expected = new ArrayList<>(Arrays.asList(testManga, testManga2));

        Mockito.when(this.mockMangaRepository.findMangaEntitiesOrderByPopularityASC())
                .thenReturn(expected);
        List<MangaDto> actual = this.underTest.findMostPopularManga();
        Assertions.assertEquals(2,actual.size());
        this.assertMangaDtoAndEntityIsEqual(expected.get(0),actual.get(0));
        this.assertMangaDtoAndEntityIsEqual(expected.get(1),actual.get(1));


    }


    @Test
    void orderMangaDtoByScore_ShouldOrderByScore() throws InvocationTargetException, IllegalAccessException {
        Class<?> animeServiceClass = this.underTest.getClass();
        String expectedClassName = "MangaServiceImpl";
        Assertions.assertEquals(animeServiceClass.getSimpleName(),expectedClassName);

        Method currentMethod = this.getMethodByName("orderMangaDtoByScore");
        currentMethod.setAccessible(true);


        MangaDto mangaDto1 = new MangaDto()
                .setScore(4)
                .setPopularity(1);
        MangaDto mangaDto2 = new MangaDto()
                .setScore(5)
                .setPopularity(2);
        MangaDto mangaDto3 = new MangaDto()
                .setScore(10)
                .setPopularity(3);

        List<MangaDto> mangaDtoList = new ArrayList<>(Arrays.asList(mangaDto1,mangaDto3,mangaDto2));
        currentMethod.invoke(this.underTest,mangaDtoList);

        Assertions.assertEquals(mangaDtoList.get(0),mangaDto3);
        Assertions.assertEquals(mangaDtoList.get(1),mangaDto2);
        Assertions.assertEquals(mangaDtoList.get(2),mangaDto1);

    }


    @Test
    void addAverageScoreFromDB_ShouldAddAverage() throws InvocationTargetException, IllegalAccessException {
        MangaDto mangaDto1 = new MangaDto()
                .setId(UUID.fromString("3b49a463-217b-4c9d-b290-aeb9a325db9b"));
        MangaDto mangaDto2 = new MangaDto()
                .setId(UUID.fromString("7125d585-f114-436a-8d63-31f6d0ce4cbb"));
        MangaDto mangaDto3 = new MangaDto()
                .setId(UUID.fromString("c55466a2-dd89-478b-8f7a-dda5967914a8"));
        List<MangaDto> mangaDtoList = new ArrayList<>(Arrays.asList(mangaDto1,mangaDto3,mangaDto2));


        Double firstScore = 10.0;
        Double secondScore = 8.0;
        Double thirdScore = 4.0;
        Mockito.when(this.mockUserMangaRepository.getAverageScoreByMangaId(mangaDto1.getId()))
                .thenReturn(null);
        Mockito.when(this.mockUserMangaRepository.getAverageScoreByMangaId(mangaDto2.getId()))
                .thenReturn(secondScore);
        Mockito.when(this.mockUserMangaRepository.getAverageScoreByMangaId(mangaDto3.getId()))
                .thenReturn(null);

        Method currentMethod = this.getMethodByName("addAverageScoreFromDB");

        currentMethod.setAccessible(true);
        currentMethod.invoke(this.underTest,mangaDtoList);



        Assertions.assertEquals(0.0,mangaDto1.getScore());
        Assertions.assertEquals(secondScore,mangaDto2.getScore());
        Assertions.assertEquals(0.0,mangaDto3.getScore());


    }


    private Method getMethodByName(String name){
        Class<?> animeServiceClass = this.underTest.getClass();
        String expectedClassName = "MangaServiceImpl";
        Assertions.assertEquals(animeServiceClass.getSimpleName(),expectedClassName);

        Method[] methods = animeServiceClass.getDeclaredMethods();
        Method currentMethod = null;
        for (Method method : methods) {
            if (method.getName().equals(name)){
                currentMethod = method;
                break;
            }
        }
        Assertions.assertNotNull(currentMethod);


        return currentMethod;

    }
}