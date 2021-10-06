package pl.project.englishwordswebapp.model;

import javax.persistence.*;
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

    @Column(nullable = false, unique = true, length = 11)
    private String pesel;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private LocalTime dateOfFound;

   @OneToMany (mappedBy = "user",
            fetch = FetchType.EAGER,
            cascade = CascadeType.PERSIST,
           orphanRemoval = true)
   Set<Category> category = new HashSet<>();

    public void addCategory(Category category){
        category.setUser(this);
        getCategory().add(category);
    }

    public User(){}
    public User(String name, String lastname, String email, String pesel, String password, LocalTime dateOfFound) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.pesel = pesel;
        this.password = password;
        this.dateOfFound = dateOfFound;
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

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalTime getDateOfFound() {
        return dateOfFound;
    }

    public void setDateOfFound(LocalTime dateOfFound) {
        this.dateOfFound = dateOfFound;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", pesel='" + pesel + '\'' +
                ", password='" + password + '\'' +
                ", dateOfFound='" + dateOfFound + '\'' +
                '}';
    }
}
