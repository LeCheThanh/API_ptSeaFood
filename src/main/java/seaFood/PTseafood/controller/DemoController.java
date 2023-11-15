package seaFood.PTseafood.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seaFood.PTseafood.service.AuthenticationService;

@RestController
@RequestMapping("/demo")
@AllArgsConstructor
public class DemoController {
    @Autowired
    private final AuthenticationService authenticationService;
    @PostMapping("/test")
    public ResponseEntity<String> login(){
        return ResponseEntity.ok("Authentication and authorization is succeded");
    }
}
