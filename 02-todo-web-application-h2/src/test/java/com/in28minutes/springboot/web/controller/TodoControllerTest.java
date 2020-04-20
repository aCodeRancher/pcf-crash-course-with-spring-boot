package com.in28minutes.springboot.web.controller;

import com.in28minutes.springboot.web.model.Todo;
import com.in28minutes.springboot.web.security.SecurityConfiguration;
import com.in28minutes.springboot.web.service.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
@AutoConfigureMockMvc
@Import({SecurityConfiguration.class})
class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TodoRepository repository;

    @Test
    @WithMockUser(roles="USER")
    public void showTodos() throws Exception{
        when(repository.findByUser(anyString())).thenReturn(new ArrayList<Todo>());
        mockMvc.perform(get("/list-todos"))
                .andExpect(view().name("list-todos"));
        verify(repository,times(1)).findByUser(anyString());
    }

    @Test
    @WithMockUser(roles="STUDENT")
    public void showTodos_notAuthorized() throws Exception{
         mockMvc.perform(get("/list-todos"))
                .andExpect(status().isForbidden());
      }

      @Test
      @WithMockUser(roles="USER")
     public void showAddTodoPage() throws Exception{
        MvcResult result =  mockMvc.perform(get("/add-todo"))
                 .andExpect(model().attributeExists("todo"))
                 .andExpect(view().name("todo")).andReturn();
        Map<String, Object> modelAttributes = result.getModelAndView().getModel();
        Todo todo =   (Todo)modelAttributes.get("todo");
        assertTrue(todo.getDesc().equals("Default Desc"));

      }

    @Test
    @WithMockUser(roles="USER")
    public void deleteTodo() throws Exception{
        doNothing().when(repository).deleteById(anyInt());
        mockMvc.perform(get("/delete-todo?id=100"))
                .andExpect(redirectedUrl("/list-todos"));
        verify(repository,times(1)) .deleteById(100);
    }

    @Test
    @WithMockUser(roles="USER")
    public void showUpdateTodoPage() throws Exception{
        Todo  todo = mock(Todo.class);
        when(repository.findById(anyInt())).thenReturn(Optional.of(todo));
        MvcResult result  = mockMvc.perform(get("/update-todo?id=100"))
                .andExpect(status().is2xxSuccessful()).andReturn();
        Map<String, Object> model = result.getModelAndView().getModel();
        assertTrue(model.get("todo")== todo);
        verify(repository,times(1)).findById(100);
    }

    @Test
    @WithMockUser(roles="USER")
    public void updateTodo() throws Exception{
        Todo todo = new Todo(100, "in28minutes","Default Desc", new Date(), false);
        when(repository.save(any(Todo.class))).thenReturn(todo);
        mockMvc.perform(post("/update-todo").flashAttr("todo", todo))
                .andExpect(redirectedUrl("/list-todos"));
        verify(repository,times(1)).save(any(Todo.class));
    }

    @Test
    @WithMockUser(roles="USER")
    public void addTodo() throws Exception{
        Todo todo = new Todo(100, "in28minutes","Default Desc", new Date(), false);
        when(repository.save(any(Todo.class))).thenReturn(todo);
        mockMvc.perform(post("/update-todo").flashAttr("todo", todo))
                .andExpect(redirectedUrl("/list-todos"));
        verify(repository,times(1)).save(any(Todo.class));
    }
}