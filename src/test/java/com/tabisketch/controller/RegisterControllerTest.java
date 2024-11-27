package com.tabisketch.controller;

import com.tabisketch.bean.form.RegisterForm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Stream;

@WebMvcTest(RegisterController.class)
public class RegisterControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void getが動作するか() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register/index"));
    }

    @ParameterizedTest
    @MethodSource("ProvideForms")
    @WithMockUser
    public void postが動作するか(final RegisterForm registerForm) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .flashAttr("registerForm", registerForm)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                ).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/register/send"));
    }

    public static Stream<RegisterForm> ProvideForms() {
        final var f1 = new RegisterForm("example@mail.com", "password", "password");
        return Stream.of(f1);
    }

    @ParameterizedTest
    @MethodSource("ProvideEmptyForms")
    @WithMockUser
    public void フォームがバリデーションエラーになるか(final RegisterForm registerForm) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .flashAttr("registerForm", registerForm)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(MockMvcResultMatchers.model().attributeExists("registerForm"))
                .andExpect(MockMvcResultMatchers.model().attribute("registerForm", registerForm))
                .andExpect(MockMvcResultMatchers.view().name("register/index"));
    }

    private static Stream<RegisterForm> ProvideEmptyForms() {
        final var f1 = new RegisterForm();
        final var f2 = new RegisterForm("", "password", "password");
        final var f3 = new RegisterForm("example@mail.com", "", "password");
        final var f4 = new RegisterForm("example@mail.com", "password", "");
        final var f5 = new RegisterForm("example@mail.com", "password", "pass");
        return Stream.of(f1, f2, f3, f4, f5);
    }

    @Test
    @WithMockUser
    public void sendが動作するか() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register/send"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register/send"));
    }
}