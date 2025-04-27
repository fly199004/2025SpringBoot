package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        User user = userService.findByUsernameAndPassword(username, password);
        if (user != null) {
            return "redirect:/admin";
        } else {
            model.addAttribute("error", "用户名或密码错误");
            return "login";
        }
    }
    
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    
    @PostMapping("/register")
    @CacheEvict(value = "users", allEntries = true)
    public String registerUser(User user, Model model) {
        try {
            userService.saveUser(user);
            return "redirect:/register?success";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
            return "register";
        }
    }
    
    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        return "admin";
    }
    
    @PostMapping("/admin/addUser")
    @CacheEvict(value = "users", allEntries = true)
    public String addUserFromAdmin(User user, Model model) {
        try {
            userService.saveUser(user);
            return "redirect:/admin";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("newUser", user);
            return "admin";
        }
    }
}