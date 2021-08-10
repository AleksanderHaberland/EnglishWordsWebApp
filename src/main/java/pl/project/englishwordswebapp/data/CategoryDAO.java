package pl.project.englishwordswebapp.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.project.englishwordswebapp.modelwords.Category;

@Repository
public interface CategoryDAO extends JpaRepository<Category, Long> {
}
