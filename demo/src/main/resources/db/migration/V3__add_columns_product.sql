-- Agregar columna producto_id a la tabla transactions
ALTER TABLE transactions ADD COLUMN producto_id INT;

-- Agregar restricción de clave foránea para relacionar con la tabla productos
ALTER TABLE transactions
  ADD CONSTRAINT fk_transactions_producto FOREIGN KEY (producto_id)
  REFERENCES productos(id) ON DELETE CASCADE;

--Agregar columna producto_id a la tabla inventory
ALTER TABLE inventories ADD COLUMN producto_id INT;

-- Agregar restricción de clave foránea para relacionar con la tabla productos
ALTER TABLE inventories
  ADD CONSTRAINT fk_inventories_producto FOREIGN KEY (producto_id)
  REFERENCES productos(id) ON DELETE CASCADE;