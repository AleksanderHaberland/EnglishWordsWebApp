package pl.project.englishwordswebapp.mainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.project.englishwordswebapp.data.WordsDAO;
import pl.project.englishwordswebapp.model.Category;
import pl.project.englishwordswebapp.model.Words;

import java.util.ArrayList;
import java.util.List;

@Service
public class CreateWordService {

    private WordsDAO wordsDAO;

    @Autowired
    public CreateWordService(WordsDAO wordsDAO){
        this.wordsDAO = wordsDAO;
    }

    public List<Words> allCateWords(Long cateId){
        return wordsDAO.findAllByCategoryId(cateId);
    }

    public void addWord(Words word, Category category){
        word.setCategory(category);
        wordsDAO.save(word);
    }

    public void deleteWord(int wordId){
        long x = wordId;
        wordsDAO.delete(getWord(x));
    }


    public void updateWord(int wordId, Words word){
        wordsDAO.updateWord(wordId, word.getEnglish(), word.getPolish());
    }

    public Words getWord(long id){
        return wordsDAO.getWordsById(id);
    }

    public Words checkExist(String eng, String pol, Category category){
        return wordsDAO.getWordsByEnglishAndPolishAndCategory(eng, pol, category);
    }
}
