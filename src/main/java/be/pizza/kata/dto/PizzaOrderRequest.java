package be.pizza.kata.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PizzaOrderRequest(
        @NotBlank(message = "Pizza must not be blank")
        String pizza,

        @NotBlank(message = "Size must not be blank")
        @Pattern(regexp = "SMALL|MEDIUM|LARGE", message = "Size must be one of SMALL, MEDIUM, or LARGE")
        String size
) {}
