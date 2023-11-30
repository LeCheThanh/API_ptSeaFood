package seaFood.PTseafood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seaFood.PTseafood.entity.Category;
import seaFood.PTseafood.entity.Product;
import seaFood.PTseafood.entity.ProductVariant;
import seaFood.PTseafood.repository.IProductRepository;
import seaFood.PTseafood.repository.IProductVariantRepository;
import seaFood.PTseafood.utils.SlugUtil;
import seaFood.PTseafood.dto.ProductStatistic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductVariantService {
    @Autowired
    private IProductVariantRepository productVariantRepository;
    @Autowired
    private IProductRepository productRepository;
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
    ///Thong ke theo sp da ban
    public List<ProductStatistic> getProductStatistics() {
        List<Product> products = productRepository.findAll();
        List<ProductStatistic> productStatistics = new ArrayList<>();

        for (Product product : products) {
            List<ProductVariant> variants = productVariantRepository.findByProduct(product);
            int totalSoldQuantity = variants.stream().mapToInt(ProductVariant::getSoldQuantity).sum();
            double totalRevenue = variants.stream().mapToDouble(v -> v.getSoldQuantity() * v.getPrice()).sum();
            ProductStatistic productStatistic = new ProductStatistic(product, totalSoldQuantity, totalRevenue);
            productStatistics.add(productStatistic);
        }

        return productStatistics;
    }
    public List<ProductVariant> getVariantByProduct(Product product){
        return productVariantRepository.findByProduct(product);
    }
    public void deleteVariant(Long id){productVariantRepository.deleteById(id);}
    public ProductVariant updateVariant(Long id,ProductVariant variant)
    {
        ProductVariant existingVariant = productVariantRepository.findById(id).orElse(null);
        Optional<Product> existingProduct = productRepository.findById(existingVariant.getProduct().getId());
        Product product = existingProduct.get();
        if (existingVariant != null) {

            existingVariant.setName(variant.getName());
            existingVariant.setDescription(variant.getDescription());
            String slug = SlugUtil.createSlug(variant.getName());
            existingVariant.setImage(variant.getImage());
            existingVariant.setSlug(slug);
            existingVariant.setStock(variant.getStock());
            existingVariant.setPrice(variant.getPrice());
            existingVariant.setWhosalePrice(variant.getWhosalePrice());
            product.setUpdateAt(LocalDateTime.now());
            return save(existingVariant);
        }
        return null;
    }
}
