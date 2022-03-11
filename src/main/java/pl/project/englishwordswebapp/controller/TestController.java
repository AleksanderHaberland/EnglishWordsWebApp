
package pl.project.englishwordswebapp.controller;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.project.englishwordswebapp.data.UserRepository;
import pl.project.englishwordswebapp.mainService.LearnService;
import pl.project.englishwordswebapp.model.Category;
import pl.project.englishwordswebapp.model.Words;
import pl.project.englishwordswebapp.service.CurrentUser;
import pl.project.englishwordswebapp.service.WordsCounter;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@SessionScope

public class TestController {

    private CurrentUser currentUser;
    private LearnService learnService;
    private WordsCounter counter;
    @Autowired
    public TestController(UserRepository userRepository, CurrentUser currentUser, LearnService learnService, WordsCounter counter) {
        this.currentUser = currentUser;
        this.learnService = learnService;
        this.counter = counter;
    }

    @ModelAttribute
    public void currentUserSession(Model model) {
        model.addAttribute("userSession", currentUser);
    }


    private String categoryTest;
    private List<Integer> score;
    private List<String> placeHolder = new ArrayList<>();

    // Chosen categories
    private List<String> testCategories = new ArrayList<>();
    private List<Integer> testCategoriesInt = new ArrayList<>();
    private List<Integer> testCategoriesInt2 = new ArrayList<>();

    private long[] wordsArray = new long[10];
    private String language = "";
    private long amount = 0;
    private boolean generate = false;
    private boolean[] userchoices = {false,false};

    private boolean postActivated = false;
    private String path = "";
    int aaa = 0;

    @GetMapping(value = {"/test/{type}"})
    public String writtenTestGet(@RequestParam(required = false) String wordsType, Model model,
                                 @ModelAttribute(value = "postActivated") String postActivated,
                                 @ModelAttribute(value = "speaker") String speaker,
                                 @ModelAttribute(value = "check") String check,
                                 @RequestParam(value = "amount", required = false) String amount,
                                 @RequestParam(value = "leanguage",required = false) String lang,
                                 @RequestParam(value = "generate", required = false) String generate,
                                 @RequestParam(required = false) String resetButton,
                                 @PathVariable(required = false ) String type
                                 ){

        if (currentUser.getLogged() == false) {
            model.addAttribute("empty", "empty");
            testCategories = new ArrayList<>();
            }
        /////////////////////////////////////////////
        if(lang != null){
            language = lang;
        }
        if(amount != null){
            this.amount = Long.parseLong(amount);
        }
        model.addAttribute("userchoices", userchoices);

        if(this.amount != 0){
            userchoices[0] = true;
            model.addAttribute("amount", this.amount);
        }
        if(!this.language.isEmpty()){
            userchoices[1] = true;
        }

        if(!postActivated.isEmpty()){
            this.postActivated = true;
        }
        // reset
        if ((path.equals("written") && type.equals("sound")) || (path.equals("sound") && type.equals("written"))){
            resetButton = "reset";
            placeHolder = new ArrayList<>();
            this.postActivated = false;
            member = new ArrayList<>();
        }
        if(type.equals("sound")){
            // sound is in http path
            path = "sound";
            model.addAttribute("path", path);
        } else if(type.equals("written")){
            path = "written";
            model.addAttribute("path", path);
        }else {
        }
        if(check != null){
            model.addAttribute("check", check);
        }
        //////////////////////////////////////////////

        //logged user gives all of his categories
        List<Category> currentUserCategoryWithOut0 = new ArrayList<>();

        if (currentUser.getId() != null) {
            List<Category> currentUserCategory;
            currentUserCategory = learnService.getAllCategories(currentUser.getId());

            for(int x = 0; x < currentUserCategory.size(); x++){
                if(!learnService.getAllByCategoryName(currentUserCategory.get(x).getCatename()).isEmpty()){
                    currentUserCategoryWithOut0.add(currentUserCategory.get(x));
                }
            }

            model.addAttribute("category", currentUserCategoryWithOut0);
        }

        //selected category in science
        if (wordsType != null) {

            categoryTest = wordsType;
            testCategories.add(wordsType);

            // double clicked same button = delete that category
            int loop = 0;
            while (loop < testCategories.size()) {
                int y = 0;
                for (int x = 0; x < testCategories.size(); x++) {

                    if (testCategories.get(loop).equals(testCategories.get(x))) {
                        y++;
                        if (y >= 2) {
                            testCategories.remove(x);
                            testCategories.remove(loop);
                            y = 0;
                            loop = testCategories.size() + 1;
                        }
                    }
                }
                loop++;
            }
            System.out.println(testCategories + " !!");
            loop = 0;
        }
///////////////////////////////////////////////////////////// show clicked buttons in orange color
        testCategoriesInt = new ArrayList<>();
        testCategoriesInt = learnService.testCategoriesInt(testCategoriesInt,testCategories,currentUserCategoryWithOut0);
        ///////////////////////////////////////////////////////////// amount of words in category
        long amountOfWords = 0;
        long[] wordsArray = new long[10];

        for(int x = 0; x < testCategories.size(); x++){
            for(Words word : learnService.getAllByCategoryName(testCategories.get(x))){
                amountOfWords += 1;
            }
        }

        int position = 0;
        float x = 0.10f;

        while(position < wordsArray.length - 1){
            wordsArray[position] = (long) Math.floor((amountOfWords * x));
            x += 0.10f;
            position++;
        }
        wordsArray[9] = amountOfWords;
        model.addAttribute("amountofwords", wordsArray);
/////////////////////////////////////////////////////////////
        model.addAttribute("counterObject",counter);
        model.addAttribute("language", this.language);

        // Counter for the score to show words in placeholder // need to be over method
        model.addAttribute("score", score);
        // Words returned in post
        model.addAttribute("placeH", placeHolder);

        model.addAttribute("testCategoriesInt", testCategoriesInt);
        model.addAttribute("testCategoriesInt2", testCategoriesInt2);

        model.addAttribute("testCategories", testCategories);
        model.addAttribute("postActivated", String.valueOf(this.postActivated));

        if(generate != null){
            model.addAttribute("generateTest", true);
            learnService.getRequestTestWords(language, this.amount, testCategories.toArray(new String[0]));
            model.addAttribute("allWords", learnService.getQuestion());
            model.addAttribute("answer", learnService.getAnswers());

            this.generate = true;
        } else if (this.generate == true){
            model.addAttribute("generateTest", true);
            model.addAttribute("allWords", learnService.getQuestion());
            model.addAttribute("answer", learnService.getAnswers());
        } else {
            model.addAttribute("generateTest", false);

        }
//////// RESET

        if(resetButton != null || currentUser.getId() == null){
            testCategories = new ArrayList<>();
            testCategoriesInt = new ArrayList<>();
            testCategoriesInt2 = new ArrayList<>();

            member = new ArrayList<>();
        }

        if(testCategories.isEmpty() || currentUser.getId() == null){
            userchoices[0]= false;
            userchoices[1]= false;
            this.amount = 0;
            this.language = "";
            this.generate = false;
            score = new ArrayList<>();
            placeHolder = new ArrayList<>();
            place2 = new ArrayList<>();

            this.postActivated = false;
            trial = 0;

            model.addAttribute("generateTest", this.generate);
        }
        if(testCategories.isEmpty() && resetButton != null){
            return "redirect:/test/".concat(path);
        }
        // check if all answers was correct
        if( trial > 0){
            long element = 0;
            long countCorrectAnswers = 0;

            while(element < score.size()){
                if(score.get((int)element) == 1){
                    countCorrectAnswers++;
                }
                element++;
            }
            if(countCorrectAnswers == learnService.getQuestion().size()){
                model.addAttribute("congratulations", true);
                System.out.println("auto RESET!");
                place2 = new ArrayList<>();
                trial = 0;
                this.postActivated = false;
            }
        }
        // lunch sound
        if(!speaker.isEmpty()){
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            final VoiceManager voiceManager = VoiceManager.getInstance();
            final Voice voice = voiceManager.getVoice("kevin16");

            voice.allocate();
            voice.speak(speaker);

            model.addAttribute("member", member);
            model.addAttribute("speakerinuse", true);
        }
        if(path.equals("sound")){
            model.addAttribute("speak", true);
        }
        return "learn/test";
    }

    private List<String> place2 = new ArrayList<>();
    private int trial = 0;
    private List<String>member = new ArrayList<>();

    @PostMapping(value = {"/test/written","test/sound"})
    public String writtenTestPost(@ModelAttribute WordsCounter counterObject,
                                  RedirectAttributes redirectAttributes,
                                  @RequestParam(required = false) String speaker,
                                  @RequestParam(required = false) String check) {


        List<String> lang = new ArrayList<>();
        if(!counterObject.getList().isEmpty()){
            for(String wo : counterObject.getList()){
                lang.add(wo);
            }
            redirectAttributes.addFlashAttribute("postActivated", "true");
        }

        // score initialized here is important to restart score give in Get Method
        score = new ArrayList<>();

        String[] answers = learnService.getAnswers().toArray(new String[learnService.getAnswers().size()]);
        placeHolder = new ArrayList<>();

        // when answer was incorrect it places 0 otherwise 1
        int i = 0;
        for (String answare : answers) {
            try {
                if(trial == 0){
                    if (answare.equals(lang.get(i))) {
                        member.add(lang.get(i));
                        placeHolder.add(lang.get(i));
                        place2.add(lang.get(i));
                        score.add(1);
                    } else {
                        member.add(lang.get(i));
                        placeHolder.add(lang.get(i));
                        place2.add("");
                        score.add(0);
                    }
                } else {
                    if(place2.get(i).equals(answare)){
                        placeHolder.add(place2.get(i));
                        score.add(1);
                    } else if(place2.get(i).isBlank()){

                        if(answare.equals(lang.get(i))){
                            placeHolder.add(lang.get(i));
                            place2.set(i,lang.get(i));
                            member.set(i,lang.get(i));
                            score.add(1);
                        } else {
                            placeHolder.add(lang.get(i));
                            place2.set(i,"");
                            score.add(0);
                            if(!lang.get(i).isBlank() || !lang.get(i).isEmpty()){
                                member.set(i,lang.get(i));
                            }
                        }
                    }
                }

            }catch (Exception e){
                score.add(1);
            }
            i++;
        }
        trial++;

        if(!(speaker == null)){
            redirectAttributes.addFlashAttribute("speaker", speaker);
            redirectAttributes.addFlashAttribute("check", check);
        }


        return "redirect:/test/".concat(path);
    }
}
