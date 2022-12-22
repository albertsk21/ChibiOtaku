package com.example.chibiotaku.services.impl;


import com.example.chibiotaku.constraints.ErrorMessage;
import com.example.chibiotaku.domain.dtos.UserAnimeDto;
import com.example.chibiotaku.domain.entities.AnimeEntity;
import com.example.chibiotaku.domain.entities.UserAnimeEntity;
import com.example.chibiotaku.domain.entities.UserEntity;
import com.example.chibiotaku.domain.enums.WatchStatusEnum;
import com.example.chibiotaku.domain.views.ReviewVM;
import com.example.chibiotaku.domain.views.UserItemUpdateVM;
import com.example.chibiotaku.repo.AnimeRepository;
import com.example.chibiotaku.repo.UserAnimeRepository;
import com.example.chibiotaku.repo.UserRepository;
import com.example.chibiotaku.services.UserAnimeService;
import com.example.chibiotaku.util.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserAnimeServiceImpl implements UserAnimeService {


    private UserAnimeRepository userAnimeRepository;
    private ModelMapper modelMapper;
    private AnimeRepository animeRepository;
    private UserRepository userRepository;

    public UserAnimeServiceImpl(UserAnimeRepository userAnimeRepository, ModelMapper modelMapper, AnimeRepository animeRepository, UserRepository userRepository) {
        this.userAnimeRepository = userAnimeRepository;
        this.modelMapper = modelMapper;
        this.animeRepository = animeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserAnimeDto findMyAnimeByAnimeIdAndUsername(String animeId, String username) {
        Optional<UserAnimeEntity> currentAnimeOpt = this.userAnimeRepository.findAnimeByAnimeIdAndUserId(
                UUID.fromString(animeId),
                username);


      UserAnimeDto userAnimeDto =  currentAnimeOpt.isEmpty() ? null : this.modelMapper.map(currentAnimeOpt.get(),UserAnimeDto.class);


      if(userAnimeDto != null){
          userAnimeDto.setType(currentAnimeOpt.get().getAnime().getType());
          userAnimeDto.setImageUrl(currentAnimeOpt.get().getAnime().getImageUrl());
          userAnimeDto.setTitle(currentAnimeOpt.get().getAnime().getTitle());
          userAnimeDto.setAired(currentAnimeOpt.get().getAnime().getAired());
          userAnimeDto.setPopularity(currentAnimeOpt.get().getAnime().getPopularity());
          userAnimeDto.setStatusObject(currentAnimeOpt.get().getAnime().getStatusObject());

      }
        return userAnimeDto;
    }

    @Override
    public void addAnimeToWatchList(UserAnimeDto userAnimeDto, UUID animeId, String username) {

        var userAnimeOpt = this.userAnimeRepository.findUserAnimeEntityByUserUsernameAndAndAnimeId(username
                ,animeId);

        if (userAnimeOpt.isPresent()){
            UserAnimeEntity currentAnime = userAnimeOpt.get();
            currentAnime.setWatchStatus(userAnimeDto.getWatchStatus());
            currentAnime.setModified(LocalDate.now());
            currentAnime.setScore(Integer.parseInt(String.valueOf(String.format("%.0f",userAnimeDto.getScore()))));
            currentAnime =  this.userAnimeRepository.save(currentAnime);
            return;
        }

        UserAnimeEntity userAnimeEntity = this.map(userAnimeDto,animeId,username);
        this.userAnimeRepository.save(userAnimeEntity);
    }

    @Override
    public void deleteMyAnimeByIdAndUsername(UUID animeId,String username) {

        UserAnimeEntity userAnimeEntity = this.userAnimeRepository.findUserAnimeEntityByUserUsernameAndAndAnimeId(username,animeId)
                .orElseThrow(() -> new ObjectNotFoundException("Anime form watch list with anime id " +
                        animeId.toString() + " and username " + username + " uas not found"));
        this.userAnimeRepository.delete(userAnimeEntity);
    }

    @Override
    public int countUserAnimeByWatchStatusAndUsername(String username, WatchStatusEnum watchStatusEnum) {
        return this.userAnimeRepository.countByWatchStatusAndUsername(username,watchStatusEnum);
    }

    @Override
    public List<UserItemUpdateVM> extractAnimesVMByUsername(String username) {
            List<Object[]> animes = this.userAnimeRepository.findUserAnimeFormatByUsername(username);

            List<UserItemUpdateVM> animesVM = new ArrayList<>();


        for (Object[] a : animes ) {
            animesVM.add(new UserItemUpdateVM()
                    .setId(UUID.fromString(a[1].toString()))
                    .setImageUrl(a[0].toString()));
        }

        return animesVM;
    }

    @Override
    public UserAnimeDto findUserAnimeDtoByUsernameAndAnimeId(String username, UUID animeId) {
       Optional<UserAnimeEntity> userAnimeOpt = this.userAnimeRepository.findUserAnimeEntityByUserUsernameAndAndAnimeId(
                username,
                animeId
        );
       if (userAnimeOpt.isEmpty()){
           return null;
       }

        return this.modelMapper.map(userAnimeOpt.get(),UserAnimeDto.class);
    }

    @Override
    public List<UserAnimeDto> extractAnimesByUsernameAndWatchEnum(String username, WatchStatusEnum watchStatusEnum) {

        List<UserAnimeEntity> animeEntities = this.userAnimeRepository.findAnimeEntityByUsernameAndWatchStatus(
                username,
                watchStatusEnum);


        List<UserAnimeDto> output = new ArrayList<>();
        for (UserAnimeEntity currentAnime : animeEntities) {


            UserAnimeDto userAnimeDto = this.modelMapper.map(currentAnime,UserAnimeDto.class);
            userAnimeDto.setTitle(currentAnime.getAnime().getTitle());
            userAnimeDto.setEps(currentAnime.getAnime().getEpisodes());
            userAnimeDto.setImageUrl(currentAnime.getAnime().getImageUrl());
            userAnimeDto.setContent(currentAnime.getAnime().getContent());
            userAnimeDto.setType(currentAnime.getAnime().getType());
            userAnimeDto.setId(currentAnime.getAnime().getId());
            userAnimeDto.setWatchStatus(userAnimeDto.getWatchStatus());
            userAnimeDto.setAired(currentAnime.getAnime().getAired());
            userAnimeDto.setAired(currentAnime.getAnime().getAired());
            userAnimeDto.setPopularity(currentAnime.getAnime().getPopularity());
            userAnimeDto.setStatusObject(currentAnime.getAnime().getStatusObject());

            output.add(userAnimeDto);
        }
        return output;
    }

    @Override
    public int getCounterByAnimeTitle(String animeTitle) {
        return this.userAnimeRepository.countUserAnimeEntityByAnimeTitle(animeTitle);
    }

    @Override
    public double getAverageScoreByAnimeTitle(String animeTitle) {


        Double scoreObj =  this.userAnimeRepository.getAverageScoreByAnimeTitle(animeTitle);;

        return scoreObj == null ? 0 : scoreObj ;
    }


    private UserAnimeEntity map(UserAnimeDto userAnimeDto, UUID animeId, String username){
        AnimeEntity anime = this.animeRepository.findAnimeEntityById(animeId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format(ErrorMessage.ANIME_NOT_FOUND,animeId)));

        UserEntity user = this.userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(ErrorMessage.USERNAME_NOT_FOUND,username)));

        UserAnimeEntity userAnimeEntity = this.modelMapper.map(userAnimeDto,UserAnimeEntity.class);
        userAnimeEntity.setCreated(LocalDate.now());
        userAnimeEntity.setUser(user);
        userAnimeEntity.setAnime(anime);
        return userAnimeEntity;
    }


}
