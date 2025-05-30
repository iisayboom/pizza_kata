DROP TABLE IF EXISTS pizza_order_toppings CASCADE;
DROP TABLE IF EXISTS pizza_order CASCADE;
CREATE TABLE pizza_order (
    id UUID PRIMARY KEY,
    pizza VARCHAR(255),
    size VARCHAR(255)
);