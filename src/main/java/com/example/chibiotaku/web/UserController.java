package com.example.chibiotaku.web;

import com.example.chibiotaku.domain.biding.UserRegisterBM;
import com.example.chibiotaku.domain.dtos.*;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import com.example.chibiotaku.domain.util.MangaAnimeForm;
import com.example.chibiotaku.domain.util.StatusObjectForm;
import com.example.chibiotaku.domain.views.*;
import com.example.chibiotaku.domain.enums.ObjectTypeEnum;
import com.example.chibiotaku.domain.enums.WatchStatusEnum;
import com.example.chibiotaku.domain.util.ProfileAnimeForm;
import com.example.chibiotaku.services.*;
import com.example.chibiotaku.util.exceptions.RequestDeniedException;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequestMapping("/users")
public class UserController {


    private UserService userService;
    private ModelMapper modelMapper;
    private UserAnimeService userAnimeService;
    private AnimeService animeService;
    private UserMangaService userMangaService;
    private MangaService mangaService;

    public UserController(UserService userService,
                          ModelMapper modelMapper,
                          UserAnimeService userAnimeService,
                          AnimeService animeService,
                          UserMangaService userMangaService,
                          MangaService mangaService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userAnimeService = userAnimeService;
        this.animeService = animeService;
        this.userMangaService = userMangaService;
        this.mangaService = mangaService;
    }

    @ModelAttribute("username")
    public String username(Principal principal){
        if(principal == null){
            return "";
        }
        return principal.getName();
    }


    @ModelAttribute("userRegisterBM")
    public UserRegisterBM userRegisterBM(){
        return new UserRegisterBM();
    }


    @GetMapping("/login")
    public String loginGET(Principal principal){

        if (principal != null){
            return "redirect:/";
        }

        return "login";
    }


    @PostMapping("/login-error")
    public String loginFailed(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String userName,
            RedirectAttributes redirectAttributes){


        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, userName);
        redirectAttributes.addFlashAttribute("bad_credentials", true);

        return "redirect:/users/login";



    }
    @GetMapping("/register")
    public String registerGET(Principal principal){
//        throw new IllegalArgumentException("some stupid bug:)");
        if (principal != null){
            return "redirect:/";
        }
        return "register";
    }


    @PostMapping("/register")
    public String registerPOST(@Valid UserRegisterBM userRegisterBM,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes){



        boolean validatePasswords = !userRegisterBM.getPassword().equals(userRegisterBM.getConfirmPassword());

        if(bindingResult.hasErrors() || validatePasswords){
            redirectAttributes
                    .addFlashAttribute("userRegisterBM", userRegisterBM)
                    .addFlashAttribute("validatePassword",validatePasswords)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBM",
                            bindingResult);

            return "redirect:/users/register";
        }
        UserDto userDto = this.map(userRegisterBM);
        this.userService.registerUser(userDto);
        return "redirect:/users/login";

    }


    private UserDto map(UserRegisterBM userRegisterBM){
        return new UserDto()
                .setEmail(userRegisterBM.getEmail())
                .setFirstName(userRegisterBM.getFirstName())
                .setLastName(userRegisterBM.getLastName())
                .setPassword(userRegisterBM.getPassword())
                .setUsername(userRegisterBM.getUsername());

    }

    @GetMapping("/{username}")
    public String userDetails(@PathVariable("username") String username, Model model, Principal principal){

       List<UserItemUpdateVM> animesVM = this.userAnimeService.extractAnimesVMByUsername(username);
       List<UserItemUpdateVM> mangasVM = this.userMangaService.extractMangaVMyUsername(username);

        if(principal == null){
            model.addAttribute("authorization", false);
        }else{
            model.addAttribute("authorization", username.equals(principal.getName()));
        }
        model.addAttribute("animes",animesVM);
        model.addAttribute("mangas",mangasVM);
        model.addAttribute("userVM",this.formatUser(username));
        return "profile-details";
    }



    @ModelAttribute("profileAnimeForm")
    public ProfileAnimeForm profileAnimeForm(){
        return  new ProfileAnimeForm();
    }

    @GetMapping("/{username}/list")
    public String userAnimeList(@PathVariable("username") String username, Model model,
                                Principal principal){
        model.addAttribute("username",username);
        if(principal == null){
            model.addAttribute("authorization", false);
        }else{
            model.addAttribute("authorization", username.equals(principal.getName()));
        }

        return "profile-animes";
    }

    @PostMapping("/{username}/list")
    public String userAnimeListPOST(@PathVariable("username") String username,
                                    @ModelAttribute("profileAnimeForm") ProfileAnimeForm profileAnimeForm,
                                    RedirectAttributes redirectAttributes){

        if(profileAnimeForm.getStatusWatch() == null || profileAnimeForm.getObjectType() == null){
            return "redirect:/users/" + username + "/list";
        }

        if(profileAnimeForm.getObjectType().equals(ObjectTypeEnum.MANGA)){
//            TODO: extract manga by watchStatus

            List<UserMangaDto> mangaDtoList = this.userMangaService.extractUserMangaByWatchStatusAndUsername(profileAnimeForm.getStatusWatch(),username);

            List<UserMangaVM> mangaVMList = mangaDtoList
                    .stream()
                    .map(m -> this.modelMapper.map(m,UserMangaVM.class))
                    .collect(Collectors.toList());
            redirectAttributes.addFlashAttribute("mangas", mangaVMList);

        }else{
//            TODO: extract anime by watchStatus

            List<UserAnimeVM> animesVM = this.
                    userAnimeService.extractAnimesByUsernameAndWatchEnum(username, profileAnimeForm.getStatusWatch())
                    .stream()
                    .map(this::map)
                    .collect(Collectors.toList());

            redirectAttributes.addFlashAttribute("animes", animesVM);
        }


        return "redirect:/users/" + username + "/list";
    }

    @GetMapping("/{username}/animes-added")
    @PreAuthorize("hasRole('USER')")
    public String animesAddedByUser(@PathVariable("username") String username, Principal principal, Model model){

        this.addUserToModel(username, principal, model);

        List<AnimeDto> animeDtos = this.animeService.extractAnimesByUsername(principal.getName());
        List<MyAnimeVM> myAnimes = animeDtos
                .stream()
                .map(a -> this.modelMapper.map(a,MyAnimeVM.class))
                .collect(Collectors.toList());
        model.addAttribute("animes",myAnimes);


        return "profile-my-animes-added";
    }

    @GetMapping("/{username}/mangas-added")
    @PreAuthorize("hasRole('USER')")
    public String mangasAddedByUser(@PathVariable("username")String username, Principal principal, Model model){
        this.addUserToModel(username, principal, model);

        List<MangaDto> myMangaDtos = this.mangaService.extractMangaByUsername(username);

        model.addAttribute("mangas",myMangaDtos);

        return "profile-my-mangas-added";
    }

    @GetMapping("/{username}/admin-mode")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminMode(@PathVariable("username") String username,Model model, Principal principal){
       model = this.addUserToModel(username,principal,model);
       model.addAttribute("mangaAnimeForm", new MangaAnimeForm());
       model.addAttribute("statusObject",new StatusObjectForm());
       return "profile-admin-mode";
    }


    @PostMapping("/admin/list-admin-mode")
    @PreAuthorize("hasRole('ADMIN')")
    public String  getAnimeMangaList(MangaAnimeForm mangaAnimeForm,
                                     RedirectAttributes redirectAttributes){
        if(mangaAnimeForm.getObjectType() == null){
            return "redirect:/users/admin/admin-mode";
        }

        if (mangaAnimeForm.getObjectType() == ObjectTypeEnum.MANGA){
            List<MangaDto> mangaDtoList = this.mangaService.extractMangaByStatusObject(StatusObjectEnum.PENDING);
            if (mangaDtoList.size() == 0){
                redirectAttributes.addFlashAttribute("mangas",new ArrayList<>());
            }
            List<MangaDetailsVM> mangaDetailsVMList = mangaDtoList
                    .stream()
                    .map(m -> this.modelMapper.map(m,MangaDetailsVM.class))
                    .collect(Collectors.toList());
            redirectAttributes.addFlashAttribute("mangas",mangaDetailsVMList);
//            TODO: extracting all mangas in pending mode
        }else{

            List<AnimeDto> animeDtoList = this.animeService.extractAllAnimeDtoByStatusObject(StatusObjectEnum.PENDING);
            if (animeDtoList.size() == 0){
                redirectAttributes.addFlashAttribute("animes",new ArrayList<>());
            }
            List<AnimeDetailsVM> animeDetailsVMList = animeDtoList
                    .stream()
                    .map(a -> this.modelMapper.map(a,AnimeDetailsVM.class))
                    .collect(Collectors.toList());
            redirectAttributes.addFlashAttribute("animes",animeDetailsVMList);
//            TODO: extracting all animes in pending mode
        }

        return "redirect:/users/admin/admin-mode";
    }

    private Model addUserToModel(String username, Principal principal, Model model){
        if(principal == null ){
            throw new RequestDeniedException();
        }
        if (!principal.getName().equals(username)){
            throw new RequestDeniedException();
        }
        model.addAttribute("userVM",this.formatUser(principal.getName()));
        model.addAttribute("authorization", username.equals(principal.getName()));
        model.addAttribute("username", username);
        return model;
    }
    private UserAnimeVM map(UserAnimeDto userAnimeDto){
        return new UserAnimeVM()
                .setAnimeType(userAnimeDto.getType())
                .setScore(userAnimeDto.getScore())
                .setContent(userAnimeDto.getContent())
                .setEps(userAnimeDto.getEps())
                .setImageUrl(userAnimeDto.getImageUrl())
                .setTitle(userAnimeDto.getTitle())
                .setId(userAnimeDto.getId());
    }
    private UserVM formatUser(String  username){
        UserDto userDto = this.userService.findUserByUsername(username);

        int completeAnime =   this.userAnimeService.countUserAnimeByWatchStatusAndUsername(username, WatchStatusEnum.COMPLETE);
        int completeManga = this.userMangaService.countUserMangaByWatchStatusAndUsername(WatchStatusEnum.COMPLETE,username);

        int droppedAnime = this.userAnimeService.countUserAnimeByWatchStatusAndUsername(username, WatchStatusEnum.DROPPED);
        int droppedManga = this.userMangaService.countUserMangaByWatchStatusAndUsername(WatchStatusEnum.DROPPED,username);

        int notFinishedAnime = this.userMangaService.countUserMangaByWatchStatusAndUsername(WatchStatusEnum.NOT_FINISHED,username);
        int notFinishedManga =  this.userAnimeService.countUserAnimeByWatchStatusAndUsername(username,WatchStatusEnum.NOT_FINISHED);

        int setAsideAnime = this.userMangaService.countUserMangaByWatchStatusAndUsername(WatchStatusEnum.SET_ASIDE,username);
        int setAsideManga =  this.userAnimeService.countUserAnimeByWatchStatusAndUsername(username,WatchStatusEnum.SET_ASIDE);

       return new UserVM()
                .setUsername(userDto.getUsername())
                .setCreated(userDto.getCreated())
                .setComplete(completeAnime + completeManga)
                .setDropped(droppedAnime + droppedManga)
                .setNotFinished(notFinishedAnime +notFinishedManga)
               .setSetAside(setAsideAnime + setAsideManga);

    }
}
