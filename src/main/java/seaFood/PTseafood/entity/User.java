package seaFood.PTseafood.entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import seaFood.PTseafood.common.Enum;

import java.math.BigInteger;
import java.util.*;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name= "wholesale", nullable = false)
    private boolean wholeSale;

    @Column(name= "address")
    private String address;

    @Column(name= "user_rank")
    @Enumerated(EnumType.STRING)
    private Enum.Rank rank;

    @Column(name = "discount_rate")
    private int discountRate;

    @Column(name="total_purchase_amount")
    private BigInteger totalPurchaseAmount;

    @Enumerated(EnumType.STRING)
    private Enum.Provider provider;


//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Favorite> favorites = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.stream().forEach(i -> authorities.add(new SimpleGrantedAuthority(i.getName())));
        System.out.println("List role: "+authorities);
        return  authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

//    public User(Long id, String email, String fullName, String password, String phone, boolean wholeSale, String address, String rank, int discountRate, BigInteger totalPurchaseAmount, Set<Role> roles) {
//        this.id = id;
//        this.email = email;
//        this.fullName = fullName;
//        this.password = password;
//        this.phone = phone;
//        this.wholeSale = wholeSale;
//        this.address = address;
//        this.rank = rank;
//        this.discountRate = discountRate;
//        this.totalPurchaseAmount = totalPurchaseAmount;
//        this.roles = roles;
//    }
//    public User() {
//        // Constructor mặc định không có tham số
//    }
}
