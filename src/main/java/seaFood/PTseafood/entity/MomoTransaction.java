package seaFood.PTseafood.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
@Table(name="momo_transaction")
public class MomoTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_code", nullable = false)
    private String orderCode;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "result_code", nullable = false)
    private String resultCode;

    @Column(name = "response_time", nullable = false)
    private Timestamp responseTime;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "trans_id", nullable = false)
    private String transId;

    @Column(name = "partner_code", nullable = false)
    private String partnerCode;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;
}
