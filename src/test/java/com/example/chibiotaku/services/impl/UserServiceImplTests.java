package com.example.chibiotaku.services.impl;

import com.example.chibiotaku.domain.dtos.UserDto;
import com.example.chibiotaku.domain.entities.RoleEntity;
import com.example.chibiotaku.domain.entities.UserEntity;
import com.example.chibiotaku.domain.enums.RoleUserEnum;
import com.example.chibiotaku.repo.RoleRepository;
import com.example.chibiotaku.repo.UserRepository;
import com.example.chibiotaku.services.UserService;
import com.example.chibiotaku.util.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.management.relation.Role;
import java.util.Optional;

import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTests {


    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private RoleRepository mockRoleRepository;


    private final UserEntity testUser = new UserEntity()
            .setFirstName("albert")
            .setLastName("jack")
            .setPassword("security")
            .setEmail("albert@example.com");
    private UserService underTest;
    @BeforeEach
    void setUp() {
        this.underTest = new UserServiceImpl(
                mockUserRepository,
                mockRoleRepository,
                new ModelMapper(),
                new BCryptPasswordEncoder()
        );
    }

    @Test
    void findUserByUsername_ObjectNotFoundExecption(){
        String username = "unknown username";
        Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> {this.underTest.findUserByUsername(username);}
        );
    }
    @Test
    void findUserByUsername_ShouldReturnUserDtoCorrectly(){
        Mockito.when(this.mockUserRepository.findUserEntityByUsername(testUser.getUsername()))
                .thenReturn(Optional.of(testUser));

        UserDto actual = this.underTest.findUserByUsername(testUser.getUsername());
        Assertions.assertEquals(testUser.getUsername(),actual.getUsername());
        Assertions.assertEquals(testUser.getFirstName(),actual.getFirstName());
        Assertions.assertEquals(testUser.getLastName(),actual.getLastName());
        Assertions.assertEquals(testUser.getEmail(),actual.getEmail());
    }

    @Test
    void registerUser_ObjectNotFound(){
        UserDto givenUser = new UserDto()
                .setFirstName("albert")
                .setLastName("jack")
                .setPassword("security")
                .setEmail("albert@example.com");

        Assertions.assertThrows(ObjectNotFoundException.class,
        ()-> {this.underTest.registerUser(givenUser);});
    }

    @Test
    void registerUser_ShouldSaveUserEntityCorrectly(){

        RoleEntity role =  new RoleEntity()
                .setRole(RoleUserEnum.USER);

        Mockito.when(this.mockRoleRepository.findRoleEntityByRole(RoleUserEnum.USER))
                .thenReturn(Optional.of(role));

        UserDto givenUser = new UserDto()
                .setUsername("alb3rtsk21")
                .setFirstName("albert")
                .setLastName("jack")
                .setPassword("security")
                .setEmail("albert@example.com");


        ArgumentCaptor<UserEntity> argumentCaptor = ArgumentCaptor.forClass(UserEntity.class);

        this.underTest.registerUser(givenUser);


        int expectedSizeRoles = 1;
        verify(this.mockUserRepository).save(argumentCaptor.capture());
        Assertions.assertEquals(givenUser.getFirstName(),argumentCaptor.getValue().getFirstName());
        Assertions.assertEquals(givenUser.getUsername(),argumentCaptor.getValue().getUsername());
        Assertions.assertEquals(givenUser.getLastName(),argumentCaptor.getValue().getLastName());
        Assertions.assertEquals(givenUser.getEmail(),argumentCaptor.getValue().getEmail());
        Assertions.assertEquals(expectedSizeRoles,argumentCaptor.getValue().getRoles().size());
        Assertions.assertEquals(role,argumentCaptor.getValue().getRoles().get(0));
    }
}