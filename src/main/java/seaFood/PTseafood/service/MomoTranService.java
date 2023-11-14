package seaFood.PTseafood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seaFood.PTseafood.entity.MomoTransaction;
import seaFood.PTseafood.repository.IMomoTransactionRepository;

@Service
public class MomoTranService {
    @Autowired
    private IMomoTransactionRepository momoTransactionRepository;

    public MomoTransaction save(MomoTransaction momoTransaction){return momoTransactionRepository.save(momoTransaction);}
}
