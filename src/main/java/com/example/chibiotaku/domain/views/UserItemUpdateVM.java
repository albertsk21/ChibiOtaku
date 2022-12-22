package com.example.chibiotaku.domain.views;

import java.util.UUID;

public class UserItemUpdateVM {

    private String imageUrl;
    private UUID id;


    public UserItemUpdateVM() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public UUID getId() {
        return id;
    }

    public UserItemUpdateVM setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public UserItemUpdateVM setId(UUID id) {
        this.id = id;
        return this;
    }
}
