package seaFood.PTseafood.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.apache.poi.hpsf.Variant;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name="event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;


    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "discount_rate", nullable = false)
    private Double discountRate;

    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL)
    private List<ProductVariant> productVariants;

}
