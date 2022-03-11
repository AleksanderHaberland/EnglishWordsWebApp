package pl.wordsAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.project.englishwordswebapp.data.CategoryDAO;
import pl.project.englishwordswebapp.data.UserRepository;
import pl.project.englishwordswebapp.data.WordsDAO;
import pl.project.englishwordswebapp.model.Category;
import pl.project.englishwordswebapp.model.User;
import pl.project.englishwordswebapp.model.Words;
import pl.wordsAPI.dto.CategoryDTO;
import pl.wordsAPI.dto.WordDTO;
import pl.wordsAPI.service.functionService.FuncitonService;
import pl.wordsAPI.service.functionService.Mapper;

import java.util.*;

@Component
public class RestWordService {

    private Mapper mapper;
    private WordsDAO wordsDAO;
    private static UserRepository userRepository;
    private FuncitonService funcitonService;
    private CategoryDAO categoryDAO;

    @Autowired
    RestWordService(Mapper mapper, WordsDAO wordsDAO, UserRepository userRepository, FuncitonService funcitonService, CategoryDAO categoryDAO){
        this.mapper = mapper;
        this.wordsDAO = wordsDAO;
        this.userRepository = userRepository;
        this.funcitonService = funcitonService;
        this.categoryDAO = categoryDAO;
    }

    private static User userData(String token){
        return userRepository.getUserByToken(token);
    }

    private static boolean checkChars(List<WordDTO> dtos){
        boolean correct = false;
        for(WordDTO wordDTO: dtos){
            for(int i = 0; i < wordDTO.getEnglish().length(); i++){
                int start = wordDTO.getEnglish().charAt(0);
                int end = wordDTO.getEnglish().charAt(wordDTO.getEnglish().length() - 1);
                if(start == 32 || end == 32){
                    return false;
                }

                int ascii = wordDTO.getEnglish().charAt(i);

                if((ascii >= 97 && ascii <= 122) || ascii == 32 || ascii == 261 || ascii == 263 || ascii == 281 || ascii == 322 ||
                        ascii == 324 ||ascii == 243 || ascii == 347 || ascii == 378 || ascii == 380){
                    correct = true;
                } else {
                    return false;
                }
            }
        }
        return correct;
    }

    private static boolean checkCharsPol(List<WordDTO> dtos){
        boolean correct = false;
        for(WordDTO wordDTO: dtos){
            for(int i = 0; i < wordDTO.getPolish().length(); i++){
                int start = wordDTO.getPolish().charAt(0);
                int end = wordDTO.getPolish().charAt(wordDTO.getPolish().length() - 1);
                if(start == 32 || end == 32){
                    return false;
                }

                int ascii = wordDTO.getPolish().charAt(i);

                if((ascii >= 97 && ascii <= 122) || ascii == 32 || ascii == 261 || ascii == 263 || ascii == 281 || ascii == 322 ||
                        ascii == 324 ||ascii == 243 || ascii == 347 || ascii == 378 || ascii == 380){
                    correct = true;
                } else {
                    return false;
                }
            }
        }
        return correct;
    }

    public Set<WordDTO> getWordsByCategoryName(String category, String token){
        User user;
        if(userData(token) == null){
            return null;
        }else {
            user = userData(token);
        }
        ArrayList<Category> categories = new ArrayList<>(user.getCategory());
        Set<Words> words = new LinkedHashSet<>();
        for(Category cate : categories){
            if(cate.getCatename().equals(category)){
                words = new LinkedHashSet<>(wordsDAO.findAllByCategoryId(cate.getId()));
                break;
            }
        }
        return mapper.convertDomainWordsToWordsDTO(words);
    }

    public boolean addWordsToCategory(String token, String category, WordDTO... dtos){
        if (checkChars(Arrays.asList(dtos)) == false){
            return false;
        }
        if (checkCharsPol(Arrays.asList(dtos)) == false){
            return false;
        }
        User user = userData(token);
        Set<Category> categories = user.getCategory();
        Category wordCategory = null;

        for(Category cate: categories){
            if(category.equals(cate.getCatename())){
                wordCategory = cate;
            }
        }
        if(wordCategory == null){
            return false;
        }

        List<Words> correctWords = wordCategory.getWords();
        String[] englishWords = new String[correctWords.size()];
        String[] polishWords = new String[correctWords.size()];

        for(int i = 0; i < correctWords.size(); i++){
            englishWords[i] = correctWords.get(i).getEnglish();
            polishWords[i] = correctWords.get(i).getPolish();
        }

        Set<Words> words = mapper.convertWordsDTOtoDomainWords(dtos);
        List<String> newEnglish = new ArrayList<>();
        List<String> newPolish = new ArrayList<>();

        for(Words words1 : words){
            newEnglish.add(words1.getEnglish());
            newPolish.add(words1.getPolish());
        }

        boolean englishDuplicate = true;
        if (funcitonService.Duplicates(englishWords, newEnglish.toArray(new String[0])) == true) {
            englishDuplicate = false;
        }
        if (funcitonService.Duplicates(polishWords, newPolish.toArray(new String[0])) == true && englishDuplicate == false) {
            return false;
        }

        for(Words word : words){
            word.setCategory(wordCategory);
            wordsDAO.save(word);
        }

        return true;
    }

    public boolean updateWord(String token, String category, WordDTO wordDTO, String eng, String pol){
        if (checkChars(Arrays.asList(wordDTO)) == false){
            return false;
        }
        if (checkCharsPol(Arrays.asList(wordDTO)) == false){
            return false;
        }

        User user = userData(token);
        Category wordCategory = null;
        boolean categoryExists = false;

        for(Category cate : user.getCategory()){
            System.out.println("??????????" + category);
            System.out.println(cate.getCatename() + " !!!!!!!!!!!!!!!!!!!!!!!!!11");
            if (cate.getCatename().equals(category)) {
                categoryExists = true;
                wordCategory = cate;
            }
        }
        if(categoryExists == false){
            return false;
        }

        Words mainWord = null;
        for(Words word : wordCategory.getWords()){
            if(word.getPolish().equals(pol) && word.getEnglish().equals(eng)){
                mainWord = word;
            }
        }
        if(mainWord == null){
            return false;
        }
        wordsDAO.updateWord(mainWord.getId(), wordDTO.getEnglish(), wordDTO.getPolish());
        return true;
    }

    public boolean deleteWord(String token, String category, String eng, String pol){
        User user = userData(token);
        Category wordCategory = null;
        boolean categoryExists = false;

        for(Category cate : user.getCategory()){
            if (cate.getCatename().equals(category)) {
                categoryExists = true;
                wordCategory = cate;
            }
        }
        if(categoryExists == false){
            return false;
        }

        Words mainWord = null;
        for(Words word : wordCategory.getWords()){
            if(word.getPolish().equals(pol) && word.getEnglish().equals(eng)){
                mainWord = word;
            }
        }
        if (mainWord == null){
            return false;
        }

        wordCategory.getWords().remove(mainWord);
        categoryDAO.save(wordCategory);
        wordsDAO.delete(mainWord);

        return true;
    }
}
