package pl.wordsAPI.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.wordsAPI.dto.CategoryDTO;
import pl.wordsAPI.dto.WordDTO;
import pl.wordsAPI.service.TestService;

import java.util.Arrays;
import java.util.Set;

@Controller
@RequestMapping(value = "/api/v1")
public class RestTest {

    @Autowired
    private TestService testService;

    @GetMapping(value = "/{token}/generateTest", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity generateTest(@PathVariable String token,
                                       @RequestBody CategoryDTO[] categoryDTOS,
                                       @RequestParam Integer amount){

        for(CategoryDTO categoryDTO: categoryDTOS){
            if(categoryDTO.getName().isBlank() || categoryDTO.getName().isEmpty() || categoryDTO.getName() == null){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        }
        Set<WordDTO> test = testService.generateTest(token, Set.copyOf(Arrays.asList(categoryDTOS)), amount);
        if(test == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(test);
    }
}
