package com.example.chibiotaku.web;

import com.example.chibiotaku.constraints.ErrorMessage;
import com.example.chibiotaku.domain.biding.ReviewBM;
import com.example.chibiotaku.domain.dtos.AnimeDto;
import com.example.chibiotaku.domain.dtos.ReviewDto;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import com.example.chibiotaku.domain.views.ReviewVM;
import com.example.chibiotaku.services.AnimeService;
import com.example.chibiotaku.services.ReviewAnimeService;
import com.example.chibiotaku.util.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/animes")
public class ReviewAnimeController {

    private AnimeService animeService;
    private ReviewAnimeService reviewsAnimeService;
    private ModelMapper modelMapper;

    public ReviewAnimeController(AnimeService animeService, ReviewAnimeService reviewsAnimeService, ModelMapper modelMapper) {
        this.animeService = animeService;
        this.reviewsAnimeService = reviewsAnimeService;
        this.modelMapper = modelMapper;
    }
    @ModelAttribute("username")
    public String username(Principal principal){

        if(principal == null){
            return "";
        }
        return principal.getName();
    }
    @ModelAttribute("reviewBM")
    public ReviewBM reviewBM(){
        return new ReviewBM();
    }

    @GetMapping("/{id}/reviews")
    @PreAuthorize("permitAll()")
    public String animeReviewsGET(@PathVariable("id") String id, Model model){

        AnimeDto currentAnime = this.animeService.getAnimeById(UUID.fromString(id));

        if (!currentAnime.getStatusObject().equals(StatusObjectEnum.ACCEPTED)){
            throw new ObjectNotFoundException(String.format(ErrorMessage.ANIME_NOT_FOUND,id));
        }

        model.addAttribute("animeDetailsVM",currentAnime);
        List<ReviewVM> reviews = this.reviewsAnimeService.exportReviewsByAnimeId(UUID.fromString(id));
        model.addAttribute("reviews",reviews);
        return "anime-reviews";
    }


    @PostMapping("/{id}/reviews")
    @PreAuthorize("hasRole('USER')")
    public String animeReviewsPOST(@PathVariable("id") String id,
                                   @Valid ReviewBM reviewBM,
                                   BindingResult bindingResult,
                                   Principal principal){


        AnimeDto animeDto = this.animeService.getAnimeById(UUID.fromString(id));


        if(!animeDto.getStatusObject().equals(StatusObjectEnum.ACCEPTED)){
            throw new ObjectNotFoundException(String.format(ErrorMessage.ANIME_NOT_FOUND,id));
        }

        ReviewDto reviewAnimeDto = this.modelMapper.map(reviewBM, ReviewDto.class);


        String username = principal.getName();



        this.reviewsAnimeService.addReviewAnime(UUID.fromString(id),username,reviewAnimeDto);
        return "redirect:/animes/" + id + "/reviews";
    }

}
