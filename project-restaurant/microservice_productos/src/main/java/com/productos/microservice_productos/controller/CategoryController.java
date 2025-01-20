package com.productos.microservice_productos.controller;

import com.productos.microservice_productos.model.entity.Category;
import com.productos.microservice_productos.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> categoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }
    @GetMapping("/products/{categoryId}")
    public ResponseEntity<?> getProductsByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getAllProductsByCategoryId(categoryId));
    }
    @PostMapping
    public ResponseEntity<?> saveCategory(@Valid @RequestBody Category newCategory) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(newCategory));
    }
    @PutMapping("/{idCategory}")
    public ResponseEntity<?> updateCategory(@PathVariable Long idCategory, @Valid @RequestBody Category newCategory) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.updateCategory(idCategory, newCategory));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();


    }
}
