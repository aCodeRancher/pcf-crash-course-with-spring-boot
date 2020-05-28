package com.in28minutes.springboot.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class SpringBootFirstWebApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Test
	public void login() throws Exception {
		mockMvc.perform(formLogin().user("in28minutes").password("dummy"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"))
				.andExpect(authenticated().withUsername("in28minutes"));
		mockMvc.perform(logout()).andExpect(redirectedUrl("/login?logout"));


	}

	@Test
	public void login_notAuthenticated() throws Exception {
		mockMvc.perform(formLogin().user("in28minutes").password("wrong"))
				.andExpect(unauthenticated())
				.andExpect(redirectedUrl("/login?error"));

	}

	@Test
	@WithMockUser(roles="USER")
	public void todoList() throws Exception {
		mockMvc.perform(get("/list-todos"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(view().name("list-todos"))
				.andExpect(authenticated().withRoles("USER"));
	}

	@Test
	@WithMockUser(roles="STUDENT")
	public void todoList_notAuthorized() throws Exception {
		mockMvc.perform(get("/list-todos"))
				.andExpect(status().isForbidden());

	}
}
