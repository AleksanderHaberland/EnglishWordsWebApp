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
import pl.project.englishwordswebapp.service.Pagination;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
        Pagination<Category> pagination = new Pagination<>();
        map.addAttribute("category", new Category());
        map.addAttribute("tableService", createTableService);
        map.addAttribute("pagination", pagination.amoutOfPages(createTableService.allCategories()));

        // returning rows of table
        List<String> categoryName = new ArrayList<>();
        for(Category cat: createTableService.allCategories()){
            categoryName.add(cat.getCatename());
        }
        map.addAttribute("categoryName", pagination.pageRow(pageNumber,categoryName));

        // returning row index and amount of words in category
        Map<Integer,Category> categoryMap = new TreeMap<>();
        for (String cateName : pagination.pageRow(pageNumber,categoryName)) {
            categoryMap.put(Integer.valueOf(createTableService.getCategory(cateName).getId().intValue()), createTableService.getCategory(cateName));
        }
        map.addAttribute("categoryRow", categoryMap);

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
