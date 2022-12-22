package com.example.chibiotaku.schedule;

import com.example.chibiotaku.domain.entities.AnimeEntity;
import com.example.chibiotaku.domain.entities.MangaEntity;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import com.example.chibiotaku.repo.AnimeRepository;
import com.example.chibiotaku.repo.MangaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeleteDeniedObjectsTask {

    private MangaRepository mangaRepository;
    private AnimeRepository animeRepository;

    public DeleteDeniedObjectsTask(MangaRepository mangaRepository, AnimeRepository animeRepository) {
        this.mangaRepository = mangaRepository;
        this.animeRepository = animeRepository;
    }
    @Scheduled(cron = "0 */48 * * *")
    public void deleteAnimesDenied(){
        List<AnimeEntity> animeEntities = this.animeRepository.findAnimeEntitiesByStatusObject(StatusObjectEnum.DECLINED);
        this.animeRepository.deleteAll(animeEntities);
    }

    @Scheduled(cron = "0 */48 * * *")
    public void deleteMangaDenied(){
        List<MangaEntity> mangaEntities =this.mangaRepository.findMangaEntitiesByStatusObject(StatusObjectEnum.DECLINED);
        this.mangaRepository.deleteAll(mangaEntities);
    }

}
