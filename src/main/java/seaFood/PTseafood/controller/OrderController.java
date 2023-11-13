package seaFood.PTseafood.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.common.Enum;
import seaFood.PTseafood.dto.OrderRequest;
import seaFood.PTseafood.entity.Order;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.entity.OrderState;
import seaFood.PTseafood.service.OrderService;
import seaFood.PTseafood.service.VnPayService;
import seaFood.PTseafood.utils.EmailValidator;
import seaFood.PTseafood.utils.JwtUtil;
import seaFood.PTseafood.utils.PhoneNumberValidator;
import seaFood.PTseafood.exception.ResourceNotFoundException;
import seaFood.PTseafood.dto.VnPayResponse;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private VnPayService vnPayService;


    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request) {
        try {
            User user = jwtUtil.getUserFromToken(request);
            if(orderRequest.getReceiverEmail() != null){
                if(EmailValidator.validateEmail(orderRequest.getReceiverEmail())==false){
                    return ResponseEntity.badRequest().body("Email không hợp lệ");
                }
            }
            if(PhoneNumberValidator.validateVNPhoneNumber(orderRequest.getReceiverPhone()) == false){
                return ResponseEntity.badRequest().body("Số điện thoại không hợp lệ");
            }
            if(!orderRequest.getPayment().equals("momo")&&!orderRequest.getPayment().equals("cash")&&!orderRequest.getPayment().equals("vnpay")){
                return ResponseEntity.badRequest().body("Phương thức thanh toán không hợp lệ");
            }
            Order order = orderService.process(orderRequest, user);
            if("vnpay".equalsIgnoreCase(orderRequest.getPayment())){
                    VnPayResponse vnPayResponse = vnPayService.paymentVnPay(order.getFinalPrice(), user,order.getCode());
                return ResponseEntity.ok(vnPayResponse.getURL());
            }
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{orderId}/update-state")
    public ResponseEntity<String> updateOrderStateByUser(@PathVariable Long orderId, HttpServletRequest request) {
        try {
            User user = jwtUtil.getUserFromToken(request);
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
            User user = jwtUtil.getUserFromToken(request);
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
