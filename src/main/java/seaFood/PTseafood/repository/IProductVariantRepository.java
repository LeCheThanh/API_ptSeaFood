package seaFood.PTseafood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seaFood.PTseafood.entity.ProductVariant;

@Repository
public interface IProductVariantRepository extends JpaRepository<ProductVariant,Long> {

}
