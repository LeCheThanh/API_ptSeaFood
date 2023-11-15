package seaFood.PTseafood.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import seaFood.PTseafood.entity.Product;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductStatistic{
    private Product product;
    private int totalSoldQuantity;
    private double totalRevenue;

}
