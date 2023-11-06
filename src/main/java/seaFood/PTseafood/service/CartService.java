package seaFood.PTseafood.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import seaFood.PTseafood.auth.AuthenticationRequest;
import seaFood.PTseafood.config.JwtAuthenticationFilter;
import seaFood.PTseafood.dto.CartItemRequest;
import seaFood.PTseafood.entity.CartItem;
import seaFood.PTseafood.entity.Product;
import seaFood.PTseafood.entity.ProductVariant;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.exception.ResourceNotFoundException;
import seaFood.PTseafood.repository.ICartRepository;
import seaFood.PTseafood.utils.JwtUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartService {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductVariantService productVariantService;
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private JwtUtil jwtUtil;
    public CartItem  add (CartItemRequest cartItemRequest,User user){
            int quantity = cartItemRequest.getQuantity();
            Long productId = cartItemRequest.getProductId();
            Long productVariantId = cartItemRequest.getProductVariantId();

            Product product = productService.getById(productId);
            ProductVariant productVariant = productVariantService.getById(productVariantId);

            if(product == null) {
                throw new ResourceNotFoundException("Sản phẩm không hợp lệ");
            }

            // Kiểm tra productVariantId có phải là của productId không
            if (productVariant == null || productVariant.getProduct().getId() != productId) {
                // ném lỗi
                throw new ResourceNotFoundException("Sản phẩm không có biến thể");
            }


            // Kiểm tra số lượng cần mua có hợp lệ hay không
            if (quantity <= 0) {
                // Thực hiện xử lý lỗi và trả về false
                throw new ResourceNotFoundException("Số lượng sản phẩm không được dưới 1");
            }

            // Kiểm tra số lượng cần mua có đủ không
            if (productVariant.getStock() < quantity) {
                // Thực hiện xử lý lỗi và trả về false
                throw new ResourceNotFoundException("Số lượng sản phẩm chỉ còn lại "+productVariant.getStock() );
            }

            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setProductVariant(productVariant);
            cartItem.setQuantity(quantity);
            cartItem.setProduct(product);
            cartItem.setProductVariant(productVariant);
            double price = productVariant.getPrice();
            double totalPrice = price * quantity;
            cartItem.setUser(user);
            cartItem.setTotal(totalPrice);



            return cartRepository.save(cartItem);
        }
    public List<CartItem> getCartItemsByUser(User user) {
            return cartRepository.findAllByUser(user);
    }

    public double getTotalCartValue(User user) {
        List<CartItem> cartItems = getCartItemsByUser(user);
        double totalValue = 0.0;

        for (CartItem cartItem : cartItems) {
            totalValue += cartItem.getTotal();
        }

        return totalValue;
    }



}
