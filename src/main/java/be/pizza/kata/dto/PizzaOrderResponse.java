package be.pizza.kata.dto;

public class PizzaOrderResponse {
    public String orderId;
    public String estimatedTime;

    public PizzaOrderResponse(String orderId, String estimatedTime) {
        this.orderId = orderId;
        this.estimatedTime = estimatedTime;
    }
}
