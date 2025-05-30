package be.pizza.kata.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record PizzaOrderRequest(
        @NotBlank(message = "Pizza must not be blank")
        String pizzaName,

        @NotBlank(message = "Size must not be blank")
        @Pattern(regexp = "SMALL|MEDIUM|LARGE", message = "Size must be one of SMALL, MEDIUM, or LARGE")
        String size,

        List<String> toppings
) {
}