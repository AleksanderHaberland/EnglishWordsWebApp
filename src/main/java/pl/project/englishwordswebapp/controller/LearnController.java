package pl.project.englishwordswebapp.controller;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.project.englishwordswebapp.data.CategoryDAO;
import pl.project.englishwordswebapp.data.UserRepository;
import pl.project.englishwordswebapp.data.WordsDAO;
import pl.project.englishwordswebapp.model.User;
import pl.project.englishwordswebapp.service.CurrentUser;
import pl.project.englishwordswebapp.model.Category;
import pl.project.englishwordswebapp.model.Words;
import pl.project.englishwordswebapp.service.WordsCounter;

import java.time.LocalDate;
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

    @Autowired
    public LearnController(UserRepository userRepository, CategoryDAO categoryDAO, WordsDAO wordsDAO, WordsCounter wordsCounter, CurrentUser currentUser) {
        // this.repository = userRepository;
        this.categoryDAO = categoryDAO;
        this.wordsDAO = wordsDAO;
        this.currentUser = currentUser;

        this.wordsCounter = wordsCounter;


      /*  LocalTime time = LocalTime.of(10,43,12);

        User u = new User("alek", "haber", "arric@wp.pl", "98082107090", "Szooter1", time);
        Category c = new Category("jobs");

        u.addCategory(c);
        userRepository.save(u);

        User u2 = new User("alek2", "haber2", "arric@wp.pl2", "98082107092", "Szooter12", time);
        Category c2 = new Category("jobs");

        u2.addCategory(c2);
        userRepository.save(u2); */
        LocalTime time = LocalTime.of(10,43,12);
        User u = new User("alek", "haber", "arric@wp.pl", "98082107090", "Szooter1", time);
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

    private String val;
    private List<Integer> score;
    private List<String> placeHolder = new ArrayList<>();


    @GetMapping("/words")
    public String words(@ModelAttribute ("wordsType") String cateName, Model model, @ModelAttribute ("speaker") String speaker){

        if(val == null){
            val = cateName;
        }
        if(!(cateName.isEmpty())){
            val = cateName;
        }

        model.addAttribute("allWords", wordsDAO.findAllByCategoryId(categoryDAO.findByCatename(val).getId()));
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        final VoiceManager voiceManager = VoiceManager.getInstance();
        final Voice voice = voiceManager.getVoice("kevin16");
        voice.allocate();

        voice.speak(speaker);
        return "/learn/words";
    }

    @PostMapping("/words")
    public String postwords(@ModelAttribute ("speaker") String speaker, Model model,RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("speaker", speaker);

        return "redirect:/words";
    }

    @GetMapping("/learntemp")
    public String learn(@ModelAttribute ("wordsType") String value, Model model, @ModelAttribute("postActive")String postA){

        // after post method check if value is not null. if it is null table can't be created
            if(val == null){
                val = value;
            }
            if(!(value.isEmpty())){
                val = value;
            }

        // Displaying chosen words
       model.addAttribute("allWords", wordsDAO.findAllByCategoryId(categoryDAO.findByCatename(val).getId()));
       model.addAttribute("counterObject", wordsCounter);

       // Counter for the score to show words in placeholder
        model.addAttribute("score", score);
        // Words returned in post
        model.addAttribute("placeH", placeHolder);

        return "learntemp";

    }

    @PostMapping("/learntemp")
    public String postlearn( @ModelAttribute WordsCounter counterObject, Model model, RedirectAttributes redirectAttributes){
        int i = 0;
        score = new ArrayList<>();
        // when answer was incorrect it places 0 otherwise 1
        for(Words x : counterObject.getList()){
            placeHolder.add(i, x.getPolish());
            if (x.getPolish().equals(wordsDAO.findAllByCategoryId(categoryDAO.findByCatename(val).getId()).get(i).getPolish())){
                    score.add(1);
                }
                else {
                    score.add(0);
                }
                i++;
        }

        redirectAttributes.addFlashAttribute("postActive", "true");
        return "redirect:/learntemp";
    }
}
