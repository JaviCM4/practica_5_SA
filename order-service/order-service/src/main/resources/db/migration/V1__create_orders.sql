CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    saga_id UUID NOT NULL UNIQUE,
    product_id BIGINT NOT NULL,
    customer_name VARCHAR(150) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    total_amount NUMERIC(12,2) NOT NULL CHECK (total_amount >= 0),
    shipping_address VARCHAR(255) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);