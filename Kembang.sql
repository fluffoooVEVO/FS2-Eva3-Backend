CREATE TABLE rol (
    id_rol INT PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL
);

CREATE TABLE usuario (
    id_usuario INT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    edad DATE,
    email VARCHAR(100) NOT NULL UNIQUE,
    id_rol INT FOREIGN KEY REFERENCES rol(id_rol)
);

CREATE TABLE comuna (
    id_comuna INT PRIMARY KEY,
    nombre_comuna VARCHAR(50) NOT NULL
);

CREATE TABLE region (
    id_region INT PRIMARY KEY,
    nombre_region VARCHAR(50) NOT NULL,
    id_comuna INT FOREIGN KEY REFERENCES comuna(id_comuna)
);

CREATE TABLE direccion (
    id_direccion INT PRIMARY KEY,
    calle VARCHAR(30) NOT NULL,
    numero INT NOT NULL,
    id_comuna INT FOREIGN KEY REFERENCES comuna(id_comuna),
    id_region INT FOREIGN KEY REFERENCES region(id_region)
);

CREATE TABLE categoria (
    id_categoria INT PRIMARY KEY,
    nombre_categoria VARCHAR(50) NOT NULL
);

CREATE TABLE producto (
    id_producto INT PRIMARY KEY,
    nom_producto VARCHAR(50) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    id_categoria INT FOREIGN KEY REFERENCES categoria(id_categoria)
);

CREATE TABLE pedido (
    id_pedido INT PRIMARY KEY,
    fecha_pedido DATE NOT NULL,
    id_usuario INT FOREIGN KEY REFERENCES usuario(id_usuario)
);

CREATE TABLE pago (
    id_pago INT PRIMARY KEY,
    metodo_pago VARCHAR(50) NOT NULL,
    id_pedido INT FOREIGN KEY REFERENCES pedido(id_pedido)
);

CREATE TABLE detalle_pedido (
    id_detalle INT PRIMARY KEY,
    id_pedido INT FOREIGN KEY REFERENCES pedido(id_pedido),
    id_pago INT FOREIGN KEY REFERENCES pago(id_pago)
);

CREATE TABLE estado (
    id_estado INT PRIMARY KEY,
    nombre_estado VARCHAR(50) NOT NULL,
    id_pedido INT FOREIGN KEY REFERENCES pedido(id_pedido)
);

INSERT INTO comuna (id_comuna, nombre_comuna) VALUES
(1, 'Santiago'),
(2, 'Providencia'),
(3, 'Lo Espejo'),
(4, 'Cerrillos'),
(5, 'La Florida');