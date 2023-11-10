package seaFood.PTseafood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import seaFood.PTseafood.dto.OrderRequest;
import seaFood.PTseafood.entity.*;
import seaFood.PTseafood.exception.ResourceNotFoundException;
import seaFood.PTseafood.repository.IOrderRepository;
import seaFood.PTseafood.repository.IOrderStateRepository;
import seaFood.PTseafood.utils.GenerateCodeUtil;
import seaFood.PTseafood.common.Enum;

import java.math.BigInteger;
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
    private ProductVariantService productVariantService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private UserService userService;
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
            int discountRate = user.getDiscountRate();
            System.out.println("Discount Rate: " + discountRate);
            double discountPrice = discountRate / 100.0;
            System.out.println(discountPrice);
            double finalPrice = totalPrice*(1-discountPrice);
            System.out.println(finalPrice);
            // làm tròn
            finalPrice = Math.round(finalPrice);
            System.out.println(finalPrice);
        if ("momo".equalsIgnoreCase(payment)) {
            // Xử lý thanh toán qua Momo

            // Làm tròn giá trị cuối cùng
            finalPrice = Math.round(finalPrice);
        } else if ("vnpay".equalsIgnoreCase(payment)) {

        } else if ("cash".equalsIgnoreCase(payment)) {

        }

        order.setFinalPrice(finalPrice);
        order.setPaymentMethod(payment);
        order.setPaymentStatus(Enum.PaymentStatus.UNPAID.getName());

        orderRepository.save(order);

        OrderState orderState = new OrderState();
        orderState.setCreatedAt(LocalDateTime.now());
        orderState.setUpdateAt(LocalDateTime.now());
        orderState.setState(Enum.OrderStatus.PENDING_CONFIRMATION.getName());
        orderState.setOrder(order);
        orderStateRepository.save(orderState);

        orderDetailService.create(order,user);
        cartService.clearCart(user);

        return order;
    }

    //update State by user
    public OrderState updateOrderStateByUser(Long orderId,User user) {
        // Kiểm tra xem đơn hàng có tồn tại không và có thuộc về người dùng hay không
        Order existingOrder = orderRepository.findByIdAndUser(orderId, user);
        OrderState newOrderState = orderStateRepository.findByOrder(existingOrder);


        if (existingOrder == null) {
            throw new RuntimeException("Không tồn tại đơn hàng!");
        }
        if (newOrderState == null) {
            throw new RuntimeException("Không tìm thấy trạng thái đơn hàng!");
        }
        //Chỉ cho phép cập nhật trạng thái nếu đơn hàng đang ở trạng thái đang vận chuyển
        if (newOrderState.getState().equals( Enum.OrderStatus.SHIPPING.getName())) {
            // Cập nhật trạng thái đơn hàng
            newOrderState.setState(Enum.OrderStatus.COMPLETED.getName());
            // Cập nhật totalUserPurchase cho người dùng
            BigInteger purchaseAmount = BigInteger.valueOf(existingOrder.getFinalPrice().longValue()); // Chuyển đổi từ double sang BigInteger
            userService.updateTotalUserPurchase(user, purchaseAmount);
            // Lưu cập nhật vào cơ sở dữ liệu
            return orderStateRepository.save(newOrderState);
        } else {
            throw new RuntimeException("Không thể cập nhật trạng thái đơn hàng ở trạng thái hiện tại");
        }
    }

    //update State by admin
    @PreAuthorize("hasRole('Admin')")
    public OrderState updateOrderStateByAdmin(Long orderId) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với ID: " + orderId));

        OrderState newOrderState = orderStateRepository.findByOrder(existingOrder);

        // Xác nhận đơn hàng đã ở trạng thái SHIPPING
//        if (!newOrderState.getState().equals(Enum.OrderStatus.SHIPPING.getName())) {
//            throw new RuntimeException("Không thể cập nhật trạng thái đơn hàng nếu không ở trạng thái SHIPPING");
//        }

        // Lấy danh sách các mục đơn hàng
        List<OrderDetail> orderItems = existingOrder.getOrderDetails();

        // Cập nhật số lượng tồn kho cho từng sản phẩm trong đơn hàng
        for (OrderDetail orderItem : orderItems) {
            ProductVariant productVariant = orderItem.getProductVariant();
            int quantity = orderItem.getQuantity();

            // Cập nhật số lượng tồn kho
            int newStock = productVariant.getStock() - quantity;
            productVariant.setStock(newStock);
            productVariant.setSoldQuantity(quantity);
            productVariantService.save(productVariant);
        }

        // Cập nhật trạng thái đơn hàng
        newOrderState.setUpdateAt(LocalDateTime.now());
        newOrderState.setState(Enum.OrderStatus.SHIPPING.getName());

        // Lưu cập nhật vào cơ sở dữ liệu
        return orderStateRepository.save(newOrderState);
    }
    //Get all order by user
    public List<Order> getAllByUser(User user){
        List<Order> orderByUser = orderRepository.findByUser(user);
//        if(orderByUser.isEmpty()){
//            throw new ResourceNotFoundException("User này không có đơn hàng!");
//        }
        return orderByUser;
    }
}
