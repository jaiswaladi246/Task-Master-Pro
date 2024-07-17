package com.example.todo.controller;

import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        List<Todo> todos = todoRepository.findByUsername(username);
        model.addAttribute("todos", todos);
        return "index";
    }

    @PostMapping("/add")
    public String addTodo(@RequestParam String task, Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Todo todo = new Todo();
        todo.setTask(task);
        todo.setUsername(username);
        todoRepository.save(todo);
        return "redirect:/";
    }

    @PostMapping("/complete")
    public String completeTodo(@RequestParam Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow();
        todo.setCompleted(true);
        todoRepository.save(todo);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteTodo(@RequestParam Long id) {
        todoRepository.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/update")
    public String updateTodo(@RequestParam Long id, @RequestParam String task) {
        Todo todo = todoRepository.findById(id).orElseThrow();
        todo.setTask(task);
        todoRepository.save(todo);
        return "redirect:/";
    }
}

