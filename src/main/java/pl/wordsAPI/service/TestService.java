package pl.wordsAPI.service;

import org.springframework.stereotype.Service;
import pl.project.englishwordswebapp.data.UserRepository;
import pl.project.englishwordswebapp.model.Category;
import pl.project.englishwordswebapp.model.User;
import pl.project.englishwordswebapp.model.Words;
import pl.wordsAPI.dto.CategoryDTO;
import pl.wordsAPI.dto.WordDTO;
import pl.wordsAPI.service.functionService.Mapper;

import java.util.*;

@Service
public class TestService {

    private Mapper mapper;
    private static UserRepository userRepository;

    public TestService(Mapper mapper, UserRepository userRepository){
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    private static User userData(String token){
        return userRepository.getUserByToken(token);
    }

    public LinkedHashSet<Category> testCategories(String token, Set<CategoryDTO> dtos){
        User user = userData(token);
        Set<Category> mapperCategories = mapper.convertDTOtoDomain(dtos.toArray(new CategoryDTO[0]));
        int allFounded = 0;

        LinkedHashSet<Category> testCategories = new LinkedHashSet<>();

        for(Category category: user.getCategory()){
            for(Category category2: mapperCategories){
                if(category2.getCatename().equals(category.getCatename())){
                    allFounded = allFounded + 1;
                    testCategories.add(category);
                }
            }
        }

        if(allFounded != dtos.size()){
            return null;
        }

        return testCategories;
    }

    public Set<WordDTO> generateTest(String token, Set<CategoryDTO> dtos, int amount){
        LinkedHashSet<Category> testCategories = testCategories(token, dtos);
        if(testCategories == null){
            return null;
        }
        if(testCategories.size() == 0){
            return null;
        }

        Set<WordDTO> words = new HashSet<>();

        for(Iterator<Category> iterator = testCategories.iterator(); iterator.hasNext();){
            Category category = iterator.next();
            words = mapper.convertDomainWordsToWordsDTO(Set.copyOf(category.getWords()));
        }
        List<WordDTO> wordsList = new ArrayList<>(words);
        if(wordsList.size() <= amount - 1){
            return null;
        }
        System.out.println(wordsList.size() + " 1");

        Random random = new Random();
        Set<WordDTO> testWords = new LinkedHashSet<>();

        do {
            WordDTO wordWithNewID = wordsList.get(random.nextInt(wordsList.size()));
            testWords.add(wordWithNewID);
        }while (testWords.size() < amount);

        int i = 1;
        for(WordDTO wordDTO: testWords){
            wordDTO.setId(i);
            i++;
        }
        return testWords;
    }
}
