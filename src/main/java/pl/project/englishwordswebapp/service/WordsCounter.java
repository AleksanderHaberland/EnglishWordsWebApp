package pl.project.englishwordswebapp.service;

import org.springframework.stereotype.Component;
import pl.project.englishwordswebapp.model.Words;

import java.util.ArrayList;
import java.util.List;

@Component
public class WordsCounter {
    private List<String> list= new ArrayList<>();

    public List<String> getList() {
        return list;
    }
    public void setList(List<String> list) {
        this.list = list;
    }
}
