package seaFood.PTseafood.dto;

import lombok.Data;
import seaFood.PTseafood.entity.Product;

@Data
public class ReviewRequest {
    private String content;
    private int rating;
    private Product product;
}
