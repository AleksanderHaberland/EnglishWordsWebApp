package pl.project.englishwordswebapp.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.project.englishwordswebapp.model.Category;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Repository
public interface CategoryDAO extends JpaRepository <Category, Long> {

    Category findByCatename(String catename);
    Category findByCatenameAndUserId(String catename,long id);

    List<Category>getAllByIdNotNull();
    List<Category>getAllByUserId(long id);
    Category findTop1ByOrderByIdDesc();

    @Transactional
    void deleteByCatename(String catename);

    Set<Category> findAllByUserId(long id);

    @Transactional
    @Modifying
    @Query("UPDATE Category cate SET cate.catename = ?2 WHERE cate.id = ?1")
    void updateCategory(long id, String name);
}
