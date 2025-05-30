package be.pizza.kata.mapper;

import be.pizza.kata.domain.Order;
import be.pizza.kata.domain.Size;
import be.pizza.kata.domain.Topping;
import be.pizza.kata.dto.PizzaOrderRequest;
import be.pizza.kata.exception.custom.InvalidFieldException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PizzaOrderMapperTest {

    private final PizzaOrderMapper mapper = new PizzaOrderMapper();

    @Test
    void mapsValidRequestToDomainOrder() {
        PizzaOrderRequest request = new PizzaOrderRequest("MARGHERITA", "MEDIUM", List.of("EXTRA_CHEESE", "OLIVES"));

        Order order = mapper.toDomain(request);

        assertThat(order.pizza().name()).isEqualTo("MARGHERITA");
        assertThat(order.pizza().toppings()).containsExactly(Topping.EXTRA_CHEESE, Topping.OLIVES);
        assertThat(order.size()).isEqualTo(Size.MEDIUM);
    }

    @Test
    void handlesNullToppingsListGracefully() {
        PizzaOrderRequest request = new PizzaOrderRequest("EXTRA_CHEESE", "SMALL", null);

        Order order = mapper.toDomain(request);

        assertThat(order.pizza().toppings()).isEmpty();
    }

    @Test
    void throwsExceptionForInvalidSize() {
        PizzaOrderRequest request = new PizzaOrderRequest("VEGGIE", "GODZILLA", List.of());

        assertThatThrownBy(() -> mapper.toDomain(request))
                .isInstanceOf(InvalidFieldException.class)
                .hasMessageContaining("Invalid size")
                .hasMessageContaining("GODZILLA");
    }

    @Test
    void throwsExceptionForInvalidTopping() {
        PizzaOrderRequest request = new PizzaOrderRequest("VEGGIE", "LARGE", List.of("BANANA"));

        assertThatThrownBy(() -> mapper.toDomain(request))
                .isInstanceOf(InvalidFieldException.class)
                .hasMessageContaining("Invalid topping")
                .hasMessageContaining("BANANA");
    }
}