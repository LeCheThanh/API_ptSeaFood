package seaFood.PTseafood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seaFood.PTseafood.entity.Favorite;
import seaFood.PTseafood.entity.Product;
import seaFood.PTseafood.entity.User;

import java.util.List;

@Repository
public interface IFavoriteRepository extends JpaRepository<Favorite,Long> {
    Favorite findByUserAndProduct(User user, Product product);

    List<Favorite> findAllByUser(User user);
}
