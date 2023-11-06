package seaFood.PTseafood.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.dto.ProductProductVariantRequest;
import seaFood.PTseafood.dto.ProductVariantRequest;
import seaFood.PTseafood.entity.Product;
import seaFood.PTseafood.entity.ProductVariant;
import seaFood.PTseafood.exception.ResourceNotFoundException;
import seaFood.PTseafood.service.CategoryService;
import seaFood.PTseafood.service.ProductService;
import seaFood.PTseafood.service.ProductVariantService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
public class ManageProductController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductVariantService productVariantService;
    //GetAll
    @GetMapping("/products")
    public List<Product> getAllProduct() {
        List<Product> products = productService.getAll();
        return products;
    }
    //Create

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestBody ProductProductVariantRequest productProductVariantRequest) {
        try{
            Product addproduct= productService.addProduct(productProductVariantRequest.getProduct());
            //tạo list productvariant mới
            List<ProductVariant> productVariants = new ArrayList<>();

            //gán giá trị từ request
            List<String> variantNames = productProductVariantRequest.getProductVariantRequest().getVariantName();
            List<Integer> variantQuantity = productProductVariantRequest.getProductVariantRequest().getVariantQuantity();
            List<String> variantPrices = productProductVariantRequest.getProductVariantRequest().getVariantPrice();
            List<String> variantWhosalePrice = productProductVariantRequest.getProductVariantRequest().getVariantWhosalePrice();
            List<String> variantDescriptions = productProductVariantRequest.getProductVariantRequest().getVariantDescription();
            List<String> variantImages = productProductVariantRequest.getProductVariantRequest().getVariantImage();

            // vòng lặp để lấy giá trị từ request
            for (int i = 0; i < variantNames.size(); i++) {
                double price = Double.parseDouble(variantPrices.get(i).replaceAll(",", ""));
                double whosale_price = Double.parseDouble(variantWhosalePrice.get(i).replaceAll(",", ""));
                ProductVariant variant = new ProductVariant();
                variant.setName(variantNames.get(i));
                variant.setStock(variantQuantity.get(i));
                variant.setPrice(price);
                variant.setWhosalePrice(whosale_price);
                variant.setImage(variantImages.get(i));
                variant.setDescription(variantDescriptions.get(i));
                variant.setProduct(productProductVariantRequest.getProduct());
                //lưu vào table ProductVariant
                productVariantService.add(variant);
                //lưu vào mảng đã tạo trước để gán vào product
                productVariants.add(variant);
            }
            // gán variants vào product
            // ~~ bị lỗi spring security do gán giá trị
//            addproduct.setProductVariants(productVariants);

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

    public List<Product> searchProducts(@RequestParam String keyword) {
        List<Product> products = productService.findByNameContaining(keyword);
        return products;
    }

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

}
