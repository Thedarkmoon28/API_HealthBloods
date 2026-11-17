package org.lasalle.controller;

import java.sql.*;
import java.time.*;
import java.util.*;
import org.lasalle.model.*;

public class EstadoController extends ControladorBase {
  public EstadoController() {
    super();
  }

  public EstadoController(Usuario usuario) {
    super(usuario);
  }

  public Object salud() {
    return Map.of("aplicacion", "Posh Control", "estado", "ok");
  }

  public Object verificarBase() throws SQLException {
    try (var c = DatosController.open()) {
      for (String t :
          List.of(
              "usuarios", "productos", "registros_etiquetado", "favoritos", "revisiones_caducidad"))
        DatosController.query(c, "SELECT 1 FROM " + t + " LIMIT 1");
      return Map.of("estado", "ok", "baseDeDatos", "disponible");
    }
  }
}
