package pl.project.englishwordswebapp.service;

import org.springframework.stereotype.Controller;
import pl.project.englishwordswebapp.model.Category;

import java.util.ArrayList;
import java.util.List;


public class Pagination<T>{
    public Pagination(){
    }
    // method return amount of pages based on received amount of categories / 10 .
    // If number categories divided by 10 has remainder, then returns pages + 1
    public List<Integer> amoutOfPages(List<T> categories){

        List<Integer> pages = new ArrayList<>();
        int amoutOfPages = 0;
        int x = 0;

        if(categories.size() == 0 || categories.size() <= 10){
            amoutOfPages = 1;
        }
        if(categories.size() > 10){
            if(categories.size() % 2 == 0){
                if(categories.size() % 10 == 0){
                    amoutOfPages = categories.size() / 10;
                }
                else{
                    amoutOfPages = (categories.size() / 10) + 1;
                }
            }
            else {
                amoutOfPages = (categories.size() / 10) + 1;
            }
        }
        // do at least once to get page page numb.1
        do {
            x++;
            pages.add(x);
        }while (x < amoutOfPages);

        return pages;
    }

    // method return all current words if there is only 1 page, and all for if pages is more than 1
    public List<String> pageRows(String currentPageNumb, List<String> categoryName){

        int pageNumberAfterParse = 0;
        if(currentPageNumb != null && !currentPageNumb.isEmpty()){
            pageNumberAfterParse = Integer.parseInt(currentPageNumb);
        }
        List<String> currentCategoryName = new ArrayList<>();
        int start = 0;

        if(pageNumberAfterParse > 1){

            start = pageNumberAfterParse * 10 - 10;

            for(int i = start; i < categoryName.size() && categoryName.size() <= pageNumberAfterParse * 10; i++ ){
                currentCategoryName.add(categoryName.get(i));
            }

            if(categoryName.size() >= pageNumberAfterParse * 10 + 1){

                for (int i = start; i < pageNumberAfterParse * 10; i++){
                    currentCategoryName.add(categoryName.get(i));
                }
            }

        }
        else {
            // could no exsits
            if(categoryName.size() > 0 && categoryName.size() < 11){
                for (String cat : categoryName){
                    currentCategoryName.add(cat);
                }
            }
            if(categoryName.size() > 10) {
                for (int i = 0; i < 10; i++){
                    currentCategoryName.add(categoryName.get(i));
                }
            }

        }

        return currentCategoryName;
    }
}
