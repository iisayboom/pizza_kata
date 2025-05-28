package be.pizza.kata.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {
    @Test
    void shouldCreateOrderWithValidData() {
        Order order = new Order("MARGHERITA", "LARGE");
        assertThat(order.pizza()).isEqualTo("MARGHERITA");
        assertThat(order.size()).isEqualTo("LARGE");
        assertThat(order.estimatePreparationTime()).isEqualTo(20);
    }

    @Test
    void shouldThrow_whenPizzaIsNull() {
        assertThatThrownBy(() -> new Order(null, "MEDIUM"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("pizza");
    }

    @Test
    void shouldThrow_whenPizzaIsBlank() {
        assertThatThrownBy(() -> new Order("   ", "MEDIUM"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("pizza");
    }

    @Test
    void shouldThrow_whenSizeIsInvalid() {
        assertThatThrownBy(() -> new Order("MARGHERITA", "GODZILLA"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("size");
    }
}