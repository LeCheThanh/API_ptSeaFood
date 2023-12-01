package seaFood.PTseafood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.entity.Category;
import seaFood.PTseafood.exception.ResourceNotFoundException;
import seaFood.PTseafood.service.CategoryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
public class ManageCategoryController {
    @Autowired
    private CategoryService categoryService;

    //GetAll
    @GetMapping("/categories")
    public List<Category> showAllCategories() {
        List<Category> categories = categoryService.getAll();
        return categories;
    }
    //Create
    @PostMapping("/category")
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        Category addcate= categoryService.addCategory(category);
        return ResponseEntity.ok(addcate);
    }
    //get by id
    @GetMapping("/category/{id}")
    public ResponseEntity<?> getCategory(@PathVariable  Long id){
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(category);
        }catch (ResourceNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //update category
    @PutMapping("/category/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id,@RequestBody  Category categoryDetail){
        try {
            Category category = categoryService.getCategoryById(id);
            category.setName(categoryDetail.getName());
            category.setDescription(categoryDetail.getDescription());
            Category updateCate = categoryService.updateCategory(category);
            return ResponseEntity.ok(updateCate);
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //Del category
    @DeleteMapping("/category/{id}")
    public ResponseEntity<Map<String,Boolean>> deleteProduct(@PathVariable Long id){
        categoryService.deleteCategory(id);
        Map<String,Boolean> reponse = new HashMap<>();
        reponse.put("deleted",Boolean.TRUE);
        return ResponseEntity.ok(reponse);
    }

}
