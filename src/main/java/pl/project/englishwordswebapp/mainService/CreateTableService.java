package pl.project.englishwordswebapp.mainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.project.englishwordswebapp.data.CategoryDAO;
import pl.project.englishwordswebapp.data.WordsDAO;
import pl.project.englishwordswebapp.model.Category;

import java.util.ArrayList;
import java.util.List;

@Service
public class CreateTableService {

    private CategoryDAO categoryDAO;
    private WordsDAO wordsDAO;

    @Autowired
    public CreateTableService(CategoryDAO categoryDAO, WordsDAO wordsDAO){
        this.categoryDAO = categoryDAO;
        this.wordsDAO = wordsDAO;
    }

    public void createCatagory(Category category){
        categoryDAO.save(category);
    }
    public void deleteCategory(String catename){
        categoryDAO.delete(categoryDAO.findByCatename(catename));
    }

    public List<Category> allCategories(){
        return categoryDAO.getAllByIdNotNull();
    }
    public Long amoutOfRows(Category category){
        return wordsDAO.countAllByCategory(category);
    }

    public List<Integer> amoutOfPages(){
        List<Integer> pages = new ArrayList<>();
        int query = allCategories().size();
        int amoutOfPages = 0;
        int x = 0;

        if(query == 0 || query <= 10){
            amoutOfPages = 1;
        }
        if(query > 10){
            if(query % 2 == 0){
                amoutOfPages = query / 10;
            } else {
                amoutOfPages = (query / 10) + 1;
            }
        }
        // do at least once to get page page numb.1
        do {
            x++;
            pages.add(x);
        }while (x < amoutOfPages);

        return pages;
    }
}
