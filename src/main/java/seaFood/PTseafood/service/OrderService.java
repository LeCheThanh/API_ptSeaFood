package seaFood.PTseafood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seaFood.PTseafood.dto.OrderRequest;
import seaFood.PTseafood.entity.Order;
import seaFood.PTseafood.entity.OrderState;
import seaFood.PTseafood.entity.User;
import seaFood.PTseafood.repository.IOrderRepository;
import seaFood.PTseafood.repository.IOrderStateRepository;
import seaFood.PTseafood.utils.GenerateCodeUtil;
import seaFood.PTseafood.common.Enum;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private  CartService cartService;
    @Autowired
    private IOrderStateRepository orderStateRepository;

    @Autowired
    private OrderDetailService orderDetailService;
    ///Admin page
    public List<Order> getAll(){return orderRepository.findAll();}
    //Search by customer
    public Order getByCode(String code){return orderRepository.findByCode(code);}

    //Create new Order
    public Order create(OrderRequest orderRequest, User user) throws Exception {

            String name = orderRequest.getReceiverName();
            String phone = orderRequest.getReceiverPhone();
            String email = orderRequest.getReceiverEmail();
            String address = orderRequest.getReceiverAddress();
            String note = orderRequest.getNote();
            String payment = orderRequest.getPayment();
            if (cartService.isCartEmpty(user)) {
                    throw new Exception("Giỏ hàng của bạn hiện đang trống. Không thể tạo đơn hàng.");
            }
            Order order = new Order();
            order.setCreatedAt(LocalDateTime.now());
            order.setCode(GenerateCodeUtil.GenerateCodeOrder());
            order.setNote(note);

            order.setUser(user);
            order.setReceiverName(name != null ? name : user.getFullName());
            order.setReceiverEmail(email != null ? email : user.getEmail());
            order.setReceiverPhone(phone != null ? phone : user.getPhone());
            order.setReceiverAddress(address != null ? address : user.getAddress());
            order.setDiscountPrice(user.getDiscountRate());
            order.setTotalPrice(cartService.getTotalCartValue(user));

            double totalPrice = cartService.getTotalCartValue(user);
            // lưu discount ở user là số nguyên vd : 10 thì 10/100 -->> 10%
            int discountPrice = user.getDiscountRate()/100;
            double finalPrice = totalPrice*(1-discountPrice);
            // làm tròn
            finalPrice = Math.round(finalPrice);

        if ("momo".equalsIgnoreCase(payment)) {
            // Xử lý thanh toán qua Momo

            // Làm tròn giá trị cuối cùng
            finalPrice = Math.round(finalPrice);
        } else if ("vnpay".equalsIgnoreCase(payment)) {

        } else if ("cash".equalsIgnoreCase(payment)) {

        }

        order.setFinalPrice(finalPrice);
        order.setPaymentMethod(payment);
        order.setPaymentStatus("Ddang cho");

        orderRepository.save(order);

        OrderState orderState = new OrderState();
        orderState.setCreatedAt(LocalDateTime.now());
        orderState.setUpdateAt(LocalDateTime.now());
        orderState.setState(Enum.OrderStatus.PENDING_CONFIRMATION.getName());
        orderState.setOrder(order);
        orderStateRepository.save(orderState);

//        orderDetailService.create(order,user);


        return order;
    }
}
