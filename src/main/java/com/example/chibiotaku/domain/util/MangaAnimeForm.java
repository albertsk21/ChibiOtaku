package com.example.chibiotaku.domain.util;

import com.example.chibiotaku.domain.enums.ObjectTypeEnum;

public class MangaAnimeForm {

    ObjectTypeEnum objectType;


    public MangaAnimeForm() {
    }

    public ObjectTypeEnum getObjectType() {
        return objectType;
    }

    public MangaAnimeForm setObjectType(ObjectTypeEnum objectType) {
        this.objectType = objectType;
        return this;
    }
}
