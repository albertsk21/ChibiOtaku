package com.example.chibiotaku.services;

import com.example.chibiotaku.domain.dtos.AnimeDto;
import com.example.chibiotaku.domain.dtos.UserAnimeDto;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import com.example.chibiotaku.domain.views.AnimeSearchVM;

import java.util.List;
import java.util.UUID;

public interface AnimeService {

    void saveAnime(AnimeDto animeDto, String username);
    List<AnimeSearchVM> getAnimesByTitle(String title);

    AnimeDto getAnimeById(UUID id);
    List<AnimeDto> extractAnimesByUsername(String username);
    List<UserAnimeDto> getAllAnimes();
    List<AnimeDto> extractAllAnimeDtoByStatusObject(StatusObjectEnum statusObject);
    void editAnimeStatusByAnimeId(StatusObjectEnum statusObject, UUID animeId);
    List<AnimeDto> findMostPopularAnime();
}
