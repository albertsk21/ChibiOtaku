package com.example.chibiotaku.web;

import com.example.chibiotaku.constraints.ErrorMessage;
import com.example.chibiotaku.domain.biding.AnimeBM;
import com.example.chibiotaku.domain.dtos.AnimeDto;
import com.example.chibiotaku.domain.dtos.UserAnimeDto;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import com.example.chibiotaku.domain.util.SearchForm;
import com.example.chibiotaku.domain.views.AnimeDetailsVM;
import com.example.chibiotaku.domain.views.AnimeSearchVM;
import com.example.chibiotaku.domain.views.TopAnimeVM;
import com.example.chibiotaku.domain.views.UserAnimeVM;
import com.example.chibiotaku.services.AnimeService;
import com.example.chibiotaku.services.ReviewAnimeService;
import com.example.chibiotaku.services.UserAnimeService;
import com.example.chibiotaku.util.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/animes")
public class AnimeController {

    private ModelMapper modelMapper;
    private AnimeService animeService;
    private ReviewAnimeService reviewsAnimeService;
    private UserAnimeService userAnimeService;

    public AnimeController(ModelMapper modelMapper, AnimeService animeService, ReviewAnimeService reviewsAnimeService, UserAnimeService userAnimeService) {
        this.modelMapper = modelMapper;
        this.animeService = animeService;
        this.reviewsAnimeService = reviewsAnimeService;
        this.userAnimeService = userAnimeService;
    }

    @ModelAttribute("username")
    public String username(Principal principal){
        if(principal == null){
            return "";
        }
        return principal.getName();
    }


    @ModelAttribute("animeBM")
    public AnimeBM animeBM(){
        return new AnimeBM();
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public String addAnimeGET(){
        return "add-anime";
    }




    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public String addAnimePOST(@ModelAttribute @Valid AnimeBM animeBM,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Principal principal){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("animeBM",animeBM);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.animeBM",bindingResult);
            return "redirect:/add/anime";
        }
        String username = principal.getName();
        AnimeDto animeDto = this.modelMapper.map(animeBM,AnimeDto.class);
        animeService.saveAnime(animeDto,username);
        return "redirect:/";
    }

    @GetMapping("/search")
    @PreAuthorize("permitAll()")
    public String searchAnimeGET(@ModelAttribute SearchForm searchForm,Model model){
        return "search-anime";
    }

    @PostMapping("/search")
    public ModelAndView searchAnimePOST(@ModelAttribute("searchForm") SearchForm searchForm, HttpStatus status, BindingResult bindingResult){

        ModelAndView modelAndView = new ModelAndView();

        if(searchForm.getData().isBlank()){
            modelAndView.setViewName("redirect:/animes/search");
            return modelAndView;
        }
        List<AnimeSearchVM> animeViewList = this.animeService.getAnimesByTitle(searchForm.getData())
                .stream()
                .filter(a -> a.getStatusObject().equals(StatusObjectEnum.ACCEPTED))
                .collect(Collectors.toList());
        searchForm.setData("");
        if (animeViewList.size() == 0){

            modelAndView.addObject("animes", new ArrayList<>());
        }else{
            modelAndView.addObject("animes",animeViewList);

        }

        modelAndView.setViewName("search-anime");
        return modelAndView;
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public String animeDetailsGET(@PathVariable("id") String id, @ModelAttribute AnimeDetailsVM animeDetailsVM,
                                  Model model,
                                  Principal principal){
        UUID uuid = UUID.fromString(id);
        AnimeDto animeDto = this.animeService.getAnimeById(uuid);
        animeDetailsVM = this.modelMapper.map(animeDto,AnimeDetailsVM.class);
        if (!animeDto.getStatusObject().equals(StatusObjectEnum.ACCEPTED)){
            throw new ObjectNotFoundException(String.format(ErrorMessage.ANIME_NOT_FOUND,id));
        }
        if (principal != null){
            model.addAttribute("username",principal.getName());
        }

        String username = "";
        if(principal != null){
            username = principal.getName();
        }
        UserAnimeDto userAnimeDto = this.userAnimeService.findUserAnimeDtoByUsernameAndAnimeId(username,uuid);
        boolean userAnimeExist = userAnimeDto != null;
        if(userAnimeExist){
            model.addAttribute("userAnimeVM",this.modelMapper.map(userAnimeDto, UserAnimeVM.class));
        }

        model.addAttribute("userAnimeExist",userAnimeExist);
        model.addAttribute("animeDetailsVM",animeDetailsVM);
        model.addAttribute("counterUsers",this.userAnimeService.getCounterByAnimeTitle(animeDto.getTitle()));
        model.addAttribute("averageScore",this.userAnimeService.getAverageScoreByAnimeTitle(animeDto.getTitle()));
        return "anime-details";
    }
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAnimeDetailsGET(@PathVariable("id") String id, @ModelAttribute AnimeDetailsVM animeDetailsVM,
                                  Model model,
                                  Principal principal){

        AnimeDto animeDto = this.animeService.getAnimeById(UUID.fromString(id));
        animeDetailsVM = this.modelMapper.map(animeDto,AnimeDetailsVM.class);
        if (principal != null){
            model.addAttribute("username",principal.getName());
        }

        String username = "";
        if(principal != null){
            username = principal.getName();
        }
        UserAnimeDto userAnimeDto = this.userAnimeService.findUserAnimeDtoByUsernameAndAnimeId(username,UUID.fromString(id));
        boolean userAnimeExist = userAnimeDto != null;
        if(userAnimeExist){
            model.addAttribute("userAnimeVM",this.modelMapper.map(userAnimeDto, UserAnimeVM.class));
        }

        model.addAttribute("userAnimeExist",userAnimeExist);
        model.addAttribute("animeDetailsVM",animeDetailsVM);
        model.addAttribute("counterUsers",this.userAnimeService.getCounterByAnimeTitle(animeDto.getTitle()));
        model.addAttribute("averageScore",this.userAnimeService.getAverageScoreByAnimeTitle(animeDto.getTitle()));
        return "simple-anime-details";
    }

    @GetMapping("/top-anime")
    @PreAuthorize("permitAll()")
    public String topAnimes(Model model){
        List<UserAnimeDto> animes = this.animeService.getAllAnimes();
        List<TopAnimeVM> exportAnimes =
                animes
                        .stream()
                        .map(a -> this.modelMapper.map(a, TopAnimeVM.class))
                        .filter(a -> a.getStatusObject().equals(StatusObjectEnum.ACCEPTED))
                        .collect(Collectors.toList());

        for (int i = 0; i < exportAnimes.size(); i++) {
            exportAnimes.get(i)
                    .setCounter(i+1);
        }
        model.addAttribute("animes",exportAnimes);
        return "top-anime";
    }

    @PostMapping("/{id}/admin-anime-accept")
    @PreAuthorize("hasRole('ADMIN')")
    public String animeAdminAccept(@PathVariable("id") String id){


        this.animeService.editAnimeStatusByAnimeId(StatusObjectEnum.ACCEPTED,UUID.fromString(id));
        return "redirect:/users/admin/admin-mode";

    }

    @PostMapping("/{id}/admin-anime-decline")
    @PreAuthorize("hasRole('ADMIN')")
    public String animeAdminDecline(@PathVariable("id") String id){
        this.animeService.editAnimeStatusByAnimeId(StatusObjectEnum.DECLINED,UUID.fromString(id));
        return "redirect:/users/admin/admin-mode";

    }

}
