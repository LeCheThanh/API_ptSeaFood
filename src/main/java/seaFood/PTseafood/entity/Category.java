package seaFood.PTseafood.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.util.Set;

@Data
@Entity
@Table(name="categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(name = "parent_id")
    @ColumnDefault("0")
    private Long parentId;

    private String slug;

    @OneToMany(mappedBy="category",cascade = CascadeType.ALL )
    private Set<Product> products;

}
