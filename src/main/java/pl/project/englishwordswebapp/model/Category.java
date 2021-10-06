package pl.project.englishwordswebapp.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pl.project.englishwordswebapp.model.Words;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_category")
    private Long id;

    @Column(nullable = false)
    private String catename;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Words> words = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
