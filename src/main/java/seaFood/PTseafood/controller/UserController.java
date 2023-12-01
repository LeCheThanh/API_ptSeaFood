package seaFood.PTseafood.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.dto.RegisterRequest;
import seaFood.PTseafood.dto.UpdateUserRequest;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.exception.ResourceNotFoundException;
import seaFood.PTseafood.repository.IUserRepository;
import seaFood.PTseafood.service.UserService;
import seaFood.PTseafood.utils.EmailValidator;
import seaFood.PTseafood.utils.JwtUtil;
import seaFood.PTseafood.utils.PasswordUtil;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest){
        Optional<User> existingUser = userRepository.findbyEmail(registerRequest.getEmail());
        if(!EmailValidator.validateEmail(registerRequest.getEmail())){
            return ResponseEntity.badRequest().body("Email không đúng định dạng!");
        }
        if(existingUser.isPresent()){
            return ResponseEntity.badRequest().body("Email đã tồn tại!");
        }
        if(!(PasswordUtil.isStrongPassword(registerRequest.getPassword()))){
            return ResponseEntity.badRequest().body("Mật khẩu phải chứa ít nhất 8 kí tự bao gồm số, chữ hoa, chữ thường và ký tự đặc biệt!");
        }
        if(!(registerRequest.getConfirmPassword().equals(registerRequest.getPassword()))){
            return ResponseEntity.badRequest().body("Mật khẩu và và xác nhận mật khẩu không khớp!");
        }
        userService.saveUser(registerRequest);
        return ResponseEntity.ok("đăng kí thành công");
    }
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequest updatedUser) {
        try {
            User updatedUserInfo = userService.updateUser(userId, updatedUser);
            return ResponseEntity.ok("Cập nhật thành công\n"+updatedUser);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/info")
    public ResponseEntity<?> getUser(HttpServletRequest request){
        try{
            User user = jwtUtil.getUserFromToken(request);
            if(user == null){
                return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không tìm thấy người dùng");
            }
            return  ResponseEntity.ok(user);
        }catch (RuntimeException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
