package org.lasalle.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.sql.SQLException;
import java.util.*;
import org.lasalle.controller.*;
import org.lasalle.model.*;

@Path("posh/caducidades")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestCaducidades extends RestBase {

  @GET
  public Object getCaducidades(
      @QueryParam("estado") @DefaultValue("ACTIVOS") String estado,
      @QueryParam("fecha") String fecha)
      throws SQLException {
    return new RevisionCaducidadController(usuario()).getCaducidades(estado, fecha);
  }
}
