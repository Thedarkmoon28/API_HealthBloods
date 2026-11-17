package org.lasalle.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.sql.SQLException;
import java.util.*;
import org.lasalle.controller.*;
import org.lasalle.model.*;

@Path("posh/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestUsuarios extends RestBase {

  @GET
  public List<Usuario> getUsuarios() throws SQLException {
    return new UsuarioController(usuario()).getUsuarios();
  }

  @POST
  public Response insertarUsuario(Map<String, Object> datos) throws SQLException {
    return resultado(new UsuarioController(usuario()).insertarUsuario(datos));
  }

  @PUT
  @Path("{id}")
  public Usuario actualizarUsuario(@PathParam("id") String id, Map<String, Object> datos)
      throws SQLException {
    return new UsuarioController(usuario()).actualizarUsuario(id, datos);
  }
}
