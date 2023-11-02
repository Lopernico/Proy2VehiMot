/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.itson.recursos.clases;

/**
 * Enumeración que define las diferentes duraciones de una licencia.
 * @author Sebastian
 */
public enum DuracionLicencia {
    UnAnio(12, 600, 200),
    dosAnios(24, 900, 500),
    tresAnios(36, 1100, 700);

    private int duracionDias;
    private double costoNormal;
    private double costoDiscapacitado;

    /**
     * Constructor de la enumeración DuracionLicencia.
     * @param duracionDias Duración en días de la Licencia.
     * @param costoNormal Costo normal de la licencia.
     * @param costoDiscapacitado Costo para discapacitados de la licencia.
     */
    private DuracionLicencia(int duracionDias, double costoNormal, double costoDiscapacitado) {
        this.duracionDias = duracionDias;
        this.costoNormal = costoNormal;
        this.costoDiscapacitado = costoDiscapacitado;
    }

    /**
     * Obtiene la duración en días de la licencia.
     * @return La duración en días de la licencia.
     */
    public int getDuracionDias() {
        return duracionDias;
    }

    /**
     * Obtiene el costo normal de la licencia.
     * @return El costo normal de la licencia.
     */
    public double getCostoNormal() {
        return costoNormal;
    }

    /**
     * Obtiene el costo de la licencia para discapacitados.
     * @return El costo de la licencia para discapacitados.
     */
    public double getCostoDiscapacitado() {
        return costoDiscapacitado;
    }

}
