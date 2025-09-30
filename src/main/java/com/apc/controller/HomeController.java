package com.apc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/login.html";
    }
    
    @GetMapping("/login")
    public String login() {
        return "redirect:/login.html";
    }
    
    @GetMapping("/register")
    public String register() {
        return "redirect:/register.html";
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "redirect:/dashboard.html";
    }

    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "OK - Application is running";
    }
}