package pl.project.englishwordswebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.project.englishwordswebapp.data.UserRepository;
import pl.project.englishwordswebapp.mainService.CreateCategoryService;
import pl.project.englishwordswebapp.mainService.UserService;
import pl.project.englishwordswebapp.model.Category;
import pl.project.englishwordswebapp.model.User;
import pl.project.englishwordswebapp.service.CurrentUser;

import java.security.SecureRandom;
import java.util.*;

@Controller
public class NavbarController {

    private UserRepository userRepository;
    private CurrentUser currentUser;
    private CreateCategoryService createCategoryService;
    private UserService us;

    @Autowired
    public NavbarController(UserRepository userRepository, CurrentUser currentUser, CreateCategoryService createCategoryService, UserService us){
        this.userRepository = userRepository;
        this.currentUser = currentUser;
        this.createCategoryService = createCategoryService;
        this.us = us;
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
    public String learn(Model model){
        String problem = "";
        //logged
        if(currentUser.getId() != null){
            List<Category> currentUserCategory = new ArrayList<>();
            currentUserCategory = createCategoryService.allCategories(currentUser.getId());
            model.addAttribute("category", currentUserCategory);
            //empty datas
            if (currentUserCategory.isEmpty()){
                problem = "empty";
                model.addAttribute("empty", problem);
            }
        }else {// nologged
            problem = "notloged";
            model.addAttribute("empty", problem);

        }
        return "learn/science";
    }

    @GetMapping("/score")
    public String score(){
        return "score";
    }

    @GetMapping("/account")
    public String account(@ModelAttribute(value = "select")String select, Model model){

        model.addAttribute("user", new User());
        switch (select){
            case "userdata":
                model.addAttribute("userAccountDatas", us.getAccountInfo(currentUser.getId().intValue()));
                break;
            case "succesPassChange":
                model.addAttribute("successPassChange", true);
                break;
            case "tokenExist":
                //give false or true if token exist
                model.addAttribute("tokenExist", us.tokenExist(currentUser.getId().intValue()));

                // if token exist
                if(us.tokenExist(currentUser.getId().intValue())){
                    // give TokenAPI
                    model.addAttribute("tokenAPI", us.getAccountInfo(currentUser.getId().intValue()).getToken());
                }
            break;
        }

        return "userAccount/account";
    }

    @PostMapping("/account")
    public String accountPost(@ModelAttribute User user, RedirectAttributes redirectAttributes,
                              @RequestParam(required = false, value = "info") String info){
        if(info == null){
            us.changePassword(currentUser.getId(), user.getPassword());
            redirectAttributes.addAttribute("select", "succesPassChange");
        } else {
           us.crateToken(currentUser.getId().intValue());
           redirectAttributes.addAttribute("select","tokenExist");
        }

        return "redirect:/account";
    }
}
