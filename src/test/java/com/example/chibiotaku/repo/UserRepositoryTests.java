package com.example.chibiotaku.repo;

import com.example.chibiotaku.domain.entities.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
class UserRepositoryTests {


    @Autowired
    private UserRepository underTest;

    private final String USERNAME_FIRST = "tester1";
    private final String USERNAME_SECOND = "tester2";
    private final String EMAIL_FIRST = "test1@example.com";
    private final String EMAIL_SECOND = "test2@example.com";
    @BeforeEach
    void setUp(){
        UserEntity user1 = new UserEntity()
                .setUsername(USERNAME_FIRST)
                .setEmail(EMAIL_FIRST)
                .setFirstName("test1")
                .setLastName("test1")
                .setPassword("123");
        UserEntity user2 = new UserEntity()
                .setUsername(USERNAME_SECOND)
                .setEmail(EMAIL_SECOND)
                .setFirstName("test2")
                .setLastName("test2")
                .setPassword("123");
;
        this.underTest.saveAll(List.of(user1,user2));
    }

    @AfterEach
    void tearDown(){
        this.underTest.deleteAll();
    }



    @Test
    void findUserEntityByUsername_ShouldReturnCorrectUser(){
        Optional<UserEntity> user = this.underTest.findUserEntityByUsername(USERNAME_FIRST);
        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals(user.get().getUsername(), USERNAME_FIRST);
        Assertions.assertEquals(user.get().getEmail(), EMAIL_FIRST);
    }   @Test
    void findUserEntityByUsername_ShouldReturnEmpty(){
        String currentUsernameTest = "testing";
        Optional<UserEntity> user = this.underTest.findUserEntityByUsername(currentUsernameTest);
        Assertions.assertTrue(user.isEmpty());
    }

    @Test
    void findUserEntityByEmail_ShouldReturnCorrectUser(){
        Optional<UserEntity> user = this.underTest.findUserEntityByEmail(EMAIL_FIRST);
        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals(user.get().getUsername(), USERNAME_FIRST);
        Assertions.assertEquals(user.get().getEmail(), EMAIL_FIRST);
    }
    @Test
    void findUserEntityByEmail_ShouldReturnEmptyUser(){
        String currentEmailTest = "testing@example.com";
        Optional<UserEntity> user = this.underTest.findUserEntityByEmail(currentEmailTest);
        Assertions.assertTrue(user.isEmpty());
    }

}
