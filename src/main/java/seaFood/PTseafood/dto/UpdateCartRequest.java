package seaFood.PTseafood.dto;

import lombok.Data;

@Data
public class UpdateCartRequest {
        private Long cartItemId;
        private int newQuantity;
}
