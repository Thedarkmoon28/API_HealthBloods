package org.lasalle.controller;

import java.util.*;
import org.lasalle.model.*;

public final class ValidacionController {
  private ValidacionController() {}

  public static String text(Map<String, Object> b, String key, int max) {
    Object v = b == null ? null : b.get(key);
    if (!(v instanceof String s) || s.isBlank() || s.trim().length() > max)
      throw new ErrorAplicacion(400, "Campo no válido: " + key);
    return s.trim();
  }

  public static int integer(Map<String, Object> b, String key, int max) {
    Object v = b == null ? null : b.get(key);
    if (!(v instanceof Number n)) throw new ErrorAplicacion(400, "Se requiere un entero: " + key);
    double d = n.doubleValue();
    if (!Double.isFinite(d) || d != Math.rint(d) || d < 1 || d > max)
      throw new ErrorAplicacion(400, "Cantidad fuera de rango: " + key);
    return (int) d;
  }

  public static boolean bool(Map<String, Object> b, String key) {
    Object v = b.get(key);
    if (!(v instanceof Boolean x))
      throw new ErrorAplicacion(400, "Se requiere un booleano: " + key);
    return x;
  }

  public static String option(Map<String, Object> b, String key, String... values) {
    String s = text(b, key, 30).toUpperCase(Locale.ROOT);
    if (!List.of(values).contains(s)) throw new ErrorAplicacion(400, "Opción no válida: " + key);
    return s;
  }

  public static String uuid(String value) {
    try {
      return UUID.fromString(value).toString();
    } catch (Exception e) {
      throw new ErrorAplicacion(400, "UUID no válido");
    }
  }

  public static String email(Map<String, Object> b) {
    String s = text(b, "correo", 254).toLowerCase(Locale.ROOT);
    if (!s.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
      throw new ErrorAplicacion(400, "Correo no válido");
    return s;
  }

  public static String password(Map<String, Object> b) {
    Object v = b == null ? null : b.get("password");
    if (!(v instanceof String s) || s.length() < 12 || s.length() > 128)
      throw new ErrorAplicacion(400, "La contraseña debe tener entre 12 y 128 caracteres");
    return s;
  }
}
