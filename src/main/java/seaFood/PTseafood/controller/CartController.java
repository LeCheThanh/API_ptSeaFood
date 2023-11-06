package seaFood.PTseafood.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.dto.CartItemRequest;
import seaFood.PTseafood.entity.CartItem;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.exception.ResourceNotFoundException;
import seaFood.PTseafood.service.AuthenticationService;
import seaFood.PTseafood.service.CartService;
import seaFood.PTseafood.utils.JwtUtil;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody CartItemRequest cartItemRequest, HttpServletRequest request) {
        try {
            //get user from token
            User user = jwtUtil.getUserFromToken(request);
            System.out.println(user);
            CartItem cartItem = cartService.add(cartItemRequest, user);
            return ResponseEntity.ok("Thêm vào giỏ hàng thành công!");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/items")
    public ResponseEntity<?> getCartItems(HttpServletRequest request) {
        try {
            // Lấy thông tin người dùng từ token
            User user = jwtUtil.getUserFromToken(request);

            // Lấy danh sách các mục trong giỏ hàng của người dùng
            List<CartItem> cartItems = cartService.getCartItemsByUser(user);
            if(cartItems.isEmpty()){
                return ResponseEntity.badRequest().body("Giỏ hàng trống!");
            }
            return ResponseEntity.ok(cartItems);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/total")
    public ResponseEntity<?> getTotalCartValue(HttpServletRequest request) {
        try {
            // Lấy thông tin người dùng từ token
            User user = jwtUtil.getUserFromToken(request);

            // Tính tổng giá trị giỏ hàng
            double totalValue = cartService.getTotalCartValue(user);

            return ResponseEntity.ok(totalValue);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
