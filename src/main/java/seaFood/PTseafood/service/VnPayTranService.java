package seaFood.PTseafood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seaFood.PTseafood.entity.VnPayTransaction;
import seaFood.PTseafood.repository.IVnPayTransactionRepository;

@Service
public class VnPayTranService {
    @Autowired
    private IVnPayTransactionRepository vnPayTransactionRepository;

    public VnPayTransaction save (VnPayTransaction vnPayTransaction){return vnPayTransactionRepository.save(vnPayTransaction);}
}
