-- =============================================================================
-- SMARTWALLET - SCRIPT DE BASE DE DATOS POSTGRESQL (smartwallet_db)
-- Puerto configurado: 5433 | Usuario: postgres
-- =============================================================================

-- 1. Crear base de datos (ejecutar en pgAdmin o psql si no existe aún):
-- CREATE DATABASE smartwallet_db;

-- 2. Limpieza de tablas previas si existen
DROP TABLE IF EXISTS gastos CASCADE;
DROP TABLE IF EXISTS presupuestos CASCADE;
DROP TABLE IF EXISTS categorias CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;

-- 3. Tabla: usuarios
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL DEFAULT 'USER',
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    creado TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    modificado TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 4. Tabla: categorias
CREATE TABLE categorias (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    creado TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    modificado TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 5. Tabla: presupuestos
CREATE TABLE presupuestos (
    id BIGSERIAL PRIMARY KEY,
    monto_limite NUMERIC(12, 2) NOT NULL,
    monto_actual NUMERIC(12, 2) NOT NULL DEFAULT 0.00,
    categoria_id BIGINT NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    creado TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    modificado TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_presupuesto_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE RESTRICT
);

-- 6. Tabla: gastos
CREATE TABLE gastos (
    id BIGSERIAL PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    monto NUMERIC(12, 2) NOT NULL,
    moneda VARCHAR(10) NOT NULL DEFAULT 'USD',
    fecha TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    categoria_id BIGINT NOT NULL,
    creado TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    modificado TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_gasto_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE RESTRICT
);

-- 7. Datos de prueba iniciales
INSERT INTO categorias (nombre, descripcion) VALUES
('Alimentacion', 'Comidas, restaurantes, supermercado'),
('Transporte', 'Gasolina, transporte publico, uber'),
('Servicios', 'Luz, agua, internet, telefonia'),
('Entretenimiento', 'Cine, suscripciones, salidas');

INSERT INTO presupuestos (monto_limite, monto_actual, categoria_id, fecha_inicio, fecha_fin) VALUES
(200.00, 0.00, 1, '2026-01-01', '2026-12-31'),
(100.00, 0.00, 2, '2026-01-01', '2026-12-31');

-- Usuario admin y user inicial (password BCrypt valido de 'admin123')
INSERT INTO usuarios (username, password, rol, activo) VALUES
('admin', '$2a$10$CpkoQnPZgLWaqjEyBPSB1uxiKeP8WJgt5c4VMHMxTGt0.9q23FTWq', 'ADMIN', true),
('usuario', '$2a$10$CpkoQnPZgLWaqjEyBPSB1uxiKeP8WJgt5c4VMHMxTGt0.9q23FTWq', 'USER', true)
ON CONFLICT (username) DO UPDATE 
SET password = EXCLUDED.password, rol = EXCLUDED.rol;
