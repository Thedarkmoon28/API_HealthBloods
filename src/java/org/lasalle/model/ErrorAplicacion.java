package org.lasalle.model;

public final class ErrorAplicacion extends RuntimeException {
  public final int status;

  public ErrorAplicacion(int status, String message) {
    super(message);
    this.status = status;
  }
}
