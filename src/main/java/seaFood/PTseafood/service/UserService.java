package seaFood.PTseafood.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import seaFood.PTseafood.common.Enum;
import seaFood.PTseafood.dto.RegisterRequest;
import seaFood.PTseafood.entity.Role;
import seaFood.PTseafood.entity.User;
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
//        user.setRank(Enum.Rank.unRank);
//        user.setAddress(null);
//        user.setPhone(null);
//        user.setWholeSale(false);
//        user.setDiscountRate(0);
//        user.setFullName("");
//        user.setRank("");
//        user.setTotalPurchaseAmount(new BigInteger("0"));
//        user.setPassword(passwordEncoderl.encode(user.getPassword()));
          userRepository.save(user);
//        Long userId = userRepository.getUserIdByEmail(user.getEmail());
//        Long roleId = roleRepository.getRoleIdByName("user");
//        if(roleId != 0 && userId != 0)
//        {
//            userRepository.addRoleToUser(userId, roleId);
//        }
        String email = user.getEmail();
        if( email != null)
        {
            this.addtoUser(email, Enum.Role.User);
        }
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

