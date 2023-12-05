package seaFood.PTseafood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.dto.CreateUserRequest;
import seaFood.PTseafood.entity.Role;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.exception.ResourceNotFoundException;
import seaFood.PTseafood.exception.RoleNotFoundException;
import seaFood.PTseafood.repository.IRoleCustomRepository;
import seaFood.PTseafood.repository.IUserRepository;
import seaFood.PTseafood.service.UserService;
import seaFood.PTseafood.utils.EmailValidator;
import seaFood.PTseafood.utils.PasswordUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@CrossOrigin
@RestController
@RequestMapping("/api/admin")
public class ManageUserController {
    @Autowired
    private UserService userService;

    @Autowired
    private IRoleCustomRepository customRepository;

    @Autowired
    private IUserRepository userRepository;

    @PutMapping("/user/{email}/roles")
    public ResponseEntity<?> updateRolesForUser(@PathVariable String email, @RequestBody List<String> newRoles) {
        try {
            userService.updateRolesForUser(email, newRoles);
            Optional<User> user = userService.getByEmail(email);
            User user1 = user.get();
            List<Role> roles=customRepository.getRole(user1);
            user1.setRoles(new HashSet<>(roles));
            return ResponseEntity.ok(user1);
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

    @Scheduled(cron = "* * * * * ?")// mỗi giây/test
//    @Scheduled(cron = "0 0 0 * * ?") // Lên lịch chạy mỗi ngày vào lúc 00:00:00
    public void updateRanks() {
        userService.updateRankForUsers();
    }
    @Scheduled(cron = "* * * * * ?")// Cập nhật theo rank ngay lập tức(mỗi giây)
    public void updateDiscount() {
        userService.updateDiscountBasedOnRank();
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<?> updateWhosale(@PathVariable Long id, @RequestParam boolean newStatus){
        try{
            userService.updateWhosale(id,newStatus);
            Optional<User> user = userService.getById(id);
            User user1 = user.get();
            return ResponseEntity.ok(user1);
        }
        catch (ResourceNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/users")
    public ResponseEntity<?> getAll(){
        List<User> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<?>getUserById(@PathVariable Long id){
        Optional<User> user = userService.getById(id);
        User user1 = user.get();
        List<Role> roles=customRepository.getRole(user1);
        user1.setRoles(new HashSet<>(roles));
        return ResponseEntity.ok(user1);
    }
    @PostMapping("/user")
    public ResponseEntity<?>createUser(@RequestBody CreateUserRequest createUserRequest){
        try{
            Optional<User> existingUser = userRepository.findbyEmail(createUserRequest.getEmail());
            if(!EmailValidator.validateEmail(createUserRequest.getEmail())){
                return ResponseEntity.badRequest().body("Email không đúng định dạng!");
            }
            if(existingUser.isPresent()){
                return ResponseEntity.badRequest().body("Email đã tồn tại!");
            }
            if(!(PasswordUtil.isStrongPassword(createUserRequest.getPassword()))){
                return ResponseEntity.badRequest().body("Mật khẩu phải chứa ít nhất 8 kí tự bao gồm số, chữ hoa, chữ thường và ký tự đặc biệt!");
            }
            User user = userService.createUser(createUserRequest);
            return ResponseEntity.ok(user);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }
}
