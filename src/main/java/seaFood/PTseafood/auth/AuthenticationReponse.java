package seaFood.PTseafood.auth;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationReponse {
    private String token;
    private String refreshToken;
}
