package pl.project.englishwordswebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.project.englishwordswebapp.data.UserRepository;
import pl.project.englishwordswebapp.service.CurrentUser;
import pl.project.englishwordswebapp.model.User;

import java.security.SecureRandom;
import java.time.LocalTime;
import java.util.Base64;

@Controller
public class
LoginRegisterLogoutController {

    private UserRepository userRepository;
    private CurrentUser currentUser;

    @Autowired
    LoginRegisterLogoutController(UserRepository userRepository, CurrentUser currentUser){
        this.userRepository = userRepository;
        this.currentUser = currentUser;
    }

    @ModelAttribute
    public void currentUserSession(Model model){
        model.addAttribute("userSession", currentUser);
    }

    @GetMapping("/login")
    public String login(Model model,
                        @ModelAttribute ("exist") String regiSuccess,
                        @ModelAttribute ("error") String nullEmailOrPass,
                        @ModelAttribute ("logged") String logged) {
        model.addAttribute("logged", logged);
        model.addAttribute("user", new User());
        return "/log/login";
    }

    @GetMapping("/register")
    public String register(Model model, @ModelAttribute("exist") String inUse){
        model.addAttribute("user", new User());
        return "/log/register";
    }


    // register
    @PostMapping("/save")
    public String save(@ModelAttribute User user, RedirectAttributes redirectAttributes, Model model){
        // point. #1
        // There is no any user with this email   ||  (!) <-before condition If there is no exist user with this email
        if((userRepository.findByEmail(user.getEmail()) == null) || !userRepository.findByEmail(user.getEmail()).getEmail().equals(user.getEmail()) ){
            //point. #2
                user.setDateOfFound(LocalTime.now());
                // authentication token generation

                userRepository.save(user);
                redirectAttributes.addFlashAttribute("exist", "true");
                return "redirect:/login";

        } else {
            // #1.2 return info. that this emailExists
            redirectAttributes.addFlashAttribute("exist", "emailInUse");
        }
       return  "redirect:/register";
    }

    // login
    @PostMapping("/entry")
    public String entry(@ModelAttribute User user, RedirectAttributes redirectAttributes){
        if(userRepository.findByEmail(user.getEmail()) == null){
            //email null
            redirectAttributes.addFlashAttribute("error", "emailWrong");
        }
        else{
            User userByEmail = userRepository.findByEmail(user.getEmail());
            if(userByEmail.getPassword().equals(user.getPassword())){
                //email correct, pass alsow
                currentUser.setId(userByEmail.getId());
                currentUser.setLogged(true);
                    return "redirect:/home";
            }
            else {
                // email correct password incorrect
                redirectAttributes.addFlashAttribute("error", "passWrong");
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(){
        currentUser.setIdAndLog(0L, false    );
        return "redirect:/home";
    }
}
