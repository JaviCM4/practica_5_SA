CREATE TABLE inventory_stock (
    product_id BIGINT PRIMARY KEY,
    product_name VARCHAR(150) NOT NULL,
    available_quantity INTEGER NOT NULL CHECK (available_quantity >= 0)
);


CREATE TABLE inventory_reservations (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL UNIQUE,
    saga_id UUID NOT NULL UNIQUE,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);