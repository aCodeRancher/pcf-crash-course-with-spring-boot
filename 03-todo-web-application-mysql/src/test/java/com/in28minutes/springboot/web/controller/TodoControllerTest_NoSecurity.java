package com.in28minutes.springboot.web.controller;

import com.in28minutes.springboot.web.model.Todo;
import com.in28minutes.springboot.web.service.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@WebMvcTest(TodoController.class)
@AutoConfigureMockMvc
public class TodoControllerTest_NoSecurity {


    @Autowired
    TodoController todoController;

    @MockBean
    TodoRepository repository;

    @Test
    public void showTodos_skipSecurity(){
        List<Todo> todos = new ArrayList<>();
        todos.add( new Todo(100, "in30minutes", "Learning spring boot", new Date(), true));
        when(repository.findByUser("in30minutes")).thenReturn(todos);
        ModelMap modelMap = new ModelMap();
        String view = todoController.showTodos(modelMap);
        assertTrue(view.equals("list-todos"));
        assertTrue(modelMap.containsKey("todos"));
        List<Todo> todosOfModelMap = (List)modelMap.get("todos");
        assertTrue(todosOfModelMap.get(0).getUser().equals("in30minutes"));
        verify(repository, times(1)).findByUser("in30minutes");
    }
}
