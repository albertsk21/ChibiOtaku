package com.example.chibiotaku.domain.biding;

import com.example.chibiotaku.domain.enums.WatchStatusEnum;

public class UserAnimeBM {

    private Double score;

    private WatchStatusEnum watchStatus;

    public UserAnimeBM() {
    }

    public Double getScore() {
        return score;
    }
    public UserAnimeBM setScore(Double score) {
        this.score = score;
        return this;
    }
    public WatchStatusEnum getWatchStatus() {
        return watchStatus;
    }

    public UserAnimeBM setWatchStatus(WatchStatusEnum watchStatus) {
        this.watchStatus = watchStatus;
        return this;
    }
}
