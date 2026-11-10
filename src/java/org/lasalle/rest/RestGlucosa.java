package org.lasalle.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.lasalle.controller.GlucosaController;
import org.lasalle.controller.UsuarioController;
import org.lasalle.model.Medida;
import org.lasalle.model.Usuario;

@Path("glucosa")
public class RestGlucosa {

    private static final String EMAIL_REGEX
            = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    @Path("saludar")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response saludar() {
        String saludo = """
                        {"saludo":"hola desde java"}
                        """;
        return Response.ok(saludo).build();
    }

    @Path("SaludarCumpleanios")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response saludarCumpleanios(
            @QueryParam("nombre") String nombre) {
        String out = """
                     {"response":"Feliz cumple %s"}
                     """;
        out = String.format(out, nombre);
        return Response.ok(out).build();
    }

    @Path("getAllUsuarios")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsuarios() {
        UsuarioController uc = new UsuarioController();
        List<Usuario> lista = new ArrayList<>();
        Gson gson = new Gson();
        String out = "";

        try {
            lista = uc.getUsuarios();
            out = gson.toJson(lista);
        } catch (Exception e) {
            e.printStackTrace();
            out = """
                  {"result":"Empty table"}
                  """;
        }

        return Response.ok(out).build();
    }

    @Path("getAllMedida")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getALLMedida() {
        GlucosaController mc = new GlucosaController();
        List<Medida> lista = new ArrayList<>();
        Gson gson = new Gson();
        String out = "";
        try {
            lista = mc.getMedida();
            out = gson.toJson(lista);
        } catch (Exception e) {
            e.printStackTrace();
            out = """
                 {"result":"Empty table"}
                 """;
        }
        return Response.ok(out).build();
    }

    @Path("registrarUsuario")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrarUsuario(
            @FormParam("nombre") String nombre,
            @FormParam("primerApellido") String primerApellido,
            @FormParam("segundoApellido") String segundoApellido,
            @FormParam("fechaNacimiento") String fechaNacimiento,
            @FormParam("tipoDiabetes") String tipoDiabetes,
            @FormParam("email") String email) {

        if (nombre == null || nombre.isBlank()) {
            return jsonError(Response.Status.BAD_REQUEST,
                    "El campo nombre es obligatorio");
        }

        if (primerApellido == null || primerApellido.isBlank()) {
            return jsonError(Response.Status.BAD_REQUEST,
                    "El campo primerApellido es obligatorio");
        }

        if (email == null || email.isBlank()) {
            return jsonError(Response.Status.BAD_REQUEST,
                    "El campo email es obligatorio");
        }

        if (!email.trim().matches(EMAIL_REGEX)) {
            return jsonError(Response.Status.BAD_REQUEST,
                    "El formato del email no es valido");
        }

        java.sql.Date fechaNacimientoSql = null;
        if (fechaNacimiento != null && !fechaNacimiento.isBlank()) {
            try {
                fechaNacimientoSql = java.sql.Date.valueOf(
                        LocalDate.parse(fechaNacimiento.trim()));
            } catch (DateTimeParseException e) {
                return jsonError(Response.Status.BAD_REQUEST,
                        "fechaNacimiento debe tener el formato yyyy-MM-dd");
            }
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre.trim());
        usuario.setPrimerApellido(primerApellido.trim());
        usuario.setSegundoApellido(blankToNull(segundoApellido));
        usuario.setFechaNacimiento(fechaNacimientoSql);
        usuario.setTipoDiabetes(blankToNull(tipoDiabetes));
        usuario.setEmail(email.trim());

        try {
            UsuarioController uc = new UsuarioController();
            int idUsuario = uc.insertarUsuario(usuario);

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("result", "ok");
            body.put("idUsuario", idUsuario);
            body.put("mensaje", "Usuario registrado correctamente");

            return Response.status(Response.Status.CREATED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(new Gson().toJson(body))
                    .build();
        } catch (SQLIntegrityConstraintViolationException e) {
            return jsonError(Response.Status.CONFLICT,
                    "El email ya esta registrado");
        } catch (SQLException e) {
            e.printStackTrace();
            return jsonError(Response.Status.INTERNAL_SERVER_ERROR,
                    "Error al registrar el usuario");
        }
    }

    @Path("registrarMedida")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrarMedida(
            @FormParam("idUsuario") String idUsuario,
            @FormParam("glucosa") String glucosa,
            @FormParam("contexto") String contexto,
            @FormParam("notas") String notas,
            @FormParam("fechaRegistro") String fechaRegistro) {

        if (idUsuario == null || idUsuario.isBlank()) {
            return jsonError(Response.Status.BAD_REQUEST,
                    "El campo idUsuario es obligatorio");
        }

        if (glucosa == null || glucosa.isBlank()) {
            return jsonError(Response.Status.BAD_REQUEST,
                    "El campo glucosa es obligatorio");
        }

        int idUsuarioInt;
        int glucosaInt;

        try {
            idUsuarioInt = Integer.parseInt(idUsuario.trim());
        } catch (NumberFormatException e) {
            return jsonError(Response.Status.BAD_REQUEST,
                    "idUsuario debe ser un numero entero");
        }

        try {
            glucosaInt = Integer.parseInt(glucosa.trim());
        } catch (NumberFormatException e) {
            return jsonError(Response.Status.BAD_REQUEST,
                    "glucosa debe ser un numero entero");
        }

        if (idUsuarioInt <= 0) {
            return jsonError(Response.Status.BAD_REQUEST,
                    "idUsuario debe ser mayor que 0");
        }

        if (glucosaInt < 1 || glucosaInt > 65535) {
            return jsonError(Response.Status.BAD_REQUEST,
                    "glucosa debe estar entre 1 y 65535 mg/dL");
        }

        java.sql.Date fechaSql = java.sql.Date.valueOf(LocalDate.now());
        if (fechaRegistro != null && !fechaRegistro.isBlank()) {
            try {
                fechaSql = java.sql.Date.valueOf(
                        LocalDate.parse(fechaRegistro.trim()));
            } catch (DateTimeParseException e) {
                return jsonError(Response.Status.BAD_REQUEST,
                        "fechaRegistro debe tener el formato yyyy-MM-dd");
            }
        }

        Medida medida = new Medida();
        medida.setIdUsuario(idUsuarioInt);
        medida.setGlucosa(glucosaInt);
        medida.setContexto(contexto == null || contexto.isBlank()
                ? "NO ESPECIFICADO" : contexto.trim());
        medida.setNotas(blankToNull(notas));
        medida.setFechaHora(fechaSql);

        try {
            GlucosaController gc = new GlucosaController();
            int idMedida = gc.insertarMedida(medida);

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("result", "ok");
            body.put("idMedida", idMedida);
            body.put("mensaje", "Medida registrada correctamente");

            return Response.status(Response.Status.CREATED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(new Gson().toJson(body))
                    .build();
        } catch (SQLIntegrityConstraintViolationException e) {
            return jsonError(Response.Status.BAD_REQUEST,
                    "El idUsuario no existe");
        } catch (SQLException e) {
            e.printStackTrace();
            return jsonError(Response.Status.INTERNAL_SERVER_ERROR,
                    "Error al registrar la medida");
        }
    }

    private Response jsonError(Response.Status status, String mensaje) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("result", "error");
        body.put("mensaje", mensaje);

        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(new Gson().toJson(body))
                .build();
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
