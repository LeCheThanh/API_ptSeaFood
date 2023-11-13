package seaFood.PTseafood.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="vnpay_transaction")
public class VnPayTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_code", nullable = false)
    private String orderCode;

    @Column(name = "transaction_code", nullable = false)
    private String transactionCode;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    @Column(name = "payment_time", nullable = false)
    private LocalDateTime paymentTime;

    @Column(name = "payment_amount", nullable = false)
    private double paymentAmount;

    @Column(name = "response_code", nullable = false)
    private String responseCode;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;
}