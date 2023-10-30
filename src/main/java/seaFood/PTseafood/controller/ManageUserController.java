package seaFood.PTseafood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.exception.ResourceNotFoundException;
import seaFood.PTseafood.exception.RoleNotFoundException;
import seaFood.PTseafood.service.UserService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
public class ManageUserController {
    @Autowired
    private UserService userService;

    @PutMapping("/user/{email}/roles")
    public ResponseEntity<?> updateRolesForUser(@PathVariable String email, @RequestBody List<String> newRoles) {
        try {
            userService.updateRolesForUser(email, newRoles);
            return ResponseEntity.ok("Cập nhật vai trò thành công");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RoleNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
//    @PostMapping("/user/test")
//    public ResponseEntity<String> test(){
//        return ResponseEntity.ok("Thành công");
//    }

//    @Scheduled(cron = "* * * * * ?")// mỗi giây/test
    @Scheduled(cron = "0 0 0 * * ?") // Lên lịch chạy mỗi ngày vào lúc 00:00:00
    public void updateRanks() {
        System.out.println("Scheduled task is running.");
        userService.updateRankForUsers();
    }
    @Scheduled(cron = "* * * * * ?")// Cập nhật theo rank ngay lập tức(mỗi giây)
    public void updateDiscount() {
        userService.updateDiscountBasedOnRank();
    }

}
