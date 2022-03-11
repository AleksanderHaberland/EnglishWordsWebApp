package pl.project.englishwordswebapp.service;

import java.util.ArrayList;
import java.util.List;

public class Pagination<T>{
    final private int amountOfRows;

    public Pagination(int amountOfRows){
        this.amountOfRows = amountOfRows;
    }
    // method return amount of pages based on received amount of categories / amountOfRows .
    // If number categories divided by amountOfRows has remainder, then returns pages + 1
    public List<Integer> amoutOfPages(List<T> categories){

        List<Integer> pages = new ArrayList<>();
        int amoutOfPages = 0;
        int x = 0;

        if(categories.size() == 0 || categories.size() <= amountOfRows){
            amoutOfPages = 1;
        }
        if(categories.size() > amountOfRows){
            if(categories.size() % 2 == 0){
                if(categories.size() % amountOfRows == 0){
                    amoutOfPages = categories.size() / amountOfRows;
                }
                else{
                    amoutOfPages = (categories.size() / amountOfRows) + 1;
                }
            }
            else {
                amoutOfPages = (categories.size() / amountOfRows) + 1;
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
    public List<T> pageRows(String currentPageNumb, List<T> categoryName){

        int pageNumberAfterParse = 0;
        if(currentPageNumb != null && !currentPageNumb.isEmpty()){
            pageNumberAfterParse = Integer.parseInt(currentPageNumb);
        }
        List<T> currentCategoryName = new ArrayList<>();
        int start = 0;

        if(pageNumberAfterParse > 1){

            start = pageNumberAfterParse * amountOfRows - amountOfRows;

            for(int i = start; i < categoryName.size() && categoryName.size() <= pageNumberAfterParse * amountOfRows; i++ ){
                currentCategoryName.add(categoryName.get(i));
            }

            if(categoryName.size() >= pageNumberAfterParse * amountOfRows + 1){

                for (int i = start; i < pageNumberAfterParse * amountOfRows; i++){
                    currentCategoryName.add(categoryName.get(i));
                }
            }

        }
        else {
            // could no exsits
            if(categoryName.size() > 0 && categoryName.size() < amountOfRows + 1){
                for (T cat : categoryName){
                    currentCategoryName.add(cat);
                }
            }
            if(categoryName.size() > amountOfRows) {
                for (int i = 0; i < amountOfRows; i++){
                    currentCategoryName.add(categoryName.get(i));
                }
            }

        }

        return currentCategoryName;
    }
}
