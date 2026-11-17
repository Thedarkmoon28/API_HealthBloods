package org.lasalle.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.*;
import java.sql.*;
import java.util.*;
import org.lasalle.model.ErrorAplicacion;

@Provider
public class Errors implements ExceptionMapper<Exception> {
  public Response toResponse(Exception e) {
    int status = 500;
    String message = "Error interno. Revisa la configuración y los registros del servidor.";
    if (e instanceof ErrorAplicacion a) {
      status = a.status;
      message = a.getMessage();
    } else if (e instanceof WebApplicationException w) {
      status = w.getResponse().getStatus();
      message = "Solicitud HTTP no válida";
    } else if (e instanceof IllegalArgumentException || e instanceof java.time.DateTimeException) {
      status = 400;
      message = "Datos o formato de fecha no válidos";
    } else if (e instanceof SQLException s && "23000".equals(s.getSQLState())) {
      status = 409;
      message = "Dato duplicado o relación no válida";
    }
    if (status == 500)
      java.util.logging.Logger.getLogger("PoshControl")
          .log(java.util.logging.Level.SEVERE, "Error de API", e);
    return Response.status(status)
        .type(MediaType.APPLICATION_JSON)
        .entity(Map.of("error", message))
        .build();
  }
}
