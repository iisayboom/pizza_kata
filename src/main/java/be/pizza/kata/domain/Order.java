package be.pizza.kata.domain;

public record Order(Pizza pizza, Size size) {
    public Order {
        if (pizza == null) {
            throw new IllegalArgumentException("pizza must not be null or blank");
        }
        if (size == null) {
            throw new IllegalArgumentException("size must be one of SMALL, MEDIUM, or LARGE");
        }
    }

    public int estimatePreparationTime() {
        return pizza.totalBakeTime();
    }
}