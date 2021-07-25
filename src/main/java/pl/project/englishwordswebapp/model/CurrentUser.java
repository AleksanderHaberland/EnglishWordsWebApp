package pl.project.englishwordswebapp.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class CurrentUser {

    private User user;
    private short id;
    private boolean logged;

    public CurrentUser(){};
    public CurrentUser(short id, boolean logged, User user) {
        this.id = id;
        this.logged = logged;
        this.user = user;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public boolean getLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }
}
