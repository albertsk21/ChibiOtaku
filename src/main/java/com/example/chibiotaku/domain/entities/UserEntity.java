package com.example.chibiotaku.domain.entities;

import com.example.chibiotaku.domain.enums.RoleUserEnum;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private LocalDate created;
    private List<RoleEntity> roles;

    private List<UserAnimeEntity> myAnimes;
    private List<UserMangaEntity> myMangas;
    private List<MangaEntity> mangaAdded;
    private List<ReviewAnimeEntity> reviewAnimes;
    private List<ReviewMangaEntity> reviewMangas;

    @Column(name = "first_name", nullable = false)
    public String getFirstName() {
        return firstName;
    }
    @Column(name = "last_name", nullable = false)
    public String getLastName() {
        return lastName;
    }
    @Column(unique = true, nullable = false)
    public String getUsername() {
        return username;
    }
    @Column(unique = true, nullable = false)
    public String getEmail() {
        return email;
    }
    public LocalDate getCreated() {
        return created;
    }
    @Column(nullable = false)
    public String getPassword() {
        return password;
    }


    @OneToMany(mappedBy = "user")
    public List<UserAnimeEntity> getMyAnimes() {
        return myAnimes;
    }
    @OneToMany(mappedBy = "user")
    public List<UserMangaEntity> getMyMangas() {
        return myMangas;
    }
    @OneToMany(mappedBy = "user")
    public List<MangaEntity> getMangaAdded() {
        return mangaAdded;
    }
    @OneToMany(mappedBy = "user")
    public List<ReviewAnimeEntity> getReviewAnimes() {
        return reviewAnimes;
    }
    @OneToMany(mappedBy = "user")
    public List<ReviewMangaEntity> getReviewMangas() {
        return reviewMangas;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    public List<RoleEntity> getRoles() {
        return roles;
    }

    public UserEntity setRoles(List<RoleEntity> roles) {
        this.roles = roles;
        return this;
    }

    public UserEntity setReviewMangas(List<ReviewMangaEntity> reviewMangas) {
        this.reviewMangas = reviewMangas;
        return this;
    }
    public UserEntity setReviewAnimes(List<ReviewAnimeEntity> reviewAnimes) {
        this.reviewAnimes = reviewAnimes;
        return this;
    }
    public UserEntity setMangaAdded(List<MangaEntity> mangaAdded) {
        this.mangaAdded = mangaAdded;
        return this;
    }
    public UserEntity setMyMangas(List<UserMangaEntity> myMangas) {
        this.myMangas = myMangas;
        return this;
    }
    public UserEntity setMyAnimes(List<UserAnimeEntity> myAnimes) {
        this.myAnimes = myAnimes;
        return this;
    }

    public UserEntity setCreated(LocalDate created) {
        this.created = created;
        return this;
    }
    public UserEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }
    public UserEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
    public UserEntity setUsername(String username) {
        this.username = username;
        return this;
    }
    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }
    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }
}
