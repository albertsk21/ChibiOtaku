package com.example.chibiotaku.services.impl;

import com.example.chibiotaku.constraints.ErrorMessage;
import com.example.chibiotaku.domain.dtos.ReviewDto;
import com.example.chibiotaku.domain.entities.MangaEntity;
import com.example.chibiotaku.domain.entities.ReviewMangaEntity;
import com.example.chibiotaku.domain.entities.UserEntity;
import com.example.chibiotaku.domain.views.ReviewVM;
import com.example.chibiotaku.repo.MangaRepository;
import com.example.chibiotaku.repo.ReviewMangaRepository;
import com.example.chibiotaku.repo.UserRepository;
import com.example.chibiotaku.services.ReviewMangaService;
import com.example.chibiotaku.util.Convert;
import com.example.chibiotaku.util.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewMangaServiceImpl implements ReviewMangaService {

    private ReviewMangaRepository reviewMangaRepository;
    private MangaRepository mangaRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    public ReviewMangaServiceImpl(ReviewMangaRepository reviewMangaRepository, MangaRepository mangaRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.reviewMangaRepository = reviewMangaRepository;
        this.mangaRepository = mangaRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addReview(UUID mangaId, String username, ReviewDto review) {

        MangaEntity manga = this.mangaRepository.findMangaEntityById(mangaId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format(ErrorMessage.MANGA_NOT_FOUND,mangaId.toString())));

        UserEntity user = this.userRepository
                .findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(ErrorMessage.USERNAME_NOT_FOUND,username)));

        ReviewMangaEntity reviewAnimeEntity = this.modelMapper.map(review,ReviewMangaEntity.class);
        reviewAnimeEntity.setUser(user);
        reviewAnimeEntity.setManga(manga);
        reviewAnimeEntity.setCreated(LocalDate.now());

        this.reviewMangaRepository.save(reviewAnimeEntity);

    }

    @Override
    public List<ReviewVM> exportReviewsByMangaId(UUID id) {
        List<Object[]> reviews = this.reviewMangaRepository.findReviewMangaEntitiesByMangaId(id);
        return Convert.convertReviewsEntityToVM(reviews);
    }

}
