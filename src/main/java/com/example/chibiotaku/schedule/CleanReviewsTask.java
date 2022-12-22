package com.example.chibiotaku.schedule;

import com.example.chibiotaku.constraints.SwearWords;
import com.example.chibiotaku.domain.entities.ReviewAnimeEntity;
import com.example.chibiotaku.domain.entities.ReviewMangaEntity;
import com.example.chibiotaku.repo.ReviewAnimeRepository;
import com.example.chibiotaku.repo.ReviewMangaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CleanReviewsTask {


    private ReviewMangaRepository reviewMangaRepository;
    private ReviewAnimeRepository reviewAnimeRepository;



    public void CleanReviewsTask(ReviewMangaRepository reviewMangaRepository,
                     ReviewAnimeRepository reviewAnimeRepository) {
        this.reviewMangaRepository = reviewMangaRepository;
        this.reviewAnimeRepository = reviewAnimeRepository;
    }


    @Scheduled(cron = "0 */12 * * *")
   public void deleteReviewMangaBadContent(){
       List<ReviewMangaEntity> reviewMangaEntities = this.reviewMangaRepository.findReviewMangaEntitiesByVerified(false);

       for (ReviewMangaEntity reviewManga : reviewMangaEntities) {
           String currentContent = reviewManga.getContent();

           boolean isAvailable = true;
           for (String swearWord : SwearWords.swearWordsList) {

               Pattern pattern = Pattern.compile(swearWord);
               Matcher matcher = pattern.matcher(currentContent);
               if (matcher.find()){
                   this.reviewMangaRepository.delete(reviewManga);
                   isAvailable = false;
                   break;
               }
           }
           if (isAvailable){
               reviewManga.setVerified(true);
               this.reviewMangaRepository.save(reviewManga);
           }

       }

   }

    @Scheduled(cron = "0 */12 * * *")
   public void deleteReviewAnimeBadContent(){{

       List<ReviewAnimeEntity> reviewAnimeEntities = this.reviewAnimeRepository.findReviewAnimeEntitiesByVerified(false);

       for (ReviewAnimeEntity reviewAnime : reviewAnimeEntities) {
           String currentContent = reviewAnime.getContent();

           boolean isAvailable = true;
           for (String swearWord : SwearWords.swearWordsList) {

               Pattern pattern = Pattern.compile(swearWord);
               Matcher matcher = pattern.matcher(currentContent);
               if (matcher.find()){
                   this.reviewAnimeRepository.delete(reviewAnime);
                   isAvailable = false;
                   break;
               }
           }
           if (isAvailable){
               reviewAnime.setVerified(true);
               this.reviewAnimeRepository.save(reviewAnime);
           }

       }

   }



   }

}
