package com.example.chibiotaku.web;

import com.example.chibiotaku.constraints.ErrorMessage;
import com.example.chibiotaku.domain.biding.MangaBM;
import com.example.chibiotaku.domain.dtos.MangaDto;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import com.example.chibiotaku.domain.util.SearchForm;
import com.example.chibiotaku.domain.views.*;
import com.example.chibiotaku.domain.dtos.UserMangaDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/manga")
public class MangaController {

    private MangaService mangaService;
    private ModelMapper modelMapper;
    private UserMangaService userMangaService;

    public MangaController(MangaService mangaService, ModelMapper modelMapper, UserMangaService userMangaService) {
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
    @ModelAttribute("mangaBM")
    public MangaBM mangaBM(){
        return new MangaBM();
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public String addMangaGET(){
        return "add-manga";
    }


    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public String addMangaPOST(@Valid MangaBM mangaBM,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Principal principal){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("mangaBM",mangaBM);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.mangaBM",bindingResult);
            return "redirect:/add/manga";
        }


        String username = principal.getName();
        MangaDto mangaDto = this.modelMapper.map(mangaBM,MangaDto.class);
        this.mangaService.addManga(mangaDto,username);

  //      throw new IllegalArgumentException("stupid bug:))))))))))))");
        return "redirect:/";
    }


    @GetMapping("/search")
    @PreAuthorize("permitAll()")
    public String searchManga( Model model){

        model.addAttribute("searchForm",new SearchForm());
        return "search-manga";
    }

    @PostMapping("/search")
    public String searchAnimePOST(Model model, @ModelAttribute("searchForm") SearchForm searchForm, BindingResult bindingResult){
        if(searchForm.getData().isBlank()){
            return "redirect:/manga/search";
        }
        List<MangaSearchVM> mangaViewList = this.mangaService.getMangasByTitle(searchForm.getData())
                .stream()
                .filter(m -> m.getStatusObject().equals(StatusObjectEnum.ACCEPTED))
                .collect(Collectors.toList());
        searchForm.setData("");
        if (mangaViewList.size() == 0){
            model.addAttribute("mangas", new ArrayList<>());
        }else{
            model.addAttribute("mangas",mangaViewList);
        }
        return "search-manga";
    }

    @GetMapping("/{id}")
    public String mangaDetailsGET(@PathVariable("id") String id,
                                  @ModelAttribute MangaDetailsVM mangaDetailsVM,
                                  Model model,
                                  Principal principal){
        MangaDto mangaDto = this.mangaService.getMangaById(UUID.fromString(id));
        MangaDetailsVM manga = this.modelMapper.map(mangaDto,MangaDetailsVM.class);
        model.addAttribute("manga", manga);
        if (!manga.getStatusObject().equals(StatusObjectEnum.ACCEPTED)){
            throw new ObjectNotFoundException(String.format(ErrorMessage.MANGA_NOT_FOUND,id));
        }


        if (principal != null){
            model.addAttribute("username",principal.getName());
        }

        String username = "";
        if(principal != null){
            username = principal.getName();
        }
        UserMangaDto userMangaDto = this.userMangaService.findUserMangaDtoByUsernameAndMangaId(username,UUID.fromString(id));
        boolean userMangaExist = userMangaDto != null;
        if(userMangaExist){
            model.addAttribute("userMangaVM",this.modelMapper.map(userMangaDto, UserMangaVM.class));
        }

        model.addAttribute("userMangaExist",userMangaExist);
        model.addAttribute("mangaDetailsVM",mangaDetailsVM);
        model.addAttribute("counterUsers",this.userMangaService.getCounterUsersByMangaId(UUID.fromString(id)));
        model.addAttribute("score",this.userMangaService.extractAverageScoreByMangaId(UUID.fromString(id)));
        return "manga-details";
    }
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminMngaDetailsGET(@PathVariable("id") String id,
                                  @ModelAttribute MangaDetailsVM mangaDetailsVM,
                                  Model model,
                                  Principal principal){
        MangaDto mangaDto = this.mangaService.getMangaById(UUID.fromString(id));
        MangaDetailsVM manga = this.modelMapper.map(mangaDto,MangaDetailsVM.class);
        model.addAttribute("manga", manga);


        if (principal != null){
            model.addAttribute("username",principal.getName());
        }


        model.addAttribute("mangaDetailsVM",mangaDetailsVM);
        model.addAttribute("counterUsers",this.userMangaService.getCounterUsersByMangaId(UUID.fromString(id)));
        model.addAttribute("score",this.userMangaService.extractAverageScoreByMangaId(UUID.fromString(id)));
        return "simple-manga-details";
    }


    @GetMapping("/top-manga")
    @PreAuthorize("permitAll()")
    public String topMangas(Model model){

        List<MangaDto> mangaList = this.mangaService.getAllManga();
        List<TopMangaVM> topMangaVMList =
                mangaList.stream()
                        .map(m -> this.modelMapper.map(m,TopMangaVM.class))
                        .filter(m -> m.getStatusObject().equals(StatusObjectEnum.ACCEPTED))
                        .collect(Collectors.toList());

        for (int i = 0; i < topMangaVMList.size() ; i++) {
            topMangaVMList.get(i)
                    .setCounter(i+1);
        }

        model.addAttribute("mangas", topMangaVMList);
        return "top-manga";
    }

    @PostMapping("/{id}/admin-manga-accept")
    @PreAuthorize("hasRole('ADMIN')")
    public String animeAdminAccept(@PathVariable("id") String id){
        this.mangaService.editMangaStatusByMangaId(StatusObjectEnum.ACCEPTED,UUID.fromString(id));
        return "redirect:/users/admin/admin-mode";
    }

    @PostMapping("/{id}/admin-manga-decline")
    @PreAuthorize("hasRole('ADMIN')")
    public String animeAdminDecline(@PathVariable("id") String id){
        this.mangaService.editMangaStatusByMangaId(StatusObjectEnum.DECLINED,UUID.fromString(id));
        return "redirect:/users/admin/admin-mode";
    }
}

