package be.pizza.kata.dto;

public record PizzaOrderResponse(
        String orderId,
        String estimatedTime
) {}
