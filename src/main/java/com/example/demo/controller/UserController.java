package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    
    @PostMapping("/register")
    @CacheEvict(value = "users", allEntries = true)
    public String registerUser(User user) {
        userService.saveUser(user);
        return "redirect:/register?success";
    }
    
    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        return "admin";
    }
    
    @PostMapping("/admin/addUser")
    @CacheEvict(value = "users", allEntries = true)
    public String addUserFromAdmin(User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }
}