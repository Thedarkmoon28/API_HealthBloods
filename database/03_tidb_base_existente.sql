-- Selecciona tu base existente en TiDB antes de ejecutar este archivo.
-- No crea otra base, no borra tablas y no modifica HealthBloods.
-- Si ya existen tablas Posh con otro esquema, revisar SHOW CREATE TABLE antes.
-- Sin CHECK para compatibilidad con TiDB donde están deshabilitados.
SET NAMES utf8mb4;
CREATE TABLE IF NOT EXISTS usuarios (
 id CHAR(36) PRIMARY KEY, nombre VARCHAR(120) NOT NULL, correo VARCHAR(254) NOT NULL UNIQUE,
 password_hash VARCHAR(255) NOT NULL, rol ENUM('ADMINISTRADOR','EMPLEADO') NOT NULL,
 activo BOOLEAN NOT NULL DEFAULT TRUE, token_version INT NOT NULL DEFAULT 0,
 fecha_creacion TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
 fecha_actualizacion TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS productos (
 id CHAR(36) PRIMARY KEY, item VARCHAR(30) NOT NULL UNIQUE, nombre VARCHAR(200) NOT NULL,
 marca VARCHAR(120) NOT NULL, dias_tras_descongelar INT NOT NULL,
 articulos_por_caja INT NULL, articulos_por_nivel INT NULL, activo BOOLEAN NOT NULL DEFAULT TRUE,
 fecha_actualizacion TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS registros_etiquetado (
 id CHAR(36) PRIMARY KEY, usuario_id CHAR(36) NOT NULL, producto_id CHAR(36) NOT NULL,
 fecha_registro TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6), fecha_descongelacion DATE NOT NULL,
 dias_aplicados INT NOT NULL, fecha_caducidad DATE NOT NULL,
 tipo_captura ENUM('CAJAS','NIVELES') NOT NULL, cantidad_ingresada INT NOT NULL,
 articulos_por_unidad_aplicados INT NOT NULL, total_etiquetas BIGINT NOT NULL,
 item_aplicado VARCHAR(30) NOT NULL, nombre_aplicado VARCHAR(200) NOT NULL,
 FOREIGN KEY(usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
 FOREIGN KEY(producto_id) REFERENCES productos(id) ON DELETE RESTRICT,
 INDEX idx_registros_caducidad(fecha_caducidad), INDEX idx_registros_usuario(usuario_id,fecha_registro)
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS favoritos (
 id CHAR(36) PRIMARY KEY, usuario_id CHAR(36) NOT NULL, producto_id CHAR(36) NOT NULL,
 fecha_agregado TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
 UNIQUE KEY uq_favorito(usuario_id,producto_id),
 FOREIGN KEY(usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
 FOREIGN KEY(producto_id) REFERENCES productos(id) ON DELETE RESTRICT
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS revisiones_caducidad (
 id CHAR(36) PRIMARY KEY, registro_etiquetado_id CHAR(36) NOT NULL, usuario_id CHAR(36) NOT NULL,
 fecha_revision TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
 resultado ENUM('VIGENTE','RETIRADO','AGOTADO') NOT NULL, observaciones VARCHAR(1000) NULL,
 FOREIGN KEY(registro_etiquetado_id) REFERENCES registros_etiquetado(id) ON DELETE RESTRICT,
 FOREIGN KEY(usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
 INDEX idx_revision_registro(registro_etiquetado_id,fecha_revision)
) ENGINE=InnoDB;
