package org.lasalle.controller;

import java.sql.*;
import java.time.*;
import java.util.*;
import org.lasalle.model.*;

public class RegistroEtiquetadoController extends ControladorBase {
  public RegistroEtiquetadoController() {
    super();
  }

  public RegistroEtiquetadoController(Usuario usuario) {
    super(usuario);
  }

  public CalculoEtiquetas calcular(Map<String, Object> b) throws SQLException {
    String pid = ValidacionController.uuid(ValidacionController.text(b, "productoID", 36));
    try (var c = DatosController.open()) {
      return ModeloMapper.calculo(
          CalculadoraController.calcular(DatosController.one(c, PRODUCT + " WHERE id=?", pid), b));
    }
  }

  public ResultadoOperacion<RegistroEtiquetado> insertarRegistro(Map<String, Object> b)
      throws SQLException {
    String pid = ValidacionController.uuid(ValidacionController.text(b, "productoID", 36)),
        rid = ValidacionController.uuid(ValidacionController.text(b, "id", 36));
    try (var c = DatosController.open()) {
      c.setAutoCommit(false);
      try {
        // Bloquea el producto para que los valores guardados formen una sola versión.
        var p = DatosController.one(c, PRODUCT + " WHERE id=? FOR UPDATE", pid);
        var existing = DatosController.query(c, RECORD + " WHERE r.id=?", rid);
        if (!existing.isEmpty()) {
          var old = existing.getFirst();
          if (!uid().equals(old.get("usuarioID")))
            throw new ErrorAplicacion(409, "El identificador ya está utilizado");
          if (!pid.equals(old.get("productoID"))
              || !ValidacionController.text(b, "fechaDescongelacion", 10)
                  .equals(old.get("fechaDescongelacion"))
              || !ValidacionController.option(b, "tipoCaptura", "CAJAS", "NIVELES")
                  .equals(old.get("tipoCaptura"))
              || ValidacionController.integer(b, "cantidad", 1000000)
                  != ((Number) old.get("cantidadIngresada")).intValue())
            throw new ErrorAplicacion(409, "El id corresponde a otra operación");
          c.commit();
          return new ResultadoOperacion<>(ModeloMapper.registro(old), false);
        }
        var r = CalculadoraController.calcular(p, b);
        DatosController.update(
            c,
            "INSERT INTO"
                + " registros_etiquetado(id,usuario_id,producto_id,fecha_descongelacion,dias_aplicados,fecha_caducidad,tipo_captura,cantidad_ingresada,articulos_por_unidad_aplicados,total_etiquetas,item_aplicado,nombre_aplicado)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)",
            rid,
            uid(),
            pid,
            r.get("fechaDescongelacion"),
            r.get("diasAplicados"),
            r.get("fechaCaducidad"),
            r.get("tipoCaptura"),
            r.get("cantidadIngresada"),
            r.get("articulosPorUnidadAplicados"),
            r.get("totalEtiquetas"),
            r.get("item"),
            r.get("nombre"));
        var saved = DatosController.one(c, RECORD + " WHERE r.id=?", rid);
        c.commit();
        return created(ModeloMapper.registro(saved));
      } catch (SQLException | RuntimeException e) {
        c.rollback();
        throw e;
      }
    }
  }

  public List<RegistroEtiquetado> getRegistros(String from, String to, String q)
      throws SQLException {
    if (q.length() > 200) throw new ErrorAplicacion(400, "Búsqueda demasiado larga");
    String sql = RECORD + " WHERE (r.nombre_aplicado LIKE ? OR r.item_aplicado LIKE ?)";
    List<Object> a = new ArrayList<>(List.of("%" + q + "%", "%" + q + "%"));
    if (!"ADMINISTRADOR".equals(usuario.getRol())) {
      sql += " AND r.usuario_id=?";
      a.add(uid());
    }
    if (from != null) {
      sql += " AND r.fecha_registro>=?";
      a.add(LocalDate.parse(from).toString());
    }
    if (to != null) {
      sql += " AND r.fecha_registro<?";
      a.add(LocalDate.parse(to).plusDays(1).toString());
    }
    try (var c = DatosController.open()) {
      return ModeloMapper.registroLista(
          DatosController.query(c, sql + " ORDER BY r.fecha_registro DESC,r.id", a.toArray()));
    }
  }

  public RegistroEtiquetado getRegistro(String id) throws SQLException {
    try (var c = DatosController.open()) {
      var r = DatosController.one(c, RECORD + " WHERE r.id=?", ValidacionController.uuid(id));
      r.put(
          "revisiones",
          DatosController.query(
              c,
              REVIEW + " WHERE v.registro_etiquetado_id=? ORDER BY v.fecha_revision DESC,v.id",
              id));
      return ModeloMapper.registro(r);
    }
  }
}
