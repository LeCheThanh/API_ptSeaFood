package seaFood.PTseafood.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.common.Enum;
import seaFood.PTseafood.dto.TransactionResponse;
import seaFood.PTseafood.entity.Order;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.entity.VnPayTransaction;
import seaFood.PTseafood.service.JwtService;
import seaFood.PTseafood.service.OrderService;
import seaFood.PTseafood.service.UserService;
import seaFood.PTseafood.service.VnPayTranService;
import seaFood.PTseafood.utils.JwtUtil;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@CrossOrigin
@RestController
@RequestMapping()
public class PaymentController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private VnPayTranService vnPayTranService;

    @Autowired
    private UserService userService;

    @GetMapping("/vnpayResult")
    public ResponseEntity<?> vnpayResult(@RequestParam("vnp_ResponseCode") String vnp_ResponseCode, HttpServletRequest request,
                                         @RequestParam String orderCode,
                                         @RequestParam("vnp_TransactionNo") String vnp_TransactionNo,
                                         @RequestParam("vnp_Amount") String vnp_Amount,
                                         @RequestParam("vnp_PayDate") String vnp_PayDate) {
        User user  = jwtUtil.getUserFromToken(request);
        TransactionResponse transactionResponse = new TransactionResponse();
        Order order = orderService.getByCode(orderCode);
        VnPayTransaction vnPayTransaction = new VnPayTransaction();

        // Định dạng của chuỗi ngày giờ từ VnPay
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        // Chuyển đổi từ chuỗi sang LocalDateTime
        LocalDateTime paymentTime = LocalDateTime.parse(vnp_PayDate, formatter);
        //String sang doub
        double paymentAmount = Double.parseDouble(vnp_Amount);
        // nếu thành công
        if (vnp_ResponseCode.equals("00")) {
            transactionResponse.setStatus("Ok");
            transactionResponse.setMessage("Successfully");
            if(order==null){
                return ResponseEntity.badRequest().body("Không tìm thấy order!");
            }
            order.setPaymentStatus(Enum.PaymentStatus.PAID.getName());
            // Thêm Order States "ĐÃ THANH TOÁN"
            orderService.saveOrder(order, user);
            // Cộng tiền đã mua vào user
            double adjustedPaymentAmount = paymentAmount / 100.0;
            BigInteger purchaseAmount = BigInteger.valueOf((long) adjustedPaymentAmount); // Chuyển đổi từ double sang BigInteger
            userService.updateTotalUserPurchase(user, purchaseAmount);

            //lưu vnpaytransaction
            vnPayTransaction.setPaymentStatus(Enum.PaymentStatus.PAID.getName());
            vnPayTransaction.setResponseCode(vnp_ResponseCode);
            vnPayTransaction.setPaymentTime(paymentTime);
            vnPayTransaction.setTransactionCode(vnp_TransactionNo);
            vnPayTransaction.setOrderCode(orderCode);
            vnPayTransaction.setOrder(order);
            vnPayTransaction.setPaymentAmount(paymentAmount);
            vnPayTranService.save(vnPayTransaction);
            return ResponseEntity.ok("Thanh toán thành công");
        }
        //thanh toán thất bại!!
        vnPayTransaction.setResponseCode(vnp_ResponseCode);
        vnPayTransaction.setPaymentTime(paymentTime);
        vnPayTransaction.setTransactionCode(vnp_TransactionNo);
        vnPayTransaction.setOrderCode(orderCode);
        vnPayTransaction.setOrder(order);
        vnPayTransaction.setPaymentAmount(paymentAmount);
        vnPayTransaction.setPaymentStatus(Enum.PaymentStatus.UNPAID.getName());
        vnPayTranService.save(vnPayTransaction);

        transactionResponse.setStatus("No");
        transactionResponse.setMessage("Failed");
        return ResponseEntity.internalServerError().body("Thanh toán thất bại");
    }
    @GetMapping("/momoResult")
    public ResponseEntity<?> momoResult(){
        return ResponseEntity.ok("2222");
    }
}
