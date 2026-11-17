package org.lasalle.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.sql.SQLException;
import java.util.*;
import org.lasalle.controller.*;
import org.lasalle.model.*;

@Path("posh/registros")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestRegistros extends RestBase {

  @GET
  public List<RegistroEtiquetado> getRegistros(
      @QueryParam("desde") String desde,
      @QueryParam("hasta") String hasta,
      @QueryParam("q") @DefaultValue("") String q)
      throws SQLException {
    return new RegistroEtiquetadoController(usuario()).getRegistros(desde, hasta, q);
  }

  @GET
  @Path("{id}")
  public RegistroEtiquetado getRegistro(@PathParam("id") String id) throws SQLException {
    return new RegistroEtiquetadoController(usuario()).getRegistro(id);
  }

  @POST
  public Response insertarRegistro(Map<String, Object> datos) throws SQLException {
    return resultado(new RegistroEtiquetadoController(usuario()).insertarRegistro(datos));
  }

  @POST
  @Path("{id}/revisiones")
  public Response insertarRevision(@PathParam("id") String id, Map<String, Object> datos)
      throws SQLException {
    return resultado(new RevisionCaducidadController(usuario()).insertarRevision(id, datos));
  }
}
