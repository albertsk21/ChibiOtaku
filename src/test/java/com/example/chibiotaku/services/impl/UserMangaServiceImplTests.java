package com.example.chibiotaku.services.impl;

import com.example.chibiotaku.domain.dtos.UserMangaDto;
import com.example.chibiotaku.domain.entities.MangaEntity;
import com.example.chibiotaku.domain.entities.UserEntity;
import com.example.chibiotaku.domain.entities.UserMangaEntity;
import com.example.chibiotaku.domain.enums.WatchStatusEnum;
import com.example.chibiotaku.domain.views.UserItemUpdateVM;
import com.example.chibiotaku.repo.MangaRepository;
import com.example.chibiotaku.repo.UserMangaRepository;
import com.example.chibiotaku.repo.UserRepository;
import com.example.chibiotaku.services.UserMangaService;
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

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserMangaServiceImplTests {

    private UserMangaService underTest;
    @Mock
    private UserMangaRepository mockUserMangaRepository;
    @Mock
    private MangaRepository mockMangaRepository;
    @Mock
    private UserRepository mockUserRepository;


    private final UUID RANDOM_UUID = UUID.randomUUID();
    private final MangaEntity MANGA_ENTITY1 = new MangaEntity()
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
            .setVolumes("10");
    private final MangaEntity MANGA_ENTITY2 = new MangaEntity()
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
            .setVolumes("9");


    private UserEntity USER_ENTITY_TEST = new UserEntity()
        .setUsername("albert")
        .setEmail("albert@example.com")
        .setPassword("security");
    private UserMangaEntity USER_MANGA_ENTITY1 = new UserMangaEntity()
            .setUser(USER_ENTITY_TEST)
            .setManga(MANGA_ENTITY1)
            .setScore(9)
            .setCreated(LocalDate.now())
            .setWatchStatus(WatchStatusEnum.NOT_FINISHED);
    private UserMangaEntity USER_MANGA_ENTITY2 = new UserMangaEntity()
            .setUser(USER_ENTITY_TEST)
            .setManga(MANGA_ENTITY2)
            .setScore(6)
            .setCreated(LocalDate.now())
            .setWatchStatus(WatchStatusEnum.DROPPED);
    @BeforeEach
    void setUp() {
        this.underTest = new UserMangaServiceImpl(
                mockUserMangaRepository,
                mockMangaRepository,
                mockUserRepository,
                new ModelMapper()
        );
    }

    @Test
    void findMyMangaByMangaIdAndUsername_ShouldReturnNull(){
        UserMangaDto actual = this.underTest.findMyMangaByMangaIdAndUsername(RANDOM_UUID,USER_ENTITY_TEST.getUsername());
        Assertions.assertNull(actual);
    }
    @Test
    void findMyMangaByMangaIdAndUsername_ShouldReturnUserMangaDto_WithValuesNull(){
        Mockito.when(this.mockUserMangaRepository.findUserMangaEntityByUserUsernameAndAndMangaId(USER_ENTITY_TEST.getUsername(),RANDOM_UUID))
                .thenReturn(Optional.of(USER_MANGA_ENTITY1));

        UserMangaDto actual = this.underTest.findMyMangaByMangaIdAndUsername(RANDOM_UUID,USER_ENTITY_TEST.getUsername());

        Assertions.assertEquals(USER_MANGA_ENTITY1.getScore(),actual.getScore());
        Assertions.assertEquals(USER_MANGA_ENTITY1.getWatchStatus(),actual.getWatchStatus());
    }

    @Test
    void addMangaToReadList_ShouldWorkCorrectOptionalIsPresent(){
        Mockito.when(this.mockUserMangaRepository.findUserMangaEntityByUserUsernameAndAndMangaId(USER_ENTITY_TEST.getUsername(),RANDOM_UUID))
                .thenReturn(Optional.of(USER_MANGA_ENTITY1));

        UserMangaDto userMangaDto = new UserMangaDto()
                .setScore(10)
                .setWatchStatus(WatchStatusEnum.NOT_FINISHED);

        ArgumentCaptor<UserMangaEntity> argumentCaptor = ArgumentCaptor.forClass(UserMangaEntity.class);

        this.underTest.addMangaToReadList(userMangaDto, RANDOM_UUID,USER_ENTITY_TEST.getUsername());
        verify(this.mockUserMangaRepository).save(argumentCaptor.capture());

        Assertions.assertEquals(userMangaDto.getScore(),argumentCaptor.getValue().getScore());
        Assertions.assertEquals(userMangaDto.getWatchStatus(),argumentCaptor.getValue().getWatchStatus());


    }
    @Test
    void addMangaToReadList_ObjectNotFound(){
        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> this.underTest.addMangaToReadList(new UserMangaDto(),RANDOM_UUID,USER_ENTITY_TEST.getUsername())
        );
    }
    @Test
    void addMangaToReadList_UserNotFound(){
        Mockito.when(this.mockMangaRepository.findMangaEntityById(RANDOM_UUID))
                        .thenReturn(Optional.of(MANGA_ENTITY1));
        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> this.underTest.addMangaToReadList(new UserMangaDto(),RANDOM_UUID,USER_ENTITY_TEST.getUsername())
        );
    }

    @Test
    void addMangaToList_ShouldAddWithOptionalIsNotPresent(){
        Mockito.when(this.mockMangaRepository.findMangaEntityById(RANDOM_UUID))
                .thenReturn(Optional.of(MANGA_ENTITY1));

        Mockito.when(this.mockUserRepository.findUserEntityByUsername(USER_ENTITY_TEST.getUsername()))
                .thenReturn(Optional.of(USER_ENTITY_TEST));

        UserMangaDto userMangaDto = new UserMangaDto()
                .setScore(10)
                .setWatchStatus(WatchStatusEnum.NOT_FINISHED);

        ArgumentCaptor<UserMangaEntity> argumentCaptor = ArgumentCaptor.forClass(UserMangaEntity.class);
        this.underTest.addMangaToReadList(userMangaDto,RANDOM_UUID,USER_ENTITY_TEST.getUsername());

        verify(this.mockUserMangaRepository).save(argumentCaptor.capture());

        Assertions.assertEquals(userMangaDto.getWatchStatus(),argumentCaptor.getValue().getWatchStatus());
        Assertions.assertEquals(userMangaDto.getScore(),argumentCaptor.getValue().getScore());
        Assertions.assertEquals(USER_ENTITY_TEST,argumentCaptor.getValue().getUser());
        Assertions.assertEquals(MANGA_ENTITY1,argumentCaptor.getValue().getManga());
    }


    @Test
    void deleteUserMangaByMangaIdAndUsermane_ShouldNotUseDeleteOperation(){
        this.underTest.deleteUserMangaByMangaIdAndUsermane(RANDOM_UUID,USER_ENTITY_TEST.getUsername());
        ArgumentCaptor<UserMangaEntity> argumentCaptor = ArgumentCaptor.forClass(UserMangaEntity.class);
        verify(this.mockUserMangaRepository, never()).delete(argumentCaptor.capture());
    }
    @Test
    void deleteUserMangaByMangaIdAndUsermane_ShouldUseDeleteOperation(){
        Mockito.when(this.mockUserMangaRepository.findUserMangaEntityByUserUsernameAndAndMangaId(USER_ENTITY_TEST.getUsername(),RANDOM_UUID))
                .thenReturn(Optional.of(USER_MANGA_ENTITY1));
        ArgumentCaptor<UserMangaEntity> argumentCaptor = ArgumentCaptor.forClass(UserMangaEntity.class);

        this.underTest.deleteUserMangaByMangaIdAndUsermane(RANDOM_UUID, USER_ENTITY_TEST.getUsername());
        verify(this.mockUserMangaRepository).delete(argumentCaptor.capture());
    }

    @Test
    void extractUserMangaByWatchStatusAndUsername_ShouldReturnUserMangaDtoListWithoutNullValues(){

        List<UserMangaEntity> expected = new ArrayList<>(Arrays.asList(USER_MANGA_ENTITY2));

        Mockito.when(this.mockUserMangaRepository.findMangaEntityByUsernameAndWatchStatus(
                USER_ENTITY_TEST.getUsername(), WatchStatusEnum.DROPPED))
                .thenReturn(expected);

        List<UserMangaDto> actual = this.underTest.extractUserMangaByWatchStatusAndUsername(
                WatchStatusEnum.DROPPED,USER_ENTITY_TEST.getUsername());



        Assertions.assertEquals(expected.get(0).getScore(),actual.get(0).getScore());
        Assertions.assertEquals(expected.get(0).getManga().getTitle(), actual.get(0).getTitle());
        Assertions.assertEquals(expected.get(0).getManga().getVolumes(), String.valueOf(actual.get(0).getVolumes()));
        Assertions.assertEquals(expected.get(0).getId(),actual.get(0).getId());
        Assertions.assertEquals(expected.get(0).getWatchStatus(),actual.get(0).getWatchStatus());
        Assertions.assertEquals(expected.get(0).getManga().getImageUrl(), actual.get(0).getImageUrl());
    }

    @Test
    void extractAverageScoreByMangaId_ShouldReturnZero(){
        Double actual = this.underTest.extractAverageScoreByMangaId(RANDOM_UUID);

        Assertions.assertEquals(0,actual);
    }

    @Test
    void extractAverageScoreByMangaId_ShouldReturnDoubleValue(){
        Mockito.when(this.mockUserMangaRepository.getAverageScoreByMangaId(RANDOM_UUID))
                .thenReturn(9.0);
        Double actual = this.underTest.extractAverageScoreByMangaId(RANDOM_UUID);


        Double expected = 9.0;
        Assertions.assertEquals(expected,actual);

    }

    @Test
    void findUserMangaDtoByUsernameAndMangaId_ReturnNull(){
        UserMangaDto actual = this.underTest.findUserMangaDtoByUsernameAndMangaId(USER_ENTITY_TEST.getUsername(), RANDOM_UUID);
        Assertions.assertNull(actual);
    }


    @Test
    void findUserMangaDtoByUsernameAndMangaId_ShouldReturnUserMangaDto(){

        Mockito.when(this.mockUserMangaRepository.findUserMangaEntityByUserUsernameAndAndMangaId(USER_ENTITY_TEST.getUsername(),RANDOM_UUID))
                .thenReturn(Optional.of(USER_MANGA_ENTITY1));
        UserMangaDto actual = this.underTest.findUserMangaDtoByUsernameAndMangaId(USER_ENTITY_TEST.getUsername(),RANDOM_UUID);



        Assertions.assertEquals(USER_MANGA_ENTITY1.getWatchStatus(),actual.getWatchStatus());
        Assertions.assertEquals(USER_MANGA_ENTITY1.getScore(),actual.getScore());
    }

    @Test
    void getCounterUsersByMangaId_ShouldReturnZERO(){
        int actual = this.underTest.getCounterUsersByMangaId(RANDOM_UUID);

        Assertions.assertEquals(0,actual);
    }
    @Test
    void getCounterUsersByMangaId_ShouldReturnNumber(){

        Mockito.when(this.mockUserMangaRepository.countUserMangaEntityByMangaId(RANDOM_UUID))
                .thenReturn(10);
        int actual = this.underTest.getCounterUsersByMangaId(RANDOM_UUID);
        int expected = 10;
        Assertions.assertEquals(expected,actual);
    }
    @Test
    void countUserMangaByWatchStatusAndUsername_ShouldReturnZERO(){
        int actual = this.underTest.countUserMangaByWatchStatusAndUsername(WatchStatusEnum.DROPPED,
                USER_ENTITY_TEST.getUsername());
        Assertions.assertEquals(0,actual);
    }
    @Test
    void countUserMangaByWatchStatusAndUsername_ShouldReturnNumber(){

        Mockito.when(this.mockUserMangaRepository.countByWatchStatusAndUsername(USER_ENTITY_TEST.getUsername(), WatchStatusEnum.DROPPED))
                .thenReturn(4);
        int actual = this.underTest.countUserMangaByWatchStatusAndUsername(WatchStatusEnum.DROPPED,
                USER_ENTITY_TEST.getUsername());
        int expected = 4;
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void extractMangaVMyUsername_ShouldReturnUserItemUpdateVMList(){

        Object[] testFormat = new Object[2];
        testFormat[0] = "some image url";
        testFormat[1] = UUID.randomUUID();
        Object[] testFormat2 = new Object[2];
        testFormat2[0] = "some image url2";
        testFormat2[1] = UUID.randomUUID();
        Object[] testFormat3 = new Object[2];
        testFormat3[0] = "some image url3";
        testFormat3[1] = UUID.randomUUID();

        List<Object[]> expected = new ArrayList<>(Arrays.asList(testFormat,testFormat2,testFormat3));

        Mockito.when(this.mockUserMangaRepository.findUserMangaFormatByUsername(USER_ENTITY_TEST.getUsername()))
                .thenReturn(expected);
        List<UserItemUpdateVM> actual = this.underTest.extractMangaVMyUsername(USER_ENTITY_TEST.getUsername());
        Assertions.assertEquals(3,actual.size());
        Assertions.assertEquals(testFormat[0],actual.get(0).getImageUrl());
        Assertions.assertEquals(testFormat[1],actual.get(0).getId());
        Assertions.assertEquals(testFormat2[0],actual.get(1).getImageUrl());
        Assertions.assertEquals(testFormat2[1],actual.get(1).getId());
        Assertions.assertEquals(testFormat3[0],actual.get(2).getImageUrl());
        Assertions.assertEquals(testFormat3[1],actual.get(2).getId());
    }
}