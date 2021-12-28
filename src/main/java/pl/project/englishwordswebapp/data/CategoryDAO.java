package pl.project.englishwordswebapp.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.project.englishwordswebapp.model.Category;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CategoryDAO extends JpaRepository <Category, Long> {

    Category findByCatename(String catename);
    Category findByCatenameAndUserId(String catename,long id);

    List<Category>getAllByIdNotNull();
    List<Category>getAllByUserId(long id);
    Category findTop1ByOrderByIdDesc();

    @Transactional
    void deleteByCatename(String catename);

}
