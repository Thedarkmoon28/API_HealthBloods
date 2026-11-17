package org.lasalle.controller;

import java.sql.*;
import java.time.*;
import java.util.*;
import org.lasalle.model.*;

public class SesionController extends ControladorBase {
  public SesionController() {
    super();
  }

  public SesionController(Usuario usuario) {
    super(usuario);
  }

  public Object iniciarSesion(Map<String, Object> b) throws SQLException {
    String correo = ValidacionController.email(b);
    String pass = ValidacionController.password(b);
    try (var c = DatosController.open()) {
      var rows =
          DatosController.query(
              c,
              "SELECT id,password_hash,activo,token_version FROM usuarios WHERE correo=?",
              correo);
      // El hash ficticio conserva un coste parecido para correos inexistentes.
      String hash = rows.isEmpty() ? Dummy.HASH : rows.getFirst().get("password_hash").toString();
      boolean valid = SeguridadController.verify(pass, hash);
      if (rows.isEmpty() || !valid || !DatosController.yes(rows.getFirst().get("activo")))
        throw new ErrorAplicacion(401, "Correo o contraseña incorrectos");
      var u = rows.getFirst();
      String token =
          SeguridadController.token(
              u.get("id").toString(), ((Number) u.get("token_version")).intValue());
      return Map.of(
          "token",
          token,
          "tokenType",
          "Bearer",
          "expiresIn",
          SeguridadController.SESSION_SECONDS,
          "usuario",
          ModeloMapper.usuario(DatosController.one(c, USER + " WHERE id=?", u.get("id"))));
    }
  }

  public Usuario getPerfil() throws SQLException {
    try (var c = DatosController.open()) {
      return ModeloMapper.usuario(DatosController.one(c, USER + " WHERE id=?", uid()));
    }
  }

  public Object cerrarSesion() throws SQLException {
    try (var c = DatosController.open()) {
      DatosController.update(
          c, "UPDATE usuarios SET token_version=token_version+1 WHERE id=?", uid());
      return Map.of("mensaje", "Sesiones cerradas");
    }
  }

  public Object cambiarPassword(Map<String, Object> b) throws SQLException {
    String actual = ValidacionController.text(b, "passwordActual", 128),
        nuevo = ValidacionController.password(b);
    try (var c = DatosController.open()) {
      String hash =
          DatosController.one(c, "SELECT password_hash FROM usuarios WHERE id=?", uid())
              .get("password_hash")
              .toString();
      if (!SeguridadController.verify(actual, hash))
        throw new ErrorAplicacion(401, "Contraseña actual incorrecta");
      DatosController.update(
          c,
          "UPDATE usuarios SET password_hash=?,token_version=token_version+1 WHERE id=?",
          SeguridadController.hash(nuevo),
          uid());
      return Map.of("mensaje", "Contraseña actualizada. Inicia sesión nuevamente.");
    }
  }

  private static final class Dummy {
    static final String HASH = SeguridadController.hash(UUID.randomUUID().toString());
  }

  public Usuario verificarToken(String token) throws SQLException {
    String[] claims = SeguridadController.claims(token);
    try (var c = DatosController.open()) {
      var filas =
          DatosController.query(
              c,
              "SELECT id,nombre,correo,rol,activo,token_version FROM usuarios WHERE id=?",
              claims[0]);
      if (filas.isEmpty()) throw new ErrorAplicacion(401, "Sesión no válida");
      var fila = filas.getFirst();
      if (!DatosController.yes(fila.get("activo"))
          || ((Number) fila.get("token_version")).intValue() != Integer.parseInt(claims[1]))
        throw new ErrorAplicacion(401, "Sesión no válida");
      return ModeloMapper.usuario(fila);
    }
  }
}
