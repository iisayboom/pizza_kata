package be.pizza.kata.service;

import be.pizza.kata.dto.PizzaOrderRequest;
import be.pizza.kata.dto.PizzaOrderResponse;

public interface PizzaOrderService {
    PizzaOrderResponse order(PizzaOrderRequest request);
}
