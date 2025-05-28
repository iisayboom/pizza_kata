package be.pizza.kata;

import be.pizza.kata.persistence.PizzaOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PizzaOrderRepository extends JpaRepository<PizzaOrder, UUID> {
}
