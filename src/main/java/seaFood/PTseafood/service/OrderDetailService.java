package seaFood.PTseafood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seaFood.PTseafood.entity.*;
import seaFood.PTseafood.repository.IOrderDetailRepository;
import seaFood.PTseafood.repository.IOrderRepository;

import java.util.List;

@Service
public class OrderDetailService {
    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private IOrderDetailRepository orderDetailRepository;
    @Autowired
    private CartService cartService;

    public void create(Order order, User user) {
        List<CartItem> cartItems = cartService.getCartItemsByUser(user);
        for (CartItem cartItem : cartItems){
            Long cartItemId = cartItem.getId();
            Product product = cartItem.getProduct();
            ProductVariant productVariant = cartItem.getProductVariant();
            int quantity =  cartItem.getQuantity();
            // Nếu user có giá sỉ và giá sỉ có giá trị, sử dụng giá sỉ.
            double price = (user.isWholeSale() && productVariant.getWhosalePrice() > 0)
                    ? productVariant.getWhosalePrice() :  productVariant.getPrice();
//            double price = productVariant.getPrice();
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProductVariant(productVariant);
            orderDetail.setProduct(product);
            orderDetail.setProductName(product.getName());
            orderDetail.setProductVariantName(productVariant.getName());
            orderDetail.setQuantity(quantity);
            orderDetail.setPrice(price);
            orderDetailRepository.save(orderDetail);
        }
    }
}