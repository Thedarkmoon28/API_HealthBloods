package org.lasalle.controller;

import java.sql.*;
import java.time.*;
import java.util.*;
import org.lasalle.model.*;

public class UsuarioController extends ControladorBase {
  public UsuarioController() {
    super();
  }

  public UsuarioController(Usuario usuario) {
    super(usuario);
  }

  public List<Usuario> getUsuarios() throws SQLException {
    admin();
    try (var c = DatosController.open()) {
      return ModeloMapper.usuarioLista(DatosController.query(c, USER + " ORDER BY nombre"));
    }
  }

  public ResultadoOperacion<Usuario> insertarUsuario(Map<String, Object> b) throws SQLException {
    admin();
    String name = ValidacionController.text(b, "nombre", 120),
        email = ValidacionController.email(b),
        role = ValidacionController.option(b, "rol", "ADMINISTRADOR", "EMPLEADO"),
        hash = SeguridadController.hash(ValidacionController.password(b));
    String id = id();
    try (var c = DatosController.open()) {
      DatosController.update(
          c,
          "INSERT INTO usuarios(id,nombre,correo,password_hash,rol) VALUES(?,?,?,?,?)",
          id,
          name,
          email,
          hash,
          role);
      return created(ModeloMapper.usuario(DatosController.one(c, USER + " WHERE id=?", id)));
    }
  }

  public Usuario actualizarUsuario(String id, Map<String, Object> b) throws SQLException {
    admin();
    ValidacionController.uuid(id);
    String name = ValidacionController.text(b, "nombre", 120),
        email = ValidacionController.email(b),
        role = ValidacionController.option(b, "rol", "ADMINISTRADOR", "EMPLEADO");
    boolean active = ValidacionController.bool(b, "activo");
    if (id.equals(uid()) && (!active || !role.equals("ADMINISTRADOR")))
      throw new ErrorAplicacion(
          409, "No puedes desactivar tu cuenta ni quitarte el rol administrador");
    try (var c = DatosController.open()) {
      DatosController.one(c, USER + " WHERE id=?", id);
      String extra = "";
      List<Object> a = new ArrayList<>(List.of(name, email, role, active));
      if (b.containsKey("password")) {
        extra = ",password_hash=?";
        a.add(SeguridadController.hash(ValidacionController.password(b)));
      }
      a.add(id);
      DatosController.update(
          c,
          "UPDATE usuarios SET nombre=?,correo=?,rol=?,activo=?,token_version=token_version+1"
              + extra
              + " WHERE id=?",
          a.toArray());
      return ModeloMapper.usuario(DatosController.one(c, USER + " WHERE id=?", id));
    }
  }
}
