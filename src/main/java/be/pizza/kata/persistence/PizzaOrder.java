package be.pizza.kata.persistence;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "pizza_order")
public class PizzaOrder {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "pizza", nullable = false)
    private String pizza;
    @Column(name = "size", nullable = false)
    private String size;
}
