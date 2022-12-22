package com.example.chibiotaku.web;

import com.example.chibiotaku.domain.biding.UserRegisterBM;
import com.example.chibiotaku.domain.dtos.UserDto;
import com.example.chibiotaku.services.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class UserControllerTests {


    @MockBean
    private UserService mockUserService;
    @MockBean
    private UserAnimeService mockUserAnimeService;
    @MockBean
    private AnimeService mockAnimeService;
    @MockBean
    private UserMangaService mockUserMangaService;
    @MockBean
    private MangaService mockMangaService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginGET_ShouldHaveViewLogin_PrincipalIsNull() throws Exception {


        String expectedViewName = "login";
        mockMvc.perform(get("/users/login")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedViewName));

    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void loginGET_ShouldRedirectToHomePage_PrincipalIsNotNull() throws Exception {
        mockMvc.perform(get("/users/login")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
    @Test
    void registerGET_PrincipalIsNull_ShouldHaveViewNameRegister() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void registerGET_PrincipalIsNotNull_ShouldRedirectUrl() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
    @Test
    void registerPOST_ShouldWorksCorrectlyWithoutErrors() throws Exception {


        ArgumentCaptor<UserDto> argumentCaptor = ArgumentCaptor.forClass(UserDto.class);

        UserRegisterBM userRegisterBMTest = new UserRegisterBM()
                .setEmail("test@example.com")
                .setUsername("testUsername")
                .setFirstName("test first name")
                .setLastName("test last name")
                .setPassword("Test.none123")
                .setConfirmPassword("Test.none123");
        mockMvc.perform(post("/users/register")
                .param("firstName",userRegisterBMTest.getFirstName())
                .param("lastName",userRegisterBMTest.getLastName())
                .param("username",userRegisterBMTest.getUsername())
                .param("email",userRegisterBMTest.getEmail())
                .param("password",userRegisterBMTest.getPassword())
                .param("confirmPassword",userRegisterBMTest.getConfirmPassword())
                .with(csrf()))
                .andExpect(status().is3xxRedirection());


        Mockito.verify(this.mockUserService).registerUser(argumentCaptor.capture());


        Assertions.assertEquals(userRegisterBMTest.getPassword(),argumentCaptor.getValue().getPassword());
        Assertions.assertEquals(userRegisterBMTest.getEmail(),argumentCaptor.getValue().getEmail());
        Assertions.assertEquals(userRegisterBMTest.getFirstName(),argumentCaptor.getValue().getFirstName());
        Assertions.assertEquals(userRegisterBMTest.getLastName(),argumentCaptor.getValue().getLastName());
        Assertions.assertEquals(userRegisterBMTest.getUsername(),argumentCaptor.getValue().getUsername());
        System.out.println();



    }
    @Test
    void registerPOST_ShouldWorksCorrectlyWithErrors_PasswordsIsNotTheSame() throws Exception {


        UserRegisterBM userRegisterBMTest = new UserRegisterBM()
                .setEmail("test@example.com")
                .setUsername("testUsername")
                .setFirstName("test first name")
                .setLastName("test last name")
                .setPassword("testpw")
                .setConfirmPassword("none");
        mockMvc.perform(post("/users/register")
                        .param("firstName",userRegisterBMTest.getFirstName())
                        .param("lastName",userRegisterBMTest.getLastName())
                        .param("username",userRegisterBMTest.getUsername())
                        .param("email",userRegisterBMTest.getEmail())
                        .param("password",userRegisterBMTest.getPassword())
                        .param("confirmPassword",userRegisterBMTest.getConfirmPassword())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

    }

    @Test
    void registerPOST_ShouldWorksCorrectlyWithErrors_HaveAFieldNull() throws Exception {
        UserRegisterBM userRegisterBMTest = new UserRegisterBM()
                .setEmail("test@example.com")
                .setUsername("testUsername")
                .setFirstName("test first name")
                .setPassword("testpw")
                .setConfirmPassword("testpw");
        mockMvc.perform(post("/users/register")
                        .param("firstName",userRegisterBMTest.getFirstName())
                        .param("lastName",userRegisterBMTest.getLastName())
                        .param("username",userRegisterBMTest.getUsername())
                        .param("email",userRegisterBMTest.getEmail())
                        .param("password",userRegisterBMTest.getPassword())
                        .param("confirmPassword",userRegisterBMTest.getConfirmPassword())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }
}