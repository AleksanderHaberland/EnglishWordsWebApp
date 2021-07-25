package pl.project.englishwordswebapp.model;

import javax.persistence.*;
import java.time.LocalTime;

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

    @Column(nullable = false,unique = false)
    private String email;

    @Column(nullable = false, unique = false, length = 11)
    private long pesel;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private LocalTime dateOfFound;

    public User(){}
    public User(String name, String lastname, String email, long pesel, String password, LocalTime dateOfFound) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.pesel = pesel;
        this.password = password;
        this.dateOfFound = dateOfFound;
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

    public long getPesel() {
        return pesel;
    }

    public void setPesel(long pesel) {
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
