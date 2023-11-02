package seaFood.PTseafood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seaFood.PTseafood.entity.Order;
import seaFood.PTseafood.repository.IOrderRepository;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private IOrderRepository orderRepository;
    ///Admin page
    public List<Order> getAll(){return orderRepository.findAll();}
    //Search by customer
    public Order getByCode(String code){return orderRepository.findByCode(code);}
}
