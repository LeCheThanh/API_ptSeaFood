package seaFood.PTseafood.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import seaFood.PTseafood.config.MomoConfig;
import seaFood.PTseafood.dto.MomoRequest;
import seaFood.PTseafood.dto.MomoResponse;
import seaFood.PTseafood.entity.Order;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.repository.IOrderRepository;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class MomoService {
    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private RestTemplate restTemplate;

    private String endPoint = MomoConfig.endPoint;
    private String partnerCode = MomoConfig.partnerCode;
    private String accessKey = MomoConfig.accessKey;
    private String secretKey= MomoConfig.secretKey;
    private String ipnUrl= MomoConfig.ipnUrl;
    private String redirectUrl = MomoConfig.redirectUrl;
    public MomoResponse paymentMomo(double finalPrice, User user, String  code) throws NoSuchAlgorithmException, InvalidKeyException {
        String requestType = "captureWallet";
        String orderInfo = "Thanh toán hóa đơn";
        long amount = Math.round(finalPrice);
        String orderId = java.util.UUID.randomUUID().toString();
        String requestId = java.util.UUID.randomUUID().toString();
        String extraData = "";
        String rawHash = "accessKey=" + accessKey +
                "&amount=" + amount +
                "&extraData=" + extraData +
                "&ipnUrl=" + ipnUrl +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&partnerClientId=PTseafood" +
                "&partnerCode=" + partnerCode +
                "&redirectUrl=" + redirectUrl+"?orderCode="+code+
                "&requestId="+ requestId +
                "&requestType=" + requestType;

        System.out.println(accessKey);
        String signature = MomoConfig.signSHA256(rawHash, secretKey);
        System.out.println(signature);

        MomoRequest momoMessage = new MomoRequest();
        momoMessage.setPartnerCode(partnerCode);
        momoMessage.setPartnerClientId("PTseafood");
        momoMessage.setStoreId("PTseafood");
        momoMessage.setRequestId(requestId);
        momoMessage.setAmount(amount);
        momoMessage.setOrderId(orderId);
        momoMessage.setOrderInfo(orderInfo);
        momoMessage.setRedirectUrl(redirectUrl+"?orderCode="+code);
        momoMessage.setIpnUrl(ipnUrl);
        momoMessage.setLang("vi");
        momoMessage.setExtraData(extraData);
        momoMessage.setRequestType(requestType);
        momoMessage.setOrderGroupId("");
        momoMessage.setSubPartnerCode("");
        momoMessage.setSignature(signature);

        String result = restTemplate.postForObject(endPoint, momoMessage, String.class);
        System.out.println(result);
        Gson gson = new Gson();
        JsonObject jsonResult = gson.fromJson(result, JsonObject.class);

        String payUrl = jsonResult.get("payUrl").getAsString();
        int resultCode = jsonResult.get("resultCode").getAsInt();
        MomoResponse momoResponse = new MomoResponse();
        momoResponse.setPayUrl(payUrl);
        return momoResponse;
    }

}
