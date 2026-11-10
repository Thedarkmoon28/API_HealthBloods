/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.lasalle.model;
import java.sql.Date;
/**
 *
 * @author mario
 */
public class Medida {
    
    private int idMedida;
    private int idUsuario;
    private int glucosa; //98 mg/dL
    private Date fechaHora;
    private String contexto; //En ayunas
    private String notas;

    public Medida(int idMedida, int idUsuario, int glucosa, String contexto, String notas,Date fechaHora) {
        this.idMedida = idMedida;
        this.idUsuario = idUsuario;
        this.glucosa = glucosa;
        this.contexto = contexto;
        this.notas = notas;
        this.fechaHora = fechaHora;
    }
    public Medida() {
    }
    public int getIdMedida() {
        return idMedida;
    }

    public void setIdMedida(int idMedida) {
        this.idMedida = idMedida;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getGlucosa() {
        return glucosa;
    }
    public void setGlucosa(int glucosa) {
        this.glucosa = glucosa;
    }
    
      public String getContexto() {
        return contexto;
    }
      
      public void setContexto(String contexto) {
        this.contexto = contexto;
    }


    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
    
    public Date getFechaHora() {
        return fechaHora;
    }
    
    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    
    
}
