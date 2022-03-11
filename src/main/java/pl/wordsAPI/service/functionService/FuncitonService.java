package pl.wordsAPI.service.functionService;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
public class FuncitonService {

    public boolean Duplicates(String[] correctArray, String[] newArray) {
        Iterator<String> newArrayPart = new ArrayIterator<>(newArray);

        while (newArrayPart.hasNext()) {
            String part = newArrayPart.next();
           for(String correct : correctArray){
               if(correct.equals(part)){
                   return true;
               }
           }
        }
        return false;
    }
}
