package seaFood.PTseafood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.dto.CartItemRequest;
import seaFood.PTseafood.entity.CartItem;
import seaFood.PTseafood.exception.ResourceNotFoundException;
import seaFood.PTseafood.service.CartService;

@CrossOrigin
@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody CartItemRequest cartItemRequest) {
        try {
            CartItem cartItem = cartService.add(cartItemRequest);
            return ResponseEntity.ok(cartItem);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @GetMapping
//    public ResponseEntity<?> getCart() {
//        CartItem shoppingCart = CartService.getCart();
//        return ResponseEntity.ok(shoppingCart);
//    }
}
