package seaFood.PTseafood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import seaFood.PTseafood.common.Enum;
import seaFood.PTseafood.entity.Order;
import seaFood.PTseafood.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository  extends JpaRepository<Order,Long> {
    Order findByCode(String code);
    Order findByIdAndUser(Long orderId, User user);

    List<Order> findByUser(User user);

    @Query("SELECT COALESCE(SUM(o.finalPrice), 0) FROM Order o WHERE o.paymentStatus = 'Đã thanh toán'")
    double getTotalSales();

    @Query("SELECT COALESCE(SUM(o.finalPrice), 0) FROM Order o WHERE o.paymentStatus = 'Đã thanh toán' AND YEAR(o.createdAt) = :year AND MONTH(o.createdAt) = :month")
    double getMonthlySales( int year, int month);

    @Query("SELECT COALESCE(SUM(o.finalPrice), 0) FROM Order o WHERE o.paymentStatus = 'Đã thanh toán' AND YEAR(o.createdAt) = :year")
    double getYearlySales(int year);

    Long countByPaymentStatus(String paymentStatus);
    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
    List<Order> findLatestOrder();

}
