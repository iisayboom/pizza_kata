package be.pizza.kata.service;

import be.pizza.kata.domain.Order;
import be.pizza.kata.dto.PizzaOrderRequest;
import be.pizza.kata.dto.PizzaOrderResponse;
import be.pizza.kata.mapper.PizzaOrderMapper;
import be.pizza.kata.persistence.PizzaOrder;
import be.pizza.kata.persistence.PizzaOrderRepository;
import org.springframework.stereotype.Service;

@Service
public class PizzaOrderServiceImpl implements PizzaOrderService {

    private final PizzaOrderRepository repository;

    public PizzaOrderServiceImpl(PizzaOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public PizzaOrderResponse order(PizzaOrderRequest request) {
        Order domainOrder = new Order(request.pizza(), request.size());

        PizzaOrder entity = PizzaOrderMapper.fromDomain(domainOrder);
        entity = repository.save(entity);

        return new PizzaOrderResponse(entity.getId().toString(), domainOrder.estimatePreparationTime() + " minutes");
    }
}
