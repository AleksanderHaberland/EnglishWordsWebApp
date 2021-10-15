package pl.project.englishwordswebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.project.englishwordswebapp.mainService.CreateCategoryService;
import pl.project.englishwordswebapp.mainService.CreateWordService;
import pl.project.englishwordswebapp.model.Category;
import pl.project.englishwordswebapp.model.Words;
import pl.project.englishwordswebapp.service.CurrentUser;
import pl.project.englishwordswebapp.service.Pagination;

import java.util.*;

@Controller
@SessionScope
public class UserTableController {

    private CurrentUser currentUser;
    private CreateCategoryService createCategoryService;
    private CreateWordService createWordService;

    @Autowired
    public UserTableController( CurrentUser currentUser, CreateCategoryService createCategoryService, CreateWordService createWordService){
        this.currentUser = currentUser;
        this.createCategoryService = createCategoryService;
        this.createWordService = createWordService;
    }

    @ModelAttribute
    public void currentUserSession(Model model){
        model.addAttribute("userSession", currentUser);
    }

    @GetMapping("/createTable")
        public String createTable(ModelMap map, @RequestParam(value = "pageNumber", required = false) String pageNumber,
                                  @ModelAttribute String nameExist, RedirectAttributes rrat){

        if(currentUser.getLogged() == false){
            rrat.addFlashAttribute("logged", false);
            return "redirect:/login";
        }

        Pagination<Category> pagination = new Pagination<>();

        List<Category> allCategories = createCategoryService.allCategories(currentUser.getId());
        // checking duplicates ! important
        boolean duplicate = false;

            for (int i = 0; i < allCategories.size(); i++) {
                for (int j = i + 1 ; j < allCategories.size(); j++) {
                    if (allCategories.get(i).getCatename().equals(allCategories.get(j).getCatename())) {
                        createCategoryService.deleteCategory(allCategories.get(j).getCatename(), currentUser.getId());
                        duplicate = true;
                    }
                }
            }

        if(duplicate == true){
            return "redirect:/createTable";
        }

        map.addAttribute("allCate", allCategories);
        map.addAttribute("newCategory", new Category());
        map.addAttribute("tableService", createCategoryService);

        try{
            map.addAttribute("pagination", pagination.amoutOfPages(allCategories));
        }catch (NullPointerException e) {
            List<Integer> list = Arrays.asList(1);
            map.addAttribute("pagination", list);
        }

        // returning category name in table
        List<String> categoryName = new ArrayList<>();
        for(Category cat: pagination.pageRows(pageNumber, allCategories)){
            categoryName.add(cat.getCatename());
        }

        map.addAttribute("category", categoryName);

        // returning row index and amount of words in category
        Map<Integer,Category> categoryMap = new TreeMap<>();
        int x;
        if(pageNumber == null || pageNumber.isEmpty() || pageNumber.equals("1")){
            x = 0;
        }
        else {
            x = Integer.parseInt(pageNumber) * 10 - 10;
        }
        for (String cateName : categoryName) {
            x++;
            categoryMap.put(x, createCategoryService.getCategory(cateName, currentUser.getId()));
        }
        map.addAttribute("catgoryValues", categoryMap);

        return "userTable/createTable";
    }

    @PostMapping("/createTable")
    public String postCreateTable(@ModelAttribute Category category,
                                  @RequestParam String categoryButton,
                                  RedirectAttributes attributes){
        if(categoryButton.equals("create")){ // button value  == "crate"
            if(createCategoryService.getCategory(category.getCatename(), currentUser.getId()) != null){
                attributes.addFlashAttribute("nameExist", true);
            }
            else {
                createCategoryService.createCatagory(category, currentUser.getId());
            }
        }
        else{ // delete button value
            createCategoryService.deleteCategory(categoryButton, currentUser.getId());
        }
        return "redirect:/createTable";
    }

    private String currentPage;
    private Category currentCate;
    @GetMapping("/editTable")
    public String editTable(ModelMap map,
                             @RequestParam(required = false) String chosenCate,
                             @RequestParam(required = false) String edit,
                             @ModelAttribute(value = "existSame") String existSame,
                             RedirectAttributes attr){
       /* if(currentUser.getLogged() == false){
            attr.addFlashAttribute("logged", false);
            return "redirect:/login";
        } */

        Pagination pagination = new Pagination();
        List<Category> allCategories = createCategoryService.allCategories(1L);//////////////////////////
        map.addAttribute("allCategories", allCategories);

        String notSelected = "false";
        List<Words> allWordsInCategory = new ArrayList<>();

        if(chosenCate != null || currentPage != null){
            if(chosenCate != null){
                currentPage = chosenCate;
            }
            currentCate = createCategoryService.getCategory(currentPage, 1L);///////////////////////////
            allWordsInCategory = createWordService.allCateWords(currentCate.getId());
            List<Words> pageRows = pagination.pageRows("0", allWordsInCategory);
            map.addAttribute("allWords", pageRows);
            map.addAttribute("pagination", pagination.amoutOfPages(allWordsInCategory));

        }else {
            notSelected = "true";
            map.addAttribute("notSelected", notSelected);
        }
        // Editing, adding word properties
        map.addAttribute("cateSize", allWordsInCategory.size());;
        map.addAttribute("newWord", new Words());

        map.addAttribute("edit", edit);
        map.addAttribute("existSame", existSame);
        System.out.println("weszlooooooo2222 " + existSame);

        return "userTable/editTable";/////

    }

    @PostMapping("/editTable")
    public String postEditTable(@ModelAttribute Words word,
                                @RequestParam(required = false) String currentWordId,
                                RedirectAttributes attr){
        if(currentWordId.equals("create")){
            //check if exist same or is more than 25 chars
            if(createWordService.checkExist(word.getEnglish(), word.getPolish(), currentCate) != null){
                attr.addFlashAttribute("existSame", true);
            } else {
                createWordService.addWord(word, currentCate);
            }

        }else if(currentWordId.endsWith("delete")){
            createWordService.deleteWord(Integer.valueOf(currentWordId.substring(0, currentWordId.length()-6)));
        }else{
            String edit = currentWordId.substring(0,currentWordId.length()-4);
            // checking if word is not null
            if (word.getEnglish() != null) {
                if(createWordService.checkExist(word.getEnglish(), word.getPolish(), currentCate) != null){
                    System.out.println("weszlooooooo1111111");
                    attr.addAttribute("edit", edit);
                    attr.addFlashAttribute("existSame", "true");
                }else {
                    createWordService.updateWord(Integer.parseInt(edit), word);
                }
            }else{
                attr.addAttribute("edit", edit);
            }
        }
        return "redirect:/editTable";
    }


}
