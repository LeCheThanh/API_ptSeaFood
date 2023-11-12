package seaFood.PTseafood.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.common.Enum;
import seaFood.PTseafood.entity.Order;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.service.JwtService;
import seaFood.PTseafood.service.OrderService;
import seaFood.PTseafood.utils.JwtUtil;

@CrossOrigin
@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/vnpayResult")
    public ResponseEntity<?> vnpayResult(@RequestParam("vnp_ResponseCode") String vnp_ResponseCode, HttpServletRequest request,
                                         @RequestParam String orderCode) {
        User user  = jwtUtil.getUserFromToken(request);
        if (vnp_ResponseCode.equals("00")) {
            Order order = orderService.getByCode(orderCode);
            if(order==null){
                return ResponseEntity.badRequest().body("Không tìm thấy order!");
            }
            order.setPaymentStatus(Enum.PaymentStatus.PAID.getName());
            // Thêm Order States "ĐÃ THANH TOÁN"
            orderService.saveOrder(order, user);
            return ResponseEntity.ok("Thanh toán thành công");
        }
        return ResponseEntity.internalServerError().body("Thanh toán thất bại");
    }
}
