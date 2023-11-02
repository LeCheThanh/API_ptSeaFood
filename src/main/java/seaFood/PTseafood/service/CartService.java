package seaFood.PTseafood.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import seaFood.PTseafood.dto.CartItemRequest;
import seaFood.PTseafood.entity.CartItem;
import seaFood.PTseafood.entity.Product;
import seaFood.PTseafood.entity.ProductVariant;
import seaFood.PTseafood.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartService {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductVariantService productVariantService;
    public String generateCartItemId(Long productId, Long productVariantId) {
        return DigestUtils.md5DigestAsHex((productId + "_" + productVariantId).getBytes());
    }
        public CartItem  add (CartItemRequest cartItemRequest){
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
                throw new ResourceNotFoundException("Số lượng sản phẩm chỉ còn lại "+quantity);
            }

            CartItem cartItem = new CartItem();
            cartItem.setCartItemId(generateCartItemId(productId, productVariantId)); // Tạo cartItemId mới
            cartItem.setProductId(productId);
            cartItem.setProductVariantId(productVariantId);
            cartItem.setQuantity(quantity);
            cartItem.setProduct(product);
            cartItem.setProductVariant(productVariant);
            double price = productVariant.getPrice();
            double totalPrice = price * quantity;
            cartItem.setTotal(totalPrice);


            return cartItem;
        }
//    public List<CartItem> getCartItems(HttpSession session) {
//        Map<String, CartItem> carts = (Map<String, CartItem>) session.getAttribute("carts");
//        List<CartItem> cartItems = new ArrayList<>();
//
//        if (carts != null) {
//            cartItems.addAll(carts.values());
//
//            for (CartItem cartItem : cartItems) {
//                String cartItemId = cartItem.getCartItemId();
//                Long productId = cartItem.getProductId();
//                Product product = productService.getById(cartItem.getProductId());
//                ProductVariant productVariant = productVariantService.getById(cartItem.getProductVariantId());
//                Long productVariantId = cartItem.getProductVariantId();
//                int quantity = cartItem.getQuantity();
//            }
//        }
//
//        return cartItems;
//    }



}
