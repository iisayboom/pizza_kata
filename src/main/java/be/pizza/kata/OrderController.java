package be.pizza.kata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

@RestController
public class OrderController {

    private final PizzaOrderRepository repository;

    @Autowired
    public OrderController(PizzaOrderRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/order")
    public Map<String, String> order(@RequestBody Map<String, String> request) {
        String pizza = request.get("pizza");
        String size = request.get("size");

        PizzaOrder order = new PizzaOrder();
        order.setPizza(pizza);
        order.setSize(size);
        order = repository.save(order);

        String estimatedTime = "20 minutes";

        Map<String, String> response = new HashMap<>();
        response.put("orderId", order.getId().toString());
        response.put("estimatedTime", estimatedTime);
        return response;
    }
}
