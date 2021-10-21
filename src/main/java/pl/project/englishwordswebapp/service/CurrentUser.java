package pl.project.englishwordswebapp.service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.SessionScope;

@Service
//@SessionScope
public class CurrentUser {

    private Long id;
    private boolean logged = false;
    Integer t = null;

    public CurrentUser(){};
    public void setIdAndLog(Long id, boolean logged){
        this.id = id;
        this.logged = logged;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public boolean getLogged() {
        return logged;
    }
    public void setLogged(boolean logged) {
        this.logged = logged;
    }
}
