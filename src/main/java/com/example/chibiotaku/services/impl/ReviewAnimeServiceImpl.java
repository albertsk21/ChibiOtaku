package com.example.chibiotaku.services.impl;

import com.example.chibiotaku.constraints.ErrorMessage;
import com.example.chibiotaku.domain.api.ReviewApiDto;
import com.example.chibiotaku.domain.dtos.ReviewDto;
import com.example.chibiotaku.domain.entities.AnimeEntity;
import com.example.chibiotaku.domain.entities.ReviewAnimeEntity;
import com.example.chibiotaku.domain.entities.UserEntity;
import com.example.chibiotaku.domain.views.ReviewVM;
import com.example.chibiotaku.repo.AnimeRepository;
import com.example.chibiotaku.repo.ReviewAnimeRepository;
import com.example.chibiotaku.repo.UserRepository;
import com.example.chibiotaku.services.ReviewAnimeService;
import com.example.chibiotaku.util.Convert;
import com.example.chibiotaku.util.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class ReviewAnimeServiceImpl implements ReviewAnimeService {

    private ReviewAnimeRepository reviewAnimeRepository;
    private UserRepository userRepository;
    private AnimeRepository animeRepository;
    private ModelMapper modelMapper;

    public ReviewAnimeServiceImpl(ReviewAnimeRepository reviewAnimeRepository,
                                  UserRepository userRepository,
                                  AnimeRepository animeRepository,
                                  ModelMapper modelMapper) {
        this.reviewAnimeRepository = reviewAnimeRepository;
        this.userRepository = userRepository;
        this.animeRepository = animeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addReviewAnime(UUID animeId, String username, ReviewDto reviewAnimeDto) {

        AnimeEntity anime = this.animeRepository.findAnimeEntityById(animeId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format(ErrorMessage.ANIME_NOT_FOUND,animeId.toString())));


        UserEntity user = this.userRepository
                .findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(ErrorMessage.USERNAME_NOT_FOUND,username)));

        ReviewAnimeEntity reviewAnimeEntity = this.modelMapper.map(reviewAnimeDto,ReviewAnimeEntity.class);
        reviewAnimeEntity.setUser(user);
        reviewAnimeEntity.setAnime(anime);
        reviewAnimeEntity.setCreated(LocalDate.now());
        reviewAnimeEntity.setVerified(false);

        this.reviewAnimeRepository.save(reviewAnimeEntity);
    }


    @Override
    public List<ReviewVM> exportReviewsByAnimeId(UUID id) {

        List<Object[]> reviews = this.reviewAnimeRepository.findReviewAnimeEntitiesByAnimeId(id);
        return Convert.convertReviewsEntityToVM(reviews);
    }

    @Override
    public List<ReviewApiDto> exportReviewsApiDtoLatestCreated() {
        return this.reviewAnimeRepository.findReviewAnimeEntitiesOrderByCreated()
                .stream()
                .map(objects -> new ReviewApiDto()
                        .setId((UUID) objects[0])
                        .setContent(objects[1].toString())
                        .setAnimeTitle(objects[2].toString())
                        .setPublished((LocalDate) objects[3])
                        .setImageUrl(objects[4].toString())
                        .setUsername(objects[5].toString()))
                .limit(10)
                .collect(Collectors.toList());
    }
}
