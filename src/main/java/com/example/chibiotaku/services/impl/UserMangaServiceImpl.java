package com.example.chibiotaku.services.impl;

import com.example.chibiotaku.constraints.ErrorMessage;
import com.example.chibiotaku.domain.enums.WatchStatusEnum;
import com.example.chibiotaku.domain.views.UserItemUpdateVM;
import com.example.chibiotaku.domain.dtos.UserMangaDto;
import com.example.chibiotaku.domain.entities.*;
import com.example.chibiotaku.repo.MangaRepository;
import com.example.chibiotaku.repo.UserMangaRepository;
import com.example.chibiotaku.repo.UserRepository;
import com.example.chibiotaku.services.UserMangaService;
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
public class UserMangaServiceImpl implements UserMangaService {
    private UserMangaRepository userMangaRepository;
    private MangaRepository mangaRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    public UserMangaServiceImpl(UserMangaRepository userMangaRepository,
                                MangaRepository mangaRepository,
                                UserRepository userRepository,
                                ModelMapper modelMapper) {
        this.userMangaRepository = userMangaRepository;
        this.mangaRepository = mangaRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserMangaDto findMyMangaByMangaIdAndUsername(UUID mangaId, String username) {
        var userMangaOpt = this.userMangaRepository.findUserMangaEntityByUserUsernameAndAndMangaId(
            username,mangaId
        );
        if (userMangaOpt.isEmpty()){
            return null;
        }
        return this.modelMapper.map(userMangaOpt.get(),UserMangaDto.class);
    }

    @Override
    public void addMangaToReadList(UserMangaDto userMangaDto, UUID id, String username) {

        var userMangaOpt = this.userMangaRepository
                .findUserMangaEntityByUserUsernameAndAndMangaId(username,id);
        if (userMangaOpt.isPresent()){
            UserMangaEntity currentManga = userMangaOpt.get();
            currentManga.setWatchStatus(userMangaDto.getWatchStatus());
            currentManga.setModified(LocalDate.now());
            currentManga.setScore(Integer.parseInt(String.valueOf(String.format("%.0f",userMangaDto.getScore()))));
            this.userMangaRepository.save(currentManga);
            return;
        }
        UserMangaEntity userMangaEntity = this.map(userMangaDto,id,username);
        this.userMangaRepository.save(userMangaEntity);
    }

    @Override
    public void deleteUserMangaByMangaIdAndUsermane(UUID id, String username) {
        var userMangaOpt = this.userMangaRepository.findUserMangaEntityByUserUsernameAndAndMangaId(username,id);
        if(!userMangaOpt.isEmpty()){
            this.userMangaRepository.delete(userMangaOpt.get());
        }
    }

    @Override
    public List<UserMangaDto> extractUserMangaByWatchStatusAndUsername(WatchStatusEnum watchStatusEnum, String username) {
        List<UserMangaEntity> mangaList = this.userMangaRepository.findMangaEntityByUsernameAndWatchStatus(username,watchStatusEnum);
        List<UserMangaDto> output = new ArrayList<>();
        for (UserMangaEntity manga : mangaList) {

            UserMangaDto userMangaDto = this.modelMapper.map(manga,UserMangaDto.class);

            userMangaDto.setContent(manga.getManga().getContent());
            userMangaDto.setImageUrl(manga.getManga().getImageUrl());
            userMangaDto.setTitle(manga.getManga().getTitle());
            userMangaDto.setVolumes(Integer.parseInt(manga.getManga().getVolumes()));
            userMangaDto.setId(manga.getManga().getId());
            output.add(userMangaDto);
        }
        return output;
    }

    @Override
    public double extractAverageScoreByMangaId(UUID id) {
        Double averageScore = this.userMangaRepository.getAverageScoreByMangaId(id);
        return averageScore == null ? 0 : averageScore;
    }

    @Override
    public UserMangaDto findUserMangaDtoByUsernameAndMangaId(String username, UUID mangaId) {
        Optional<UserMangaEntity> userMangaEntityOpt = this.userMangaRepository.findUserMangaEntityByUserUsernameAndAndMangaId(username, mangaId);
//                .orElseThrow(() -> new ObjectNotFoundException(String.format(ErrorMessage.MANGA_NOT_FOUND,mangaId.toString())));
        if (userMangaEntityOpt.isEmpty()){
            return null;
        }
        return this.modelMapper.map(userMangaEntityOpt.get(),UserMangaDto.class);
    }

    @Override
    public int getCounterUsersByMangaId(UUID mangaId) {
        return this.userMangaRepository.countUserMangaEntityByMangaId(mangaId);
    }

    @Override
    public List<UserItemUpdateVM> extractMangaVMyUsername(String username) {
        List<Object[]> mangaObjs = this.userMangaRepository.findUserMangaFormatByUsername(username);
        List<UserItemUpdateVM> items = new ArrayList<>();
        for (Object[] ob : mangaObjs){
            items.add(new UserItemUpdateVM()
                    .setId(UUID.fromString(ob[1].toString()))
                    .setImageUrl(ob[0].toString()));
        }
        return items;
    }

    @Override
    public int countUserMangaByWatchStatusAndUsername(WatchStatusEnum watchStatus, String username) {
        return this.userMangaRepository.countByWatchStatusAndUsername(username,watchStatus);
    }


    private UserMangaEntity map(UserMangaDto userMangaDto, UUID mangaId, String username){
        MangaEntity manga = this.mangaRepository.findMangaEntityById(mangaId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format(ErrorMessage.MANGA_NOT_FOUND,mangaId)));

        UserEntity user = this.userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(ErrorMessage.USERNAME_NOT_FOUND,username)));

        UserMangaEntity userMangaEntity = this.modelMapper.map(userMangaDto,UserMangaEntity.class);
        userMangaEntity.setCreated(LocalDate.now());
        userMangaEntity.setUser(user);
        userMangaEntity.setManga(manga);
        return userMangaEntity;
    }

}
