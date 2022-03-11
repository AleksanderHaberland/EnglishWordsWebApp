package pl.wordsAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.project.englishwordswebapp.data.CategoryDAO;
import pl.project.englishwordswebapp.data.UserRepository;
import pl.project.englishwordswebapp.model.Category;
import pl.project.englishwordswebapp.model.User;
import pl.wordsAPI.dto.CategoryDTO;
import pl.wordsAPI.service.functionService.FuncitonService;
import pl.wordsAPI.service.functionService.Mapper;

import java.util.*;

@Service
public class RestCategoryService {

    private CategoryDAO categoryDAO;
    private static UserRepository userRepository;
    private Mapper mapper;
    private FuncitonService funcitonService;


    @Autowired
    RestCategoryService(CategoryDAO categoryDAO, UserRepository userRepository, Mapper mapper, FuncitonService funcitonService){
        this.categoryDAO = categoryDAO;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.funcitonService = funcitonService;
    }

    private static String[] converterDomainToString(Category... categories){
        String[] stringArray = new String[categories.length];
        int x = 0;
        for(Category category : categories){
            stringArray[x] = category.getCatename();
            x++;
        }
        return stringArray;
    }

    private static boolean checkChars(List<CategoryDTO> dtos){
        boolean correct = false;
        for(CategoryDTO categoryDTO: dtos){
            for(int i = 0; i < categoryDTO.getName().length(); i++){
                int start = categoryDTO.getName().charAt(0);
                int end = categoryDTO.getName().charAt(categoryDTO.getName().length() - 1);
                if(start == 32 || end == 32){
                    return false;
                }

                int ascii = categoryDTO.getName().charAt(i);
                System.out.println("asci = !  " + ascii);
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

    private static User userData(String token){
        return userRepository.getUserByToken(token);
    }

    public Set<CategoryDTO> getAllCategoriesAsDTOs(String token){
        Long id = userData(token).getId();
        Set<CategoryDTO> categories = mapper.convertDomainToDTO(new LinkedHashSet<>(categoryDAO.getAllByUserId(id)));
    return categories;
    }

    public boolean addCategory(CategoryDTO[] categoryDTO, String token){
        if (checkChars(Arrays.asList(categoryDTO)) == false){
            return false;
        }

        Set<Category> categories = mapper.convertDTOtoDomain(categoryDTO);
        Category[] newCategories = categories.toArray(new Category[categories.size()]);
        User user = userData(token);

        Category[] oldCategories = categoryDAO.findAllByUserId(user.getId()).toArray(new Category[0]);
        boolean duplicates = funcitonService.Duplicates(converterDomainToString(oldCategories), converterDomainToString(newCategories));
        if(duplicates == true){
            return false;
        }
        int x = 0;
        do{
            newCategories[x].setUser(user);
            categoryDAO.save(newCategories[x]);
            x++;
        } while (x < newCategories.length);

        return true;
    }

    public boolean updateCategory(String newName, String token, CategoryDTO categoryDTO){
        if (checkChars(Arrays.asList(categoryDTO)) == false){
            return false;
        }
        long id = 0;
        Category[] oldCategories = categoryDAO.findAllByUserId(userData(token).getId()).toArray(new Category[0]);
        boolean categoryExists = false;
        for(Category cate: oldCategories){
            if(cate.getCatename().equals(categoryDTO.getName())){
                categoryExists = true;
            }
        }
        if(categoryExists == false){
            return false;
        }
        Category newNameCate = new Category();
        newNameCate.setCatename(newName);

        boolean duplicates = funcitonService.Duplicates(converterDomainToString(oldCategories), converterDomainToString(newNameCate));
        if(duplicates == true){
            return false;
        }

        for(Category cate : oldCategories){
            if(cate.getCatename().equals(categoryDTO.getName())){
                id = cate.getId();
            }
        }
        categoryDAO.updateCategory(id, newName);

        return true;
    }

    public void deleteCategory(String token, String deleteName){
        Set categories = new LinkedHashSet(categoryDAO.getAllByUserId(userData(token).getId()));
        Iterator iterator = categories.iterator();

        while (iterator.hasNext()){
            Category category = (Category) iterator.next();
            if(category.getCatename().equals(deleteName)){
                categoryDAO.delete(category);
            }
        }
    }
}
