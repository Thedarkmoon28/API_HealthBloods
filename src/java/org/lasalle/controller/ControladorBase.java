package org.lasalle.controller;

import java.time.*;
import java.util.*;
import org.lasalle.model.*;

/** Contexto del usuario autenticado y consultas compartidas entre controladores. */
public abstract class ControladorBase {
  protected final Usuario usuario;

  protected ControladorBase() {
    this(null);
  }

  protected ControladorBase(Usuario usuario) {
    this.usuario = usuario;
  }

  protected String uid() {
    if (usuario == null) throw new ErrorAplicacion(401, "Inicia sesión");
    return usuario.getId();
  }

  protected void admin() {
    if (usuario == null || !"ADMINISTRADOR".equals(usuario.getRol()))
      throw new ErrorAplicacion(403, "Se requiere rol administrador");
  }

  protected static String id() {
    return UUID.randomUUID().toString();
  }

  protected static LocalDate today() {
    return LocalDate.now(
        ZoneId.of(System.getenv().getOrDefault("APP_TIMEZONE", "America/Mexico_City")));
  }

  protected static <T> ResultadoOperacion<T> created(T datos) {
    return new ResultadoOperacion<>(datos, true);
  }

  protected static final String PRODUCT =
      "SELECT id,item,nombre,marca,dias_tras_descongelar AS diasTrasDescongelar,articulos_por_caja"
          + " AS articulosPorCaja,articulos_por_nivel AS"
          + " articulosPorNivel,activo,fecha_actualizacion AS fechaActualizacion FROM productos";
  protected static final String USER =
      "SELECT id,nombre,correo,rol,activo,fecha_creacion AS fechaCreacion,fecha_actualizacion AS"
          + " fechaActualizacion FROM usuarios";
  protected static final String RECORD =
      "SELECT r.id,r.usuario_id AS usuarioID,u.nombre AS usuarioNombre,r.producto_id AS"
          + " productoID,r.item_aplicado AS item,r.nombre_aplicado AS nombre,r.fecha_registro AS"
          + " fechaRegistro,r.fecha_descongelacion AS fechaDescongelacion,r.dias_aplicados AS"
          + " diasAplicados,r.fecha_caducidad AS fechaCaducidad,r.tipo_captura AS"
          + " tipoCaptura,r.cantidad_ingresada AS"
          + " cantidadIngresada,r.articulos_por_unidad_aplicados AS"
          + " articulosPorUnidadAplicados,r.total_etiquetas AS totalEtiquetas FROM"
          + " registros_etiquetado r JOIN usuarios u ON u.id=r.usuario_id";
  protected static final String REVIEW =
      "SELECT v.id,v.registro_etiquetado_id AS registroEtiquetadoID,v.usuario_id AS"
          + " usuarioID,u.nombre AS usuarioNombre,v.fecha_revision AS"
          + " fechaRevision,v.resultado,v.observaciones FROM revisiones_caducidad v JOIN usuarios u"
          + " ON u.id=v.usuario_id";
}
