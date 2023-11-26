package seaFood.PTseafood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.dto.OrderResponse;
import seaFood.PTseafood.entity.Order;
import seaFood.PTseafood.service.OrderService;
import seaFood.PTseafood.entity.OrderState;
import seaFood.PTseafood.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/order")
public class ManageOrderController {
    @Autowired
    private OrderService orderService;
    @PutMapping("/{orderId}/update-state")
    public ResponseEntity<String> updateOrderStateByAdmin(@PathVariable Long orderId) {
        try {
            OrderState updatedOrderState = orderService.updateOrderStateByAdmin(orderId);
            return ResponseEntity.ok("Cập nhật trạng thái đơn hàng thành công. Trạng thái mới: " + updatedOrderState.getState());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<?> getAll(){
        List<Order> orders = orderService.getAll();
        if(orders.isEmpty()){
            return ResponseEntity.badRequest().body("hiện tại chưa có đơn hàng nào!");
        }
        return ResponseEntity.ok(orders);
    }
    @GetMapping("/total-sales")
    public ResponseEntity<Double> getTotalSales() {
        double totalSales = orderService.getTotalSales();
        return ResponseEntity.ok(totalSales);
    }

    @GetMapping("/monthly-sales")
    public ResponseEntity<Double> getMonthlySales(@RequestParam int year, @RequestParam int month) {
        double monthlySales = orderService.getMonthlySales(year, month);
        return ResponseEntity.ok(monthlySales);
    }

    @GetMapping("/yearly-sales")
    public ResponseEntity<Double> getYearlySales(@RequestParam int year) {
        double yearlySales = orderService.getYearlySales(year);
        return ResponseEntity.ok(yearlySales);
    }
    @GetMapping("/count")
    public ResponseEntity<?> count(){
        Long all = orderService.countPaidOrders();
        return ResponseEntity.ok(all);
    }
    @GetMapping("/count/shippingstate")
    public ResponseEntity<?> countState(){
        Long all = orderService.countByShippingState();
        if(all==null){
            return ResponseEntity.ok(0);
        }
        return ResponseEntity.ok(all);
    }
    @GetMapping("/latest-order")
    public ResponseEntity<?> getLatestOrder(){
        List<Order> orders = orderService.getLatesOrder();
        if(orders.isEmpty()){
            return ResponseEntity.badRequest().body("Không có order nào");
        }
        List<OrderResponse> orderResponseList = new ArrayList<>();
        for(Order o : orders){
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setOrderState(o.getOrderStates());
            orderResponse.setCode(o.getCode());
            orderResponse.setId(o.getId());
            orderResponse.setPayment(o.getPaymentMethod());
            orderResponse.setFinalPrice(o.getFinalPrice());
            orderResponse.setReceiverAddress(o.getReceiverAddress());
            orderResponse.setReceiverEmail(o.getReceiverEmail());
            orderResponse.setReceiverName(o.getReceiverName());
            orderResponse.setReceiverPhone(o.getReceiverPhone());
            orderResponseList.add(orderResponse);
        }
        return ResponseEntity.ok(orderResponseList);
    }
}
