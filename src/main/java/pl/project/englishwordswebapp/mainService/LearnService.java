package pl.project.englishwordswebapp.mainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.project.englishwordswebapp.data.CategoryDAO;
import pl.project.englishwordswebapp.data.WordsDAO;
import pl.project.englishwordswebapp.model.Category;
import pl.project.englishwordswebapp.model.Words;

import java.util.*;

@Service
public class LearnService {

    private CategoryDAO categoryDAO;
    private WordsDAO wordsDAO;

    @Autowired
    public LearnService(CategoryDAO categoryDAO, WordsDAO wordsDAO){
        this.categoryDAO = categoryDAO;
        this.wordsDAO = wordsDAO;
    }

    private Set<String> question;
    private Set<String> answers;

    public Set<String> getQuestion() {
        if(question.isEmpty()){
            return null;
        }
        return question;
    }

    public Set<String> getAnswers() {
        if(answers.isEmpty()){
            return null;
        }
        return answers;
    }

    public List<Words> getAllByCategoryName(String categoryName){
        Category c = categoryDAO.findByCatename(categoryName);
        return wordsDAO.findAllByCategoryId(c.getId());
    }

    public List<Category> getAllCategories(long userId){
        return categoryDAO.getAllByUserId(userId);
    }


    public void getRequestTestWords(String language, long amount, String... categories){
        Random random = new Random();
        List<Words> words = new ArrayList<>();
        Set<Words> allCategories = new HashSet<>();

        for (String category : categories){
            words.addAll(getAllByCategoryName(category));
        }

        int y = 0;
        while (allCategories.size() < amount){
            Words word = words.get(random.nextInt(words.size()));
            allCategories.add(word);

            Iterator<Words> iterator = allCategories.iterator();
            Iterator<Words> iterator2 = allCategories.iterator();

            // function to remove duplicates
            if(allCategories.size() >= 2){
                int i = 0;
                while(iterator.hasNext()){
                    while(iterator2.hasNext()){
                        if(i >= 1){
                            if(iterator.next() == iterator2.next()){
                                allCategories.remove(iterator2);
                            }
                        }
                        i++;
                    }
                }
            }
            y++;
        }

        question = new LinkedHashSet<>();
        answers = new LinkedHashSet<>();
        for(Words words1 : allCategories){
            switch (language){
                case "engpol":
                    question.add(words1.getEnglish());
                    answers.add(words1.getPolish());
                    break;
                case "poleng":
                    question.add(words1.getPolish());
                    answers.add(words1.getEnglish());
                    break;
                case "mix":
                    if (random.nextInt(2) == 1){
                        question.add(words1.getEnglish());
                        answers.add(words1.getPolish());
                    } else {
                        question.add(words1.getPolish());
                        answers.add(words1.getEnglish());
                    }
                    break;
            }

        }

    }

    public List<Integer> testCategoriesInt(List<Integer> testCategoriesInt, List<String> testCategories, List<Category> userCategory){
        List<Integer> looper;

        if (!testCategories.isEmpty()) {
            for (Category cate : userCategory) {

                int loop2 = 0;
                looper = new ArrayList<>();
                int memory = 0;

                while (loop2 < testCategories.size()) {
                    if (cate.getCatename().equals(testCategories.get(loop2))) {
                        testCategoriesInt.add(1);
                        looper.add(1);
                    } else {
                        looper.add(0);
                        memory += 1;
                    }
                    loop2++;
                }
                if(memory == testCategories.size()){
                    testCategoriesInt.add(0);
                }
            }
        }
        return testCategoriesInt;
    }

}
