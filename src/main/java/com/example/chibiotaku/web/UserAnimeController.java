package com.example.chibiotaku.web;

import com.example.chibiotaku.constraints.ErrorMessage;
import com.example.chibiotaku.domain.biding.UserAnimeBM;
import com.example.chibiotaku.domain.dtos.AnimeDto;
import com.example.chibiotaku.domain.dtos.UserAnimeDto;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import com.example.chibiotaku.domain.views.AnimeDetailsVM;
import com.example.chibiotaku.domain.views.UserAnimeVM;
import com.example.chibiotaku.services.AnimeService;
import com.example.chibiotaku.services.UserAnimeService;
import com.example.chibiotaku.util.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping("/animes")
public class UserAnimeController {

    private AnimeService animeService;
    private ModelMapper modelMapper;
    private UserAnimeService userAnimeService;

    public UserAnimeController(AnimeService animeService, ModelMapper modelMapper, UserAnimeService userAnimeService) {
        this.animeService = animeService;
        this.modelMapper = modelMapper;
        this.userAnimeService = userAnimeService;
    }

    @ModelAttribute("username")
    public String username(Principal principal){
        if(principal == null){
            return "";
        }
        return principal.getName();
    }

    @ModelAttribute("animeForm")
    public UserAnimeBM animeForm(){
        return new UserAnimeBM();
    }

    @GetMapping("/{id}/add")
    @PreAuthorize("hasRole('USER')")
    public String addAnimeInMyList(@PathVariable("id") String id,@ModelAttribute("animeForm") UserAnimeBM animeForm ,Model model, Principal principal){
        UUID uuid = UUID.fromString(id);
        AnimeDto animeDto = this.animeService.getAnimeById(uuid);
        AnimeDetailsVM animeDetailsVM = this.modelMapper.map(animeDto,AnimeDetailsVM.class);
        model.addAttribute("animeDetailsVM",animeDetailsVM);

        if (!animeDto.getStatusObject().equals(StatusObjectEnum.ACCEPTED)){
            throw new ObjectNotFoundException(String.format(ErrorMessage.ANIME_NOT_FOUND,id));
        }

        String username = principal.getName();

        UserAnimeDto userAnimeDto = this.userAnimeService.findMyAnimeByAnimeIdAndUsername(id,username);
        boolean exist = userAnimeDto != null;
        model.addAttribute("existAnime",exist);

        if(exist){
            model.addAttribute("currentAnime",this.modelMapper.map(userAnimeDto, UserAnimeVM.class));
            animeForm.setScore(userAnimeDto.getScore());
            animeForm.setWatchStatus(userAnimeDto.getWatchStatus());
        }
        return "anime-add-to-list";
    }

    @PostMapping("/{id}/add")
    @PreAuthorize("hasRole('USER')")
    public String addAnimeInMyListPOST(@PathVariable("id") String id, @Valid UserAnimeBM animeForm, BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes, Principal principal){


        if(bindingResult.hasErrors()){
            redirectAttributes
                    .addFlashAttribute("animeForm", animeForm)
                    .addFlashAttribute("org.springframework.validation.BindingResult.animeBM",
                            bindingResult);
            return "redirect:/animes/" + id + "/add";
        }

        String username = principal.getName();
        UserAnimeDto userAnimeDto = this.modelMapper.map(animeForm,UserAnimeDto.class);
        this.userAnimeService.addAnimeToWatchList(userAnimeDto,UUID.fromString(id),username);
        return "redirect:/";
    }


    @DeleteMapping("/{id}/my-list/delete")
    @PreAuthorize("hasRole('USER')")
    public String deleteMyAnimeDELETE(@PathVariable("id") String id, Principal principal){
        UUID uuid = UUID.fromString(id);
        this.userAnimeService.deleteMyAnimeByIdAndUsername(uuid, principal.getName());
        return "redirect:/";
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}/edit")
    public String editWatchListAnime(@PathVariable("id") String id,@ModelAttribute UserAnimeVM animeForm, Principal principal){
        UserAnimeDto userAnimeDto = new UserAnimeDto()
                .setWatchStatus(animeForm.getWatchStatus())
                .setScore(animeForm.getScore());
        this.userAnimeService.addAnimeToWatchList(userAnimeDto,UUID.fromString(id),principal.getName());
        return "redirect:/";
    }




}
