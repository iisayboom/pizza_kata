package be.pizza.kata.service;

import be.pizza.kata.persistence.PizzaOrder;
import be.pizza.kata.PizzaOrderRepository;
import be.pizza.kata.dto.PizzaOrderRequest;
import be.pizza.kata.dto.PizzaOrderResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PizzaOrderServiceImplTest {
    @Mock
    private PizzaOrderRepository repository;

    @InjectMocks
    private PizzaOrderServiceImpl service;

    @Test
    public void createOrder_shouldSaveAndReturnCorrectResponse() {
        PizzaOrderRequest request = new PizzaOrderRequest("MARGHERITA", "MEDIUM");

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
    public void shouldThrow_whenPizzaIsNull() {
        PizzaOrderRequest request = new PizzaOrderRequest(null, "MEDIUM");

        assertThatThrownBy(() -> service.order(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("pizza");
    }

    @Test
    public void shouldThrow_whenSizeIsInvalid() {
        PizzaOrderRequest request = new PizzaOrderRequest("MARGHERITA", "GODZILLA");

        assertThatThrownBy(() -> service.order(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("size");
    }

    @Test
    public void shouldHandle_repositoryException() {
        PizzaOrderRequest request = new PizzaOrderRequest("MARGHERITA", "MEDIUM");

        when(repository.save(any())).thenThrow(new RuntimeException("Broken DB connection"));

        assertThatThrownBy(() -> service.order(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Broken DB connection");
    }
}