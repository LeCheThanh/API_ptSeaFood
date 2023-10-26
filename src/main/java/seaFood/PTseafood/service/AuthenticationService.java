package seaFood.PTseafood.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import seaFood.PTseafood.auth.AuthenticationReponse;
import seaFood.PTseafood.auth.AuthenticationRequest;
import seaFood.PTseafood.entity.Role;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.repository.IRoleCustomRepository;
import seaFood.PTseafood.repository.IUserRepository;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
//xu ly token
public class AuthenticationService {

    private final IUserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final IRoleCustomRepository roleCustomRepository;
    private final JwtService jwtService;
    public AuthenticationReponse authenticationReponse(AuthenticationRequest authenticationRequest){
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail()
//                ,authenticationRequest.getPassword()));
//        User user = userRepository.findbyEmail(authenticationRequest.getEmail()).orElseThrow();
//        List<Role> role = null;
//        if(user != null){
//            role = roleCustomRepository.getRole(user);
//        }
//        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        Set<Role> set = new HashSet<>();
//        role.stream().forEach(r->set.add(new Role(r.getName())));
//        user.setRoles(set);
//        set.stream().forEach(r->authorities.add(new SimpleGrantedAuthority(r.getName())));
//        var jwtToken = jwtService.generateToken(user, authorities);
//        var jwtRefreshToken = jwtService.generateRefreshToken(user, authorities);
//        return AuthenticationReponse.builder().token(jwtToken).refreshToken(jwtRefreshToken).build();
//        try {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        User user = userRepository.findbyEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Role> role = null;
        if(user != null){
            role = roleCustomRepository.getRole(user);
        }
        // Xử lý và gán vai trò cho người dùng

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Set<Role> set = new HashSet<>();
        role.stream().forEach(r->set.add(new Role(r.getName())));
        user.setRoles(set);

         ///
        set.stream().forEach(r->authorities.add(new SimpleGrantedAuthority(r.getName())));
//        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        var jwtToken = jwtService.generateToken(user, authorities);
        var jwtRefreshToken = jwtService.generateRefreshToken(user, authorities);

        return AuthenticationReponse.builder()
                .token(jwtToken)
                .refreshToken(jwtRefreshToken)
                .build();
//        } catch (AuthenticationException e) {
//            // Xử lý ngoại lệ xác thực không thành công
//
//                throw new ExecutionException("Authentication failed: " + e.getMessage());
//        }

    }
}
