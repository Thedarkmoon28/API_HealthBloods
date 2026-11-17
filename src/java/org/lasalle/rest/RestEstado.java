package org.lasalle.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.sql.SQLException;
import java.util.*;
import org.lasalle.controller.*;
import org.lasalle.model.*;

@Path("posh")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestEstado extends RestBase {

  @GET
  @Path("salud")
  public Object salud() throws SQLException {
    return new EstadoController().salud();
  }

  @GET
  @Path("salud/db")
  public Object verificarBase() throws SQLException {
    return new EstadoController().verificarBase();
  }
}
