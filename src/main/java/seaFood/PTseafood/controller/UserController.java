package seaFood.PTseafood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.dto.RegisterRequest;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.repository.IUserRepository;
//import seaFood.PTseafood.service.UserService;
import seaFood.PTseafood.service.UserServiceImpl;
import seaFood.PTseafood.utils.EmailValidator;
import seaFood.PTseafood.utils.PasswordUtil;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest){
        Optional<User> existingUser = userRepository.findbyEmail(registerRequest.getEmail());
        if(!EmailValidator.validateEmail(registerRequest.getEmail())){
            return ResponseEntity.badRequest().body("Email không đúng định dạng");
        }
        if(existingUser.isPresent()){
            return ResponseEntity.badRequest().body("Email đã tồn tại");
        }
        if(!(PasswordUtil.isStrongPassword(registerRequest.getPassword()))){
            return ResponseEntity.badRequest().body("Mật khẩu chưa đủ mạnh");
        }
        userService.saveUser(registerRequest);
        return ResponseEntity.ok("đăng kí thành công");
    }

}
