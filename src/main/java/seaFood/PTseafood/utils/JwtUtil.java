package seaFood.PTseafood.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.service.UserService;

import java.util.List;
import java.util.Optional;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String Secret_key;

    @Autowired
    private UserService userService;

    public DecodedJWT decodeJWT(String token) {
        return JWT.decode(token);
    }

    public String extractUsername(String token) {
        DecodedJWT decodedJWT = decodeJWT(token);
        return decodedJWT.getSubject();
    }

    public List<String> extractRoles(String token) {
        DecodedJWT decodedJWT = decodeJWT(token);
        Claim rolesClaim = decodedJWT.getClaim("roles");
        return rolesClaim.asList(String.class);
    }

    public User getUserFromToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String token = authorizationHeader.substring("Bearer ".length());
                // Giải mã token và trích xuất thông tin người dùng
                // Ví dụ: Sử dụng thư viện JWT để giải mã token
                DecodedJWT decodedJWT = JWT.decode(token);
                String username = decodedJWT.getSubject();
                List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
                Optional<User> checkUser = userService.getByEmail(username);
                User user = checkUser.get();
                return user;
            } catch (Exception ex) {
                // Xử lý lỗi nếu cần
            }
        }
        return null;
    }

    public List<String> getRoleFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
        return roles;
    }
}
