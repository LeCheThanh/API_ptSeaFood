package seaFood.PTseafood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.service.OrderService;
import seaFood.PTseafood.entity.OrderState;
import seaFood.PTseafood.exception.ResourceNotFoundException;

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
}
