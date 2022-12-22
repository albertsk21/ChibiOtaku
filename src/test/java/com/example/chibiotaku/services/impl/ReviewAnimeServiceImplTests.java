package com.example.chibiotaku.services.impl;

import com.example.chibiotaku.domain.api.ReviewApiDto;
import com.example.chibiotaku.domain.dtos.ReviewDto;
import com.example.chibiotaku.domain.entities.AnimeEntity;
import com.example.chibiotaku.domain.entities.ReviewAnimeEntity;
import com.example.chibiotaku.domain.entities.UserEntity;
import com.example.chibiotaku.domain.enums.StatusReviewEnum;
import com.example.chibiotaku.domain.views.ReviewVM;
import com.example.chibiotaku.repo.AnimeRepository;
import com.example.chibiotaku.repo.ReviewAnimeRepository;
import com.example.chibiotaku.repo.UserRepository;
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
class ReviewAnimeServiceImplTests {


    private ReviewAnimeServiceImpl underTest;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private AnimeRepository mockAnimeRepository;
    @Mock
    private ReviewAnimeRepository mockReviewAnimeRepository;

    @BeforeEach
    void setUp() {
        underTest = new ReviewAnimeServiceImpl(mockReviewAnimeRepository,
                mockUserRepository,
                mockAnimeRepository,
                new ModelMapper());
    }

    @Test
    void addReviewAnime_ObjectNotFound(){
        UUID randomAnimeId = UUID.randomUUID();
        String testUsername = "some user..";
        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> this.underTest.addReviewAnime(randomAnimeId,testUsername,new ReviewDto())
        );
    }

    @Test
    void addReviewAnime_ShouldSaveCorrectly(){
        UUID randomAnimeId = UUID.randomUUID();
        String username = "some username..";
        AnimeEntity animeTest = new AnimeEntity();
                animeTest.setId(randomAnimeId);

        Mockito.when(this.mockAnimeRepository.findAnimeEntityById(randomAnimeId))
                .thenReturn(Optional.of(animeTest));

        UserEntity userTest = new UserEntity();
        Mockito.when(this.mockUserRepository.findUserEntityByUsername(username))
                .thenReturn(Optional.of(userTest));

        ReviewDto reviewDto = new ReviewDto();
        ArgumentCaptor<ReviewAnimeEntity> argumentCaptor = ArgumentCaptor.forClass(ReviewAnimeEntity.class);
        this.underTest.addReviewAnime(randomAnimeId,username,reviewDto);

        verify(this.mockReviewAnimeRepository).save(argumentCaptor.capture());
        Assertions.assertFalse(argumentCaptor.getValue().isVerified());
        Assertions.assertEquals(animeTest,argumentCaptor.getValue().getAnime());
        Assertions.assertEquals(userTest,argumentCaptor.getValue().getUser());

    }

    @Test
    void addReviewAnime_UserNotFound(){
        UUID randomAnimeId = UUID.randomUUID();
        String username = "some username..";
        AnimeEntity animeTest = new AnimeEntity();
        animeTest.setId(randomAnimeId);

        Mockito.when(this.mockAnimeRepository.findAnimeEntityById(randomAnimeId))
                .thenReturn(Optional.of(animeTest));

        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> this.underTest.addReviewAnime(randomAnimeId,username,new ReviewDto())
        );
    }
    @Test
    void exportReviewsByAnimeId_ShouldReturnReviewVMList(){


        Object[] reviewTest1 = new Object[4];
            reviewTest1[0] = "username one";
            reviewTest1[1] = LocalDate.of(1999,2,1);
            reviewTest1[2] = StatusReviewEnum.NOT_RECOMMENDED;
            reviewTest1[3] = "some content one";

        Object[] reviewTest2 = new Object[4];
            reviewTest2[0] = "username two";
            reviewTest2[1] = LocalDate.of(2002,1,2);
            reviewTest2[2] = StatusReviewEnum.NOT_RECOMMENDED;
            reviewTest2[3] = "some content two";

        UUID randomAnimeId = UUID.randomUUID();

        List<Object[]> reviewsTest = new ArrayList<>(Arrays.asList(reviewTest1,reviewTest2));
        Mockito.when(this.mockReviewAnimeRepository.findReviewAnimeEntitiesByAnimeId(randomAnimeId))
                .thenReturn(reviewsTest);


        List<ReviewVM> actual = this.underTest.exportReviewsByAnimeId(randomAnimeId);


        Assertions.assertEquals(2,actual.size());
        Assertions.assertEquals(reviewTest1[3],actual.get(0).getContent());
        Assertions.assertEquals(reviewTest1[2],actual.get(0).getStatusReview());
        Assertions.assertEquals(reviewTest1[0],actual.get(0).getUsername());
        Assertions.assertEquals(reviewTest1[1],actual.get(0).getCreated());

        Assertions.assertEquals(reviewTest2[3],actual.get(1).getContent());
        Assertions.assertEquals(reviewTest2[2],actual.get(1).getStatusReview());
        Assertions.assertEquals(reviewTest2[0],actual.get(1).getUsername());
        Assertions.assertEquals(reviewTest2[1],actual.get(1).getCreated());

    }

    @Test
    void exportReviewsApiDtoLatestCreated_ShouldReturnReviewApiDto(){

        Object[] reviewTest1 = new Object[6];
        reviewTest1[0] = UUID.randomUUID();
        reviewTest1[1] = "some content";
        reviewTest1[2] = "some title";
        reviewTest1[3] = LocalDate.of(2002,3,1);
        reviewTest1[4] = "some image url";
        reviewTest1[5] = "username";
        Object[] reviewTest2 = new Object[6];
        reviewTest2[0] = UUID.randomUUID();
        reviewTest2[1] = "some content2";
        reviewTest2[2] = "some title2";
        reviewTest2[3] = LocalDate.of(2020,1,5);
        reviewTest2[4] = "some image url2";
        reviewTest2[5] = "username2";




        List<Object[]> expected = new ArrayList<>(Arrays.asList(reviewTest1,reviewTest2));
        Mockito.when(this.mockReviewAnimeRepository.findReviewAnimeEntitiesOrderByCreated())
                .thenReturn(expected);

        List<ReviewApiDto> actual = this.underTest.exportReviewsApiDtoLatestCreated();

        Assertions.assertEquals(2,actual.size());

        Assertions.assertEquals(expected.get(0)[0],actual.get(0).getId());
        Assertions.assertEquals(expected.get(0)[1],actual.get(0).getContent());
        Assertions.assertEquals(expected.get(0)[2],actual.get(0).getAnimeTitle());
        Assertions.assertEquals(expected.get(0)[3],actual.get(0).getPublished());
        Assertions.assertEquals(expected.get(0)[4],actual.get(0).getImageUrl());
        Assertions.assertEquals(expected.get(0)[5],actual.get(0).getUsername());

        Assertions.assertEquals(expected.get(1)[0],actual.get(1).getId());
        Assertions.assertEquals(expected.get(1)[1],actual.get(1).getContent());
        Assertions.assertEquals(expected.get(1)[2],actual.get(1).getAnimeTitle());
        Assertions.assertEquals(expected.get(1)[3],actual.get(1).getPublished());
        Assertions.assertEquals(expected.get(1)[4],actual.get(1).getImageUrl());
        Assertions.assertEquals(expected.get(1)[5],actual.get(1).getUsername());

    }

}