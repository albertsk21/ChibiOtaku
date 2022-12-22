package com.example.chibiotaku.services;

import com.example.chibiotaku.domain.dtos.MangaDto;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import com.example.chibiotaku.domain.views.MangaSearchVM;

import java.util.List;
import java.util.UUID;

public interface MangaService {

    void addManga(MangaDto mangaDto, String username);

    List<MangaSearchVM> getMangasByTitle(String title);
    MangaDto getMangaById(UUID id);
    List<MangaDto> extractMangaByUsername(String username);
    List<MangaDto> getAllManga();
    List<MangaDto> extractMangaByStatusObject(StatusObjectEnum status);
    void editMangaStatusByMangaId(StatusObjectEnum statusObject, UUID animeId);

    List<MangaDto> findMostPopularManga();
}
