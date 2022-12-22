package com.example.chibiotaku.domain.views;

import java.time.LocalDate;

public class UserVM {



    private LocalDate created;
    private String  username;
    private int complete;
    private int notFinished;
    private int dropped;
    private int setAside;

    public UserVM() {
    }

    public LocalDate getCreated() {
        return created;
    }

    public String getUsername() {
        return username;
    }

    public int getComplete() {
        return complete;
    }

    public int getNotFinished() {
        return notFinished;
    }

    public int getDropped() {
        return dropped;
    }

    public UserVM setCreated(LocalDate created) {
        this.created = created;
        return this;
    }

    public UserVM setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserVM setComplete(int complete) {
        this.complete = complete;
        return this;
    }

    public UserVM setNotFinished(int notFinished) {
        this.notFinished = notFinished;
        return this;
    }

    public UserVM setDropped(int dropped) {
        this.dropped = dropped;
        return this;
    }

    public int getSetAside() {
        return setAside;
    }

    public UserVM setSetAside(int setAside) {
        this.setAside = setAside;
        return this;
    }
}
