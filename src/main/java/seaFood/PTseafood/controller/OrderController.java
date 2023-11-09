package seaFood.PTseafood.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.dto.OrderRequest;
import seaFood.PTseafood.entity.Order;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.service.OrderService;
import seaFood.PTseafood.utils.JwtUtil;
import seaFood.PTseafood.utils.PhoneNumberValidator;

import static seaFood.PTseafood.utils.PhoneNumberValidator.validateVNPhoneNumber;

@CrossOrigin
@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private JwtUtil jwtUtill;
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request) {
        try {
            User user = jwtUtill.getUserFromToken(request);
            Order order = orderService.create(orderRequest, user);
            if(PhoneNumberValidator.validateVNPhoneNumber(orderRequest.getReceiverPhone()) == false){
                return ResponseEntity.badRequest().body("Số điện thoại không hợp lệ");
            }
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
