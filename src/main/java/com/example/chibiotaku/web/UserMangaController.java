package com.example.chibiotaku.web;

import com.example.chibiotaku.constraints.ErrorMessage;
import com.example.chibiotaku.domain.biding.UserAnimeBM;
import com.example.chibiotaku.domain.dtos.MangaDto;
import com.example.chibiotaku.domain.dtos.UserMangaDto;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import com.example.chibiotaku.domain.views.MangaDetailsVM;
import com.example.chibiotaku.domain.views.UserAnimeVM;
import com.example.chibiotaku.domain.views.UserMangaVM;
import com.example.chibiotaku.services.MangaService;
import com.example.chibiotaku.services.UserMangaService;
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
@RequestMapping("/manga")
public class UserMangaController {


    private MangaService mangaService;
    private ModelMapper modelMapper;
    private UserMangaService userMangaService;


    public UserMangaController(MangaService mangaService,
                               ModelMapper modelMapper,
                               UserMangaService userMangaService) {
        this.mangaService = mangaService;
        this.modelMapper = modelMapper;
        this.userMangaService = userMangaService;
    }

    @ModelAttribute("username")
    public String username(Principal principal){
        if(principal == null){
            return "";
        }
        return principal.getName();
    }

    @GetMapping("/{id}/add")
    @PreAuthorize("hasRole('USER')")
    public String addMangaInMyList(@PathVariable("id") String id,
                                   @ModelAttribute("animeForm") UserAnimeBM animeForm ,
                                   Model model, Principal principal){
        MangaDto mangaDto = this.mangaService.getMangaById(UUID.fromString(id));
        MangaDetailsVM manga = this.modelMapper.map(mangaDto,MangaDetailsVM.class);

        UUID mangaId = UUID.fromString(id);

        if (!manga.getStatusObject().equals(StatusObjectEnum.ACCEPTED)){
            throw new ObjectNotFoundException(String.format(ErrorMessage.MANGA_NOT_FOUND,id));
        }
        model.addAttribute("manga",manga);

        String username = principal.getName();
        UserMangaDto userMangaDto = this.userMangaService.findMyMangaByMangaIdAndUsername(mangaId,username);
        boolean exist = userMangaDto != null;
        model.addAttribute("existManga",exist);
        if(exist){
            model.addAttribute("currentManga",this.modelMapper.map(userMangaDto, UserMangaVM.class));
            animeForm.setScore(userMangaDto.getScore());
            animeForm.setWatchStatus(userMangaDto.getWatchStatus());
        }
        return "manga-add-to-list";
    }
    @PostMapping("/{id}/add")
    @PreAuthorize("hasRole('USER')")
    public String addMangaInMyListPOST(@PathVariable("id") String id, @Valid UserAnimeBM animeForm, BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes, Principal principal){


        if(bindingResult.hasErrors()){
            redirectAttributes
                    .addFlashAttribute("mangaForm", animeForm)
                    .addFlashAttribute("org.springframework.validation.BindingResult.animeBM",
                            bindingResult);
            return "redirect:/manga/" + id + "/add";
        }

        String username = principal.getName();
        UserMangaDto userMangaDto = this.modelMapper.map(animeForm,UserMangaDto.class);
        this.userMangaService.addMangaToReadList(userMangaDto,UUID.fromString(id),username);
        return "redirect:/";
    }
    @DeleteMapping("/{id}/my-list/delete")
    public String deleteMangaFromReadList(@PathVariable("id") String id,Principal principal){
        this.userMangaService.deleteUserMangaByMangaIdAndUsermane(UUID.fromString(id),principal.getName());
        return "redirect:/";
    }
    @PutMapping("/{id}/edit")
    @PreAuthorize("hasRole('USER')")
    public String editMangaFromMyList(@PathVariable("id") String id,
                                      @ModelAttribute UserAnimeVM animeForm,
                                      Principal principal){

        UserMangaDto userMangaDto = new UserMangaDto()
                .setWatchStatus(animeForm.getWatchStatus())
                .setScore(animeForm.getScore());

        this.userMangaService.addMangaToReadList(userMangaDto,UUID.fromString(id),principal.getName());

        return "redirect:/";
    }
}
