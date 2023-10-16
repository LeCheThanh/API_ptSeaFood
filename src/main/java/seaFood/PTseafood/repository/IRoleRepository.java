package seaFood.PTseafood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import seaFood.PTseafood.entity.Role;

public interface IRoleRepository extends JpaRepository<Role,Long> {
    @Query("SELECT r.id FROM Role r WHERE r.name = ?1")
    Role findByName(String role);
}
