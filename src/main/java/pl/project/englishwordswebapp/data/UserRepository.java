package pl.project.englishwordswebapp.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.project.englishwordswebapp.model.User;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {

    User findByEmail(String email);
    User findByPesel(long pesel);

    User findByPassword(String password);
}
