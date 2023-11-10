package seaFood.PTseafood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seaFood.PTseafood.entity.Order;
import seaFood.PTseafood.entity.User;

import java.util.List;

@Repository
public interface IOrderRepository  extends JpaRepository<Order,Long> {
    Order findByCode(String code);
    Order findByIdAndUser(Long orderId, User user);

    List<Order> findByUser(User user);
}
