package be.pizza.kata.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static be.pizza.kata.domain.Size.LARGE;
import static be.pizza.kata.domain.Size.MEDIUM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @Test
    void createsOrderSuccessfully_withValidPizzaAndSize() {
        Order order = new Order(new Pizza("MARGHERITA", List.of()), LARGE);

        assertThat(order).isNotNull();
        assertThat(order.pizza()).isNotNull();
        assertThat(order.pizza().name()).isEqualTo("MARGHERITA");
        assertThat(order.size()).isEqualTo(LARGE);
        assertThat(order.pizza().toppings()).isEqualTo(List.of());
        assertThat(order.estimatePreparationTime()).isEqualTo(20);
    }

    @Test
    void throwsException_whenPizzaIsNull() {
        assertThatThrownBy(() -> new Order(null, MEDIUM))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("pizza");
    }

    @Test
    void throwsException_whenPizzaNameIsBlank() {
        assertThatThrownBy(() -> new Order(new Pizza("   ", List.of()), MEDIUM))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pizza");
    }

    @Test
    void throwsException_whenSizeIsNull() {
        assertThatThrownBy(() -> new Order(new Pizza("MARGHERITA", List.of()), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("size");
    }

    @Test
    void estimatesPreparationTimeCorrectly_withToppings() {
        Pizza pizza = new Pizza("VEGGIE", List.of(Topping.OLIVES, Topping.EXTRA_CHEESE));
        Order order = new Order(pizza, Size.SMALL);

        assertThat(order.estimatePreparationTime()).isEqualTo(24);
    }

    @Test
    void retainsToppingsInsidePizzaInstance() {
        List<Topping> toppings = List.of(Topping.EXTRA_CHEESE);
        Pizza pizza = new Pizza("NAPOLITANA", toppings);
        Order order = new Order(pizza, Size.MEDIUM);

        assertThat(order.pizza().toppings()).containsExactly(Topping.EXTRA_CHEESE);
    }
}