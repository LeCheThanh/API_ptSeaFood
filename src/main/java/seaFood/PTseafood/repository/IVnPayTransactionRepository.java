package seaFood.PTseafood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seaFood.PTseafood.entity.VnPayTransaction;

@Repository
public interface IVnPayTransactionRepository extends JpaRepository<VnPayTransaction,Long> {
}
