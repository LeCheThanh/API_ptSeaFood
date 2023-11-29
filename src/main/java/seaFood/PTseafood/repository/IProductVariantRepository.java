package seaFood.PTseafood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seaFood.PTseafood.entity.Product;
import seaFood.PTseafood.entity.ProductVariant;

import java.util.List;

@Repository
public interface IProductVariantRepository extends JpaRepository<ProductVariant,Long> {
    List<ProductVariant> findByStock(int stock);

    List<ProductVariant> findByProduct(Product product);



}
