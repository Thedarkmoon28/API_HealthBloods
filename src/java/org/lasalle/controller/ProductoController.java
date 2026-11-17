package org.lasalle.controller;

import java.sql.*;
import java.time.*;
import java.util.*;
import org.lasalle.model.*;

public class ProductoController extends ControladorBase {
  public ProductoController() {
    super();
  }

  public ProductoController(Usuario usuario) {
    super(usuario);
  }

  public List<Producto> getProductos(String q, boolean all) throws SQLException {
    if (q.length() > 200) throw new ErrorAplicacion(400, "Búsqueda demasiado larga");
    if (all) admin();
    String term = "%" + q.trim() + "%";
    try (var c = DatosController.open()) {
      return ModeloMapper.productoLista(
          DatosController.query(
              c,
              PRODUCT
                  + " WHERE (nombre LIKE ? OR item LIKE ? OR marca LIKE ?)"
                  + (all ? "" : " AND activo=TRUE")
                  + " ORDER BY nombre",
              term,
              term,
              term));
    }
  }

  public Producto getProducto(String id) throws SQLException {
    try (var c = DatosController.open()) {
      return ModeloMapper.producto(
          DatosController.one(c, PRODUCT + " WHERE id=?", ValidacionController.uuid(id)));
    }
  }

  public ResultadoOperacion<Producto> insertarProducto(Map<String, Object> b) throws SQLException {
    admin();
    String id = id();
    List<Object> a = new ArrayList<>();
    a.add(id);
    a.addAll(Arrays.asList(productValues(b)));
    try (var c = DatosController.open()) {
      DatosController.update(
          c,
          "INSERT INTO"
              + " productos(id,item,nombre,marca,dias_tras_descongelar,articulos_por_caja,articulos_por_nivel,activo)"
              + " VALUES(?,?,?,?,?,?,?,?)",
          a.toArray());
      return created(ModeloMapper.producto(DatosController.one(c, PRODUCT + " WHERE id=?", id)));
    }
  }

  public Producto actualizarProducto(String id, Map<String, Object> b) throws SQLException {
    admin();
    ValidacionController.uuid(id);
    List<Object> a = new ArrayList<>(Arrays.asList(productValues(b)));
    a.add(id);
    try (var c = DatosController.open()) {
      DatosController.one(c, PRODUCT + " WHERE id=?", id);
      DatosController.update(
          c,
          "UPDATE productos SET"
              + " item=?,nombre=?,marca=?,dias_tras_descongelar=?,articulos_por_caja=?,articulos_por_nivel=?,activo=?"
              + " WHERE id=?",
          a.toArray());
      return ModeloMapper.producto(DatosController.one(c, PRODUCT + " WHERE id=?", id));
    }
  }

  public Object desactivarProducto(String id) throws SQLException {
    admin();
    try (var c = DatosController.open()) {
      DatosController.one(c, PRODUCT + " WHERE id=?", ValidacionController.uuid(id));
      DatosController.update(c, "UPDATE productos SET activo=FALSE WHERE id=?", id);
      return Map.of("mensaje", "Producto desactivado; historial conservado");
    }
  }

  private Object[] productValues(Map<String, Object> b) {
    return new Object[] {
      ValidacionController.text(b, "item", 30),
      ValidacionController.text(b, "nombre", 200),
      ValidacionController.text(b, "marca", 120),
      ValidacionController.integer(b, "diasTrasDescongelar", 3650),
      ValidacionController.integer(b, "articulosPorCaja", 1000000),
      ValidacionController.integer(b, "articulosPorNivel", 1000000),
      ValidacionController.bool(b, "activo")
    };
  }
}
