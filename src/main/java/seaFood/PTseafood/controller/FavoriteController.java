package seaFood.PTseafood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.entity.Favorite;
import seaFood.PTseafood.entity.Product;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.service.FavoriteService;
import seaFood.PTseafood.service.ProductService;
import seaFood.PTseafood.service.UserService;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {
    @Autowired
    FavoriteService favoriteService;
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;

    //GetAll
    @GetMapping("/all/{id}")
    public ResponseEntity<?> getAll(@PathVariable Long id) {
        try{
            List<Favorite> favorites = favoriteService.getFavoritesByUser(id);
            if(favorites.isEmpty()){
                return ResponseEntity.badRequest().body("Không có sản phẩm yêu thích");
            }
            return ResponseEntity.ok(favorites);
        }catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    //add to favorite
    @PostMapping("/add")
    public ResponseEntity<?> addToFavorite(@RequestParam Long userId, @RequestParam Long productId){
        Optional<User> userOptional = userService.getById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("sai user id");
        }
        User user = userOptional.get();
        Product product = productService.getById(productId);
        if (product == null) {
            return ResponseEntity.badRequest().body("sai productid");
        }
        Favorite favorite = favoriteService.addToFavorites(user, product);
        return ResponseEntity.ok(favorite);
    }
}
