package pl.project.englishwordswebapp.mainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.project.englishwordswebapp.data.CategoryDAO;
import pl.project.englishwordswebapp.data.UserRepository;
import pl.project.englishwordswebapp.data.WordsDAO;
import pl.project.englishwordswebapp.model.Category;
import pl.project.englishwordswebapp.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CreateCategoryService {

    private CategoryDAO categoryDAO;
    private WordsDAO wordsDAO;
    private UserRepository userRepository;

    @Autowired
    public CreateCategoryService(CategoryDAO categoryDAO, WordsDAO wordsDAO, UserRepository userRepository){
        this.categoryDAO = categoryDAO;
        this.wordsDAO = wordsDAO;
        this.userRepository = userRepository;
    }

    public void createCatagory(Category category, Long userId){
        int x = 0;
        if(x == 0){
            userRepository.getById(userId).addCategory(category);
            categoryDAO.save(category);
            x++;
        }

    }

    public void deleteCategory(String catename, Long userId){
        try{
            Category c = getCategory(catename, (long)userId);
            userRepository.getById(userId).getCategory().remove(c);
            categoryDAO.delete(c);
        }catch (Exception e){
            System.out.println("double click");
        }
    }

    public List<Category> allCategories(Long id){
        return categoryDAO.getAllByUserId(id);
    }

    public Long amoutOfRows(Category category){
        return wordsDAO.countAllByCategory(category);
    }

    public Category getCategory(String catename, long id){
        Category cat = null;
        try{
            cat = categoryDAO.findByCatenameAndUserId(catename, id);
        } catch (Exception e){
            List<Category> cate = allCategories(id);
            cat = cate.get(cate.size()-1);
            System.out.println("dobule on get");
        }
        return cat;
    }

}
