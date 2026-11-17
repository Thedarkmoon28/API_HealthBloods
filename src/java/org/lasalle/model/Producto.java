package org.lasalle.model;

/** Datos de Producto. Las fechas se intercambian en formato ISO-8601. */
public class Producto {
  private String id;
  private String item;
  private String nombre;
  private String marca;
  private int diasTrasDescongelar;
  private Integer articulosPorCaja;
  private Integer articulosPorNivel;
  private boolean activo;
  private String fechaActualizacion;

  public Producto() {}

  public Producto(
      String id,
      String item,
      String nombre,
      String marca,
      int diasTrasDescongelar,
      Integer articulosPorCaja,
      Integer articulosPorNivel,
      boolean activo,
      String fechaActualizacion) {
    this.id = id;
    this.item = item;
    this.nombre = nombre;
    this.marca = marca;
    this.diasTrasDescongelar = diasTrasDescongelar;
    this.articulosPorCaja = articulosPorCaja;
    this.articulosPorNivel = articulosPorNivel;
    this.activo = activo;
    this.fechaActualizacion = fechaActualizacion;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public String getMarca() {
    return marca;
  }

  public void setMarca(String marca) {
    this.marca = marca;
  }

  public int getDiasTrasDescongelar() {
    return diasTrasDescongelar;
  }

  public void setDiasTrasDescongelar(int diasTrasDescongelar) {
    this.diasTrasDescongelar = diasTrasDescongelar;
  }

  public Integer getArticulosPorCaja() {
    return articulosPorCaja;
  }

  public void setArticulosPorCaja(Integer articulosPorCaja) {
    this.articulosPorCaja = articulosPorCaja;
  }

  public Integer getArticulosPorNivel() {
    return articulosPorNivel;
  }

  public void setArticulosPorNivel(Integer articulosPorNivel) {
    this.articulosPorNivel = articulosPorNivel;
  }

  public boolean isActivo() {
    return activo;
  }

  public void setActivo(boolean activo) {
    this.activo = activo;
  }

  public String getFechaActualizacion() {
    return fechaActualizacion;
  }

  public void setFechaActualizacion(String fechaActualizacion) {
    this.fechaActualizacion = fechaActualizacion;
  }
}
