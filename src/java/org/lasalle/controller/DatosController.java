package org.lasalle.controller;

import java.sql.*;
import java.util.*;
import org.lasalle.model.*;

public final class DatosController {
  private DatosController() {}

  public static Connection open() throws SQLException {
    Connection c = new org.lasalle.connection.ConeccionMysql().open();
    try (Statement s = c.createStatement()) {
      s.execute("SET time_zone = '+00:00'");
    } catch (SQLException e) {
      c.close();
      throw e;
    }
    return c;
  }

  public static String env(String k) {
    String v = System.getenv(k);
    if (v == null || v.isBlank()) throw new IllegalStateException("Falta configurar " + k);
    return v;
  }

  public static List<Map<String, Object>> query(Connection c, String sql, Object... args)
      throws SQLException {
    try (PreparedStatement s = c.prepareStatement(sql)) {
      bind(s, args);
      try (ResultSet rs = s.executeQuery()) {
        List<Map<String, Object>> rows = new ArrayList<>();
        ResultSetMetaData md = rs.getMetaData();
        while (rs.next()) {
          Map<String, Object> row = new LinkedHashMap<>();
          for (int i = 1; i <= md.getColumnCount(); i++) {
            Object v = rs.getObject(i);
            if (v instanceof java.sql.Date d) v = d.toLocalDate().toString();
            else if (v instanceof Timestamp t) v = t.toInstant().toString();
            row.put(md.getColumnLabel(i), v);
          }
          rows.add(row);
        }
        return rows;
      }
    }
  }

  public static Map<String, Object> one(Connection c, String sql, Object... args)
      throws SQLException {
    var rows = query(c, sql, args);
    if (rows.isEmpty()) throw new ErrorAplicacion(404, "Registro no encontrado");
    return rows.getFirst();
  }

  public static int update(Connection c, String sql, Object... args) throws SQLException {
    try (PreparedStatement s = c.prepareStatement(sql)) {
      bind(s, args);
      return s.executeUpdate();
    }
  }

  private static void bind(PreparedStatement s, Object[] args) throws SQLException {
    for (int i = 0; i < args.length; i++) s.setObject(i + 1, args[i]);
  }

  public static boolean yes(Object v) {
    return Boolean.TRUE.equals(v) || (v instanceof Number n && n.intValue() != 0);
  }
}
