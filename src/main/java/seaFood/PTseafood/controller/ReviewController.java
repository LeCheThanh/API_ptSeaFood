package seaFood.PTseafood.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.dto.ReviewRequest;
import seaFood.PTseafood.entity.Product;
import seaFood.PTseafood.entity.Review;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.service.ProductService;
import seaFood.PTseafood.service.ReviewService;
import seaFood.PTseafood.utils.JwtUtil;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductService productService;

    @Autowired
    JwtUtil jwtUtil;
    @PostMapping
    public ResponseEntity<?> createReview(HttpServletRequest httpRequest, @RequestBody ReviewRequest reviewRequest) {
        try{
            User user = jwtUtil.getUserFromToken(httpRequest);
            if(reviewRequest.getContent()==""){
                return ResponseEntity.badRequest().body("Hãy đánh giá gì đó!");
            }
            Review review = reviewService.createReview(user,reviewRequest);
            return ResponseEntity.ok(review);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getReviewsByProduct(@PathVariable Long productId) {
        try{
            Product product = productService.getById(productId);
            List<Review> reviews= reviewService.getReviewsByProduct(product);
            if(reviews.isEmpty()){
                return ResponseEntity.ok(0);
            }
            return ResponseEntity.ok(reviews);

        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("/all/{productId}")
    public  ResponseEntity<?> getAllRating(@PathVariable Long productId){
        try{
            Product product = productService.getById(productId);
            Double rating = reviewService.calculateAverageRating(product);
            return ResponseEntity.ok(rating);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("/count/{productId}")
    public  ResponseEntity<?> getCount(@PathVariable Long productId){
        try{
            Product product = productService.getById(productId);
            Long count = reviewService.countReviewsForProduct(product);
            if(count == 0 ){
                return ResponseEntity.ok(0);
            }
            return ResponseEntity.ok(count);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
