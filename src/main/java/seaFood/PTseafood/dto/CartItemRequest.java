package seaFood.PTseafood.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private int quantity;
    private Long productId;
    private Long productVariantId;

}
