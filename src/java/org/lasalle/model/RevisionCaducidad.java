package org.lasalle.model;

/** Datos de RevisionCaducidad. Las fechas se intercambian en formato ISO-8601. */
public class RevisionCaducidad {
  private String id;
  private String registroEtiquetadoID;
  private String usuarioID;
  private String usuarioNombre;
  private String fechaRevision;
  private String resultado;
  private String observaciones;

  public RevisionCaducidad() {}

  public RevisionCaducidad(
      String id,
      String registroEtiquetadoID,
      String usuarioID,
      String usuarioNombre,
      String fechaRevision,
      String resultado,
      String observaciones) {
    this.id = id;
    this.registroEtiquetadoID = registroEtiquetadoID;
    this.usuarioID = usuarioID;
    this.usuarioNombre = usuarioNombre;
    this.fechaRevision = fechaRevision;
    this.resultado = resultado;
    this.observaciones = observaciones;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRegistroEtiquetadoID() {
    return registroEtiquetadoID;
  }

  public void setRegistroEtiquetadoID(String registroEtiquetadoID) {
    this.registroEtiquetadoID = registroEtiquetadoID;
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

  public String getFechaRevision() {
    return fechaRevision;
  }

  public void setFechaRevision(String fechaRevision) {
    this.fechaRevision = fechaRevision;
  }

  public String getResultado() {
    return resultado;
  }

  public void setResultado(String resultado) {
    this.resultado = resultado;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }
}
