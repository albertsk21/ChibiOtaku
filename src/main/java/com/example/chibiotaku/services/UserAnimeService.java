package com.example.chibiotaku.services;

import com.example.chibiotaku.domain.dtos.UserAnimeDto;
import com.example.chibiotaku.domain.enums.WatchStatusEnum;
import com.example.chibiotaku.domain.views.UserItemUpdateVM;

import java.util.List;
import java.util.UUID;

public interface UserAnimeService {

    UserAnimeDto findMyAnimeByAnimeIdAndUsername(String animeId, String username);
    void addAnimeToWatchList(UserAnimeDto userAnimeDto, UUID animeId, String username);
    void deleteMyAnimeByIdAndUsername(UUID animeId,String username);
    int countUserAnimeByWatchStatusAndUsername(String username,WatchStatusEnum watchStatusEnum);
    List<UserItemUpdateVM> extractAnimesVMByUsername(String username);
    UserAnimeDto findUserAnimeDtoByUsernameAndAnimeId(String username, UUID animeId);
    List<UserAnimeDto> extractAnimesByUsernameAndWatchEnum(String username, WatchStatusEnum watchStatusEnum );
    int getCounterByAnimeTitle(String animeTitle);
    double getAverageScoreByAnimeTitle(String animeTitle);

}
