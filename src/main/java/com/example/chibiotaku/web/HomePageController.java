package com.example.chibiotaku.web;

import com.example.chibiotaku.services.AnimeService;
import com.example.chibiotaku.services.MangaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomePageController {

    private AnimeService animeService;
    private MangaService mangaService;

    public HomePageController(AnimeService animeService, MangaService mangaService) {
        this.animeService = animeService;
        this.mangaService = mangaService;
    }

    @GetMapping("/")
    public String dashboard(Principal principal, Model model){
        model.addAttribute("animes",animeService.findMostPopularAnime());
        model.addAttribute("mangas",mangaService.findMostPopularManga());
        if (principal != null){
            model.addAttribute("username",principal.getName());
        }
        return "dashboard";
    }
}
