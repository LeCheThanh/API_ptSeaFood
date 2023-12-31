package seaFood.PTseafood.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

    private String receiverEmail;

    private String note;

    private String payment;
}
