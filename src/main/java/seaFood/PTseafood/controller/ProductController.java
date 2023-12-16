package seaFood.PTseafood.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.entity.Product;
import seaFood.PTseafood.service.ProductService;
import seaFood.PTseafood.service.ProductVariantService;

import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin
@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductVariantService productVariantService;
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam("keyword") String keyword)  { // dùng chấm hỏi chỗ <?> để cho
        // kiểu đại diện vì phải trả về message và list book!
        // Xử lý tìm kiếm và trả về kết quả dưới dạng List<Book>
        List<Product> searchResults = performSearch(keyword);
        if (searchResults.isEmpty()) {
            JSONObject responseJson = new JSONObject();
            responseJson.put("message", "Không tìm thấy sản phẩm cần search!");

            return ResponseEntity.badRequest().body(responseJson.toString());
        }
        JSONArray jsonArr = new JSONArray();
        for (Product product : searchResults) {
            JSONObject productJson = new JSONObject();
            productJson.put("name", product.getName());
            productJson.put("description", product.getDescription());
            productJson.put("image", product.getImage());
            productJson.put("slug", product.getSlug());
            jsonArr.add(productJson);
        }
        return ResponseEntity.ok(jsonArr.toString());
    }

    public List<Product> performSearch(String keyword) {
        // Thực hiện tìm kiếm trong danh sách đối tượng
        List<Product> searchResults = productService.getAll().stream()
                .filter(m -> m.getName().toUpperCase().contains(keyword.toUpperCase())
                        || m.getDescription().toUpperCase().contains(keyword.toUpperCase()))
                .collect(Collectors.toList());

        return searchResults;
    }

    @GetMapping("/top-selling")
    public ResponseEntity<List<Product>> getTopSellingProducts() {
        List<Product> topSellingProducts = productVariantService.getTopSellingProducts();
        return ResponseEntity.ok(topSellingProducts);
    }
    @GetMapping("/products")
    public List<Product> getAllProduct() {
        List<Product> products = productService.getAll();
        return products;
    }
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products =productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }
    @GetMapping("/{slug}")
    public ResponseEntity<?> getProductBySlug(@PathVariable String slug) {
        Product product =productService.getProductBySlug(slug);
        if(product == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }
}
