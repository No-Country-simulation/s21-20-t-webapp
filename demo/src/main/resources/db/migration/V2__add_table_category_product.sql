-- Tabla de Categorías de Productos
CREATE TABLE IF NOT EXISTS categorias_productos (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    campos_personalizados JSONB,
    creado_en DATE NOT NULL,
    actualizado_en DATE NOT NULL,
    CONSTRAINT fk_categorias_productos_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id) ON DELETE CASCADE
);

-- Tabla de Productos
CREATE TABLE IF NOT EXISTS productos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    sku VARCHAR(255) UNIQUE NOT NULL,
    campos_personalizados JSONB,
    tenant_id INT NOT NULL,
    categoria_id INT NOT NULL,
    creado_en DATE NOT NULL,
    actualizado_en DATE NOT NULL,
    CONSTRAINT fk_productos_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id) ON DELETE CASCADE,
    CONSTRAINT fk_productos_categoria FOREIGN KEY (categoria_id) REFERENCES categorias_productos(id) ON DELETE CASCADE
);
