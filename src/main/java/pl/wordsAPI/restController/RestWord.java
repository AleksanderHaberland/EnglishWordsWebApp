package pl.wordsAPI.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wordsAPI.dto.WordDTO;
import pl.wordsAPI.service.RestWordService;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class RestWord {

    private RestWordService restWordService;

    @Autowired
    RestWord(RestWordService restWordService){
        this.restWordService = restWordService;
    }

    private ResponseEntity checkWordsDtoNotNull(List<WordDTO> DTOs){
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);

        if (DTOs.size() == 0) {
            responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else{
            for(WordDTO dto : DTOs){
                if(dto.getEnglish() == null || dto.getEnglish().isBlank() || dto.getEnglish().isEmpty()){
                    responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                } else if (dto.getPolish() == null || dto.getPolish().isBlank() || dto.getPolish().isEmpty()){
                    responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
        }

        if(DTOs.size() > 1){
            for(int i = 1; i < DTOs.size() - 1; i++){
                for(WordDTO dtos : DTOs){
                    if(dtos.getPolish().equals(DTOs.get(i).getPolish()) && dtos.getEnglish().equals(DTOs.get(i).getEnglish())){
                        responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                }
            }

        }

        return responseEntity;
    }

    @RequestMapping(value = "/{token}/getAllWords", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<pl.wordsAPI.dto.WordDTO> getAllWordsByCateName(@PathVariable String token, @RequestParam String category){
        return restWordService.getWordsByCategoryName(category,token);
    }

    @RequestMapping(path = "/{token}/addWords", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addWords(@PathVariable String token, @RequestParam String category, @RequestBody WordDTO... wordsDTO){
        ResponseEntity responseEntity = checkWordsDtoNotNull(Arrays.asList(wordsDTO));

        switch (responseEntity.getStatusCode()){
            case NO_CONTENT:
                return responseEntity;
            case BAD_REQUEST:
                return responseEntity;
        }

        if( restWordService.addWordsToCategory(token, category, wordsDTO) == false){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return responseEntity;
    }

    @PutMapping(value = "/{token}/updateWord", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateWord(@PathVariable String token, @RequestParam String category,
                                     @RequestParam String eng,
                                     @RequestParam String pol,
                                     @RequestBody WordDTO wordDTOS){
        ResponseEntity responseEntity = checkWordsDtoNotNull(List.of(wordDTOS));

        switch (responseEntity.getStatusCode()){
            case NO_CONTENT:
                return responseEntity;
            case BAD_REQUEST:
                return responseEntity;
        }

        if(restWordService.updateWord(token, category, wordDTOS, eng, pol) == false){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return responseEntity;
    }

    @RequestMapping(value = "/{token}/deleteWord", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteWord(@PathVariable String token, @RequestParam String category,
                                     @RequestParam String eng,
                                     @RequestParam String pol){
        ResponseEntity responseEntity = checkWordsDtoNotNull(List.of(new WordDTO(1, eng, pol)));

        switch (responseEntity.getStatusCode()){
            case NO_CONTENT:
                return responseEntity;
            case BAD_REQUEST:
                return responseEntity;
        }

        if(restWordService.deleteWord(token, category, eng, pol) == false){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return responseEntity;
    }
}
