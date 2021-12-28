package pl.project.englishwordswebapp.controller;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

        LocalTime time = LocalTime.of(10,43,12);
        User u = new User("alek", "haber", "arric@wp.pl", "a", time);
        Category c = new Category("jobs");

        u.addCategory(c);
        userRepository.save(u);

        Words w = new Words("job", "praca");
        Words w3 = new Words("accountant", "księgowy");

        w.setCategory(c);
        wordsDAO.save(w);

        w3.setCategory(c);
        wordsDAO.save(w3);

        Words w2 = new Words("animal", "zwierze");
        Category c2 = new Category("animals");

        u.addCategory(c2);
        categoryDAO.save(c2);
        w2.setCategory(c2);
        wordsDAO.save(w2);
    }

    @ModelAttribute
    public void currentUserSession(Model model){
        model.addAttribute("userSession", currentUser);
    }

    private String categoryWord;

    @GetMapping("/words")
    public String words(@RequestParam(required = false) String wordsType, Model model, @ModelAttribute ("speaker") String speaker){
        if(currentUser.getLogged() == false){
            return "redirect:/science";
        }

        String problem = "";
        //logged
        if(currentUser.getId() != null){
            List<Category> currentUserCategory = new ArrayList<>();
            currentUserCategory = learnService.getAllCategories(currentUser.getId());
            model.addAttribute("category", currentUserCategory);
            //empty datas
            if (currentUserCategory.isEmpty()){
                problem = "empty";
                model.addAttribute("empty", problem);
            }
        }

        if(wordsType != null ){
            model.addAttribute("allWords", learnService.getAllByCategoryName(wordsType));
            categoryWord = wordsType;
        }else {
            if (categoryWord != null){
                model.addAttribute("allWords", learnService.getAllByCategoryName(categoryWord));
            }else {
                model.addAttribute("notChoosenTable", true);
            }
        }


        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        final VoiceManager voiceManager = VoiceManager.getInstance();
        final Voice voice = voiceManager.getVoice("kevin16");

        voice.speak(speaker);
        return "/learn/words";
    }


}
