package seaFood.PTseafood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seaFood.PTseafood.entity.ProductVariant;
import seaFood.PTseafood.entity.Product;

@Repository
public interface IProductRepository extends JpaRepository<Product,Long>{
}
