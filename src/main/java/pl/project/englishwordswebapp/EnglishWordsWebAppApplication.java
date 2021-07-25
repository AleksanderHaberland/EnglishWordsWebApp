package pl.project.englishwordswebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pl.project.englishwordswebapp.data.UserRepository;
import pl.project.englishwordswebapp.model.User;

@SpringBootApplication
public class EnglishWordsWebAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnglishWordsWebAppApplication.class, args);

        /* ConfigurableApplicationContext ctx =  SpringApplication.run(EnglishWordsWebAppApplication.class, args);
        UserRepository userRepository = ctx.getBean(UserRepository.class);
        User user = new User("a","b","c@gmail.com","12345678911","e","17-07");
        userRepository.save(user); */
    }

}
