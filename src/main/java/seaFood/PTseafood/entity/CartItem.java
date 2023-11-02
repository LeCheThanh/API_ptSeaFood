package seaFood.PTseafood.entity;

import lombok.Data;

@Data
public class CartItem {
    private String cartItemId;
    private Long productId;
    private Long productVariantId;
    private int quantity;
    private double total;
    private Product product;
    private ProductVariant productVariant;
}
