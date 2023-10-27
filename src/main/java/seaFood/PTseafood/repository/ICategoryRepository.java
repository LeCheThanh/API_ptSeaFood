package seaFood.PTseafood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seaFood.PTseafood.entity.Category;

@Repository
public interface ICategoryRepository extends JpaRepository<Category,Long> {
}
