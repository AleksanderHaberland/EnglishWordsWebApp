package pl.project.englishwordswebapp.mainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.project.englishwordswebapp.data.CategoryDAO;
import pl.project.englishwordswebapp.data.UserRepository;
import pl.project.englishwordswebapp.model.Category;
import pl.project.englishwordswebapp.model.User;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Base64;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class UserService {

    private UserRepository userRepository;
    private CategoryDAO categoryDAO;

    @Autowired
    public UserService(UserRepository userRepository, CategoryDAO categoryDAO) {
        this.userRepository = userRepository;
        this.categoryDAO = categoryDAO;
    }


    public User getAccountInfo(int id){
        return userRepository.findUserById(id);
    }

    public void changePassword(long id, String password){
        userRepository.updateUserPass(id, password);
    }

    public void saveUser(User user, LocalDate localDate){
        user.setDateOfFound(localDate);

        Set<Category> basicCategories = Set.copyOf(categoryDAO.getAllByUserId(1));
        Iterator<Category> category = basicCategories.iterator();

        while (category.hasNext()){
           Category c = category.next();
            user.addCategory(c);
        }
        userRepository.save(user);
    }


    @Transactional
    public String crateToken(int id){
        byte[] randomBytes = new byte[8];
        SecureRandom secureRandom = new SecureRandom();

        // new authentication token
        secureRandom.nextBytes(randomBytes);
        String token = Base64.getUrlEncoder().encodeToString(randomBytes);
        final String tokenFinish = token.substring(0, token.length()-1);
        //save info
        User user = getAccountInfo(id);
        user.setToken(tokenFinish);
        userRepository.save(user);

        return token;
    }

    public boolean tokenExist(int id){
        boolean exist = false;
        if (getAccountInfo(id).getToken() == null){
            exist = false;
        }else{
            exist = true;
        }

        return exist;
    }
}
