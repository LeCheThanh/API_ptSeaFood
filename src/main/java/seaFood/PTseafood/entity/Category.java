package seaFood.PTseafood.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name="categories")
@NoArgsConstructor
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

    @OneToMany(mappedBy="category",cascade = CascadeType.ALL)
    private Set<Product> products;
    public Category(String id) {
        this.id = Long.valueOf(id);
    }
}
