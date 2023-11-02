/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.recursos.clases;

import java.util.Calendar;

/**
 * Clase para manejar los parámetros de búsqueda de un conjunto de datos.
 * @author Sebastian
 */
public class ParametrosBusqueda {

    //Atributos para búsqueda.
    private String RFC;
    private String nombreSimilar;
    private Calendar fechaNacimiento;
    private Paginado confPag;
    private Calendar periodo1;
    private Calendar periodo2;
    private String tipo;

    /**
     * Establece el RFC ParametrosBusqueda.
     * @param RFC El RFC para búsqueda.
     * @return Una referencia a la instancia de ParametrosBusqueda 
     * para permitir el encadenamiento de métodos.
     */
    public ParametrosBusqueda setRFC(String RFC) {
        this.RFC = RFC;
        return this;
    }

    /**
     * Establece el nombre similar para búsqueda.
     * @param nombreSimilar El nombre similar para búsqueda.
     * @return Una referencia a la instancia de ParametrosBusqueda para 
     * permitir el encadenamiento de métodos.
     */
    public ParametrosBusqueda setNombreSimilar(String nombreSimilar) {
        this.nombreSimilar = nombreSimilar;
        return this;
    }

    /**
     * Establece la fecha de nacimiento para búsqueda.
     * @param fechaNacimiento La fecha de nacimiento para búsqueda.
     * @return Una referencia a la instancia de ParametrosBusqueda para 
     * permitir el encadenamiento de métodos.
     */
    public ParametrosBusqueda setFechaNacimiento(Calendar fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
        return this;
    }

    /**
     * Obtiene el RFC para búsqueda.
     * @return El RFC para búsqueda.
     */
    public String getRFC() {
        return RFC;
    }

    /**
     * Obtiene el nombre similar para búsqueda.
     * @return El nombre similar para búsqueda.
     */
    public String getNombreSimilar() {
        return nombreSimilar;
    }

    /**
     * Obtiene la fecha de nacimiento para búsqueda.
     * @return La fecha de nacimiento para búsqueda.
     */
    public Calendar getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Obtiene la configuración de paginación.
     * @return La configuración de paginación.
     */
    public Paginado getConfPag() {
        return confPag;
    }

    /**
     * Establece la configuración de paginación.
     * @param confPag La configuración de paginación.
     */
    public void setConfPag(Paginado confPag) {
        this.confPag = confPag;
    }

    /**
     * Obtiene el periodo de búsqueda 1.
     * @return El periodo de búsqueda 1.
     */
    public Calendar getPeriodo1() {
        return periodo1;
    }

    /**
     * Establece el periodo de búsqueda 1.
     * @param periodo1 El periodo de búsqueda 1.
     * @return Una referencia a la instancia de ParametrosBusqueda para permitir el encadenamiento de métodos.
     */
    public ParametrosBusqueda setPeriodo1(Calendar periodo1) {
        this.periodo1 = periodo1;
        return this;
    }

    /**
     * Obtiene el periodo de búsqueda 2.
     * @return El periodo de búsqueda 2.
     */
    public Calendar getPeriodo2() {
        return periodo2;
    }

    /**
     * Establece el periodo de búsqueda 2.
     * @param periodo2 El periodo de búsqueda 2.
     * @return Una referencia a la instancia de ParametrosBusqueda para permitir el encadenamiento de métodos.
     */
    public ParametrosBusqueda setPeriodo2(Calendar periodo2) {
        this.periodo2 = periodo2;
        return this;
    }

    /**
     * Obtiene el tipo de búsqueda.
     * @return El tipo de búsqueda.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de búsqueda.
     * @param tipo El tipo de búsqueda.
     * @return Una referencia a la instancia de ParametrosBusqueda para permitir el encadenamiento de métodos.
     */
    public ParametrosBusqueda setTipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

}
