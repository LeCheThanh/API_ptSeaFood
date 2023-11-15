package seaFood.PTseafood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seaFood.PTseafood.entity.Product;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product,Long>{
    List<Product> findByNameContaining(String keyword);

}
