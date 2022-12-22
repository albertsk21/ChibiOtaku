package com.example.chibiotaku.web;

import com.example.chibiotaku.constraints.ErrorMessage;
import com.example.chibiotaku.domain.biding.ReviewBM;
import com.example.chibiotaku.domain.dtos.MangaDto;
import com.example.chibiotaku.domain.dtos.ReviewDto;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import com.example.chibiotaku.domain.views.MangaDetailsVM;
import com.example.chibiotaku.domain.views.ReviewVM;
import com.example.chibiotaku.services.MangaService;
import com.example.chibiotaku.services.ReviewMangaService;
import com.example.chibiotaku.util.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/manga")
public class ReviewsMangaController {


    private ReviewMangaService reviewMangaService;
    private MangaService mangaService;
    private ModelMapper modelMapper;

    public ReviewsMangaController(ReviewMangaService reviewMangaService, MangaService mangaService, ModelMapper modelMapper) {
        this.reviewMangaService = reviewMangaService;
        this.mangaService = mangaService;
        this.modelMapper = modelMapper;
    }
    @ModelAttribute("reviewBM")
    public ReviewBM reviewBM(){
        return new ReviewBM();
    }

    @ModelAttribute("username")
    public String username(Principal principal){
        if(principal == null){
            return "";
        }
        return principal.getName();
    }

    @GetMapping("/{id}/reviews")
    @PreAuthorize("permitAll()")
    public String animeReviewsGET(@PathVariable("id") String id, Model model){

        MangaDto mangaDto = this.mangaService.getMangaById(UUID.fromString(id));

        if (!mangaDto.getStatusObject().equals(StatusObjectEnum.ACCEPTED)){
            throw new ObjectNotFoundException(String.format(ErrorMessage.MANGA_NOT_FOUND,id));
        }

        model.addAttribute("manga",this.modelMapper.map(mangaDto, MangaDetailsVM.class));

        List<ReviewVM> reviewVMList = this.reviewMangaService.exportReviewsByMangaId(UUID.fromString(id));
        model.addAttribute("reviews",reviewVMList);
        return "manga-reviews";
    }

    @PostMapping("/{id}/reviews")
    @PreAuthorize("hasRole('USER')")
    public String mangaReviewsPOST(@PathVariable("id") String id,
                                   @ModelAttribute("reviewBM")  ReviewBM reviewBM,
                                   Principal principal){
        ReviewDto reviewDto= this.modelMapper.map(reviewBM, ReviewDto.class);
        String username = principal.getName();
        this.reviewMangaService.addReview(UUID.fromString(id),username,reviewDto);
        return "redirect:/manga/" + id + "/reviews";
    }


}
