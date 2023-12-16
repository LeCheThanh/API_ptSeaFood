package seaFood.PTseafood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seaFood.PTseafood.entity.Category;
import seaFood.PTseafood.service.CategoryService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    //GetAll
        @GetMapping("/categories")
    public List<Category> showAllCategories() {
        List<Category> categories = categoryService.getAll();
        return categories;
    }
}
