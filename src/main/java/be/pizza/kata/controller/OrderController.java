package be.pizza.kata.controller;

import be.pizza.kata.dto.PizzaOrderRequest;
import be.pizza.kata.dto.PizzaOrderResponse;
import be.pizza.kata.service.PizzaOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class OrderController {

    private final PizzaOrderService service;

    @Autowired
    public OrderController(PizzaOrderService service) {
        this.service = service;
    }

    @PostMapping("/order")
    public ResponseEntity<PizzaOrderResponse> order(@RequestBody @Valid PizzaOrderRequest request) {
        PizzaOrderResponse response = service.order(request);
        URI location = URI.create("/order/" + response.orderId());
        return ResponseEntity.created(location).body(response);
    }
}
