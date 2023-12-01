package seaFood.PTseafood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seaFood.PTseafood.entity.Category;
import seaFood.PTseafood.entity.Product;
import seaFood.PTseafood.exception.ResourceNotFoundException;
import seaFood.PTseafood.repository.ICategoryRepository;
import seaFood.PTseafood.repository.IProductRepository;
import seaFood.PTseafood.utils.SlugUtil;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private ICategoryRepository categoryRepository;


    LocalDateTime now = LocalDateTime.now();
    public List<Product> getAll() {
        return productRepository.findAll();
    }
    public long countAll() {return productRepository.count();}
    public Product getById(Long id)
    {
        return productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not exist with id:"+id));
    }
    public Product save(Product product)
    {
        return productRepository.save(product);
    }
    public Product addProduct(Product product)
    {
        String slug = SlugUtil.createSlug(product.getName());
        product.setSlug(slug);
        product.setCreateAt(now);
        product.setUpdateAt(now);
        return save(product);
    }
    public void deleteProduct(Long id)
    {
        productRepository.deleteById(id);
    }

    public Product updateProduct(Long id,Product product)
    {
                Product existingProduct = productRepository.findById(id).orElse(null);
//                Category category = categoryRepository.findById(categoryId).orElse(null);
                if (existingProduct != null) {

                    existingProduct.setName(product.getName());
                    existingProduct.setDescription(product.getDescription());
                    String slug = SlugUtil.createSlug(product.getName());
                    existingProduct.setSlug(slug);
                    existingProduct.setImage(product.getImage());
                    existingProduct.setCategory(product.getCategory());
                    existingProduct.setUpdateAt(now);
                    return save(existingProduct);

                }
                return null;
    }
    public List<Product> findByNameContaining (String keyword){
        return productRepository.findByNameContaining(keyword);
    }
}
