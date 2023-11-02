package seaFood.PTseafood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seaFood.PTseafood.entity.Order;

@Repository
public interface IOrderRepository  extends JpaRepository<Order,Long> {
    Order findByCode(String code);
}
