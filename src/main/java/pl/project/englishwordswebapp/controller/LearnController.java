package pl.project.englishwordswebapp.controller;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.project.englishwordswebapp.data.CategoryDAO;
import pl.project.englishwordswebapp.data.UserRepository;
import pl.project.englishwordswebapp.data.WordsDAO;
import pl.project.englishwordswebapp.mainService.CreateCategoryService;
import pl.project.englishwordswebapp.mainService.LearnService;
import pl.project.englishwordswebapp.model.User;
import pl.project.englishwordswebapp.service.CurrentUser;
import pl.project.englishwordswebapp.model.Category;
import pl.project.englishwordswebapp.model.Words;
import pl.project.englishwordswebapp.service.Pagination;
import pl.project.englishwordswebapp.service.WordsCounter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LearnController {

    // private UserRepository repository;
    private CategoryDAO categoryDAO;
    private WordsDAO wordsDAO;
    private CurrentUser currentUser;
    private WordsCounter wordsCounter;
    private LearnService learnService;

    @Autowired
    public LearnController(UserRepository userRepository, CategoryDAO categoryDAO, WordsDAO wordsDAO,
                           WordsCounter wordsCounter, CurrentUser currentUser, LearnService learnService) {
        // this.repository = userRepository;
        this.categoryDAO = categoryDAO;
        this.wordsDAO = wordsDAO;
        this.currentUser = currentUser;
        this.learnService = learnService;

        this.wordsCounter = wordsCounter;

    }

    @ModelAttribute
    public void currentUserSession(Model model){
        model.addAttribute("userSession", currentUser);
    }

    private List<Words> words;
    private String pageNumber;
    @GetMapping(value = {"/words/{pageNumber}", "/words"})
    public String words(@RequestParam(required = false) String wordsType,
                        Model model,
                        @ModelAttribute ("speaker") String speaker,
                        @PathVariable(required = false) String pageNumber
                        ){



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
        }

        // pagiantion
        Pagination<Words> pagination = new Pagination<>(20);

        if(wordsType != null ){
            words = learnService.getAllByCategoryName(wordsType);
            model.addAttribute("pagination", pagination.amoutOfPages(words));
            model.addAttribute("allWords", pagination.pageRows("0", words));
        }else {
            if (!words.isEmpty()){
                model.addAttribute("pagination", pagination.amoutOfPages(words));
                if(pageNumber == null){
                    model.addAttribute("allWords", pagination.pageRows(this.pageNumber, words));
                } else {
                    System.out.println(pageNumber + " after");
                    this.pageNumber = pageNumber;
                    model.addAttribute("allWords", pagination.pageRows(this.pageNumber, words));
                }
            }else {
                model.addAttribute("notChoosenTable", true);
            }
        }

        // voice
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        final VoiceManager voiceManager = VoiceManager.getInstance();
        final Voice voice = voiceManager.getVoice("kevin16");

        voice.allocate();
        voice.speak(speaker);


        return "/learn/words";
    }


}
