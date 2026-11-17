package org.lasalle.model;

/** Datos de Usuario. Las fechas se intercambian en formato ISO-8601. */
public class Usuario {
  private String id;
  private String nombre;
  private String correo;
  private String rol;
  private boolean activo;
  private String fechaCreacion;
  private String fechaActualizacion;

  public Usuario() {}

  public Usuario(
      String id,
      String nombre,
      String correo,
      String rol,
      boolean activo,
      String fechaCreacion,
      String fechaActualizacion) {
    this.id = id;
    this.nombre = nombre;
    this.correo = correo;
    this.rol = rol;
    this.activo = activo;
    this.fechaCreacion = fechaCreacion;
    this.fechaActualizacion = fechaActualizacion;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public String getRol() {
    return rol;
  }

  public void setRol(String rol) {
    this.rol = rol;
  }

  public boolean isActivo() {
    return activo;
  }

  public void setActivo(boolean activo) {
    this.activo = activo;
  }

  public String getFechaCreacion() {
    return fechaCreacion;
  }

  public void setFechaCreacion(String fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }

  public String getFechaActualizacion() {
    return fechaActualizacion;
  }

  public void setFechaActualizacion(String fechaActualizacion) {
    this.fechaActualizacion = fechaActualizacion;
  }
}
