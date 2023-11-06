package seaFood.PTseafood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seaFood.PTseafood.entity.ProductVariant;
import seaFood.PTseafood.repository.IProductVariantRepository;
import seaFood.PTseafood.utils.SlugUtil;

import java.util.List;

@Service
public class ProductVariantService {
    @Autowired
    private IProductVariantRepository productVariantRepository;
    public ProductVariant getById(Long id) {
        return productVariantRepository.findById(id).orElse(null);
    }
    public ProductVariant save(ProductVariant productVariant)
    {
        return productVariantRepository.save(productVariant);
    }
    public ProductVariant add(ProductVariant productVariant)
    {
        String slug = SlugUtil.createSlug(productVariant.getName());
        productVariant.setSlug(slug);
        productVariant.setSoldQuantity(0);
        return save(productVariant);
    }
    public List<ProductVariant> findOutOfStockProducts() {
        return productVariantRepository.findByStock(0);
    }

}
