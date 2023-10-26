package seaFood.PTseafood.controller;

import lombok.AllArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.auth.AuthenticationReponse;
import seaFood.PTseafood.auth.AuthenticationRequest;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.repository.IUserRepository;
import seaFood.PTseafood.service.AuthenticationService;
import seaFood.PTseafood.service.UserServiceImpl;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@CrossOrigin
public class AuthenticationController {
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private IUserRepository userRepository;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest){
        try {
            AuthenticationReponse response = authenticationService.authenticationReponse(authenticationRequest);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai email hoặc mật khẩu");
//            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/test-request")
    public ResponseEntity<String> testPostRequest() {
        return ResponseEntity.ok("POST request successful");
    }

}
