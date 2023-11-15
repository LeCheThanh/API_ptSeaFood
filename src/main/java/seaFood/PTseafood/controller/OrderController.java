package seaFood.PTseafood.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.dto.MomoResponse;
import seaFood.PTseafood.dto.OrderRequest;
import seaFood.PTseafood.dto.VnPayResponse;
import seaFood.PTseafood.entity.Order;
import seaFood.PTseafood.entity.OrderState;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.exception.ResourceNotFoundException;
import seaFood.PTseafood.service.MailService;
import seaFood.PTseafood.service.MomoService;
import seaFood.PTseafood.service.OrderService;
import seaFood.PTseafood.service.VnPayService;
import seaFood.PTseafood.utils.EmailValidator;
import seaFood.PTseafood.utils.JwtUtil;
import seaFood.PTseafood.utils.PhoneNumberValidator;

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
    private MomoService momoService;

    @Autowired
    private MailService mailService;

    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request) {
        try {
            User user = jwtUtil.getUserFromToken(request);
            Order order = orderService.process(orderRequest, user);

            if(orderRequest.getReceiverEmail()==null && user.getEmail()==null){
                return ResponseEntity.badRequest().body("Email không được để trống");
            }
            if(orderRequest.getReceiverPhone()==null && user.getPhone()==null){
                return ResponseEntity.badRequest().body("SĐT không được để trống");
            }
            if(orderRequest.getReceiverName()==null && user.getFullName() == null){
                return ResponseEntity.badRequest().body("Tên người nhận không được để trống");
            }

            if(orderRequest.getReceiverEmail() != null){
                if(EmailValidator.validateEmail(orderRequest.getReceiverEmail())==false){
                    return ResponseEntity.badRequest().body("Email không hợp lệ");
                }
            }
            if(orderRequest.getReceiverPhone()!=null){
                if(PhoneNumberValidator.validateVNPhoneNumber(orderRequest.getReceiverPhone()) == false){
                    return ResponseEntity.badRequest().body("Số điện thoại không hợp lệ");
                }
            }
            if(!orderRequest.getPayment().equals("momo")&&!orderRequest.getPayment().equals("cash")&&!orderRequest.getPayment().equals("vnpay")){
                return ResponseEntity.badRequest().body("Phương thức thanh toán không hợp lệ");
            }
            if("vnpay".equalsIgnoreCase(orderRequest.getPayment())){
                    VnPayResponse vnPayResponse = vnPayService.paymentVnPay(order.getFinalPrice(), user,order.getCode());
                return ResponseEntity.ok(vnPayResponse.getURL());
            }
            if("momo".equalsIgnoreCase(orderRequest.getPayment())){
                MomoResponse momoResponse = momoService.paymentMomo(order.getFinalPrice(), user,order.getCode());
                return ResponseEntity.ok(momoResponse.getPayUrl());
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
    @PostMapping("/send-confirmation")
    public ResponseEntity<String> sendConfirmationEmail(HttpServletRequest request) {
        try {
            Order order = orderService.getByCode("16999660814225VGcG3");
            User user = jwtUtil.getUserFromToken(request);
            mailService.sendConfirmationEmail(order, user);
            return ResponseEntity.ok("gui mail thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send confirmation email.");
        }
    }

}
