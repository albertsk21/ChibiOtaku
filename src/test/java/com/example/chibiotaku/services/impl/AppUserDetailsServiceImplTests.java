package com.example.chibiotaku.services.impl;

import com.example.chibiotaku.domain.entities.RoleEntity;
import com.example.chibiotaku.domain.entities.UserEntity;
import com.example.chibiotaku.domain.enums.RoleUserEnum;
import com.example.chibiotaku.repo.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class AppUserDetailsServiceImplTests {


    private UserEntity testUser;
    private RoleEntity adminRole, userRole;

    private AppUserDetailsServiceImpl underTest;

    @Mock
    private UserRepository mockUserRepository;


    @BeforeEach
    void setUp(){
        this.underTest = new AppUserDetailsServiceImpl(mockUserRepository);

        adminRole = new RoleEntity().setRole(RoleUserEnum.ADMIN);
        userRole = new RoleEntity().setRole(RoleUserEnum.USER);

        testUser = new UserEntity()
        .setUsername("albert")
        .setEmail("albert@example.com")
        .setPassword("security");
        testUser.setRoles(List.of(adminRole, userRole));
    }

    @Test
    void loadUserByUsername_userNotFound() {
        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> underTest.loadUserByUsername("usernameInvalid")
        );
    }
    @Test
    void loadUserByUsername_userFound() {
        String testUsername = "albert";
        Mockito.when(this.mockUserRepository
                .findUserEntityByUsername(testUsername)).thenReturn(Optional.of(testUser));


       UserDetails actual =  this.underTest.loadUserByUsername(testUsername);
       String expectedRoles = "ROLE_ADMIN, ROLE_USER";
       String actualRoles = actual.getAuthorities().stream().map(GrantedAuthority::getAuthority)
               .collect(Collectors.joining(", "));
       String expectedPassword = "security";
       int expectedRolesSize = 2;
       Assertions.assertEquals(testUsername,actual.getUsername());
       Assertions.assertEquals(expectedPassword,actual.getPassword());
       Assertions.assertEquals(actual.getAuthorities().size(),expectedRolesSize);
       Assertions.assertEquals(expectedRoles,actualRoles);

    }
}