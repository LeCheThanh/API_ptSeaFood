package seaFood.PTseafood.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MomoRequest {
    @JsonProperty("partnerCode")
    private String partnerCode;

    @JsonProperty("partnerClientId")
    private String partnerClientId;

//    @JsonProperty("partnerName")
//    private String partnerName;

    @JsonProperty("storeId")
    private String storeId;

    @JsonProperty("requestId")
    private String requestId;

    @JsonProperty("amount")
    private long amount;

    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("orderInfo")
    private String orderInfo;

    @JsonProperty("redirectUrl")
    private String redirectUrl;

    @JsonProperty("ipnUrl")
    private String ipnUrl;

    @JsonProperty("lang")
    private String lang;

    @JsonProperty("extraData")
    private String extraData;

    @JsonProperty("requestType")
    private String requestType;

    @JsonProperty("signature")
    private String signature;

    @JsonProperty("orderGroupId")
    private String orderGroupId;

    @JsonProperty("subPartnerCode")
    private String subPartnerCode;

}
