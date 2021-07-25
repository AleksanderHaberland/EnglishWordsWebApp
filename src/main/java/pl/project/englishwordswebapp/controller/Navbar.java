package pl.project.englishwordswebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.project.englishwordswebapp.data.UserRepository;
import pl.project.englishwordswebapp.model.User;

@Controller
public class Navbar {

    private UserRepository userRepository;

    @Autowired
    public Navbar(UserRepository userRepository){
        this.userRepository = userRepository;
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

    @GetMapping("/login")
    public String login(Model model, @ModelAttribute ("exist") String regiSuccess, @ModelAttribute ("error") String nullEmailOrPass) {
        model.addAttribute("user", new User());
        return "login";
    }


    @GetMapping("/logout")
    public String lg(){
        return "redirect:/home";
    }

    @GetMapping("/register")
    public String register(Model model, @ModelAttribute("exist") String inUse){
        model.addAttribute("user", new User());

        return "register";
    }

}
