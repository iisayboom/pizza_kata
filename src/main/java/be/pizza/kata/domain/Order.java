package be.pizza.kata.domain;

public record Order(String pizza, String size) {
    public Order {
        if (pizza == null || pizza.isBlank()) {
            throw new IllegalArgumentException("pizza must not be null or blank");
        }
        if (size == null || !isValidSize(size)) {
            throw new IllegalArgumentException("size must be one of SMALL, MEDIUM, or LARGE");
        }
    }

    private boolean isValidSize(String size) {
        return size.equals("SMALL") || size.equals("MEDIUM") || size.equals("LARGE");
    }

    public int estimatePreparationTime() {
        return 20;
    }
}
