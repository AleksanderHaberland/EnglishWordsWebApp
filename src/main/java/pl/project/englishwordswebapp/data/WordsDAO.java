package pl.project.englishwordswebapp.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.project.englishwordswebapp.model.Category;
import pl.project.englishwordswebapp.model.Words;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface WordsDAO extends JpaRepository<Words, Long> {

    List<Words> findAllByCategoryNotNull();

    List<Words> findAllByCategoryId(Long id);

    Long countAllByCategory(Category category);

    Words getWordsById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Words w SET w.english=:eng, w.polish=:pol WHERE w.id=:id")
    void updateWord(@Param("id") long id, @Param("eng") String eng, @Param("pol") String pol);

    List<Words> getWordsByEnglishAndPolishAndCategory(String eng, String pol, Category category);

}
