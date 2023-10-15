package seaFood.PTseafood.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import seaFood.PTseafood.entity.Role;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.repository.IRoleRepository;
import seaFood.PTseafood.repository.IUserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoderl;
    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoderl.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void addtoUser(String email, String roleName) {
        User user = userRepository.findbyEmail(email);
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }
}

