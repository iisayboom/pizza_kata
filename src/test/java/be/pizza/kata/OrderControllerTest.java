package be.pizza.kata;

import be.pizza.kata.persistence.PizzaOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OrderControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PizzaOrderRepository repository;

    private HttpHeaders headers;

    @BeforeEach
    void setup() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void shouldReturnOrderIdAndEstimatedTime() {
        String requestJson = "{ \"pizza\": \"MARGHERITA\", \"size\": \"MEDIUM\" }";
        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/order",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body)
                .containsKeys("orderId", "estimatedTime")
                .extractingByKey("estimatedTime").isEqualTo("20 minutes");
        assertThat(body.get("orderId")).isNotNull();
    }

    @Test
    void shouldPersistOrderInDatabase() {
        long countBefore = repository.count();
        String requestJson = "{ \"pizza\": \"PEPPERONI\", \"size\": \"LARGE\" }";
        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        restTemplate.exchange(
                "/order",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        long countAfter = repository.count();
        assertThat(countAfter).isEqualTo(countBefore + 1);
    }
}
