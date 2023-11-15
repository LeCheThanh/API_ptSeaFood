package seaFood.PTseafood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seaFood.PTseafood.dto.CartItemRequest;
import seaFood.PTseafood.dto.UpdateCartRequest;
import seaFood.PTseafood.entity.CartItem;
import seaFood.PTseafood.entity.Product;
import seaFood.PTseafood.entity.ProductVariant;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.exception.ResourceNotFoundException;
import seaFood.PTseafood.repository.ICartRepository;
import seaFood.PTseafood.utils.JwtUtil;

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

    //    public CartItem  add (CartItemRequest cartItemRequest, User user){
//            int quantity = cartItemRequest.getQuantity();
//            Long productId = cartItemRequest.getProductId();
//            Long productVariantId = cartItemRequest.getProductVariantId();
//            ProductVariant productVariant = productVariantService.getById(productVariantId);
//            Product product = productService.getById(productId);
//            // Kiểm tra xem sản phẩm đã có trong giỏ hàng của người dùng chưa
//            CartItem existingCartItem = cartRepository.findByUserAndProductVariant(user, productVariant);
//            double price;
//            if (product == null) {
//                throw new ResourceNotFoundException("Sản phẩm không hợp lệ");
//
//            }
//            // Kiểm tra productVariantId có phải là của productId không
//            if (productVariant == null || productVariant.getProduct().getId() != productId) {
//                // ném lỗi
//                throw new ResourceNotFoundException("Sản phẩm không có biến thể");
//            }
//            // Kiểm tra số lượng cần mua có hợp lệ hay không
//            if (quantity <= 0) {
//                // Thực hiện xử lý lỗi và trả về false
//                throw new ResourceNotFoundException("Số lượng sản phẩm không được dưới 1");
//            }
//            // Kiểm tra số lượng cần mua có đủ không
//            if (productVariant.getStock() < quantity) {
//                // Thực hiện xử lý lỗi và trả về false
//                throw new ResourceNotFoundException("Số lượng sản phẩm chỉ còn lại " + productVariant.getStock());
//            }
//             if (existingCartItem == null) {
//                 // Nếu sản phẩm đã có trong giỏ hàng, thì tăng số lượng (quantity) lên
//                 System.out.println("Add to Cart 20- Start"+existingCartItem);
//                 existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
//                 System.out.println("Add to Cart 21- Start"+existingCartItem);
//                 // Sử dụng giá sỉ nếu user có giá sỉ và giá sỉ có giá trị
//                 price = (user.isWholeSale() && existingCartItem.getProductVariant().getWhosalePrice() > 0)
//                             ? existingCartItem.getProductVariant().getWhosalePrice()
//                             : existingCartItem.getProductVariant().getPrice();
//                 System.out.println("Add to Cart 20- Start"+price);
//                 double total = price * existingCartItem.getQuantity();
//                 System.out.println("Add to Cart 20- Start"+total);
//                 existingCartItem.setTotal(total);
//                 System.out.println("Product ex: " + existingCartItem);
//                 // Lưu cập nhật của mục trong giỏ hàng
//                 return cartRepository.save(existingCartItem);
//             }
//             else {
//                 CartItem cartItem = new CartItem();
//                 cartItem.setProduct(product);
//                 cartItem.setProductVariant(productVariant);
//                 cartItem.setQuantity(quantity);
//                 price =  (user.isWholeSale() && productVariant.getWhosalePrice() > 0)
//                         ? productVariant.getWhosalePrice()
//                         : productVariant.getPrice();
//                 double totalPrice = price * quantity;
//                 cartItem.setUser(user);
//                 cartItem.setTotal(totalPrice);
//
//
//                 return cartRepository.save(cartItem);
//             }
//        }
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

        double price;

        if (existingCartItem != null) {
            // Nếu sản phẩm đã có trong giỏ hàng, thì tăng số lượng (quantity) lên
            if(existingCartItem.getQuantity() + quantity > productVariant.getStock()){
                throw new ResourceNotFoundException("Số lượng sản phẩm chỉ còn lại " + productVariant.getStock());
            }
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);

            // Sử dụng giá sỉ nếu user có giá sỉ và giá sỉ có giá trị
            price = (user.isWholeSale() && existingCartItem.getProductVariant().getWhosalePrice() > 0)
                    ? existingCartItem.getProductVariant().getWhosalePrice()
                    : existingCartItem.getProductVariant().getPrice();

            double total = price * existingCartItem.getQuantity();
            existingCartItem.setTotal(total);

            // Lưu cập nhật của mục trong giỏ hàng
            return cartRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setProductVariant(productVariant);
            cartItem.setQuantity(quantity);

            price = (user.isWholeSale() && productVariant.getWhosalePrice() > 0)
                    ? productVariant.getWhosalePrice()
                    : productVariant.getPrice();

            double totalPrice = price * quantity;
            cartItem.setUser(user);
            cartItem.setTotal(totalPrice);
            return cartRepository.save(cartItem);
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

        // Tính toán lại tổng giá trị
        double newTotal = cartItem.getProductVariant().getPrice() * newQuantity;
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