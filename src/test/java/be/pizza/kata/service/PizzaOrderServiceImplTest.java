package be.pizza.kata.service;

import be.pizza.kata.dto.PizzaOrderRequest;
import be.pizza.kata.dto.PizzaOrderResponse;
import be.pizza.kata.exception.custom.InvalidFieldException;
import be.pizza.kata.mapper.PizzaOrderMapper;
import be.pizza.kata.persistence.PizzaOrder;
import be.pizza.kata.persistence.PizzaOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PizzaOrderServiceImplTest {

    private final PizzaOrderMapper pizzaOrderMapper = new PizzaOrderMapper();
    private PizzaOrderServiceImpl service;

    @Mock
    private PizzaOrderRepository repository;

    @BeforeEach
    void setUp() {
        service = new PizzaOrderServiceImpl(repository, pizzaOrderMapper);
    }

    @Test
    void returnsResponseWithOrderIdAndTime_whenOrderIsCreated() {
        PizzaOrderRequest request = new PizzaOrderRequest("MARGHERITA", "MEDIUM", List.of());

        PizzaOrder savedEntity = new PizzaOrder();
        savedEntity.setId(UUID.randomUUID());
        savedEntity.setPizza("MARGHERITA");
        savedEntity.setSize("MEDIUM");

        when(repository.save(any(PizzaOrder.class))).thenReturn(savedEntity);

        PizzaOrderResponse response = service.order(request);

        assertThat(response).isNotNull();
        assertThat(response.orderId()).isNotNull();
        assertThat(response.estimatedTime()).isEqualTo("20 minutes");
    }

    @Test
    void throwsException_whenPizzaIsNull() {
        PizzaOrderRequest request = new PizzaOrderRequest(null, "MEDIUM", List.of());

        assertThatThrownBy(() -> service.order(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pizza name must not be blank");
    }

    @Test
    void throwsException_whenSizeIsInvalid() {
        PizzaOrderRequest request = new PizzaOrderRequest("MARGHERITA", "GODZILLA", List.of());

        assertThatThrownBy(() -> service.order(request))
                .isInstanceOf(InvalidFieldException.class)
                .hasMessageContaining("Invalid size: GODZILLA");
    }

    @Test
    void throwsException_whenRepositoryFailsToSave() {
        PizzaOrderRequest request = new PizzaOrderRequest("MARGHERITA", "MEDIUM", List.of());

        when(repository.save(any())).thenThrow(new RuntimeException("Broken DB connection"));

        assertThatThrownBy(() -> service.order(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Broken DB connection");
    }

    @Test
    void returnsCorrectEstimatedTime_whenToppingsAreProvided() {
        PizzaOrderRequest request = new PizzaOrderRequest("EXTRA_CHEESE", "SMALL", List.of("EXTRA_CHEESE", "EXTRA_CHEESE"));

        PizzaOrder savedEntity = new PizzaOrder();
        savedEntity.setId(UUID.randomUUID());
        savedEntity.setPizza("EXTRA_CHEESE");
        savedEntity.setSize("SMALL");

        when(repository.save(any(PizzaOrder.class))).thenReturn(savedEntity);

        PizzaOrderResponse response = service.order(request);

        assertThat(response.estimatedTime()).isEqualTo("24 minutes");
    }

    @Test
    void throwsException_whenToppingIsInvalid() {
        PizzaOrderRequest request = new PizzaOrderRequest("EXTRA_CHEESE", "LARGE", List.of("BANANA"));

        assertThatThrownBy(() -> service.order(request))
                .isInstanceOf(InvalidFieldException.class)
                .hasMessageContaining("Invalid topping: BANANA");
    }

}