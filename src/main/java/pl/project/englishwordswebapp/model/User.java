package pl.project.englishwordswebapp.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "User")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = true, unique = true)
    private String token;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true, columnDefinition = "DATE")
    private LocalDate date_of_found;

   @OneToMany (mappedBy = "user", fetch = FetchType.EAGER)
   Set<Category> category = new HashSet<>();

    public User(){}



    public void addCategory(Category category){
        category.setUser(this);
        getCategory().add(category);
    }

    public User(String name, String lastname, String email, String password) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;

        this.password = password;

    }

    public Set<Category> getCategory() {
        return category;
    }

    public void setCategory(Set<Category> category) {
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDateOfFound() {
        return date_of_found;
    }

    public void setDateOfFound(LocalDate dateOfFound) {
        this.date_of_found = dateOfFound;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", pesel='" + token + '\'' +
                ", password='" + password + '\'' +
                ", dateOfFound='" + date_of_found + '\'' +
                '}';
    }
}
