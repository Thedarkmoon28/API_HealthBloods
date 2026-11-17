package org.lasalle.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.sql.SQLException;
import java.util.*;
import org.lasalle.controller.*;
import org.lasalle.model.*;

@Path("posh/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestAuth extends RestBase {

  @POST
  @Path("login")
  public Object login(Map<String, Object> datos) throws SQLException {
    return new SesionController().iniciarSesion(datos);
  }

  @GET
  @Path("me")
  public Usuario perfil() throws SQLException {
    return new SesionController(usuario()).getPerfil();
  }

  @POST
  @Path("logout")
  public Object logout() throws SQLException {
    return new SesionController(usuario()).cerrarSesion();
  }

  @PUT
  @Path("password")
  public Object password(Map<String, Object> datos) throws SQLException {
    return new SesionController(usuario()).cambiarPassword(datos);
  }
}
