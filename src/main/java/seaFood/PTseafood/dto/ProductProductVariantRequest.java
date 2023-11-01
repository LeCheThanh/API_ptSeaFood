package seaFood.PTseafood.dto;

import lombok.Data;
import seaFood.PTseafood.entity.Product;

@Data
public class ProductProductVariantRequest {
    private Product product;
    private ProductVariantRequest productVariantRequest;
}
