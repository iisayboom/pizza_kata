package be.pizza.kata.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PizzaTest {
    @Test
    void shouldCreatePizzaWithValidInput() {
        Pizza pizza = new Pizza("MARGHERITA", List.of(Topping.OLIVES, Topping.EXTRA_CHEESE));
        assertThat(pizza.name()).isEqualTo("MARGHERITA");
        assertThat(pizza.toppings()).containsExactly(Topping.OLIVES, Topping.EXTRA_CHEESE);
        assertThat(pizza.totalBakeTime()).isEqualTo(24);
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        assertThatThrownBy(() -> new Pizza(null, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pizza name must not be blank");
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        assertThatThrownBy(() -> new Pizza("   ", List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pizza name must not be blank");
    }

    @Test
    void shouldThrowWhenToppingsIsNull() {
        assertThatThrownBy(() -> new Pizza("MARGHERITA", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Toppings must not be null");
    }

    @Test
    void shouldReturnBaseBakeTimeForNoToppings() {
        Pizza pizza = new Pizza("EXTRA_CHEESE", List.of());
        assertThat(pizza.totalBakeTime()).isEqualTo(20);
    }

    @Test
    void toppingsListShouldBeImmutable() {
        Pizza pizza = new Pizza("FUNGI", List.of(Topping.OLIVES));
        assertThatThrownBy(() -> pizza.toppings().add(Topping.EXTRA_CHEESE))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}