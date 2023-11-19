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
    private VnPayService vnPayService;

    @Autowired
    private MomoService momoService;

    @Autowired
    private UserService userService;
    ///Admin page
    public List<Order> getAll(){return orderRepository.findAll();}
    //Search by customer
    public Order getByCode(String code){return orderRepository.findByCode(code);}

    //Create new Order
    public Order process(OrderRequest orderRequest, User user) throws Exception {

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
            order.setNote(note);

            order.setUser(user);
            order.setReceiverName(name != null ? name : user.getFullName());
            order.setReceiverEmail(email != null ? email : user.getEmail());
            order.setReceiverPhone(phone != null ? phone : user.getPhone());
            order.setReceiverAddress(address != null ? address : user.getAddress());
            order.setDiscountPrice(user.getDiscountRate());
            order.setTotalPrice(cartService.getTotalCartValue(user));
            order.setCode(GenerateCodeUtil.GenerateCodeOrder());

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


            order.setFinalPrice(finalPrice);
            order.setPaymentMethod(payment);
            order.setPaymentStatus(Enum.PaymentStatus.UNPAID.getName());
            orderRepository.save(order);

            if(payment.equals("cash")){
                saveOrder(order,user);
            }if(payment.equals("vnpay")){
                vnPayService.paymentVnPay(finalPrice,user,order.getCode());
            }if(payment.equals("momo")){
                momoService.paymentMomo(finalPrice, user, order.getCode());
            }

            return order;
    }

    public String saveOrder(Order order, User user){
        Order newOrder = orderRepository.save(order);
        orderDetailService.create(order,user);
        OrderState orderState = new OrderState();
        orderState.setCreatedAt(LocalDateTime.now());
        orderState.setUpdateAt(LocalDateTime.now());
        orderState.setState(Enum.OrderStatus.PENDING_CONFIRMATION.getName());
        orderState.setOrder(order);
        orderStateRepository.save(orderState);
        updateStock(order);
        cartService.clearCart(user);
        return "Thanh toán đã được xử lý. Mã đơn hàng của bạn là: " + newOrder.getCode();
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
            //Cập nhật payment status
            existingOrder.setPaymentStatus(Enum.PaymentStatus.PAID.getName());
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

        // Cập nhật trạng thái đơn hàng
        newOrderState.setUpdateAt(LocalDateTime.now());
        newOrderState.setState(Enum.OrderStatus.SHIPPING.getName());

        // Lưu cập nhật vào cơ sở dữ liệu
        return orderStateRepository.save(newOrderState);
    }

    //Cập nhật số lượng stock
    public void updateStock(Order order){
        List<OrderDetail> orderItems = order.getOrderDetails();
        for (OrderDetail orderItem : orderItems) {
            ProductVariant productVariant = orderItem.getProductVariant();
            int quantity = orderItem.getQuantity();

            // Cập nhật số lượng tồn kho
            int newStock = productVariant.getStock() - quantity;
            productVariant.setStock(newStock);

            int newSoldQuantity = productVariant.getSoldQuantity() + quantity;
            productVariant.setSoldQuantity(newSoldQuantity);

            productVariantService.save(productVariant);
        }

    }
    //Get all order by user
    public List<Order> getAllByUser(User user){
        List<Order> orderByUser = orderRepository.findByUser(user);
//        if(orderByUser.isEmpty()){
//            throw new ResourceNotFoundException("User này không có đơn hàng!");
//        }
        return orderByUser;
    }
    ///Thong ke
    public double getTotalSales() {
        return orderRepository.getTotalSales();
    }

    public double getMonthlySales(int year, int month) {
        return orderRepository.getMonthlySales(year, month);
    }

    public double getYearlySales(int year) {
        return orderRepository.getYearlySales(year);
    }
    public Long countAll(){return orderRepository.count();}

}
