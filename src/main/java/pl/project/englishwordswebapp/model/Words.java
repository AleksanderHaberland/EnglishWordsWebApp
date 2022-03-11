package pl.project.englishwordswebapp.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Words implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_word")
    private Long id;

    @Column(nullable = false)
    private String english;

    @Column(nullable = false)
    private String polish;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Words(){}

    public Words(String english, String polish) {
        this.english = english;
        this.polish = polish;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getPolish() {
        return polish;
    }

    public void setPolish(String polish) {
        this.polish = polish;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Words{" +
                "id to = " + id +
                ", english to = '" + english + '\'' +
                ", polish to = '" + polish + '\'' +
                '}';
    }
}
