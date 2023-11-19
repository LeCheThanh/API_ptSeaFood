package seaFood.PTseafood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.entity.Order;
import seaFood.PTseafood.service.OrderService;
import seaFood.PTseafood.entity.OrderState;
import seaFood.PTseafood.exception.ResourceNotFoundException;

import java.util.List;

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
        Long all = orderService.countAll();
        return ResponseEntity.ok(all);
    }
}
