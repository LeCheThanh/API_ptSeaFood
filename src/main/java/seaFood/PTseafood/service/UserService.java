package seaFood.PTseafood.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import seaFood.PTseafood.common.Enum;
import seaFood.PTseafood.dto.RegisterRequest;
import seaFood.PTseafood.dto.UpdateUserRequest;
import seaFood.PTseafood.entity.Role;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.exception.ResourceNotFoundException;
import seaFood.PTseafood.repository.IRoleRepository;
import seaFood.PTseafood.repository.IUserRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
//    @Override
    public void saveUser(RegisterRequest registerRequest) {
        var user = new User();
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setRank(Enum.Rank.unRank);
        user.setProvider(Enum.Provider.LOCAL);
        userRepository.save(user);
        String email = user.getEmail();
        if( email != null)
        {
            this.addtoUser(email, Enum.Role.User);
        }
    }
    //update thong tin co ban
    public User updateUser(Long userId, UpdateUserRequest updatedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Cập nhật thông tin người dùng với dữ liệu từ updatedUser
        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setAddress(updatedUser.getAddress());
        // Lưu thông tin người dùng đã cập nhật
        return userRepository.save(existingUser);
    }
    public Optional<User> getById(Long id){
        return userRepository.findById(id);
    }
//    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }
//    @Override
    public void addtoUser(String email, Enum.Role rolename) {
        User user = userRepository.findbyEmail(email).get();
        Role role = roleRepository.findByName(rolename.getName());
        user.getRoles().add(role);
    }


}

