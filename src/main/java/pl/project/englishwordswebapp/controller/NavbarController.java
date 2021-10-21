package pl.project.englishwordswebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.project.englishwordswebapp.data.UserRepository;
import pl.project.englishwordswebapp.service.CurrentUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public class NavbarController {

    private UserRepository userRepository;
    private CurrentUser currentUser;

    @Autowired
    public NavbarController(UserRepository userRepository, CurrentUser currentUser){
        this.userRepository = userRepository;
        this.currentUser = currentUser;
    }

    @ModelAttribute
    public void currentUserSession(Model model){
        model.addAttribute("userSession", currentUser);
    }
    public List<Object> lista(List<Object> lista){
        Collections.reverse(lista);
        return lista;
    }

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


}
