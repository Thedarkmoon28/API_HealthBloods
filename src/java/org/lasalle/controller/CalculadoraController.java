package org.lasalle.controller;

import java.time.*;
import java.util.*;
import org.lasalle.model.*;

public final class CalculadoraController {
  private CalculadoraController() {}

  public static Map<String, Object> calcular(Map<String, Object> p, Map<String, Object> b) {
    if (!DatosController.yes(p.get("activo")))
      throw new ErrorAplicacion(409, "El producto está inactivo");
    String tipo = ValidacionController.option(b, "tipoCaptura", "CAJAS", "NIVELES");
    int cantidad = ValidacionController.integer(b, "cantidad", 1000000);
    String key = tipo.equals("CAJAS") ? "articulosPorCaja" : "articulosPorNivel";
    if (!(p.get(key) instanceof Number n) || n.intValue() <= 0)
      throw new ErrorAplicacion(409, "Completa los datos del producto");
    LocalDate fecha = LocalDate.parse(ValidacionController.text(b, "fechaDescongelacion", 10));
    if (fecha.getYear() < 2000 || fecha.getYear() > 2100)
      throw new ErrorAplicacion(400, "Fecha fuera de rango (2000-2100)");
    int dias = ((Number) p.get("diasTrasDescongelar")).intValue();
    Map<String, Object> r = new LinkedHashMap<>();
    r.put("productoID", p.get("id"));
    r.put("item", p.get("item"));
    r.put("nombre", p.get("nombre"));
    r.put("fechaDescongelacion", fecha.toString());
    r.put("diasAplicados", dias);
    r.put("fechaCaducidad", fecha.plusDays(dias).toString());
    r.put("tipoCaptura", tipo);
    r.put("cantidadIngresada", cantidad);
    r.put("articulosPorUnidadAplicados", n.intValue());
    r.put("totalEtiquetas", Math.multiplyExact((long) cantidad, n.longValue()));
    return r;
  }
}
