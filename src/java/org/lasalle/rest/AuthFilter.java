package org.lasalle.rest;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.ext.Provider;
import org.lasalle.controller.SesionController;
import org.lasalle.model.ErrorAplicacion;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {
  public void filter(ContainerRequestContext request) {
    String path = request.getUriInfo().getPath();
    if ((request.getMethod().equals("GET")
            && (path.equals("posh/salud") || path.equals("posh/salud/db")))
        || (request.getMethod().equals("POST") && path.equals("posh/auth/login"))) return;
    String header = request.getHeaderString("Authorization");
    if (header == null || !header.startsWith("Bearer "))
      throw new ErrorAplicacion(401, "Inicia sesión para continuar");
    try {
      request.setProperty("usuario", new SesionController().verificarToken(header.substring(7)));
    } catch (java.sql.SQLException e) {
      throw new IllegalStateException("No se pudo verificar la sesión", e);
    }
  }
}
