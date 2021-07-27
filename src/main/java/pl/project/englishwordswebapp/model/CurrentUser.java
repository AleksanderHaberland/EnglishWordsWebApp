package pl.project.englishwordswebapp.model;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.SessionScope;

@Component
@Scope(scopeName= WebApplicationContext.SCOPE_SESSION, proxyMode= ScopedProxyMode.TARGET_CLASS)
public class CurrentUser {

    private int id;
    private boolean logged = false;
    Integer t = null;

    public CurrentUser(){};
    public void setIdAndLog(int id, boolean logged){
        this.id = id;
        this.logged = logged;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public boolean getLogged() {
        return logged;
    }
    public void setLogged(boolean logged) {
        this.logged = logged;
    }
}
