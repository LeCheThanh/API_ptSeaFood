package seaFood.PTseafood.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.common.Enum;
import seaFood.PTseafood.dto.TransactionResponse;
import seaFood.PTseafood.entity.MomoTransaction;
import seaFood.PTseafood.entity.Order;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.entity.VnPayTransaction;
import seaFood.PTseafood.service.MomoTranService;
import seaFood.PTseafood.service.OrderService;
import seaFood.PTseafood.service.UserService;
import seaFood.PTseafood.service.VnPayTranService;
import seaFood.PTseafood.utils.JwtUtil;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@CrossOrigin
@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private VnPayTranService vnPayTranService;

    @Autowired
    private MomoTranService momoTranService;

    @Autowired
    private UserService userService;
    // Định dạng của chuỗi ngày giờ
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    @GetMapping("/vnpayResult")
    public ResponseEntity<?> vnpayResult(@RequestParam("vnp_ResponseCode") String vnp_ResponseCode, HttpServletRequest request,
                                         @RequestParam String orderCode,
                                         @RequestParam("vnp_TransactionNo") String vnp_TransactionNo,
                                         @RequestParam("vnp_Amount") String vnp_Amount,
                                         @RequestParam("vnp_PayDate") String vnp_PayDate)
    {
        User user  = jwtUtil.getUserFromToken(request);
        TransactionResponse transactionResponse = new TransactionResponse();
        Order order = orderService.getByCode(orderCode);
        VnPayTransaction vnPayTransaction = new VnPayTransaction();

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
    public ResponseEntity<?> momoResult(HttpServletRequest request,
                                        @RequestParam("resultCode") String resultCode,
                                        @RequestParam String orderCode,
                                        @RequestParam("partnerCode") String partnerCode,
                                        @RequestParam("amount") String amount,
                                        @RequestParam("message") String message,
                                        @RequestParam("responseTime") String responseTime,
                                        @RequestParam("transId") String transId)
    {
        User user  = jwtUtil.getUserFromToken(request);
        TransactionResponse transactionResponse = new TransactionResponse();
        Order order = orderService.getByCode(orderCode);
        MomoTransaction momoTransaction = new MomoTransaction();

        Instant instant = Instant.ofEpochMilli(Long.parseLong(responseTime));
        //
        double paymentAmount = Double.parseDouble(amount);
        if(resultCode.equals("0")){
            transactionResponse.setStatus("Ok");
            transactionResponse.setMessage("Successfully");
            if(order==null){
                return ResponseEntity.badRequest().body("Không tìm thấy order!");
            }
            order.setPaymentStatus(Enum.PaymentStatus.PAID.getName());
            // Thêm Order States "ĐÃ THANH TOÁN"
            orderService.saveOrder(order, user);
            // Cộng tiền đã mua vào user
//            double adjustedPaymentAmount = paymentAmount / 100.0;
            BigInteger purchaseAmount = BigInteger.valueOf((long) paymentAmount); // Chuyển đổi từ double sang BigInteger
            userService.updateTotalUserPurchase(user, purchaseAmount);

            //lưu vnpaytransaction
            momoTransaction.setPaymentStatus(Enum.PaymentStatus.PAID.getName());
            momoTransaction.setMessage(message);
            momoTransaction.setResponseTime(Timestamp.from(instant));
            momoTransaction.setAmount(paymentAmount);
            momoTransaction.setOrderCode(orderCode);
            momoTransaction.setTransId(transId);
            momoTransaction.setResultCode(resultCode);
            momoTransaction.setOrder(order);
            momoTransaction.setPartnerCode(partnerCode);
            momoTranService.save(momoTransaction);
            return ResponseEntity.ok("Thanh toán thành công");
        }
        //thanh toán thất bại!!
        momoTransaction.setMessage(message);
        momoTransaction.setResponseTime(Timestamp.from(instant));
        momoTransaction.setAmount(paymentAmount);
        momoTransaction.setOrderCode(orderCode);
        momoTransaction.setTransId(transId);
        momoTransaction.setResultCode(resultCode);
        momoTransaction.setOrder(order);
        momoTransaction.setPartnerCode(partnerCode);
        momoTranService.save(momoTransaction);

        transactionResponse.setStatus("No");
        transactionResponse.setMessage("Failed");

        return ResponseEntity.internalServerError().body("Thanh toán thất bại");

    }
}
