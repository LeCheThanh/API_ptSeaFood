package seaFood.PTseafood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seaFood.PTseafood.entity.Role;

public interface IRoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String role);
//    @Query("SELECT r.id FROM Role r WHERE r.name = ?1")
//    Long getRoleIdByName(String roleName);
}
