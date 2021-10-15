package pl.project.englishwordswebapp.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.project.englishwordswebapp.model.Category;
import pl.project.englishwordswebapp.model.Words;

import java.util.List;

@Repository
public interface WordsDAO extends JpaRepository<Words, Long> {

    List<Words> findAllByCategoryNotNull();

    List<Words> findAllByCategoryId(Long id);

    Long countAllByCategory(Category category);


}
