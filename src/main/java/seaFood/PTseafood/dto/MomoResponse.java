package seaFood.PTseafood.dto;

import lombok.Data;

@Data
public class MomoResponse {
    private String partnerCode;
    private String requestId;
    private String orderId;
    private Long amount;
    private Long responseTime;
    private String message;
    private int resultCode;
    private String payUrl;
}
