package pl.project.englishwordswebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.project.englishwordswebapp.data.UserRepository;
import pl.project.englishwordswebapp.mainService.CreateCategoryService;
import pl.project.englishwordswebapp.mainService.LearnService;
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
    private UserService us;
    private LearnService learnService;
    @Autowired
    public NavbarController(UserRepository userRepository, CurrentUser currentUser, UserService us, LearnService learnService){
        this.userRepository = userRepository;
        this.currentUser = currentUser;
        this.us = us;
        this.learnService = learnService;
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
        if (currentUser.getId() == null){
            List<Category> currentUserCategory = new ArrayList<>();
            currentUserCategory = learnService.getAllCategories(1);

            List<Category> baseCategory = new ArrayList<>();
            for(int y = 0; y < 7; y++){
                baseCategory.add(currentUserCategory.get(y));
            }
            model.addAttribute("baseCategory", baseCategory);
            problem = "notloged";
            model.addAttribute("empty", problem);
        }
        if(currentUser.getId() != null){
            List<Category> currentUserCategory = new ArrayList<>();
            currentUserCategory = learnService.getAllCategories(currentUser.getId());

            List<Category> baseCategory = new ArrayList<>();
            for(int y = 0; y < 7; y++){
                baseCategory.add(currentUserCategory.get(y));
            }
            List<Category> userCategory = currentUserCategory;
            for(int x = 0; x < 7; x++){
                userCategory.remove(0);
            }

            model.addAttribute("baseCategory", baseCategory);
            model.addAttribute("userCategory", userCategory);
            //empty datas
            if (currentUserCategory.isEmpty()){
                problem = "empty";
                model.addAttribute("empty", problem);
            }
        }else {// nologed
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

    @GetMapping("/documentation")
    public String doc(){
        return "restdoc";
    }
}
