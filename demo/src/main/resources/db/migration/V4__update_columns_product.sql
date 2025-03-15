-- Renombrar la columna producto_id a product_id en transactions
ALTER TABLE transactions RENAME COLUMN producto_id TO product_id;

-- Renombrar la columna producto_id a product_id en inventories
ALTER TABLE inventories RENAME COLUMN producto_id TO product_id;
