package pl.project.englishwordswebapp.modelwords;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category implements    Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_category")
    private Long id;

    @Column(nullable = false)
    private String catename;

    @OneToMany(mappedBy = "category")
    private List<Words> words = new ArrayList<>();

    public Category(){}

    public Category(String catename) {
        this.catename = catename;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCatename() {
        return catename;
    }

    public void setCatename(String catename) {
        this.catename = catename;
    }

    public List<Words> getWords() {
        return words;
    }

    public void setWords(List<Words> words) {
        this.words = words;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", catename='" + catename + '\'' +
                ", words=" + words +
                '}';
    }
}
