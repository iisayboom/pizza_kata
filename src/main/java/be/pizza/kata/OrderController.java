package be.pizza.kata;

import be.pizza.kata.dto.PizzaOrderRequest;
import be.pizza.kata.dto.PizzaOrderResponse;
import be.pizza.kata.service.PizzaOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final PizzaOrderService service;

    @Autowired
    public OrderController(PizzaOrderService service) {
        this.service = service;
    }

    @PostMapping("/order")
    public PizzaOrderResponse order(@RequestBody PizzaOrderRequest request) {
        return service.order(request);
    }
}
