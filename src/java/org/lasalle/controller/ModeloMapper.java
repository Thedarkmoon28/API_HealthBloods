package org.lasalle.controller;

import java.util.*;
import org.lasalle.model.*;

/** Convierte las columnas de consultas preparadas a los modelos del proyecto. */
public final class ModeloMapper {
  private ModeloMapper() {}

  private static String texto(Object v) {
    return v == null ? null : v.toString();
  }

  private static int entero(Object v) {
    return v == null ? 0 : ((Number) v).intValue();
  }

  private static Integer enteroOpcional(Object v) {
    return v == null ? null : ((Number) v).intValue();
  }

  private static long largo(Object v) {
    return v == null ? 0 : ((Number) v).longValue();
  }

  private static Long largoOpcional(Object v) {
    return v == null ? null : ((Number) v).longValue();
  }

  private static Boolean booleanoOpcional(Object v) {
    return v == null ? null : DatosController.yes(v);
  }

  public static Usuario usuario(Map<String, Object> fila) {
    Usuario modelo = new Usuario();
    modelo.setId(texto(fila.get("id")));
    modelo.setNombre(texto(fila.get("nombre")));
    modelo.setCorreo(texto(fila.get("correo")));
    modelo.setRol(texto(fila.get("rol")));
    modelo.setActivo(DatosController.yes(fila.get("activo")));
    modelo.setFechaCreacion(texto(fila.get("fechaCreacion")));
    modelo.setFechaActualizacion(texto(fila.get("fechaActualizacion")));
    return modelo;
  }

  public static List<Usuario> usuarioLista(List<Map<String, Object>> filas) {
    List<Usuario> lista = new ArrayList<>();
    for (Map<String, Object> fila : filas) lista.add(usuario(fila));
    return lista;
  }

  public static Producto producto(Map<String, Object> fila) {
    Producto modelo = new Producto();
    modelo.setId(texto(fila.get("id")));
    modelo.setItem(texto(fila.get("item")));
    modelo.setNombre(texto(fila.get("nombre")));
    modelo.setMarca(texto(fila.get("marca")));
    modelo.setDiasTrasDescongelar(entero(fila.get("diasTrasDescongelar")));
    modelo.setArticulosPorCaja(enteroOpcional(fila.get("articulosPorCaja")));
    modelo.setArticulosPorNivel(enteroOpcional(fila.get("articulosPorNivel")));
    modelo.setActivo(DatosController.yes(fila.get("activo")));
    modelo.setFechaActualizacion(texto(fila.get("fechaActualizacion")));
    return modelo;
  }

  public static List<Producto> productoLista(List<Map<String, Object>> filas) {
    List<Producto> lista = new ArrayList<>();
    for (Map<String, Object> fila : filas) lista.add(producto(fila));
    return lista;
  }

  public static RegistroEtiquetado registro(Map<String, Object> fila) {
    RegistroEtiquetado modelo = new RegistroEtiquetado();
    modelo.setId(texto(fila.get("id")));
    modelo.setUsuarioID(texto(fila.get("usuarioID")));
    modelo.setUsuarioNombre(texto(fila.get("usuarioNombre")));
    modelo.setProductoID(texto(fila.get("productoID")));
    modelo.setItem(texto(fila.get("item")));
    modelo.setNombre(texto(fila.get("nombre")));
    modelo.setFechaRegistro(texto(fila.get("fechaRegistro")));
    modelo.setFechaDescongelacion(texto(fila.get("fechaDescongelacion")));
    modelo.setDiasAplicados(entero(fila.get("diasAplicados")));
    modelo.setFechaCaducidad(texto(fila.get("fechaCaducidad")));
    modelo.setTipoCaptura(texto(fila.get("tipoCaptura")));
    modelo.setCantidadIngresada(entero(fila.get("cantidadIngresada")));
    modelo.setArticulosPorUnidadAplicados(entero(fila.get("articulosPorUnidadAplicados")));
    modelo.setTotalEtiquetas(largo(fila.get("totalEtiquetas")));
    if (fila.get("revisiones") instanceof List<?> filas) {
      List<RevisionCaducidad> revisiones = new ArrayList<>();
      for (Object valor : filas) {
        @SuppressWarnings("unchecked")
        Map<String, Object> datos = (Map<String, Object>) valor;
        revisiones.add(revision(datos));
      }
      modelo.setRevisiones(revisiones);
    }
    modelo.setDiasRestantes(largoOpcional(fila.get("diasRestantes")));
    modelo.setEstadoCaducidad(texto(fila.get("estadoCaducidad")));
    modelo.setUltimaRevision(texto(fila.get("ultimaRevision")));
    modelo.setCerrado(booleanoOpcional(fila.get("cerrado")));
    return modelo;
  }

  public static List<RegistroEtiquetado> registroLista(List<Map<String, Object>> filas) {
    List<RegistroEtiquetado> lista = new ArrayList<>();
    for (Map<String, Object> fila : filas) lista.add(registro(fila));
    return lista;
  }

  public static Favorito favorito(Map<String, Object> fila) {
    Favorito modelo = new Favorito();
    modelo.setId(texto(fila.get("id")));
    modelo.setUsuarioID(texto(fila.get("usuarioID")));
    modelo.setProductoID(texto(fila.get("productoID")));
    modelo.setFechaAgregado(texto(fila.get("fechaAgregado")));
    modelo.setItem(texto(fila.get("item")));
    modelo.setNombre(texto(fila.get("nombre")));
    modelo.setMarca(texto(fila.get("marca")));
    modelo.setDiasTrasDescongelar(enteroOpcional(fila.get("diasTrasDescongelar")));
    modelo.setActivo(booleanoOpcional(fila.get("activo")));
    return modelo;
  }

  public static List<Favorito> favoritoLista(List<Map<String, Object>> filas) {
    List<Favorito> lista = new ArrayList<>();
    for (Map<String, Object> fila : filas) lista.add(favorito(fila));
    return lista;
  }

  public static RevisionCaducidad revision(Map<String, Object> fila) {
    RevisionCaducidad modelo = new RevisionCaducidad();
    modelo.setId(texto(fila.get("id")));
    modelo.setRegistroEtiquetadoID(texto(fila.get("registroEtiquetadoID")));
    modelo.setUsuarioID(texto(fila.get("usuarioID")));
    modelo.setUsuarioNombre(texto(fila.get("usuarioNombre")));
    modelo.setFechaRevision(texto(fila.get("fechaRevision")));
    modelo.setResultado(texto(fila.get("resultado")));
    modelo.setObservaciones(texto(fila.get("observaciones")));
    return modelo;
  }

  public static List<RevisionCaducidad> revisionLista(List<Map<String, Object>> filas) {
    List<RevisionCaducidad> lista = new ArrayList<>();
    for (Map<String, Object> fila : filas) lista.add(revision(fila));
    return lista;
  }

  public static CalculoEtiquetas calculo(Map<String, Object> fila) {
    CalculoEtiquetas modelo = new CalculoEtiquetas();
    modelo.setProductoID(texto(fila.get("productoID")));
    modelo.setItem(texto(fila.get("item")));
    modelo.setNombre(texto(fila.get("nombre")));
    modelo.setFechaDescongelacion(texto(fila.get("fechaDescongelacion")));
    modelo.setDiasAplicados(entero(fila.get("diasAplicados")));
    modelo.setFechaCaducidad(texto(fila.get("fechaCaducidad")));
    modelo.setTipoCaptura(texto(fila.get("tipoCaptura")));
    modelo.setCantidadIngresada(entero(fila.get("cantidadIngresada")));
    modelo.setArticulosPorUnidadAplicados(entero(fila.get("articulosPorUnidadAplicados")));
    modelo.setTotalEtiquetas(largo(fila.get("totalEtiquetas")));
    return modelo;
  }

  public static List<CalculoEtiquetas> calculoLista(List<Map<String, Object>> filas) {
    List<CalculoEtiquetas> lista = new ArrayList<>();
    for (Map<String, Object> fila : filas) lista.add(calculo(fila));
    return lista;
  }
}
