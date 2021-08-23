package pl.project.englishwordswebapp.controller;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.project.englishwordswebapp.data.UserRepository;
import pl.project.englishwordswebapp.model.CurrentUser;
import pl.project.englishwordswebapp.model.User;

@Controller
public class Navbar {

    private UserRepository userRepository;
    private CurrentUser currentUser;

    @Autowired
    public Navbar(UserRepository userRepository, CurrentUser currentUser){
        this.userRepository = userRepository;
        this.currentUser = currentUser;
    }

    @ModelAttribute
    public void currentUserSession(Model model){
        model.addAttribute("userSession", currentUser);
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

    @GetMapping("/register")
    public String register(Model model, @ModelAttribute("exist") String inUse){
        model.addAttribute("user", new User());

        return "register";
    }

}
