package seaFood.PTseafood.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.dto.OrderRequest;
import seaFood.PTseafood.entity.Order;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.entity.OrderState;
import seaFood.PTseafood.service.OrderService;
import seaFood.PTseafood.utils.JwtUtil;
import seaFood.PTseafood.utils.PhoneNumberValidator;
import seaFood.PTseafood.exception.ResourceNotFoundException;

import java.util.List;

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

            if(PhoneNumberValidator.validateVNPhoneNumber(orderRequest.getReceiverPhone()) == false){
                return ResponseEntity.badRequest().body("Số điện thoại không hợp lệ");
            }
            Order order = orderService.create(orderRequest, user);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{orderId}/update-state")
    public ResponseEntity<String> updateOrderStateByUser(@PathVariable Long orderId, HttpServletRequest request) {
        try {
            User user = jwtUtill.getUserFromToken(request);
            OrderState updatedOrderState = orderService.updateOrderStateByUser(orderId, user);
            return ResponseEntity.ok("Cập nhật trạng thái đơn hàng thành công. Trạng thái mới: " + updatedOrderState.getState());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/user")
    public ResponseEntity<?> getAllOrdersByUser(HttpServletRequest request) {
        try {
            User user = jwtUtill.getUserFromToken(request);
            List<Order> orders = orderService.getAllByUser(user);
            // Kiểm tra nếu danh sách đơn hàng rỗng
            if (orders.isEmpty()) {
                return ResponseEntity.badRequest().body("Người dùng này không có đơn hàng!");
            }
            // Trả về danh sách đơn hàng
            return ResponseEntity.ok(orders);
        } catch (ResourceNotFoundException e) {
            // Xử lý nếu không tìm thấy người dùng
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
