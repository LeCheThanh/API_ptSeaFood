package seaFood.PTseafood.service;

import seaFood.PTseafood.entity.Role;
import seaFood.PTseafood.entity.User;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addtoUser(String email,String roleName);
}
