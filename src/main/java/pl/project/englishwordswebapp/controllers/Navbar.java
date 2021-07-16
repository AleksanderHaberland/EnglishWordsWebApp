package pl.project.englishwordswebapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Navbar {

    @GetMapping("/home")
    public String home(){
        return "home";
    }

    @GetMapping("/science")
    public String learn(){
        return "science";
    }

    @GetMapping("/score")
    public String score(){
        return "score";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/logout")
    public String lg(){
        return "redirect:/home";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/x")
    public String x(){
        return "x";
    }


}
