package seaFood.PTseafood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seaFood.PTseafood.dto.CartItemRequest;
import seaFood.PTseafood.dto.UpdateCartRequest;
import seaFood.PTseafood.entity.*;
import seaFood.PTseafood.exception.ResourceNotFoundException;
import seaFood.PTseafood.repository.ICartRepository;
import seaFood.PTseafood.utils.JwtUtil;

import java.time.LocalDateTime;
import java.util.List;

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

    @Autowired
    private EventService eventService;

    public CartItem add(CartItemRequest cartItemRequest, User user) {
        int quantity = cartItemRequest.getQuantity();
        Long productId = cartItemRequest.getProductId();
        Long productVariantId = cartItemRequest.getProductVariantId();
        ProductVariant productVariant = productVariantService.getById(productVariantId);
        Product product = productService.getById(productId);
        // Kiểm tra số lượng cần mua có hợp lệ hay không
        if (quantity <= 0) {
            throw new ResourceNotFoundException("Số lượng sản phẩm không được dưới 1");
        }

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng của người dùng chưa
        CartItem existingCartItem = cartRepository.findByUserAndProductVariant(user, productVariant);

        if (product == null) {
            throw new ResourceNotFoundException("Sản phẩm không hợp lệ");
        }

        // Kiểm tra productVariantId có phải là của productId không
        if (productVariant == null || productVariant.getProduct().getId() != productId) {
            throw new ResourceNotFoundException("Sản phẩm không có biến thể");
        }

        // Kiểm tra số lượng cần mua có đủ không
        if (productVariant.getStock() < quantity) {
            throw new ResourceNotFoundException("Số lượng sản phẩm chỉ còn lại " + productVariant.getStock());
        }

        double price; // Giá cuối cùng sau khi đã xét đến giảm giá và giá sỉ

        // Kiểm tra sự kiện và áp dụng giá giảm nếu có
        LocalDateTime now = LocalDateTime.now();
        Event currentEvent = productVariant.getEvent();
        boolean isEventActive = currentEvent != null &&
                now.isAfter(currentEvent.getStartTime()) &&
                now.isBefore(currentEvent.getEndTime());

        if (isEventActive) {
            // Tính giá đã giảm dựa trên discountRate của sự kiện
            price = eventService.calculateDiscountedPrice(currentEvent, productVariant.getPrice());
        } else {
            // Sử dụng giá gốc hoặc giá sỉ nếu không trong thời gian sự kiện hoặc không có sự kiện
            price = (user.isWholeSale() && productVariant.getWhosalePrice() > 0)
                    ? productVariant.getWhosalePrice()
                    : productVariant.getPrice();
        }

        double total = price * quantity; // Tính tổng giá dựa trên số lượng và giá

        if (existingCartItem != null) {
            // Nếu sản phẩm đã có trong giỏ hàng, cập nhật số lượng và tổng giá
            if(existingCartItem.getQuantity() + quantity > productVariant.getStock()){
                throw new ResourceNotFoundException("Số lượng sản phẩm chỉ còn lại " + productVariant.getStock());
            }

            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            existingCartItem.setTotal(total + existingCartItem.getTotal()); // Cập nhật tổng giá

            return cartRepository.save(existingCartItem); // Lưu và trả về
        } else {
            // Nếu sản phẩm chưa có trong giỏ hàng, thêm sản phẩm mới
            CartItem cartItem = new CartItem();
            cartItem.setProductVariant(productVariant);
            cartItem.setQuantity(quantity);
            cartItem.setProduct(product);
            cartItem.setUser(user);
            cartItem.setTotal(total); // Thiết lập tổng giá cho cart item mới

            return cartRepository.save(cartItem); // Lưu và trả về cartItem mới
        }

    }

    ////////////
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

    public CartItem updateCartItem(UpdateCartRequest updateRequest, User user) {
        Long cartItemId = updateRequest.getCartItemId();
        int newQuantity = updateRequest.getNewQuantity();

        // Kiểm tra xem cartItemId có thuộc về người dùng không và tồn tại trong giỏ hàng
        CartItem cartItem = cartRepository.findByIdAndUser(cartItemId, user);

        if (cartItem == null) {
            throw new ResourceNotFoundException("Không tìm thấy mục trong giỏ hàng");
        }

        // Kiểm tra số lượng mới có hợp lệ không
        if (newQuantity <= 0) {
            throw new ResourceNotFoundException("Số lượng sản phẩm không hợp lệ");
        }
        if (cartItem.getProductVariant().getStock() < newQuantity) {
            throw new ResourceNotFoundException("Số lượng tồn kho không đủ");
        }
        // Cập nhật số lượng của mục trong giỏ hàng
        cartItem.setQuantity(newQuantity);
        LocalDateTime now = LocalDateTime.now();
        Event currentEvent = cartItem.getProductVariant().getEvent();
        boolean isEventActive = currentEvent != null &&
                now.isAfter(currentEvent.getStartTime()) &&
                now.isBefore(currentEvent.getEndTime());
        double price=0.0;
        if (isEventActive) {
            // Tính giá đã giảm dựa trên discountRate của sự kiện
            price = eventService.calculateDiscountedPrice(currentEvent, cartItem.getProductVariant().getPrice());
        } else {
            // Sử dụng giá gốc hoặc giá sỉ nếu không trong thời gian sự kiện hoặc không có sự kiện
            price = (user.isWholeSale() && cartItem.getProductVariant().getWhosalePrice() > 0)
                    ? cartItem.getProductVariant().getWhosalePrice()
                    : cartItem.getProductVariant().getPrice();
        }
        // Tính toán lại tổng giá trị
        double newTotal = price * newQuantity;
        cartItem.setTotal(newTotal);

        // Lưu lại mục giỏ hàng đã cập nhật
        return cartRepository.save(cartItem);
    }

    /// Xóa sản phẩm khỏi giỏ hàng
    public void removeCartItem(Long cartItemId, User user) {
        // Kiểm tra xem cartItemId có thuộc về người dùng không và tồn tại trong giỏ hàng
        CartItem cartItem = cartRepository.findByIdAndUser(cartItemId, user);

        if (cartItem == null) {
            throw new ResourceNotFoundException("Không tìm thấy mục trong giỏ hàng");
        }

        // Xóa mục khỏi giỏ hàng
        cartRepository.delete(cartItem);
    }

    /// Xóa tất cả sản phẩm
    public void clearCart(User user) {
        // Lấy tất cả các mục trong giỏ hàng của người dùng và xóa chúng
        List<CartItem> cartItems = cartRepository.findByUser(user);
        cartRepository.deleteAll(cartItems);
    }

    /// xóa sản phẩm nếu số lượng = 0
    public void delProductOutStock(User user) {
        List<CartItem> cartItems = cartRepository.findByUser(user);
        for (CartItem cartItem : cartItems) {
            // Kiểm tra số lượng tồn kho của sản phẩm liên quan
            ProductVariant productVariant = cartItem.getProductVariant();
            if (productVariant.getStock() == 0) {
                cartRepository.delete(cartItem);
            }
        }
    }
    // tự động xóa theo stock
    public String autoDelProOutOfStock() {
        //hàng tồn về 0
        List<CartItem> itemsToRemove = cartRepository.findByProductVariantStockLessThan(1);
        String message = "";
        if(!itemsToRemove.isEmpty()) {
            for (CartItem item : itemsToRemove) {
                // Xóa sản phẩm khỏi giỏ hàng
                message = notifyUserOutOfStock(item.getProductVariant().getName());
            }
        }

        /// khi số lượng sản phẩm trong giỏ hàng > hơn số hàng tồn

        List<CartItem> itemsWithStockExceeded = cartRepository.findItemsWithStockExceeded();
        if(!itemsWithStockExceeded.isEmpty()) {
            for (CartItem item : itemsWithStockExceeded) {
                // Xóa sản phẩm khỏi giỏ hàng
                message = notifyUserStockExceeded(item.getProductVariant().getName());
            }
        }
        return message;
    }
    //check cart
    public boolean isCartEmpty(User user) {
        List<CartItem> cartItems = cartRepository.findByUser(user);
        return cartItems.isEmpty();
    }
    private String notifyUserOutOfStock(String productVariantName) {
        // Thực hiện xử lý thông báo cho trường hợp hết hàng
        return("Sản phẩm "+productVariantName+" đã hết hàng");
    }

    private String notifyUserStockExceeded(String productVariantName) {
        // Thực hiện xử lý thông báo cho trường hợp số lượng trong giỏ hàng vượt quá số lượng tồn kho
        return ("Số lượng sản phẩm " + productVariantName + " trong giỏ hàng vượt quá số lượng tồn kho.");
    }
}