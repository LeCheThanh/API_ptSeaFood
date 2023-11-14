package seaFood.PTseafood.config;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MomoConfig {

    public static String endPoint = "https://test-payment.momo.vn/v2/gateway/api/create";
    public static String partnerCode = "MOMOOJOI20210710";
    public static String accessKey  ="iPXneGmrJH0G8FOP";
    public static  String secretKey = "sFcbSGRSJjwGxwhhcEktCHWYUuTuPNDB";
    public static  String ipnUrl = "https://webhook.site/8e055cf5-a2e1-408a-bcb6-1ec9ff5f5007";
    public static String redirectUrl = "http://localhost:8080/api/payment/momoResult";

    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    public static String signSHA256(String data, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA256_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(rawHmac);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("%02x", aByte));
        }
        return result.toString();
    }
}