package seaFood.PTseafood.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class CartItemRequest {

    private int quantity;

    private Long productId;

    private Long productVariantId;

}
