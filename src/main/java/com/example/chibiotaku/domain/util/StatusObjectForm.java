package com.example.chibiotaku.domain.util;

import com.example.chibiotaku.domain.enums.StatusObjectEnum;

import java.util.UUID;

public class StatusObjectForm {


    StatusObjectEnum statusObject;


    public StatusObjectForm() {
    }

    public StatusObjectEnum getStatusObject() {
        return statusObject;
    }

    public StatusObjectForm setStatusObject(StatusObjectEnum statusObject) {
        this.statusObject = statusObject;
        return this;
    }
}
