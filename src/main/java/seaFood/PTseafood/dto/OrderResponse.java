package seaFood.PTseafood.dto;

import lombok.Data;
import seaFood.PTseafood.entity.OrderState;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private String code;
    private List<OrderState> orderState;
    private String receiverPhone;
    private String receiverName;
    private String receiverEmail;
    private String receiverAddress;
    private String payment;
    private Double finalPrice;
    private LocalDateTime createdAt;
}
