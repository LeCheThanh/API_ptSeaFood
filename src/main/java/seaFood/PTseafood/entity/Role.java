package seaFood.PTseafood.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import seaFood.PTseafood.common.Enum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
