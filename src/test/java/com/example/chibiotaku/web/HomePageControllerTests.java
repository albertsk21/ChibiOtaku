package com.example.chibiotaku.web;

import com.example.chibiotaku.domain.dtos.AnimeDto;
import com.example.chibiotaku.domain.dtos.MangaDto;
import com.example.chibiotaku.services.AnimeService;
import com.example.chibiotaku.services.MangaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class HomePageControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AnimeService animeService;
    @MockBean
    private MangaService mangaService;

    private final AnimeDto animeTest1 = new AnimeDto()
            .setImageUrl("some url")
            .setId(UUID.randomUUID());

    private final AnimeDto animeTest2 = new AnimeDto()
            .setImageUrl("some url1")
            .setId(UUID.randomUUID());

    private final AnimeDto animeTest3 = new AnimeDto()
            .setImageUrl("some url3")
            .setId(UUID.randomUUID());

    private final MangaDto mangaDto1 = new MangaDto()
            .setImageUrl("some url1")
            .setId(UUID.randomUUID());

    private final MangaDto mangaDto2 = new MangaDto()
            .setImageUrl("some url2")
            .setId(UUID.randomUUID());

    private final MangaDto mangaDto3 = new MangaDto()
            .setImageUrl("some url3")
            .setId(UUID.randomUUID());

    @BeforeEach
    void setUp() {
        Mockito.when(this.animeService.findMostPopularAnime())
                .thenReturn(List.of(animeTest1,animeTest2,animeTest3));
        Mockito.when(this.mangaService.findMostPopularManga())
                .thenReturn(List.of(mangaDto1,mangaDto2,mangaDto3));
    }

    @Test
    void dashboard_PrincipalIsNull() throws Exception {
        mockMvc.perform(get("/")
                        .with(csrf()))
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("animes"))
                .andExpect(model().attributeExists("mangas"))
                .andExpect(model().attributeDoesNotExist("username"));
    }
    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void dashboard_PrincipalIsNotNull() throws Exception {
        String expectedUsername = "user";
        mockMvc.perform(get("/")
                        .with(csrf()))
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("animes"))
                .andExpect(model().attributeExists("mangas"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attribute("username",expectedUsername));
    }
}