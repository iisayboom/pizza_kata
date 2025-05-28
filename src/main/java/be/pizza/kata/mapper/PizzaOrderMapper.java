package be.pizza.kata.mapper;

import be.pizza.kata.persistence.PizzaOrder;
import be.pizza.kata.domain.Order;

public class PizzaOrderMapper {

    public static PizzaOrder fromDomain(Order order) {
        PizzaOrder entity = new PizzaOrder();
        entity.setPizza(order.pizza());
        entity.setSize(order.size());
        return entity;
    }

}
