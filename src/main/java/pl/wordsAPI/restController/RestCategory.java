package pl.wordsAPI.restController;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.project.englishwordswebapp.service.CurrentUser;
import pl.wordsAPI.dto.CategoryDTO;
import pl.wordsAPI.service.functionService.Mapper;
import pl.wordsAPI.service.RestCategoryService;

import java.util.Set;

@Controller
@RequestMapping("/api/v1")
public class RestCategory {

    private RestCategoryService restCategoryService;

    public RestCategory(RestCategoryService restCategoryService){
        this.restCategoryService = restCategoryService;
    }

    private ResponseEntity<?> checkCategoryDtoNotNull(CategoryDTO... DTOs){
        ResponseEntity<?> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        if (DTOs.length == 0) {
            responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else{
            for(CategoryDTO dto : DTOs){
                if(dto.getName() == null || dto.getName().isBlank() || dto.getName().isEmpty()){
                    responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
            int i = 0;
            for(CategoryDTO dto: DTOs){
                while (i < DTOs.length - 1) {
                    if (dto.getName().equals(DTOs[i + 1].getName())) {
                        // duplicate
                        responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                    i++;
                }
            }

        }

        return responseEntity;
    }

    @RequestMapping(path="/{token}/allCategories",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    @ResponseBody
    public Set<CategoryDTO> getAllCategories(@PathVariable String token) {
        Set<CategoryDTO> categoriesFromToken = restCategoryService.getAllCategoriesAsDTOs(token);
        return categoriesFromToken;
    }

    @RequestMapping(value = "/{token}/addCategories",
    method = RequestMethod.POST)
    public ResponseEntity<?> saveCategories(@RequestBody CategoryDTO[] categoryDTO, @PathVariable String token){
        ResponseEntity responseEntity = checkCategoryDtoNotNull(categoryDTO);

        switch (responseEntity.getStatusCode()){
            case NO_CONTENT:
                return responseEntity;
            case BAD_REQUEST:
                return responseEntity;
        }

        for(CategoryDTO cateDTO: restCategoryService.getAllCategoriesAsDTOs(token)){
            for(int i = 0; i < categoryDTO.length; i++){
                if(cateDTO.getName().equals(categoryDTO[i].getName())){
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
            }
        }
        if(restCategoryService.addCategory(categoryDTO, token) == false){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{token}/updateCategoryName")
    public ResponseEntity<?> updateCategoryName(@PathVariable String token,
                                                @RequestBody CategoryDTO categoryDTO,
                                                @RequestParam(value = "name") String newName){
        ResponseEntity responseEntity = checkCategoryDtoNotNull(categoryDTO);
        switch (responseEntity.getStatusCode()){
            case NO_CONTENT:
                return responseEntity;
            case BAD_REQUEST:
                return responseEntity;
        }

        if(restCategoryService.updateCategory(newName, token, categoryDTO) == false){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return responseEntity;
    }

    @DeleteMapping("{token}/deleteCategory")
    public ResponseEntity<?> deleteCategory(@PathVariable String token, @RequestParam String delete){

        if(delete.isEmpty()|| delete.isBlank() || delete == null){
            return ResponseEntity.noContent().build() ;
        }
        restCategoryService.deleteCategory(token, delete);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
