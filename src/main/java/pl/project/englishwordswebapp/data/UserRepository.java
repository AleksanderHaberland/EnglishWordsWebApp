package pl.project.englishwordswebapp.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.project.englishwordswebapp.model.User;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {

    User findByEmail(String email);

    User findByPassword(String password);
    User findUserById(long id);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password=:pass WHERE u.id=:id")
    void updateUserPass(@Param("id") long id, @Param("pass") String pass);

    @Transactional
    @Query("SELECT user FROM User user WHERE user.token = ?1")
    User getUserByToken(String token);
}