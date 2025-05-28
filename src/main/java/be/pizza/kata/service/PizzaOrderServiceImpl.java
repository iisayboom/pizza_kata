package be.pizza.kata.service;

import be.pizza.kata.PizzaOrder;
import be.pizza.kata.PizzaOrderRepository;
import be.pizza.kata.dto.PizzaOrderRequest;
import be.pizza.kata.dto.PizzaOrderResponse;
import org.springframework.stereotype.Service;

@Service
public class PizzaOrderServiceImpl implements PizzaOrderService {

    private final PizzaOrderRepository repository;

    public PizzaOrderServiceImpl(PizzaOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public PizzaOrderResponse order(PizzaOrderRequest request) {
        PizzaOrder order = new PizzaOrder();
        order.setPizza(request.pizza);
        order.setSize(request.size);
        order = repository.save(order);

        String estimatedTime = "20 minutes";

        return new PizzaOrderResponse(order.getId().toString(), estimatedTime);
    }
}
