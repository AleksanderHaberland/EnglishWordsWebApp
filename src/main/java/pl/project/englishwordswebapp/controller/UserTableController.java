package pl.project.englishwordswebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.project.englishwordswebapp.data.UserRepository;
import pl.project.englishwordswebapp.mainService.CreateTableService;
import pl.project.englishwordswebapp.model.Category;
import pl.project.englishwordswebapp.service.CurrentUser;
import pl.project.englishwordswebapp.service.Pagination;

import java.util.*;

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
        public String createTable(ModelMap map, @RequestParam(value = "pageNumber", required = false) String pageNumber, @ModelAttribute String nameExist, RedirectAttributes rrat){

        if(currentUser.getLogged() == false){
            rrat.addFlashAttribute("logged", false);
            return "redirect:/login";
        }

        Pagination<Category> pagination = new Pagination<>();

        List<Category> allCategories = createTableService.allCategories(currentUser.getId());
        // checking duplicates ! important
        boolean duplicate = false;

            for (int i = 0; i < allCategories.size(); i++) {
                for (int j = i + 1 ; j < allCategories.size(); j++) {
                    if (allCategories.get(i).getCatename().equals(allCategories.get(j).getCatename())) {
                        createTableService.deleteCategory(allCategories.get(j).getCatename(), currentUser.getId());
                        duplicate = true;
                    }
                }
            }

        if(duplicate == true){
            return "redirect:/createTable";
        }

        map.addAttribute("allCate", allCategories);
        map.addAttribute("newCategory", new Category());
        map.addAttribute("tableService", createTableService);

        try{
            map.addAttribute("pagination", pagination.amoutOfPages(allCategories));
        }catch (NullPointerException e) {
            List<Integer> list = Arrays.asList(1);
            map.addAttribute("pagination", list);
        }

        // returning category name in table
        List<String> categoryName = new ArrayList<>();
        for(Category cat: allCategories){
            categoryName.add(cat.getCatename());
        }
        map.addAttribute("category", pagination.pageRows(pageNumber, categoryName));

        // returning row index and amount of words in category
        Map<Integer,Category> categoryMap = new TreeMap<>();
        int x;
        if(pageNumber == null || pageNumber.isEmpty() || pageNumber.equals("1")){
            x = 0;
        }
        else {
            x = Integer.parseInt(pageNumber) * 10 - 10;
        }
        for (String cateName : pagination.pageRows(pageNumber,categoryName)) {
            x++;
            categoryMap.put(x, createTableService.getCategory(cateName, currentUser.getId()));
        }
        map.addAttribute("catgoryValues", categoryMap);

        return "userTable/createTable";
    }

    @PostMapping("/createTable")
    public String postCreateTable(@ModelAttribute Category category, @RequestParam String categoryButton, RedirectAttributes attributes){
        if(categoryButton.equals("create")){ // button value  == "crate"
            if(createTableService.getCategory(category.getCatename(), currentUser.getId()) != null){
                attributes.addFlashAttribute("nameExist", true);
            }
            else {
                createTableService.createCatagory(category, currentUser.getId());
            }
        }
        else{ // delete button value
            createTableService.deleteCategory(categoryButton, currentUser.getId());
        }
        return "redirect:/createTable";
    }


    @GetMapping("/editTable")
    public String editTable(RedirectAttributes rrat){
        if(currentUser.getLogged() == false){
            rrat.addFlashAttribute("logged", false);
            return "redirect:/login";
        }
        return "/userTable/editTable";
    }


}
