package seaFood.PTseafood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seaFood.PTseafood.entity.Role;
import seaFood.PTseafood.repository.IRoleRepository;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
public class ManageRoleController {
    @Autowired
    private IRoleRepository roleRepository;
    @GetMapping("/roles")
    public ResponseEntity<?>getAll(){
        List<Role> role = roleRepository.findAll();
        return ResponseEntity.ok(role);
    }
}
