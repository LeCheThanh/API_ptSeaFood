package seaFood.PTseafood.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
//    @Enumerated(EnumType.STRING)
    private String name;


//    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
//    private List<User> users = new ArrayList<>();
    @ManyToMany(mappedBy = "roles")
    private Set<User> user = new HashSet<>();

//    public Role(Long id, String name) {
//        this.id = id;
//        this.name = name;
//    }

    public Role( String name) {
        this.name = name;
    }
}
