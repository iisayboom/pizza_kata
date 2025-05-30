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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OrderControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PizzaOrderRepository repository;

    private HttpHeaders headers;

    @SuppressWarnings("resource")
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("pizza-test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setup() {
        repository.deleteAll();

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void shouldReturnOrderIdAndEstimatedTime() {
        String requestJson = "{ \"pizzaName\": \"MARGHERITA\", \"size\": \"MEDIUM\" }";
        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/order",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
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
        String requestJson = "{ \"pizzaName\": \"EXTRA_CHEESE\", \"size\": \"LARGE\" }";
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

    @Test
    void shouldReturn400_whenRequestValidationFails() {
        String invalidJson = "{ \"pizzaName\": \"\", \"size\": \"GODZILLA\" }";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(invalidJson, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/order",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body).containsEntry("error", "Validation failed");
        assertThat(body).containsKey("details");

        @SuppressWarnings("unchecked")
        Map<String, String> details = (Map<String, String>) body.get("details");
        assertThat(details).containsKeys("pizzaName", "size");
    }

    @Test
    void shouldReturn400_whenDomainValidationFails() {
        String json = "{ \"pizzaName\": null, \"size\": \"MEDIUM\" }";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/order",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body).containsEntry("error", "Validation failed");

        @SuppressWarnings("unchecked")
        Map<String, String> details = (Map<String, String>) body.get("details");
        assertThat(details).containsEntry("pizzaName", "Pizza must not be blank");
    }

    @Test
    void shouldReturnCorrectEstimatedTime_whenToppingsAreIncluded() {
        String requestJson = """
                {
                    "pizzaName": "VEGGIE",
                    "size": "SMALL",
                    "toppings": ["OLIVES", "EXTRA_CHEESE"]
                }
                """;
        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/order",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body).containsEntry("estimatedTime", "24 minutes");
        assertThat(body.get("orderId")).isNotNull();
    }

    @Test
    void shouldReturn400_whenToppingIsInvalid() {
        String json = """
                {
                    "pizzaName": "FUNGI",
                    "size": "MEDIUM",
                    "toppings": ["BANANA"]
                }
                """;
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/order",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body).containsEntry("error", "Validation failed");
        assertThat(body.get("details")).asString().contains("BANANA");
    }

}
