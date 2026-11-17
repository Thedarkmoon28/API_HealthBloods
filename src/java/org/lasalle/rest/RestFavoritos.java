package org.lasalle.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.sql.SQLException;
import java.util.*;
import org.lasalle.controller.*;
import org.lasalle.model.*;

@Path("posh/favoritos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestFavoritos extends RestBase {

  @GET
  public List<Favorito> getFavoritos() throws SQLException {
    return new FavoritoController(usuario()).getFavoritos();
  }

  @PUT
  @Path("{productoID}")
  public Favorito agregarFavorito(@PathParam("productoID") String id) throws SQLException {
    return new FavoritoController(usuario()).agregarFavorito(id);
  }

  @DELETE
  @Path("{productoID}")
  public Response eliminarFavorito(@PathParam("productoID") String id) throws SQLException {
    new FavoritoController(usuario()).eliminarFavorito(id);
    return Response.noContent().build();
  }
}
