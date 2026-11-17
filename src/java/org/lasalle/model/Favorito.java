package org.lasalle.model;

/** Datos de Favorito. Las fechas se intercambian en formato ISO-8601. */
public class Favorito {
  private String id;
  private String usuarioID;
  private String productoID;
  private String fechaAgregado;
  private String item;
  private String nombre;
  private String marca;
  private Integer diasTrasDescongelar;
  private Boolean activo;

  public Favorito() {}

  public Favorito(
      String id,
      String usuarioID,
      String productoID,
      String fechaAgregado,
      String item,
      String nombre,
      String marca,
      Integer diasTrasDescongelar,
      Boolean activo) {
    this.id = id;
    this.usuarioID = usuarioID;
    this.productoID = productoID;
    this.fechaAgregado = fechaAgregado;
    this.item = item;
    this.nombre = nombre;
    this.marca = marca;
    this.diasTrasDescongelar = diasTrasDescongelar;
    this.activo = activo;
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

  public String getProductoID() {
    return productoID;
  }

  public void setProductoID(String productoID) {
    this.productoID = productoID;
  }

  public String getFechaAgregado() {
    return fechaAgregado;
  }

  public void setFechaAgregado(String fechaAgregado) {
    this.fechaAgregado = fechaAgregado;
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

  public Integer getDiasTrasDescongelar() {
    return diasTrasDescongelar;
  }

  public void setDiasTrasDescongelar(Integer diasTrasDescongelar) {
    this.diasTrasDescongelar = diasTrasDescongelar;
  }

  public Boolean getActivo() {
    return activo;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
  }
}
