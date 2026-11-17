package org.lasalle.rest;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import org.lasalle.model.*;

public abstract class RestBase {
  @Context protected ContainerRequestContext request;

  protected Usuario usuario() {
    return (Usuario) request.getProperty("usuario");
  }

  protected Response resultado(ResultadoOperacion<?> operacion) {
    return Response.status(operacion.creado() ? 201 : 200).entity(operacion.datos()).build();
  }
}
