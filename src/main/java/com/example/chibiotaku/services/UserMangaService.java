package com.example.chibiotaku.services;

import com.example.chibiotaku.domain.enums.WatchStatusEnum;
import com.example.chibiotaku.domain.views.UserItemUpdateVM;
import com.example.chibiotaku.domain.dtos.UserMangaDto;

import java.util.List;
import java.util.UUID;

public interface UserMangaService {
    UserMangaDto findMyMangaByMangaIdAndUsername(UUID animeId, String username);
    void addMangaToReadList(UserMangaDto userMangaDto, UUID id, String username);
    void deleteUserMangaByMangaIdAndUsermane(UUID id, String username);
    List<UserMangaDto> extractUserMangaByWatchStatusAndUsername(WatchStatusEnum watchStatusEnum, String username);
    double extractAverageScoreByMangaId(UUID id);
    UserMangaDto findUserMangaDtoByUsernameAndMangaId(String username,UUID mangaId);
    int getCounterUsersByMangaId(UUID mangaId) ;
    List<UserItemUpdateVM> extractMangaVMyUsername(String username);

    int countUserMangaByWatchStatusAndUsername(WatchStatusEnum watchStatus, String username);
}

