package seaFood.PTseafood.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.entity.Favorite;
import seaFood.PTseafood.entity.Product;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.service.FavoriteService;
import seaFood.PTseafood.service.ProductService;
import seaFood.PTseafood.service.UserService;
import seaFood.PTseafood.utils.JwtUtil;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/favorite")
public class        FavoriteController {
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    @Autowired
    private JwtUtil jwtUtil;

    //GetAll
    @GetMapping("/all")
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        try{
            User user = jwtUtil.getUserFromToken(request);
            List<Favorite> favorites = favoriteService.getFavoritesByUser(user);
            if(favorites.isEmpty()){
                return ResponseEntity.ok().body(0);
            }
            return ResponseEntity.ok(favorites);
        }catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    //add to favorite
    @PostMapping("/add")
    public ResponseEntity<?> addToFavorite(HttpServletRequest request, @RequestParam Long productId){
        // Lấy thông tin người dùng từ token
        try {
            User user = jwtUtil.getUserFromToken(request);
            Product product = productService.getById(productId);
            if (product == null) {
                return ResponseEntity.badRequest().body("sai productid");
            }
            Favorite favorite = favoriteService.addToFavorites(user, product);
            return ResponseEntity.ok(favorite);
        }catch (RuntimeException e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //xoa
    @DeleteMapping("/delete")
    public ResponseEntity<?> delFavorite(HttpServletRequest request,@RequestParam Long id){
        try{
            User user = jwtUtil.getUserFromToken(request);
            Product product = productService.getById(id);
            favoriteService.removeFromFavorites(user,product);
            List<Favorite> favorites = favoriteService.getFavoritesByUser(user);
            return ResponseEntity.ok(favorites);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
