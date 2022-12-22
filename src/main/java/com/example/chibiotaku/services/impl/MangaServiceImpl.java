package com.example.chibiotaku.services.impl;

import com.example.chibiotaku.constraints.ErrorMessage;
import com.example.chibiotaku.domain.dtos.MangaDto;
import com.example.chibiotaku.domain.entities.MangaEntity;
import com.example.chibiotaku.domain.entities.UserEntity;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import com.example.chibiotaku.domain.views.MangaSearchVM;
import com.example.chibiotaku.repo.MangaRepository;
import com.example.chibiotaku.repo.UserMangaRepository;
import com.example.chibiotaku.repo.UserRepository;
import com.example.chibiotaku.services.MangaService;
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
public class MangaServiceImpl implements MangaService {

    private MangaRepository mangaRepository;
    private UserMangaRepository userMangaRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    public MangaServiceImpl(MangaRepository mangaRepository,
                            UserMangaRepository userMangaRepository,
                            UserRepository userRepository,
                            ModelMapper modelMapper) {
        this.mangaRepository = mangaRepository;
        this.userMangaRepository = userMangaRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addManga(MangaDto mangaDto, String username) {
        MangaEntity manga = this.modelMapper.map(mangaDto,MangaEntity.class);
        UserEntity user = this.userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(ErrorMessage.USERNAME_NOT_FOUND,username)));
        manga.setStatusObject(StatusObjectEnum.PENDING);
        manga.setCreated(LocalDate.now());
        manga.setUser(user);
        this.mangaRepository.save(manga);
    }

    @Override
    public List<MangaSearchVM> getMangasByTitle(String title) {

        List<Object[]> mangaEntities = this.mangaRepository.findMangaEntityContainsTitle(title);
        List< MangaSearchVM> resultSearch = new ArrayList<>();

        for (Object[] m : mangaEntities) {
            MangaSearchVM mangaSearchVM =  new MangaSearchVM()
                    .setId(UUID.fromString(m[0].toString()))
                    .setTitle(m[1].toString())
                    .setChapters(Integer.parseInt(m[2].toString()))
                    .setVolumes(Integer.parseInt(m[3].toString()))
                    .setPopularity(Integer.parseInt(m[4].toString()))
                    .setImageUrl(m[5].toString())
                    .setContent(m[6].toString())
                    .setStatusObject((StatusObjectEnum) m[7]);
            resultSearch.add(mangaSearchVM);
        }
        return resultSearch;
    }

    @Override
    public MangaDto getMangaById(UUID id) {
        MangaEntity manga = this.mangaRepository.findMangaEntityById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format(ErrorMessage.MANGA_NOT_FOUND, id.toString())));
        return this.modelMapper.map(manga, MangaDto.class);
    }

    @Override
    public List<MangaDto> extractMangaByUsername(String username) {
        List<MangaEntity> mangas = this.mangaRepository.findMangaEntitiesByUsername(username);
        return mangas
                        .stream()
                        .map(m -> this.modelMapper.map(m,MangaDto.class))
                        .collect(Collectors.toList());
    }

    @Override
    public List<MangaDto> getAllManga() {

        List<MangaEntity> mangaEntities = this.mangaRepository.findAll();


        List<MangaDto> mangaDtos = mangaEntities
                .stream()
                .map(m -> this.modelMapper
                        .map(m, MangaDto.class)

                )
                .collect(Collectors.toList());

        this.addAverageScoreFromDB(mangaDtos);
        this.orderMangaDtoByScore(mangaDtos);
        return mangaDtos;
    }

    @Override
    public List<MangaDto> extractMangaByStatusObject(StatusObjectEnum status) {
       return this.mangaRepository.findMangaEntitiesByStatusObject(status)
                .stream()
                .map(m -> this.modelMapper.map(m,MangaDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void editMangaStatusByMangaId(StatusObjectEnum statusObject, UUID mangaId) {
        MangaEntity manga = this.mangaRepository.findMangaEntityById(mangaId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format(ErrorMessage.MANGA_NOT_FOUND,mangaId.toString())));
        manga.setStatusObject(statusObject);
        this.mangaRepository.save(manga);
    }

    @Override
    public List<MangaDto> findMostPopularManga() {
     return this.mangaRepository.findMangaEntitiesOrderByPopularityASC()
                .stream()
                .map(m ->  this.modelMapper.map(m,MangaDto.class))
                .limit(4)
                .collect(Collectors.toList());
    }

    private void addAverageScoreFromDB(List<MangaDto> mangaDtoList){

        mangaDtoList.forEach(m ->{

            Double currentScoreDB = this.userMangaRepository.getAverageScoreByMangaId(m.getId());
            if(currentScoreDB == null){
                m.setScore(0);
            }else {
                m.setScore(currentScoreDB.doubleValue());
            }
        });

    }
    private void orderMangaDtoByScore(List<MangaDto> manga){

        Comparator<MangaDto> orderByScoreComparator = (m1,m2) ->{

            int scoreOrderValue =  Double.compare(m2.getScore(),m1.getScore());
            if( scoreOrderValue == 0){
                return Integer.compare(m1.getPopularity(),m2.getPopularity());
            }
            return scoreOrderValue;
        };
        manga.sort(orderByScoreComparator);
    }
}
