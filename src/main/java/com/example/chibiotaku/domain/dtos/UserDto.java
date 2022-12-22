package com.example.chibiotaku.domain.dtos;

import com.example.chibiotaku.domain.entities.UserEntity;

import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link UserEntity} entity
 */
public class UserDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private LocalDate created;

    public UserDto() {
    }



    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public LocalDate getCreated() {
        return created;
    }

    public UserDto setCreated(LocalDate created) {
        this.created = created;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto entity = (UserDto) o;
        return Objects.equals(this.firstName, entity.firstName) &&
                Objects.equals(this.lastName, entity.lastName) &&
                Objects.equals(this.username, entity.username) &&
                Objects.equals(this.email, entity.email) &&
                Objects.equals(this.password, entity.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, username, email, password);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "firstName = " + firstName + ", " +
                "lastName = " + lastName + ", " +
                "username = " + username + ", " +
                "email = " + email + ", " +
                "password = " + password + ")";
    }
}