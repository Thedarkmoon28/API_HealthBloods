package org.lasalle.controller;

import java.sql.*;
import java.time.*;
import java.util.*;
import org.lasalle.model.*;

public class FavoritoController extends ControladorBase {
  public FavoritoController() {
    super();
  }

  public FavoritoController(Usuario usuario) {
    super(usuario);
  }

  public List<Favorito> getFavoritos() throws SQLException {
    try (var c = DatosController.open()) {
      return ModeloMapper.favoritoLista(
          DatosController.query(
              c,
              "SELECT f.id,f.producto_id AS productoID,f.fecha_agregado AS"
                  + " fechaAgregado,p.item,p.nombre,p.marca,p.dias_tras_descongelar AS"
                  + " diasTrasDescongelar,p.activo FROM favoritos f JOIN productos p ON"
                  + " p.id=f.producto_id WHERE f.usuario_id=? ORDER BY p.nombre",
              uid()));
    }
  }

  public Favorito agregarFavorito(String pid) throws SQLException {
    try (var c = DatosController.open()) {
      DatosController.one(c, PRODUCT + " WHERE id=?", ValidacionController.uuid(pid));
      DatosController.update(
          c,
          "INSERT INTO favoritos(id,usuario_id,producto_id) VALUES(?,?,?) ON DUPLICATE KEY UPDATE"
              + " producto_id=VALUES(producto_id)",
          id(),
          uid(),
          pid);
      return ModeloMapper.favorito(
          DatosController.one(
              c,
              "SELECT id,producto_id AS productoID,fecha_agregado AS fechaAgregado FROM favoritos"
                  + " WHERE usuario_id=? AND producto_id=?",
              uid(),
              pid));
    }
  }

  public void eliminarFavorito(String pid) throws SQLException {
    try (var c = DatosController.open()) {
      DatosController.update(
          c,
          "DELETE FROM favoritos WHERE usuario_id=? AND producto_id=?",
          uid(),
          ValidacionController.uuid(pid));
      return;
    }
  }
}
