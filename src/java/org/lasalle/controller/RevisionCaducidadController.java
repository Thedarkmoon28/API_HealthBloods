package org.lasalle.controller;

import java.sql.*;
import java.time.*;
import java.util.*;
import org.lasalle.model.*;

public class RevisionCaducidadController extends ControladorBase {
  public RevisionCaducidadController() {
    super();
  }

  public RevisionCaducidadController(Usuario usuario) {
    super(usuario);
  }

  public Object getCaducidades(String state, String date) throws SQLException {
    if (!List.of("ACTIVOS", "PROXIMOS", "VENCIDOS", "CERRADOS").contains(state))
      throw new ErrorAplicacion(400, "Estado no válido");
    LocalDate reference = date == null ? today() : LocalDate.parse(date);
    try (var c = DatosController.open()) {
      var rows = DatosController.query(c, RECORD + " ORDER BY r.fecha_caducidad,r.id");
      List<Map<String, Object>> out = new ArrayList<>();
      for (var r : rows) {
        var reviews =
            DatosController.query(
                c,
                REVIEW
                    + " WHERE v.registro_etiquetado_id=? ORDER BY v.fecha_revision DESC,v.id LIMIT"
                    + " 1",
                r.get("id"));
        String latest =
            reviews.isEmpty() ? "SIN_REVISION" : reviews.getFirst().get("resultado").toString();
        boolean closed = latest.equals("RETIRADO") || latest.equals("AGOTADO");
        long days =
            java.time.temporal.ChronoUnit.DAYS.between(
                reference, LocalDate.parse(r.get("fechaCaducidad").toString()));
        String temporal = days < 0 ? "VENCIDO" : days <= 3 ? "PROXIMO" : "VIGENTE";
        r.put("diasRestantes", days);
        r.put("estadoCaducidad", temporal);
        r.put("ultimaRevision", latest);
        r.put("cerrado", closed);
        if (state.equals("CERRADOS")
            ? closed
            : !closed
                && (state.equals("ACTIVOS")
                    || state.equals("PROXIMOS") && days >= 0 && days <= 3
                    || state.equals("VENCIDOS") && days < 0)) out.add(r);
      }
      return Map.of(
          "fechaReferencia", reference.toString(), "registros", ModeloMapper.registroLista(out));
    }
  }

  public ResultadoOperacion<RevisionCaducidad> insertarRevision(String rid, Map<String, Object> b)
      throws SQLException {
    ValidacionController.uuid(rid);
    String result = ValidacionController.option(b, "resultado", "VIGENTE", "RETIRADO", "AGOTADO"),
        notes = null;
    if (b != null && b.get("observaciones") != null) {
      Object n = b.get("observaciones");
      if (!(n instanceof String s) || s.length() > 1000)
        throw new ErrorAplicacion(400, "Observaciones no válidas");
      notes = s.trim();
    }
    try (var c = DatosController.open()) {
      c.setAutoCommit(false);
      try {
        var r =
            DatosController.one(
                c,
                "SELECT fecha_caducidad AS fechaCaducidad FROM registros_etiquetado WHERE id=? FOR"
                    + " UPDATE",
                rid);
        var last =
            DatosController.query(
                c,
                "SELECT resultado FROM revisiones_caducidad WHERE registro_etiquetado_id=? ORDER BY"
                    + " fecha_revision DESC,id LIMIT 1",
                rid);
        if (!last.isEmpty() && !last.getFirst().get("resultado").equals("VIGENTE"))
          throw new ErrorAplicacion(409, "El registro ya está cerrado");
        if (result.equals("VIGENTE")
            && LocalDate.parse(r.get("fechaCaducidad").toString()).isBefore(today()))
          throw new ErrorAplicacion(400, "Un producto vencido no puede marcarse vigente");
        String id = id();
        DatosController.update(
            c,
            "INSERT INTO"
                + " revisiones_caducidad(id,registro_etiquetado_id,usuario_id,resultado,observaciones)"
                + " VALUES(?,?,?,?,?)",
            id,
            rid,
            uid(),
            result,
            notes);
        var v = DatosController.one(c, REVIEW + " WHERE v.id=?", id);
        c.commit();
        return created(ModeloMapper.revision(v));
      } catch (SQLException | RuntimeException e) {
        c.rollback();
        throw e;
      }
    }
  }
}
