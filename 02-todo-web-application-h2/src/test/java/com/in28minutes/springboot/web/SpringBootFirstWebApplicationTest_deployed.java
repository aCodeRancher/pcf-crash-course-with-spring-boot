package com.in28minutes.springboot.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SpringBootFirstWebApplicationTest_deployed {

    @Autowired
    MockMvc mockMvc;

    private String myRoute =  "todo-web-application-h2-hma2-101.cfapps.io";
    @Test
    public void login() throws Exception {

        mockMvc.perform(formLogin("http://"+myRoute+"/login").user("in28minutes").password("dummy"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("in28minutes"));
        mockMvc.perform(logout()).andExpect(redirectedUrl("/login?logout"));
    }

    @Test
    public void login_notAuthenticated() throws Exception {
        mockMvc.perform(formLogin("http://"+myRoute+"/login").user("in28minutes").password("wrong"))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login?error"));

    }

}