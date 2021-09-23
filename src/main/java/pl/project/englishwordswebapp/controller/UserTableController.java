package pl.project.englishwordswebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import pl.project.englishwordswebapp.data.UserRepository;
import pl.project.englishwordswebapp.mainService.CreateTableService;
import pl.project.englishwordswebapp.model.Category;
import pl.project.englishwordswebapp.service.CurrentUser;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserTableController {

    private UserRepository userRepository;
    private CurrentUser currentUser;
    private CreateTableService createTableService;

    @Autowired
    public UserTableController(UserRepository userRepository, CurrentUser currentUser, CreateTableService createTableService){
        this.userRepository = userRepository;
        this.currentUser = currentUser;
        this.createTableService = createTableService;
    }

    @ModelAttribute
    public void currentUserSession(Model model){
        model.addAttribute("userSession", currentUser);
    }

    @GetMapping("/createTable")
        public String createTable(ModelMap map, @RequestParam(value = "pageNumber", required = false) String pageNumber){
        boolean moreRows = false;
        map.addAttribute("category", new Category());
        map.addAttribute("tableService", createTableService);
        map.addAttribute("rowsMoreThen10", moreRows);
        System.out.println(":o to jest request= " + pageNumber);
        map.addAttribute("pagination", createTableService.amoutOfPages());

        if(createTableService.allCategories().size() > 10){
            moreRows = true;
        }
        else {
            moreRows = false;
        }
        int pageNumberAfterParse = 0;
        if(pageNumber != null && !pageNumber.isEmpty()){
            pageNumberAfterParse = Integer.parseInt(pageNumber);
        }

        List<Category> categoryList = createTableService.allCategories();
        List<String> categoryName = new ArrayList<>();
        map.addAttribute("row", categoryName);
        if(pageNumberAfterParse > 1){
            int start = (pageNumberAfterParse*10) - 10;

            for(int i = start; i < categoryList.size(); i++ ){
                categoryName.add(categoryList.get(i).getCatename());
            }
        }
        else {
            // could no exsits
            if(categoryList.size() > 0){
                for (Category cat : categoryList){
                    categoryName.add(cat.getCatename());
                }
            }
        }


        return "userTable/createTable";
    }

    @PostMapping("/createTable")
    public String postCreateTable(@ModelAttribute Category category, @RequestParam String categoryButton){
        if(categoryButton.equals("create")){
            createTableService.createCatagory(category);
        }
        else{
            createTableService.deleteCategory(categoryButton);
        }
        return "redirect:/createTable";
    }


    @GetMapping("/editTable")
    public String editTable(    ){
        return "/userTable/editTable";
    }


}
