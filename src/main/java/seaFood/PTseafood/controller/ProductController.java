package seaFood.PTseafood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.entity.Category;
import seaFood.PTseafood.entity.Product;
import seaFood.PTseafood.exception.ResourceNotFoundException;
import seaFood.PTseafood.service.CategoryService;
import seaFood.PTseafood.service.ProductService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
public class ProductController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    //GetAll
    @GetMapping("/products")
    public List<Product> getAllProduct() {
        List<Product> products = productService.getAll();
        return products;
    }
    //Create
    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        try{
            Product addproduct= productService.addProduct(product);
            return ResponseEntity.ok(addproduct);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body("Thêm sản phẩm thất bại "+ ex.getMessage());
        }
    }
//    get by id
    @GetMapping("/product/{id}")
    public ResponseEntity<?> getById(@PathVariable  Long id){
        try {
            Product product = productService.getById(id);
            return ResponseEntity.ok(product);
        }catch (ResourceNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //update
    @PutMapping("/product/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,@RequestBody  Product productDetail,@RequestParam Long cateId){
        try {
            Product updateProduct = productService.updateProduct(id,productDetail,cateId);
            return ResponseEntity.ok(updateProduct);
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //Del
    @DeleteMapping("/product/{id}")
    public ResponseEntity<Map<String,Boolean>> deleteProduct(@PathVariable Long id){
            productService.deleteProduct(id);
            Map<String,Boolean> reponse = new HashMap<>();
            reponse.put("deleted",Boolean.TRUE);
            return ResponseEntity.ok(reponse);
    }
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String keyword) {
        List<Product> products = productService.findByNameContaining(keyword);
        return products;
    }

}
