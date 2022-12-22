package com.example.chibiotaku.services.impl;

import com.example.chibiotaku.domain.dtos.ReviewDto;
import com.example.chibiotaku.domain.dtos.UserMangaDto;
import com.example.chibiotaku.domain.entities.MangaEntity;
import com.example.chibiotaku.domain.entities.ReviewMangaEntity;
import com.example.chibiotaku.domain.entities.UserEntity;
import com.example.chibiotaku.domain.entities.UserMangaEntity;
import com.example.chibiotaku.domain.enums.StatusReviewEnum;
import com.example.chibiotaku.domain.views.ReviewVM;
import com.example.chibiotaku.repo.MangaRepository;
import com.example.chibiotaku.repo.ReviewMangaRepository;
import com.example.chibiotaku.repo.UserRepository;
import com.example.chibiotaku.services.MangaService;
import com.example.chibiotaku.services.ReviewMangaService;
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

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ReviewMangaServiceImplTests {

    @Mock
    private ReviewMangaRepository mockReviewMangaRepository;
    @Mock
    private MangaRepository mockMangaRepository;
    @Mock
    private UserRepository mockUserRepository;
    private ReviewMangaService underTest;

    @BeforeEach
    void setUp() {
        this.underTest = new ReviewMangaServiceImpl(
                mockReviewMangaRepository,
                mockMangaRepository,
                mockUserRepository,
                new ModelMapper()
        );
    }

    @Test
    void addReview_ObjectNotFound(){
        String username = "some username";
        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> this.underTest.addReview(UUID.randomUUID(),username,new ReviewDto())
        );
    }

    @Test
    void addReview_UserNotFound(){

        MangaEntity manga = new MangaEntity();
        UUID randomMangaId = UUID.randomUUID();

        Mockito.when(this.mockMangaRepository.findMangaEntityById(randomMangaId))
                .thenReturn(Optional.of(manga));

        String username = "some username";
        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> this.underTest.addReview(randomMangaId,username,new ReviewDto())
        );
    }

    @Test
    void addReview_ShouldAddCorrectly(){
        MangaEntity testManga = new MangaEntity();
        testManga.setId(UUID.randomUUID());

        UserEntity testUser = new UserEntity()
                .setEmail("albert@example.com")
                .setUsername("albert");

        ReviewDto expected = new ReviewDto()
                .setStatusReview(StatusReviewEnum.RECOMMENDED)
                .setContent("some content");
        Mockito.when(this.mockMangaRepository.findMangaEntityById(testManga.getId()))
                .thenReturn(Optional.of(testManga));

        Mockito.when(this.mockUserRepository.findUserEntityByUsername(testUser.getUsername()))
                .thenReturn(Optional.of(testUser));

        ArgumentCaptor<ReviewMangaEntity> argumentCaptor = ArgumentCaptor.forClass(ReviewMangaEntity.class);
        this.underTest.addReview(testManga.getId(),testUser.getUsername(),expected);


        verify(this.mockReviewMangaRepository).save(argumentCaptor.capture());

        Assertions.assertEquals(expected.getContent(),argumentCaptor.getValue().getContent());
        Assertions.assertEquals(expected.getStatusReview(),argumentCaptor.getValue().getStatusReview());
        Assertions.assertNotNull(argumentCaptor.getValue().getManga());
        Assertions.assertNotNull(argumentCaptor.getValue().getUser());
    }

    @Test
    void exportReviewsByMangaId_ShouldReviewVMList(){
        UUID randomId = UUID.randomUUID();

        Object[] reviewTestObj1 = new Object[4];
                 reviewTestObj1[0] = "username1";
                 reviewTestObj1[1] = LocalDate.of(1999,12,3);
                 reviewTestObj1[2] = StatusReviewEnum.NOT_RECOMMENDED;
                 reviewTestObj1[3] = "some content1";

        Object[] reviewTestObj2 = new Object[4];
                 reviewTestObj2[0] = "username2";
                 reviewTestObj2[1] = LocalDate.of(1999,12,3);
                 reviewTestObj2[2] = StatusReviewEnum.NOT_RECOMMENDED;
                 reviewTestObj2[3] = "some content2";

        List<Object[]> expected = new ArrayList<>(Arrays.asList(reviewTestObj1,reviewTestObj2));

        Mockito.when(this.mockReviewMangaRepository.findReviewMangaEntitiesByMangaId(randomId))
                .thenReturn(expected);

        List<ReviewVM> actual = this.underTest.exportReviewsByMangaId(randomId);

        Assertions.assertEquals(2,actual.size());
        Assertions.assertEquals(reviewTestObj1[0],actual.get(0).getUsername());
        Assertions.assertEquals(reviewTestObj1[1],actual.get(0).getCreated());
        Assertions.assertEquals(reviewTestObj1[3],actual.get(0).getContent());

        Assertions.assertEquals(reviewTestObj2[0],actual.get(1).getUsername());
        Assertions.assertEquals(reviewTestObj2[1],actual.get(1).getCreated());
        Assertions.assertEquals(reviewTestObj2[3],actual.get(1).getContent());
    }
}