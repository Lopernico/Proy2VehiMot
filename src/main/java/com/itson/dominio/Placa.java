/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.dominio;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.*;

/**
 * Clase entidad de placa
 * @author Sebastian
 */
@Entity
@Table(name = "placas")
public class Placa extends Tramite implements Serializable {
    
    /**
     * Numero de placa de la placa
     */
    @Column(name = "numPlaca", nullable = false, unique = true, length = 7)
    private String numPlaca;
    
    /**
     * Fecha de recepcion de la placa
     */
    @Column(name = "fechaRecepcion", nullable = true)
    @Temporal(TemporalType.DATE)
    private Calendar fechaRecepcion;
    
    /**
     * Relacion muchos a uno con licencia
     */
    @ManyToOne
    @JoinColumn(name = "id_licencia")
    private Licencia licencia;
    
    /**
     * Relacion muchos a uno con automovil
     */
    @ManyToOne
    @JoinColumn(name = "id_automovil")
    private Automovil automovil;
    
    /**
     * Metodo de acceso para obtener el numero de placa de la placa
     * @return Numero de placa
     */
    public String getNumPlaca() {
        return numPlaca;
    }
    
    /**
     * Metodo de acceso para establecer el numero de placa
     * @param numPlaca Numero de placa a establecer
     */
    public void setNumPlaca(String numPlaca) {
        this.numPlaca = numPlaca;
    }
    
    /**
     * Metodo de acceso para obtener la fecha de recepcion de la placa
     * @return Fecha de recepcion de la placa
     */
    public Calendar getFechaRecepcion() {
        return fechaRecepcion;
    }
    
    /**
     * Metodo de acceso para establecer la fecha de recepcion de la placa
     * @param fechaRecepcion Fecha de recepcion a establecer
     */
    public void setFechaRecepcion(Calendar fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }
    
    /**
     * Metodo de acceso para obtener la licencia relacionada a la placa
     * @return Licencia de la placa
     */
    public Licencia getLicencia() {
        return licencia;
    }
    
    /**
     * Metodo de acceso para establecer la licencia relacionada a placa
     * @param licencia Licencia a establecer
     */
    public void setLicencia(Licencia licencia) {
        this.licencia = licencia;
    }
    
    /**
     * Metodo de acceso para obtener el automovil relacionado a la placa
     * @return Automovil relacionado a la placa
     */
    public Automovil getAutomovil() {
        return automovil;
    }
    
    /**
     * Metodo de acceso para establecer el automovil relacionado a placa
     * @param automovil Automovil a establecer
     */
    public void setAutomovil(Automovil automovil) {
        this.automovil = automovil;
    }
    
    /**
     * Hashcode de placa
     * @return Hashcode de placa
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    /**
     * Se compara con otra placa, tomando en cuenta el id
     * @param object Placa a comparar
     * @return Si las placas son iguales
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Placa)) {
            return false;
        }
        Placa other = (Placa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    /**
     * Cadena de texto que contiene los atributos de placa
     * @return Cadena con atributos
     */
    @Override
    public String toString() {
        return "Placa{" + "numPlaca=" + numPlaca + ", fechaRecepcion=" + fechaRecepcion + ", licencia=" + licencia + ", automovil=" + automovil.getNumSerie();
    }

}
