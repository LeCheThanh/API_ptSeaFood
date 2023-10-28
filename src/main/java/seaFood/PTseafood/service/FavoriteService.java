package seaFood.PTseafood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seaFood.PTseafood.entity.Favorite;
import seaFood.PTseafood.entity.Product;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.exception.ResourceNotFoundException;
import seaFood.PTseafood.repository.IFavoriteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {
    @Autowired
    private IFavoriteRepository favoriteRepository;
    @Autowired
    private UserService userService;
    //Lấy tất cả sản phẩm trong ds yêu thích của user
    public List<Favorite> getFavoritesByUser(Long id) {
        Optional<User> userOptional  = userService.getById(id);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        User user = userOptional.get();
        return favoriteRepository.findAllByUser(user);

    }

    //Thêm vào ds yêu thích
    public Favorite addToFavorites(User user, Product product) {
        // kiểm tra xem sản phẩm này đã thêm vào ds yêu thích của user hiện tại chưa
        Favorite existingFavorite = favoriteRepository.findByUserAndProduct(user, product);

        if (existingFavorite == null) {
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setProduct(product);
            return favoriteRepository.save(favorite);
        }

        return existingFavorite;
    }
    //xóa khỏi ds yêu thích
    public void removeFromFavorites(User user, Product product) {
        Favorite favorite = favoriteRepository.findByUserAndProduct(user, product);
        if (favorite != null) {
            favoriteRepository.delete(favorite);
        }
    }
}
