package com.example.chibiotaku.web;

import com.example.chibiotaku.domain.biding.AnimeBM;
import com.example.chibiotaku.domain.dtos.AnimeDto;
import com.example.chibiotaku.domain.dtos.UserAnimeDto;
import com.example.chibiotaku.domain.enums.AnimeTypeEnum;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import com.example.chibiotaku.domain.enums.WatchStatusEnum;
import com.example.chibiotaku.domain.util.SearchForm;
import com.example.chibiotaku.domain.views.AnimeSearchVM;
import com.example.chibiotaku.services.AnimeService;
import com.example.chibiotaku.services.UserAnimeService;
import com.example.chibiotaku.util.exceptions.ObjectNotFoundException;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AnimeControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AnimeService animeService;
    @MockBean
    private UserAnimeService userAnimeService;

    AnimeBM animeBM = new AnimeBM()
            .setImageUrl("some image")
            .setAired(LocalDate.now())
            .setDuration(24)
            .setEpisodes(3)
            .setGenres("test2")
            .setImageUrl("testIamgeUrl2")
            .setProducers("Test producers2")
            .setRanked(1)
            .setRating("test2")
            .setTitle("test2")
            .setContent("Lorem Ipsum is simply dummy text of the printing and " +
                    "typesetting industry. Lorem Ipsum has been the industry's " +
                    "standard dummy text ever since the 1500s, when an unknown" +
                    " printer took a galley of type and scrambled it to make a" +
                    " type specimen book. It has survived not only five centuries," +
                    " but also the leap into electronic typesetting," +
                    " remaining essentially unchanged. It was popularised in" +
                    " the 1960s with the release of Letraset sheets containing" +
                    " Lorem Ipsum passages, and more recently with desktop" +
                    " publishing software like Aldus PageMaker including " +
                    "versions of Lorem Ipsum.\n")
            .setPopularity(2)
            .setType(AnimeTypeEnum.MOVIE.name())
            .setMembers(24)
            .setStudios("studio test")
            .setRanked(34)
            .setSource("Manga");
   private AnimeDto animeTest = new AnimeDto()
            .setAired(LocalDate.now())
            .setDuration(24)
            .setEpisodes(0)
            .setGenres("test1")
            .setImageUrl("testIamgeUrl")
            .setProducers("Test")
            .setRanked(1)
            .setRating("test")
            .setStatusObject(StatusObjectEnum.ACCEPTED)
            .setTitle("test1")
            .setPopularity(1);

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void showAddAnimePage() throws Exception {
        mockMvc.perform(get("/animes/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-anime"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void showAddAnimePage_ShouldReturnForbbidenStatus() throws Exception {
        mockMvc.perform(get("/animes/add"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER", password = "123")
    void postAddAnimePage_ShouldReturnForbbidenStatus() throws Exception {

        mockMvc.perform(post("/animes/add")
                .param("title", animeBM.getTitle())
                .param("producers", animeBM.getProducers())
                .param("content", animeBM.getContent())
                .param("episodes", Integer.toString(animeBM.getEpisodes()))
                .param("aired", animeBM.getAired().toString())
                .param("duration", Integer.toString(animeBM.getDuration()))
                .param("imageUrl", animeBM.getImageUrl())
                .param("rating", animeBM.getRating())
                .param("genres", animeBM.getGenres())
                .param("popularity", Integer.toString(animeBM.getPopularity()))
                .param("type", animeBM.getType())
                .param("members", Integer.toString(animeBM.getMembers()))
                .param("ranked", Integer.toString(animeBM.getRanked()))
                .param("studios", animeBM.getStudios())
                .param("source", animeBM.getSource())
                .with(csrf())
        ).andExpect(status().is3xxRedirection());
    }

    @Test
    void searchAnimeGET_ShouldHaveAnimesNullValues() throws Exception {
        mockMvc.perform(get("/animes/search"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("animes"))
                .andExpect(view().name("search-anime"));
    }


    @Test
    void searchAnimePOST_AnimesWithAnimesObject() throws Exception {
        SearchForm searchForm = new SearchForm()
                .setData("some form data");

        List<AnimeSearchVM> expected = new ArrayList<>();
        expected.add(new AnimeSearchVM()
                .setStatusObject(StatusObjectEnum.ACCEPTED)
                .setType(AnimeTypeEnum.TV)
                .setEpisodes(23)
                .setTitle("some title")
                .setImageUrl("some image url")
                .setId(UUID.randomUUID()));
        Mockito.when(this.animeService.getAnimesByTitle(searchForm.getData()))
                .thenReturn(expected);


        mockMvc.perform(post("/animes/search")
                        .param("data", searchForm.getData())
                        .with(csrf()))
                .andExpect(view().name("search-anime"))
                .andExpect(model().attributeExists("animes"))
                .andExpect(model().attribute("animes", hasSize(1)));

    }


    @Test
    @WithMockUser(username = "user",roles = "USER",password = "123")
    void addAnimePOST_BindingResultHasError() throws Exception {

        mockMvc.perform(post("/animes/add")
                        .param("title", animeBM.getTitle())
                        .param("producers", animeBM.getProducers())
                        .param("content", animeBM.getContent())
                        .param("episodes", Integer.toString(animeBM.getEpisodes()))
                        .param("aired", animeBM.getAired().toString())
                        .param("duration", Integer.toString(animeBM.getDuration()))
                        .param("imageUrl", animeBM.getImageUrl())
                        .param("rating", animeBM.getRating())
                        .param("genres", animeBM.getGenres())
                        .param("popularity", Integer.toString(animeBM.getPopularity()))
                        .param("type", animeBM.getType())
                        .param("members", Integer.toString(animeBM.getMembers()))
                        .param("ranked", Integer.toString(animeBM.getRanked()))
                        .param("studios", animeBM.getStudios())
//                .param("source",animeBM.getSource())
                        .with(csrf())
        ).andExpect(status().is3xxRedirection());
    }

    @Test
    void searchAnimePOST_AnimesWithEmptyList() throws Exception {

        SearchForm searchForm = new SearchForm()
                .setData("some form data");

        Mockito.when(this.animeService.getAnimesByTitle(searchForm.getData()))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(post("/animes/search")
                        .param("data", searchForm.getData())
                        .with(csrf()))
                .andExpect(view().name("search-anime"))
                .andExpect(model().attributeExists("animes"))
                .andExpect(model().attribute("animes", hasSize(0)));
    }

    @Test
    void searchAnimePOST_AnimesWithSearchFormData_IsBlank() throws Exception {
        SearchForm searchForm = new SearchForm()
                .setData("");

        mockMvc.perform(post("/animes/search")
                        .param("data", searchForm.getData())
                        .with(csrf()))
                .andExpect(model().attributeDoesNotExist("animes"));

    }


    @Test
    void animeDetailsGET_ObjectNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();

        AnimeDto animeDto = new AnimeDto()
                .setImageUrl("some image")
                .setAired(LocalDate.now())
                .setDuration(24)
                .setEpisodes(3)
                .setGenres("test2")
                .setImageUrl("testIamgeUrl2")
                .setProducers("Test producers2")
                .setRanked(1)
                .setRating("test2")
                .setTitle("test2")
                .setContent("Lorem Ipsum is simply dummy text of the printing and " +
                        "typesetting industry. Lorem Ipsum has been the industry's " +
                        "standard dummy text ever since the 1500s, when an unknown" +
                        " printer took a galley of type and scrambled it to make a" +
                        " type specimen book. It has survived not only five centuries," +
                        " but also the leap into electronic typesetting," +
                        " remaining essentially unchanged. It was popularised in" +
                        " the 1960s with the release of Letraset sheets containing" +
                        " Lorem Ipsum passages, and more recently with desktop" +
                        " publishing software like Aldus PageMaker including " +
                        "versions of Lorem Ipsum.\n")
                .setPopularity(2)
                .setType(AnimeTypeEnum.MOVIE)
                .setMembers(24)
                .setStudios("studio test")
                .setRanked(34)
                .setSource("Manga")
                .setStatusObject(StatusObjectEnum.DECLINED);


        Mockito.when(this.animeService.getAnimeById(uuid))
                .thenReturn(animeDto);


        mockMvc.perform(get("/animes/" + uuid))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ObjectNotFoundException));
    }


    @Test
    @WithMockUser(username = "user", roles = "USER", password = "123")
    void animeDetailsGET_PrincipleIsNotNull_UserAnimeDtoIsNull() throws Exception {
        UUID uuid = UUID.randomUUID();

        AnimeDto animeDto = new AnimeDto()
                .setImageUrl("some image")
                .setAired(LocalDate.now())
                .setDuration(24)
                .setEpisodes(3)
                .setGenres("test2")
                .setImageUrl("testIamgeUrl2")
                .setProducers("Test producers2")
                .setRanked(1)
                .setRating("test2")
                .setTitle("test2")
                .setContent("Lorem Ipsum is simply dummy text of the printing and " +
                        "typesetting industry. Lorem Ipsum has been the industry's " +
                        "standard dummy text ever since the 1500s, when an unknown" +
                        " printer took a galley of type and scrambled it to make a" +
                        " type specimen book. It has survived not only five centuries," +
                        " but also the leap into electronic typesetting," +
                        " remaining essentially unchanged. It was popularised in" +
                        " the 1960s with the release of Letraset sheets containing" +
                        " Lorem Ipsum passages, and more recently with desktop" +
                        " publishing software like Aldus PageMaker including " +
                        "versions of Lorem Ipsum.")
                .setPopularity(2)
                .setType(AnimeTypeEnum.MOVIE)
                .setMembers(24)
                .setStudios("studio test")
                .setRanked(34)
                .setSource("Manga")
                .setStatusObject(StatusObjectEnum.ACCEPTED);
//        String username = "user";

        Mockito.when(this.animeService.getAnimeById(uuid))
                .thenReturn(animeDto);

        Mockito.when(this.userAnimeService.getAverageScoreByAnimeTitle(animeDto.getTitle()))
                .thenReturn(2.2);


        Double expectedAverage = 2.2;
        int expectedCount = 0;
        mockMvc.perform(get("/animes/" + uuid))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("userAnimeExist"))
                .andExpect(model().attributeExists("counterUsers"))
                .andExpect(model().attributeExists("averageScore"))
                .andExpect(model().attribute("averageScore", expectedAverage))
                .andExpect(model().attribute("counterUsers", expectedCount))
                .andExpect(model().attributeDoesNotExist("userAnimeVM"))
                .andExpect(view().name("anime-details"));


    }

    @Test
    @WithMockUser(username = "user", roles = "USER", password = "123")
    void animeDetailsGET_PrincipleIsNotNull_UserAnime() throws Exception {

        UUID uuid = UUID.randomUUID();
        String username = "user";

        AnimeDto animeDto = new AnimeDto()
                .setImageUrl("some image")
                .setAired(LocalDate.now())
                .setDuration(24)
                .setEpisodes(3)
                .setGenres("test2")
                .setImageUrl("testIamgeUrl2")
                .setProducers("Test producers2")
                .setRanked(1)
                .setRating("test2")
                .setTitle("test2")
                .setContent("Lorem Ipsum is simply dummy text of the printing and " +
                        "typesetting industry. Lorem Ipsum has been the industry's " +
                        "standard dummy text ever since the 1500s, when an unknown" +
                        " printer took a galley of type and scrambled it to make a" +
                        " type specimen book. It has survived not only five centuries," +
                        " but also the leap into electronic typesetting," +
                        " remaining essentially unchanged. It was popularised in" +
                        " the 1960s with the release of Letraset sheets containing" +
                        " Lorem Ipsum passages, and more recently with desktop" +
                        " publishing software like Aldus PageMaker including " +
                        "versions of Lorem Ipsum.")
                .setPopularity(2)
                .setType(AnimeTypeEnum.MOVIE)
                .setMembers(24)
                .setStudios("studio test")
                .setRanked(34)
                .setSource("Manga")
                .setStatusObject(StatusObjectEnum.ACCEPTED);


        UserAnimeDto userAnimeDtoTest = new UserAnimeDto()
                .setWatchStatus(WatchStatusEnum.NOT_FINISHED)
                .setScore(12);

        Mockito.when(this.animeService.getAnimeById(uuid))
                .thenReturn(animeDto);

        Mockito.when(this.userAnimeService.getAverageScoreByAnimeTitle(animeDto.getTitle()))
                .thenReturn(1.4);


        Mockito.when(this.userAnimeService.getCounterByAnimeTitle(animeDto.getTitle()))
                .thenReturn(10);


        Mockito.when(this.userAnimeService.findUserAnimeDtoByUsernameAndAnimeId(username,uuid))
                .thenReturn(userAnimeDtoTest);

        Double expectedAverage = 1.4;
        int expectedCount = 10;
        mockMvc.perform(get("/animes/" + uuid)
                        .with(csrf()))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("userAnimeExist"))
                .andExpect(model().attributeExists("counterUsers"))
                .andExpect(model().attributeExists("averageScore"))
                .andExpect(model().attribute("averageScore", expectedAverage))
                .andExpect(model().attribute("counterUsers", expectedCount))
                .andExpect(model().attributeExists("userAnimeVM"))
                .andExpect(view().name("anime-details"));


    }


    @Test
    @WithMockUser(username = "user", roles = "USER", password = "123")

    void topAnimes_ShouldHaveAnimesCorrectly() throws Exception {


        UserAnimeDto userAnimeDto1 = new UserAnimeDto()
                .setScore(10)
                .setEps(20)
                .setImageUrl("some url1")
                .setTitle("some title1")
                .setAired(LocalDate.of(2003,2,1))
                .setId(UUID.randomUUID())
                .setStatusObject(StatusObjectEnum.ACCEPTED)
                .setType(AnimeTypeEnum.TV);

        UserAnimeDto userAnimeDto2 = new UserAnimeDto()
                .setScore(15)
                .setEps(244)
                .setImageUrl("some url2")
                .setTitle("some title2")
                .setAired(LocalDate.of(2000,1,12))
                .setId(UUID.randomUUID())
                .setStatusObject(StatusObjectEnum.ACCEPTED)
                .setType(AnimeTypeEnum.TV);

        Mockito.when(this.animeService.getAllAnimes())
                .thenReturn(List.of(userAnimeDto1,userAnimeDto2));


        mockMvc.perform(get("/animes/top-anime")
                        .with(csrf()))
                .andExpect(model().attributeExists("animes"))
                .andExpect(view().name("top-anime"));

    }
    @Test
    @WithMockUser(username = "user", roles = "USER", password = "123")
    void adminAnimeDetailsGET_Status403_Forbbiden() throws Exception {


        UUID uuid = UUID.randomUUID();

        Mockito.when( this.animeService.getAnimeById(uuid))
                        .thenReturn(animeTest);
        Mockito.when(this.userAnimeService.getAverageScoreByAnimeTitle(animeTest.getTitle()))
                .thenReturn(1.4);
        Mockito.when(this.userAnimeService.getCounterByAnimeTitle(animeTest.getTitle()))
                .thenReturn(10);



        mockMvc.perform(get("/animes/admin/" + uuid)
                .with(csrf()))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN", password = "123")
    void adminAnimeDetailsGET_WorksCorrectly() throws Exception {


        UUID uuid = UUID.randomUUID();


        Mockito.when( this.animeService.getAnimeById(uuid))
                .thenReturn(animeTest);
        Mockito.when(this.userAnimeService.getAverageScoreByAnimeTitle(animeTest.getTitle()))
                .thenReturn(1.4);
        Mockito.when(this.userAnimeService.getCounterByAnimeTitle(animeTest.getTitle()))
                .thenReturn(10);

        mockMvc.perform(get("/animes/admin/" + uuid)
                        .with(csrf()))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("userAnimeExist"))
                .andExpect(model().attributeExists("counterUsers"))
                .andExpect(model().attributeExists("averageScore"))
                .andExpect(model().attributeDoesNotExist("userAnimeVM"))
                .andExpect(view().name("simple-anime-details"));

    }
    @Test
    @WithMockUser(username = "user", roles = "ADMIN", password = "123")
    void adminAnimeDetailsGET_UserAnimeExistIsTrue() throws Exception {

        UUID uuid = UUID.randomUUID();
        UserAnimeDto userAnimeDtoTest = new UserAnimeDto()
                .setWatchStatus(WatchStatusEnum.NOT_FINISHED)
                .setScore(12);

        String username = "user";
        Mockito.when( this.animeService.getAnimeById(uuid))
                .thenReturn(animeTest);

        Mockito.when(this.userAnimeService.findUserAnimeDtoByUsernameAndAnimeId(username,uuid))
                .thenReturn(userAnimeDtoTest);

        Mockito.when(this.userAnimeService.getAverageScoreByAnimeTitle(animeTest.getTitle()))
                .thenReturn(1.4);
        Mockito.when(this.userAnimeService.getCounterByAnimeTitle(animeTest.getTitle()))
                .thenReturn(10);

        mockMvc.perform(get("/animes/admin/" + uuid)
                        .with(csrf()))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("userAnimeExist"))
                .andExpect(model().attributeExists("counterUsers"))
                .andExpect(model().attributeExists("averageScore"))
                .andExpect(model().attributeExists("userAnimeVM"))
                .andExpect(view().name("simple-anime-details"));
    }


    @Test
    @WithMockUser(username = "user", roles = "USER", password = "123")
    void animeAdminAccept_UserRole_StatusForbbiden403() throws Exception {
        UUID uuid = UUID.randomUUID();
        mockMvc.perform(post("/animes/" + uuid + "/admin-anime-accept")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "user", roles = "ADMIN", password = "123")
    void animeAdminAccept_ShouldWorkCorrectly() throws Exception {
        UUID uuid = UUID.randomUUID();
        mockMvc.perform(post("/animes/" + uuid + "/admin-anime-accept")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER", password = "123")
    void animeAdminDecline_UserRole_StatusForbbiden403() throws Exception {
        UUID uuid = UUID.randomUUID();
        mockMvc.perform(post("/animes/" + uuid + "/admin-anime-decline")
                .with(csrf()))
                .andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(username = "user", roles = "ADMIN", password = "123")
    void animeAdminDecline_UserRole_ShouldWorkCorrectly() throws Exception {
        UUID uuid = UUID.randomUUID();
        mockMvc.perform(post("/animes/" + uuid + "/admin-anime-decline")
                .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }



}