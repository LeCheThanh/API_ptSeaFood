package seaFood.PTseafood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seaFood.PTseafood.entity.MomoTransaction;

@Repository
public interface IMomoTransactionRepository extends JpaRepository<MomoTransaction,Long> {
}
