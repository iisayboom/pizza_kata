package be.pizza.kata.domain;

import java.util.List;

public record Pizza(String name, List<Topping> toppings) {
    public Pizza {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Pizza name must not be blank");
        }
        if (toppings == null) {
            throw new IllegalArgumentException("Toppings must not be null");
        }
    }

    public int totalBakeTime() {
        int base = 20;
        return base + (toppings.size() * 2);
    }
}
