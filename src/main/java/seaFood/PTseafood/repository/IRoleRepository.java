package seaFood.PTseafood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seaFood.PTseafood.entity.Role;

public interface IRoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String role);
}
