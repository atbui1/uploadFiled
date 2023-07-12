package com.edu.uploadfile.mobiles.controllers;

import com.edu.uploadfile.mobiles.models.Category;
import com.edu.uploadfile.mobiles.payloads.response.ResponseObject;
import com.edu.uploadfile.mobiles.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "http://localhost:8082")
@RestController
@RequestMapping(path = "/api/v1")
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;


    /** get all category */
    @GetMapping("/categories")
    public ResponseEntity<ResponseObject> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.size() < 1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "not found category", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "get all catrgories successfully", categories)
        );
    }

    /** get category by id */
    @GetMapping("/category/{id}")
    public ResponseEntity<ResponseObject> getCategoryById(@PathVariable(name = "id") Long categoryId) {
        Optional category = categoryRepository.findById(categoryId);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("ok", "not found category id: " + categoryId, "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "get category id: " + categoryId + " successfully ", category)
        );
    }

    /** add new category */
    @PostMapping("/category")
    public ResponseEntity<ResponseObject> addNewCategory(@RequestBody Category category) {
        if (category.getName().trim().equals("") || category.getKeyword().trim().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    new ResponseObject("failed",  " name or keyword not null", "")
            );
        }
        if (categoryRepository.findByName(category.getName()).size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    new ResponseObject("failed",  " duplicate name:  " + category.getName(), "")
            );
        }
        if (categoryRepository.findByKeyword(category.getKeyword()).size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    new ResponseObject("failed",  " duplicate keyword:  " + category.getKeyword(), "")
            );
        }

        Category categoryMew = categoryRepository.save(new Category(category.getName().trim(), category.getKeyword().trim().toUpperCase()));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject("ok", "add new category success", categoryMew)
        );
    }

    /** update category by id */
    @PutMapping("/category/{id}")
    public ResponseEntity<ResponseObject> updateCategoryById(@PathVariable(name = "id") Long categoryId, @RequestBody Category category) {
        Optional<Category> _category = categoryRepository.findById(categoryId);
        if (!_category.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("ok", "not found category's  id: " + categoryId, "")
            );
        }
        if (categoryRepository.findByName(category.getName()).size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    new ResponseObject("ok", "exits category' name: " + category.getName(), "")
            );
        }
        if (categoryRepository.findByKeyword(category.getKeyword()).size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    new ResponseObject("ok", "exits category' keyword: " + category.getKeyword(), "")
            );
        }

        _category.map(element ->{
            element.setName(category.getName());
            element.setKeyword(category.getKeyword());
            return categoryRepository.save(element);
        });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "update success category's: " + categoryId, _category)
        );
    }

    /** delete category */
    @DeleteMapping("/category/{id}")
    public ResponseEntity<ResponseObject> deleteCategoryById(@PathVariable(name = "id") Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "not found category's id: " + categoryId, "")
            );
        }
        categoryRepository.deleteById(categoryId);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("failed", "delete success category's id: " + categoryId, "")
        );
    }

    /** delete all category */
    @DeleteMapping("/categories")
    public ResponseEntity<ResponseObject> deleteAllCategory() {
        categoryRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "delete all category success", "")
        );
    }
}
