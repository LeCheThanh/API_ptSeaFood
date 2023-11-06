package seaFood.PTseafood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seaFood.PTseafood.entity.CartItem;
import seaFood.PTseafood.entity.User;

import java.util.List;

@Repository
public interface ICartRepository extends JpaRepository<CartItem,Long> {
    List<CartItem> findAllByUser(User user);
}
