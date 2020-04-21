package com.in28minutes.springboot.web.controller;

import com.in28minutes.springboot.web.model.Todo;
import com.in28minutes.springboot.web.service.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TodoController todoController;


    @Test
    @WithMockUser(username="in30minutes", password="dummy", roles="USER")
    public void showTodos() throws Exception {
      TodoRepository repository = todoController.getRepository();
      List<Todo> todos = repository.findByUser("in30minutes");
      mockMvc.perform(get("/list-todos/"))
               .andExpect(model().attribute("todos", todos ))
              .andExpect(view().name("list-todos"));
    }
}