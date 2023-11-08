package seaFood.PTseafood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seaFood.PTseafood.entity.OrderDetail;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
