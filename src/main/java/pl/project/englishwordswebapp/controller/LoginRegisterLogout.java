package pl.project.englishwordswebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.project.englishwordswebapp.data.UserRepository;
import pl.project.englishwordswebapp.model.User;

import java.time.LocalTime;

@Controller
public class LoginRegisterLogout {

    private UserRepository userRepository;

    @Autowired
    LoginRegisterLogout(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    
    @PostMapping("/save")
    public String save(@ModelAttribute User user, RedirectAttributes redirectAttributes, Model model){
        // point. #1
        // There is no any user with this email   ||  (!) <-before condition If there is no exist user with this email
        if((userRepository.findByEmail(user.getEmail()) == null) || !userRepository.findByEmail(user.getEmail()).getEmail().equals(user.getEmail()) ){
            //point. #2
            //  If there not exist user with this pesel (null)  ||   If there not exist user with provided pesel
            if((userRepository.findByPesel(user.getPesel()) == null) || user.getPesel() != userRepository.findByPesel(user.getPesel()).getPesel()){
                user.setDateOfFound(LocalTime.now());
                userRepository.save(user);
                redirectAttributes.addFlashAttribute("exist", "true");
                return "redirect:/login";
            }
            // #2.1 return info. that this peselExists
            else{
                redirectAttributes.addFlashAttribute("exist", "peselInUse");
            }
        } else {
            // #1.2 return info. that this emailExists
            redirectAttributes.addFlashAttribute("exist", "emailInUse");
        }
       return  "redirect:/register";
    }

    @PostMapping("/entry")
    public String entry(@ModelAttribute User user, RedirectAttributes redirectAttributes){


        return "redirect:/login";
    }
}
