package org.lasalle.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.sql.SQLException;
import java.util.*;
import org.lasalle.controller.*;
import org.lasalle.model.*;

@Path("posh/productos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestProductos extends RestBase {

  @GET
  public List<Producto> getProductos(
      @QueryParam("q") @DefaultValue("") String q,
      @QueryParam("incluirInactivos") @DefaultValue("false") boolean incluirInactivos)
      throws SQLException {
    return new ProductoController(usuario()).getProductos(q, incluirInactivos);
  }

  @GET
  @Path("{id}")
  public Producto getProducto(@PathParam("id") String id) throws SQLException {
    return new ProductoController(usuario()).getProducto(id);
  }

  @POST
  public Response insertarProducto(Map<String, Object> datos) throws SQLException {
    return resultado(new ProductoController(usuario()).insertarProducto(datos));
  }

  @PUT
  @Path("{id}")
  public Producto actualizarProducto(@PathParam("id") String id, Map<String, Object> datos)
      throws SQLException {
    return new ProductoController(usuario()).actualizarProducto(id, datos);
  }

  @DELETE
  @Path("{id}")
  public Object desactivarProducto(@PathParam("id") String id) throws SQLException {
    return new ProductoController(usuario()).desactivarProducto(id);
  }
}
