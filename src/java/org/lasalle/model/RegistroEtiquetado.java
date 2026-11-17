package org.lasalle.model;

import java.util.List;

/** Datos de RegistroEtiquetado. Las fechas se intercambian en formato ISO-8601. */
public class RegistroEtiquetado {
  private String id;
  private String usuarioID;
  private String usuarioNombre;
  private String productoID;
  private String item;
  private String nombre;
  private String fechaRegistro;
  private String fechaDescongelacion;
  private int diasAplicados;
  private String fechaCaducidad;
  private String tipoCaptura;
  private int cantidadIngresada;
  private int articulosPorUnidadAplicados;
  private long totalEtiquetas;
  private List<RevisionCaducidad> revisiones;
  private Long diasRestantes;
  private String estadoCaducidad;
  private String ultimaRevision;
  private Boolean cerrado;

  public RegistroEtiquetado() {}

  public RegistroEtiquetado(
      String id,
      String usuarioID,
      String usuarioNombre,
      String productoID,
      String item,
      String nombre,
      String fechaRegistro,
      String fechaDescongelacion,
      int diasAplicados,
      String fechaCaducidad,
      String tipoCaptura,
      int cantidadIngresada,
      int articulosPorUnidadAplicados,
      long totalEtiquetas,
      List<RevisionCaducidad> revisiones,
      Long diasRestantes,
      String estadoCaducidad,
      String ultimaRevision,
      Boolean cerrado) {
    this.id = id;
    this.usuarioID = usuarioID;
    this.usuarioNombre = usuarioNombre;
    this.productoID = productoID;
    this.item = item;
    this.nombre = nombre;
    this.fechaRegistro = fechaRegistro;
    this.fechaDescongelacion = fechaDescongelacion;
    this.diasAplicados = diasAplicados;
    this.fechaCaducidad = fechaCaducidad;
    this.tipoCaptura = tipoCaptura;
    this.cantidadIngresada = cantidadIngresada;
    this.articulosPorUnidadAplicados = articulosPorUnidadAplicados;
    this.totalEtiquetas = totalEtiquetas;
    this.revisiones = revisiones;
    this.diasRestantes = diasRestantes;
    this.estadoCaducidad = estadoCaducidad;
    this.ultimaRevision = ultimaRevision;
    this.cerrado = cerrado;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsuarioID() {
    return usuarioID;
  }

  public void setUsuarioID(String usuarioID) {
    this.usuarioID = usuarioID;
  }

  public String getUsuarioNombre() {
    return usuarioNombre;
  }

  public void setUsuarioNombre(String usuarioNombre) {
    this.usuarioNombre = usuarioNombre;
  }

  public String getProductoID() {
    return productoID;
  }

  public void setProductoID(String productoID) {
    this.productoID = productoID;
  }

  public String getItem() {
    return item;
  }

  public void setItem(String item) {
    this.item = item;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getFechaRegistro() {
    return fechaRegistro;
  }

  public void setFechaRegistro(String fechaRegistro) {
    this.fechaRegistro = fechaRegistro;
  }

  public String getFechaDescongelacion() {
    return fechaDescongelacion;
  }

  public void setFechaDescongelacion(String fechaDescongelacion) {
    this.fechaDescongelacion = fechaDescongelacion;
  }

  public int getDiasAplicados() {
    return diasAplicados;
  }

  public void setDiasAplicados(int diasAplicados) {
    this.diasAplicados = diasAplicados;
  }

  public String getFechaCaducidad() {
    return fechaCaducidad;
  }

  public void setFechaCaducidad(String fechaCaducidad) {
    this.fechaCaducidad = fechaCaducidad;
  }

  public String getTipoCaptura() {
    return tipoCaptura;
  }

  public void setTipoCaptura(String tipoCaptura) {
    this.tipoCaptura = tipoCaptura;
  }

  public int getCantidadIngresada() {
    return cantidadIngresada;
  }

  public void setCantidadIngresada(int cantidadIngresada) {
    this.cantidadIngresada = cantidadIngresada;
  }

  public int getArticulosPorUnidadAplicados() {
    return articulosPorUnidadAplicados;
  }

  public void setArticulosPorUnidadAplicados(int articulosPorUnidadAplicados) {
    this.articulosPorUnidadAplicados = articulosPorUnidadAplicados;
  }

  public long getTotalEtiquetas() {
    return totalEtiquetas;
  }

  public void setTotalEtiquetas(long totalEtiquetas) {
    this.totalEtiquetas = totalEtiquetas;
  }

  public List<RevisionCaducidad> getRevisiones() {
    return revisiones;
  }

  public void setRevisiones(List<RevisionCaducidad> revisiones) {
    this.revisiones = revisiones;
  }

  public Long getDiasRestantes() {
    return diasRestantes;
  }

  public void setDiasRestantes(Long diasRestantes) {
    this.diasRestantes = diasRestantes;
  }

  public String getEstadoCaducidad() {
    return estadoCaducidad;
  }

  public void setEstadoCaducidad(String estadoCaducidad) {
    this.estadoCaducidad = estadoCaducidad;
  }

  public String getUltimaRevision() {
    return ultimaRevision;
  }

  public void setUltimaRevision(String ultimaRevision) {
    this.ultimaRevision = ultimaRevision;
  }

  public Boolean getCerrado() {
    return cerrado;
  }

  public void setCerrado(Boolean cerrado) {
    this.cerrado = cerrado;
  }
}
