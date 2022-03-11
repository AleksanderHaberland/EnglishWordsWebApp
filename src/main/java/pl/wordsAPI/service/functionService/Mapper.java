package pl.wordsAPI.service.functionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.project.englishwordswebapp.model.Category;
import pl.project.englishwordswebapp.model.Words;
import pl.wordsAPI.dto.CategoryDTO;
import pl.wordsAPI.dto.WordDTO;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class Mapper {

    public Set<CategoryDTO> convertDomainToDTO(Set<Category> domainCategory){
        Set<CategoryDTO> categoryDTO = new LinkedHashSet<>();
        for(Category domainCate: domainCategory){
            categoryDTO.add(new CategoryDTO(domainCate.getId(), domainCate.getCatename()));
        }
        return categoryDTO;
    }

    public Set<Category> convertDTOtoDomain(CategoryDTO... DTOs){
        Set<Category> categories = new LinkedHashSet<>();
        for(CategoryDTO dto: DTOs){
            Category category = new Category();
            category.setCatename(dto.getName());
            categories.add(category);
        }
        return categories;
    }

    public Set<WordDTO> convertDomainWordsToWordsDTO(Set<Words> domainWords){
        Set<WordDTO> wordDTOS = new LinkedHashSet<>();
        for(Words word: domainWords){
            wordDTOS.add(new WordDTO(word.getId(), word.getEnglish(), word.getPolish()));
        }
        return wordDTOS;
    }

    public Set<Words> convertWordsDTOtoDomainWords(WordDTO... DTOs){
        Set<Words> words = new LinkedHashSet<>();
        for(WordDTO dto: DTOs){
            Words word = new Words();
            word.setEnglish(dto.getEnglish());
            word.setPolish(dto.getPolish());
            words.add(word);
        }
        return words;
    }
}
