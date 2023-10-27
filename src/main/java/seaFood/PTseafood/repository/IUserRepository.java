package seaFood.PTseafood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import seaFood.PTseafood.entity.User;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findbyEmail(String email);

    @Query("SELECT u.id FROM User u WHERE u.email = ?1")
    Long getUserIdByEmail(String email);

    @Query(value = "INSERT INTO user_roles (user_id, role_id) VALUES (?1, ?2)", nativeQuery = true)
    void addRoleToUser(Long userId, Long roleId);
}
