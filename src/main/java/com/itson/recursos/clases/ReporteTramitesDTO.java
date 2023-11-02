/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.recursos.clases;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Sebastian
 */
public class ReporteTramitesDTO {

    // Atributos a incluir en el reporte
    private Date fechaEmision;
    private String tipoTramite;
    private String nombresSolicitante;
    private String apellidoPSolicitante;
    private String apellidoMSolicitante;
    private String nombreSolicitante;
    private double costo;
    
    /**
     * Constructor de la clase ReporteTramitesDTO.
     *
     * @param fechaEmision La fecha de emisión del reporte.
     * @param tipoTramite El tipo de trámite del reporte.
     * @param nombresSolicitante Los nombres del solicitante del trámite.
     * @param apellidoPSolicitante El apellido paterno del solicitante del trámite.
     * @param apellidoMSolicitante El apellido materno del solicitante del trámite.
     * @param costo El costo del trámite.
     */
    public ReporteTramitesDTO(Calendar fechaEmision, String tipoTramite, String nombresSolicitante, String apellidoPSolicitante, String apellidoMSolicitante, double costo) {
        this.fechaEmision = fechaEmision.getTime();
        this.tipoTramite = tipoTramite;
        this.nombresSolicitante = nombresSolicitante;
        this.apellidoPSolicitante = apellidoPSolicitante;
        this.apellidoMSolicitante = apellidoMSolicitante;
        this.costo = costo;
        this.nombreSolicitante = this.nombresSolicitante + " " + this.apellidoPSolicitante + " " + this.apellidoMSolicitante;
    }

    /**
     * Obtiene la fecha de emisión del reporte.
     * @return La fecha de emisión del reporte.
     */
    public Date getFechaEmision() {
        return fechaEmision;
    }

    /**
     * Establece la fecha de emisión del reporte.
     * @param fechaEmision La fecha de emisión del reporte.
     */
    public void setFechaEmision(Calendar fechaEmision) {
        this.fechaEmision = fechaEmision.getTime();
    }

    /**
     * Obtiene el tipo de trámite del reporte.
     * @return El tipo de trámite del reporte.
     */
    public String getTipoTramite() {
        return tipoTramite;
    }

    /**
     * Establece el tipo de trámite del reporte.
     * @param tipoTramite El tipo de trámite del reporte.
     */
    public void setTipoTramite(String tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    /**
     * Obtiene los nombres del solicitante del trámite.
     * @return Los nombres del solicitante del trámite.
     */
    public String getNombresSolicitante() {
        return nombresSolicitante;
    }

    /**
     * Establece los nombres del solicitante del trámite.
     * @param nombresSolicitante Los nombres del solicitante del trámite.
     */
    public void setNombresSolicitante(String nombresSolicitante) {
        this.nombresSolicitante = nombresSolicitante;
    }

    /**
     * Obtiene el apellido paterno del solicitante del trámite.
     * @return El apellido paterno del solicitante del trámite.
     */
    public String getApellidoPSolicitante() {
        return apellidoPSolicitante;
    }

    /**
     * Establece el apellido paterno del solicitante del trámite.
     * @param apellidoPSolicitante El apellido paterno del solicitante del trámite.
     */
    public void setApellidoPSolicitante(String apellidoPSolicitante) {
        this.apellidoPSolicitante = apellidoPSolicitante;
    }

    /**
     * Obtiene el apellido materno del solicitante del trámite.
     * @return Apellido materno del solicitante del trámite.
     */
    public String getApellidoMSolicitante() {
        return apellidoMSolicitante;
    }

    /**
     * Establece el apellido materno del solicitante del trámite.
     * @param apellidoMSolicitante Apellido materno del solicitante del trámite.
     */
    public void setApellidoMSolicitante(String apellidoMSolicitante) {
        this.apellidoMSolicitante = apellidoMSolicitante;
    }

    /**
     * Obtiene el costo del trámite.
     * @return Costo del trámite.
     */
    public double getCosto() {
        return costo;
    }

    /**
     * Establece el costo del trámite.
     * @param costo Costo del trámite.
     */
    public void setCosto(double costo) {
        this.costo = costo;
    }

    /**
     * Obtiene el nombre completo del solicitante del trámite.
     * @return Nombre completo del solicitante del trámite.
     */
    public String getNombreSolicitante() {
        return nombreSolicitante;
    }

    /**
     * Establece el nombre completo del solicitante del trámite.
     * @param nombreSolicitante Nombre completo del solicitante del trámite.
     */
    public void setNombreSolicitante(String nombreSolicitante) {
        this.nombreSolicitante = nombreSolicitante;
    }  
    
    @Override
    public String toString() {
        return "ReporteTramitesDTO{" + "fechaEmision=" + fechaEmision + ", tipoTramite=" + tipoTramite + ", nombresSolicitante=" + nombresSolicitante + ", apellidoPSolicitante=" + apellidoPSolicitante + ", apellidoMSolicitante=" + apellidoMSolicitante + ", costo=" + costo + '}';
    }

}
