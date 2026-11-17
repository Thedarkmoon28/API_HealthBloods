package org.lasalle.controller;

import java.util.*;
import org.lasalle.model.*;

public final class AdminController {
  public static void main(String[] args) throws Exception {
    String name = DatosController.env("ADMIN_NAME"),
        email = DatosController.env("ADMIN_EMAIL"),
        password = DatosController.env("ADMIN_PASSWORD");
    Map<String, Object> b = Map.of("nombre", name, "correo", email, "password", password);
    name = ValidacionController.text(b, "nombre", 120);
    email = ValidacionController.email(b);
    password = ValidacionController.password(b);
    try (var c = DatosController.open()) {
      DatosController.update(
          c,
          "INSERT INTO usuarios(id,nombre,correo,password_hash,rol)"
              + " VALUES(?,?,?,?,'ADMINISTRADOR')",
          UUID.randomUUID().toString(),
          name,
          email,
          SeguridadController.hash(password));
      System.out.println("Administrador creado. No se imprimió la contraseña.");
    }
  }
}
