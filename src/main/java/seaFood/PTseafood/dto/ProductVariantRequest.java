package seaFood.PTseafood.dto;

import lombok.Data;

import java.util.List;
@Data
public class ProductVariantRequest {
    private List<String> variantName;
    private List<Integer> variantQuantity;
    private List<String> variantPrice;
    private List<String> variantWhosalePrice;
    private List<String> variantDescription;
    private List<String> variantImage;

}
