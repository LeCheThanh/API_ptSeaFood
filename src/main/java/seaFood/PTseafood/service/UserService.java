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
import seaFood.PTseafood.exception.RoleNotFoundException;
import seaFood.PTseafood.repository.IRoleRepository;
import seaFood.PTseafood.repository.IUserRepository;

import java.math.BigInteger;
import java.util.*;

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
        user.setWholeSale(false);
        user.setTotalPurchaseAmount(new BigInteger("0"));
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
    public Optional<User> getByEmail(String email){return userRepository.findbyEmail(email);}
    public void addtoUser(String email, Enum.Role rolename) {
        User user = userRepository.findbyEmail(email).get();
        Role role = roleRepository.findByName(rolename.getName());
        user.getRoles().add(role);
    }
    ///////////////////Admin//////////////////
    public void updateRolesForUser(String email, List<String> newRoles) {
        User user = userRepository.findbyEmail(email).orElse(null);
        if (user == null) {
            throw new ResourceNotFoundException("Người dùng không tồn tại");
        }

        Set<Role> updatedRoles = new HashSet<>();
        for (String roleName : newRoles) {
            Role role = roleRepository.findByName(roleName);
            if (role != null) {
                updatedRoles.add(role);
            } else {
                var message = "Vai trò không tồn tại";
                throw new RoleNotFoundException(message, roleName);
            }
        }
        user.setRoles(updatedRoles);
        System.out.println(updatedRoles);
//        addtoUser()
        userRepository.save(user);
    }
    ///---------------------------------
    @Transactional
    public void updateRankForUsers() {
        List<User> users = userRepository.findAll();
        BigInteger speding = new BigInteger("5000000"); // đặt biến tiêu dùng của user
        for (User user : users) {
            if (user.getTotalPurchaseAmount().compareTo(speding) >= 0) {
                user.setRank(Enum.Rank.Silver);
                if (user.getTotalPurchaseAmount().compareTo(speding.multiply(new BigInteger("2"))) >= 0) {
                    user.setRank(Enum.Rank.Gold);
                }
                if (user.getTotalPurchaseAmount().compareTo(speding.multiply(new BigInteger("4"))) >= 0) {
                    user.setRank(Enum.Rank.Platinum);
                }
            } else {
                user.setRank(Enum.Rank.unRank);
            }
            userRepository.save(user);
        }
    }
    public void updateDiscountBasedOnRank() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
                Enum.Rank rank = user.getRank();
                int discountRate = 0;

                if (rank == Enum.Rank.Silver) {
                    discountRate = 10; // Đặt giảm giá 10% cho Silver
                } else if (rank == Enum.Rank.Gold) {
                    discountRate = 15; // Đặt giảm giá 15% cho Gold
                } else if (rank == Enum.Rank.Platinum) {
                    discountRate = 20; // Đặt giảm giá 20% cho Platinum
                }
            user.setDiscountRate(discountRate);
            userRepository.save(user);
        }
    }

    public void updateWhosale(Long id,boolean newStatus){
        Optional<User> existingUser = userRepository.findById(id);
        if(existingUser.isPresent()){
            User user = existingUser.get();
            user.setWholeSale(newStatus);
            userRepository.save(user);
        }else throw new ResourceNotFoundException("User not found");
    }

    //thay đổi đều được thực hiện, có lỗi xảy ra ở đâu đó thì sẽ dừng phương thức
    @Transactional
    public void updateTotalUserPurchase(User user, BigInteger  purchaseAmount) {
        // Lấy thông tin người dùng từ cơ sở dữ liệu
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + user.getId()));

        // Cập nhật giá trị total_user_purchase
        existingUser.setTotalPurchaseAmount(existingUser.getTotalPurchaseAmount().add(purchaseAmount));

        // Lưu cập nhật vào cơ sở dữ liệu
        userRepository.save(existingUser);
    }

}

