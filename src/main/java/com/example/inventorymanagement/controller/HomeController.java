package com.example.inventorymanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/ui/dashboard";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        model.addAttribute("hasError", error != null);
        model.addAttribute("hasLogout", logout != null);
        return "login";
    }
}

