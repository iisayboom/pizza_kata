package be.pizza.kata.mapper;

import be.pizza.kata.domain.Order;
import be.pizza.kata.domain.Pizza;
import be.pizza.kata.domain.Size;
import be.pizza.kata.domain.Topping;
import be.pizza.kata.dto.PizzaOrderRequest;
import be.pizza.kata.exception.custom.InvalidFieldException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PizzaOrderMapper {

    public Order toDomain(PizzaOrderRequest dto) {
        Pizza pizza = mapPizza(dto);
        Size size = mapSize(dto.size());
        return new Order(pizza, size);
    }

    private Pizza mapPizza(PizzaOrderRequest dto) {
        List<Topping> toppings = mapToppings(dto.toppings());
        return new Pizza(dto.pizzaName(), toppings);
    }

    private Size mapSize(String rawSize) {
        try {
            return Size.valueOf(rawSize.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidFieldException("size", "Invalid size: " + rawSize);
        }
    }

    private List<Topping> mapToppings(List<String> toppingStrings) {
        if (toppingStrings == null) return List.of();

        return toppingStrings.stream()
                .map(this::mapTopping)
                .toList();
    }

    private Topping mapTopping(String rawTopping) {
        try {
            return Topping.valueOf(rawTopping.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidFieldException("toppings", "Invalid topping: " + rawTopping);
        }
    }
}
