package seaFood.PTseafood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seaFood.PTseafood.entity.OrderState;
@Repository
public interface IOrderStateRepository extends JpaRepository<Long, OrderState> {
}
