package seaFood.PTseafood.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "product_id",referencedColumnName = "id")
    private Product product;


    @ManyToOne
    @JoinColumn(name = "product_variant_id",referencedColumnName = "id")
    private ProductVariant productVariant;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "total")
    private double total;
}
