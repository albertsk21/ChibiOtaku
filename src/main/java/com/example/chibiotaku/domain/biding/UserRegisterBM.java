package com.example.chibiotaku.domain.biding;

import com.example.chibiotaku.util.annotations.IsPasswordValid;
import com.example.chibiotaku.util.annotations.UniqueEmail;
import com.example.chibiotaku.util.annotations.UniqueUsername;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserRegisterBM {


    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;


    @NotEmpty(message = "Email cannot be empty")
    @UniqueEmail(message = "Email already exist")
    public String getEmail() {
        return email;
    }

    @Size(min = 3, message = "First name must have min 3 letters")
    public String getFirstName() {
        return firstName;
    }

    @Size(min = 3, message = "Last name must have min 3 letters")
    public String getLastName() {
        return lastName;
    }

    @UniqueUsername(message = "Username already exist")
    @NotEmpty(message = "Username cannot be empty")
    public String getUsername() {
        return username;
    }

    @NotEmpty(message = "Password cannot be empty")

    @IsPasswordValid(message = "Password needs to have a lowercase character,\n" +
            "uppercase character\n" +
            "between 8 and 20 characters\n" +
            "\n" +
            "at least one digit")
    public String getPassword() {
        return password;
    }


    @NotEmpty(message = "Confirm password cannot be empty")
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public UserRegisterBM setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserRegisterBM setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserRegisterBM setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserRegisterBM setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserRegisterBM setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserRegisterBM setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }
}
