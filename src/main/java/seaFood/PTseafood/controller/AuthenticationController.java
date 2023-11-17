package seaFood.PTseafood.controller;

import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.auth.AuthenticationReponse;
import seaFood.PTseafood.auth.AuthenticationRequest;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.repository.IUserRepository;
import seaFood.PTseafood.service.AuthenticationService;
import seaFood.PTseafood.utils.EmailValidator;
import seaFood.PTseafood.utils.JwtUtil;
import top.jfunc.json.JsonObject;

import java.util.List;

@RestController
@RequestMapping("/api/admin/auth")
@AllArgsConstructor
@CrossOrigin
public class AuthenticationController {
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest){
        JSONObject responseJson = new JSONObject();
        try {
            AuthenticationReponse response = authenticationService.authenticationReponse(authenticationRequest);
            String token = response.getToken();
            String refreshToken = response.getRefreshToken();
            List<String> roles  = jwtUtil.getRoleFromToken(token);
            System.out.println("XXXXXXXXXXX"+roles);
            // Kiểm tra vai trò
            if (roles.contains("Admin") || roles.contains("Manager")) {
                responseJson.put("message","Đăng nhập thành công");
                responseJson.put("token",token);
                responseJson.put("refreshToken",refreshToken);
                return ResponseEntity.ok(responseJson.toString());
            } else {
                responseJson.put("message","Vai trò không hợp lệ");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseJson.toString());
            }
        } catch (RuntimeException e) {
            if(authenticationRequest.getEmail().isEmpty() & authenticationRequest.getPassword().isEmpty()){
                responseJson.put("message","Không được trống");
                return ResponseEntity.badRequest().body(responseJson.toString());
            }
            if(authenticationRequest.getEmail().isEmpty() || authenticationRequest.getPassword().isEmpty()){
                responseJson.put("message","Không được để trống email hoặc mật khẩu");
                return ResponseEntity.badRequest().body(responseJson.toString());
            }
            if(!EmailValidator.validateEmail(authenticationRequest.getEmail())){
                responseJson.put("message","Email không đúng định dạng!");
                return ResponseEntity.badRequest().body(responseJson.toString());
            }
            responseJson.put("message","Sai email hoặc mật khẩu");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseJson.toString());
        }
    }
    @PostMapping("/test-request")
    public ResponseEntity<String> testPostRequest() {
        return ResponseEntity.ok("POST request successful");
    }

}
