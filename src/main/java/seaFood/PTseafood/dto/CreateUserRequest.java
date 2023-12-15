package seaFood.PTseafood.dto;

import lombok.Data;
import seaFood.PTseafood.entity.Role;

import java.util.Set;

@Data
public class CreateUserRequest {
    private String email;
    private String password;
    private Set<String> roles;
    private boolean wholeSale;
}
