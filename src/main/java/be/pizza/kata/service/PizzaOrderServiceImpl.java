package be.pizza.kata.service;

import be.pizza.kata.domain.Order;
import be.pizza.kata.domain.Topping;
import be.pizza.kata.dto.PizzaOrderRequest;
import be.pizza.kata.dto.PizzaOrderResponse;
import be.pizza.kata.mapper.PizzaOrderMapper;
import be.pizza.kata.persistence.PizzaOrder;
import be.pizza.kata.persistence.PizzaOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PizzaOrderServiceImpl implements PizzaOrderService {

    private final PizzaOrderRepository repository;
    private final PizzaOrderMapper mapper;

    public PizzaOrderServiceImpl(PizzaOrderRepository repository, PizzaOrderMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public PizzaOrderResponse order(PizzaOrderRequest request) {
        Order order = mapper.toDomain(request);
        int estimatedMinutes = order.estimatePreparationTime();

        PizzaOrder pizzaOrder = createPizza(order.pizza().name(), order.size().name(), order.pizza().toppings());

        PizzaOrder saved = repository.save(pizzaOrder);
        return new PizzaOrderResponse(saved.getId().toString(), estimatedMinutes + " minutes");
    }

    private PizzaOrder createPizza(String pizzaName, String pizzaSize, List<Topping> toppings) {
        PizzaOrder pizzaOrder = new PizzaOrder();

        pizzaOrder.setPizza(pizzaName);
        pizzaOrder.setSize(pizzaSize);
        pizzaOrder.setToppings(toppings.stream()
                .map(Enum::name)
                .toList());

        return pizzaOrder;
    }
}
