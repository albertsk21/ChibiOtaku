package com.example.chibiotaku.util;

import com.example.chibiotaku.domain.enums.StatusReviewEnum;
import com.example.chibiotaku.domain.views.ReviewVM;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Convert {


   public static List<ReviewVM> convertReviewsEntityToVM(List<Object[]> reviews) {
        List<ReviewVM> reviewOutput = new ArrayList<>();
        for (Object[] reviewDetails : reviews) {
            reviewOutput.add(new ReviewVM()
                    .setUsername(reviewDetails[0].toString())
                    .setCreated((LocalDate) reviewDetails[1])
                    .setStatusReview((StatusReviewEnum) reviewDetails[2])
                    .setContent(reviewDetails[3].toString()));

        }
        return reviewOutput;
    }
}
