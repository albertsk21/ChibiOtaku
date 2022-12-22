package com.example.chibiotaku.domain.util;

import com.example.chibiotaku.domain.enums.ObjectTypeEnum;
import com.example.chibiotaku.domain.enums.WatchStatusEnum;

public class ProfileAnimeForm {


    private WatchStatusEnum statusWatch;
    private ObjectTypeEnum objectType;

    public ProfileAnimeForm() {
    }

    public WatchStatusEnum getStatusWatch() {
        return statusWatch;
    }

    public ObjectTypeEnum getObjectType() {
        return objectType;
    }

    public ProfileAnimeForm setStatusWatch(WatchStatusEnum statusWatch) {
        this.statusWatch = statusWatch;
        return this;
    }

    public ProfileAnimeForm setObjectType(ObjectTypeEnum objectType) {
        this.objectType = objectType;
        return this;
    }
}
