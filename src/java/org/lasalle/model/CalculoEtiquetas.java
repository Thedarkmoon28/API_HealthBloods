package org.lasalle.model;

/** Datos de CalculoEtiquetas. Las fechas se intercambian en formato ISO-8601. */
public class CalculoEtiquetas {
  private String productoID;
  private String item;
  private String nombre;
  private String fechaDescongelacion;
  private int diasAplicados;
  private String fechaCaducidad;
  private String tipoCaptura;
  private int cantidadIngresada;
  private int articulosPorUnidadAplicados;
  private long totalEtiquetas;

  public CalculoEtiquetas() {}

  public CalculoEtiquetas(
      String productoID,
      String item,
      String nombre,
      String fechaDescongelacion,
      int diasAplicados,
      String fechaCaducidad,
      String tipoCaptura,
      int cantidadIngresada,
      int articulosPorUnidadAplicados,
      long totalEtiquetas) {
    this.productoID = productoID;
    this.item = item;
    this.nombre = nombre;
    this.fechaDescongelacion = fechaDescongelacion;
    this.diasAplicados = diasAplicados;
    this.fechaCaducidad = fechaCaducidad;
    this.tipoCaptura = tipoCaptura;
    this.cantidadIngresada = cantidadIngresada;
    this.articulosPorUnidadAplicados = articulosPorUnidadAplicados;
    this.totalEtiquetas = totalEtiquetas;
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
}
