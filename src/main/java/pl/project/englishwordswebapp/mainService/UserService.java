package pl.project.englishwordswebapp.mainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.project.englishwordswebapp.data.UserRepository;
import pl.project.englishwordswebapp.model.User;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getAccountInfo(int id){
        return userRepository.findUserById(id);
    }

    public void changePassword(long id, String password){
        userRepository.updateUserPass(id, password);
    }

    @Transactional
    public String crateToken(int id){
        byte[] randomBytes = new byte[12];
        SecureRandom secureRandom = new SecureRandom();

        // new authentication token
        secureRandom.nextBytes(randomBytes);
        final String token = Base64.getEncoder().encodeToString(randomBytes);

        //save info
        User user = getAccountInfo(id);
        user.setToken(token);
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
