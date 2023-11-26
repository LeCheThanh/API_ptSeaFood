package seaFood.PTseafood.dto;

import lombok.Data;
import seaFood.PTseafood.entity.Product;

import java.util.Optional;

@Data
public class ProductProductVariantRequest {
    private Product product;
    private Optional<ProductVariantRequest> productVariantRequest;
}
