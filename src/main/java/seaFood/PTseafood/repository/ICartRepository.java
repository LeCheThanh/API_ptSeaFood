package seaFood.PTseafood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import seaFood.PTseafood.entity.CartItem;
import seaFood.PTseafood.entity.ProductVariant;
import seaFood.PTseafood.entity.User;

import java.util.List;

@Repository
public interface ICartRepository extends JpaRepository<CartItem,Long> {
    List<CartItem> findAllByUser(User user);

    CartItem findByIdAndUser(Long id, User user);
    List<CartItem>  findByUser(User user);
    List<CartItem> findByProductVariantStockLessThan(int stock);

    @Query("SELECT t FROM CartItem t WHERE t.quantity > t.productVariant.stock")
    List<CartItem> findItemsWithStockExceeded();

    CartItem findByUserAndProductVariant(User user, ProductVariant productVariant);
}
