package org.lasalle.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.sql.SQLException;
import java.util.*;
import org.lasalle.controller.*;
import org.lasalle.model.*;

@Path("posh/calculos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestCalculos extends RestBase {

  @POST
  public CalculoEtiquetas calcular(Map<String, Object> datos) throws SQLException {
    return new RegistroEtiquetadoController(usuario()).calcular(datos);
  }
}
