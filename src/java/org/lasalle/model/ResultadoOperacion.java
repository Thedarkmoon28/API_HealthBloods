package org.lasalle.model;

/** Distingue una inserción nueva de un reintento sin mezclar HTTP en el controlador. */
public record ResultadoOperacion<T>(T datos, boolean creado) {}
