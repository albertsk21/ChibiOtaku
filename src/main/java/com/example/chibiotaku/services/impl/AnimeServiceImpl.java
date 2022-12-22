package com.example.chibiotaku.services.impl;

import com.example.chibiotaku.domain.dtos.UserAnimeDto;
import com.example.chibiotaku.domain.enums.AnimeTypeEnum;
import com.example.chibiotaku.constraints.ErrorMessage;
import com.example.chibiotaku.domain.dtos.AnimeDto;
import com.example.chibiotaku.domain.entities.AnimeEntity;
import com.example.chibiotaku.domain.entities.UserEntity;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import com.example.chibiotaku.domain.views.AnimeSearchVM;
import com.example.chibiotaku.repo.AnimeRepository;
import com.example.chibiotaku.repo.UserAnimeRepository;
import com.example.chibiotaku.repo.UserRepository;
import com.example.chibiotaku.services.AnimeService;
import com.example.chibiotaku.util.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnimeServiceImpl implements AnimeService {

    private AnimeRepository animeRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private UserAnimeRepository userAnimeRepository;

    public AnimeServiceImpl(AnimeRepository animeRepository,
                            UserRepository userRepository,
                            ModelMapper modelMapper,
                            UserAnimeRepository userAnimeRepository) {
        this.animeRepository = animeRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.userAnimeRepository = userAnimeRepository;
    }

    @Override
    public void saveAnime(AnimeDto animeDto, String username) {

        UserEntity user = this.userRepository
                .findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(ErrorMessage.USERNAME_NOT_FOUND,username)));

        AnimeEntity anime = this.modelMapper.map(animeDto,AnimeEntity.class);
        anime.setStatusObject(StatusObjectEnum.PENDING);
        anime.setCreated(LocalDate.now());
        anime.setUser(user);

        this.animeRepository.save(anime);
    }

    @Override
    public List<AnimeSearchVM> getAnimesByTitle(String title) {
        List<Object[]> animes = this.animeRepository.findAnimeEntityContainsTitle(title);
        if (animes.size() == 0){
            return new ArrayList<>();
        }
        return this.map(animes);
    }

    @Override
    public AnimeDto getAnimeById(UUID id) {
        AnimeEntity anime = this.animeRepository.findAnimeEntityById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format(ErrorMessage.ANIME_NOT_FOUND,id.toString())));
        return this.modelMapper.map(anime,AnimeDto.class);
    }

    @Override
    public List<AnimeDto> extractAnimesByUsername(String username) {
        List<AnimeEntity> animeEntities  = this.animeRepository.findAnimeEntitiesByUserUsername(username);
        return animeEntities
                .stream()
                .map(a -> this.modelMapper.map(a,AnimeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserAnimeDto> getAllAnimes() {

        List<AnimeEntity> animes = this.animeRepository.findAllOrderByPopularityAndRanked();
       List<UserAnimeDto> exportAnimes =  animes
                .stream()
                .map(a -> this.modelMapper.map(a,UserAnimeDto.class)
                        .setScore(this.userAnimeRepository
                                .getAverageScoreByAnimeTitle(a.getTitle()) == null ? 0 : this.userAnimeRepository
                                .getAverageScoreByAnimeTitle(a.getTitle()) )
                        .setEps(a.getEpisodes()))
                .collect(Collectors.toList());
       this.orderAnimesByScore(exportAnimes);
       return exportAnimes;
    }

    @Override
    public List<AnimeDto> extractAllAnimeDtoByStatusObject(StatusObjectEnum statusObject) {
        return this.animeRepository.findAnimeEntitiesByStatusObject(statusObject)
                .stream()
                .map(m ->  this.modelMapper.map(m,AnimeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void editAnimeStatusByAnimeId(StatusObjectEnum statusObject, UUID animeId) {
        AnimeEntity anime = this.animeRepository.findAnimeEntityById(animeId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format(ErrorMessage.ANIME_NOT_FOUND,animeId.toString())));
        anime.setStatusObject(statusObject);
        this.animeRepository.save(anime);
    }

    @Override
    public List<AnimeDto> findMostPopularAnime() {
        return this.animeRepository.findAnimeEntitiesOrderByPopularityASC()
                .stream()
                .map(a -> this.modelMapper.map(a,AnimeDto.class))
                .limit(4)
                .collect(Collectors.toList());
    }


    private List<AnimeSearchVM> map(List<Object[]> animes){
        List<AnimeSearchVM> views = new ArrayList<>();
        animes.forEach(a ->{
            views.add(new AnimeSearchVM()
                    .setId( (UUID) a[0])
                    .setTitle(a[1].toString())
                    .setEpisodes((int) a[3])
                    .setPopularity((int) a[4])
                    .setImageUrl(a[5].toString())
                    .setContent(a[6].toString())
                    .setType((AnimeTypeEnum) a[2])
                    .setStatusObject((StatusObjectEnum) a[7])
            );
        });

        return views;
    }
    private void orderAnimesByScore(List<UserAnimeDto> animeDtos){
        Comparator<UserAnimeDto> sortByScore = (a,b) ->{
            if (a.getPopularity() == b.getPopularity()){
                return 1;
            }

            return Double.compare(b.getScore(),a.getScore());
        };
        animeDtos.sort(sortByScore);
    }


}
