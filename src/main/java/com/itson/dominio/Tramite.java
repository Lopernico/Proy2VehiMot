/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.dominio;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.*;

/**
 * Clase entidad de tramites
 *
 * @author Sebastian
 */
@Table(name = "tramites")
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public abstract class Tramite implements Serializable {

    /**
     * id de tramite
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    /**
     * Fecha de emision del tramite
     */
    @Column(name = "fechaEmision", nullable = false)
    @Temporal(TemporalType.DATE)
    protected Calendar fechaEmision;

    /**
     * Costo del tramite
     */
    @Column(name = "costo", nullable = false)
    protected double costo;

    /**
     * Relacion uno a uno con pago
     */
    @OneToOne(mappedBy = "tramite")
    protected Pago pago;

    /**
     * Metodo de acceso para obtener el id del tramite
     *
     * @return id del tramite
     */
    public Long getId() {
        return id;
    }

    /**
     * Metodo de acceso para establecer el id del tramite
     *
     * @param id id a establecer
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Metodo de acceso para obtener la fecha de emision del tramite
     *
     * @return Fecha de emision del tramite
     */
    public Calendar getFechaEmision() {
        return fechaEmision;
    }

    /**
     * Metodo de acceso para establecer la fecha de emision del tramite
     *
     * @param fechaEmision Fecha de emision a establecer
     */
    public void setFechaEmision(Calendar fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    /**
     * Metodo de acceso para obtener el costo del tramite
     *
     * @return Costo del tramite
     */
    public double getCosto() {
        return costo;
    }

    /**
     * Metodo de acceso para establecer el costo del tramite
     *
     * @param costo Costo a establecer
     */
    public void setCosto(double costo) {
        this.costo = costo;
    }

    /**
     * Metodo de acceso para obtener el pago del tramite
     *
     * @return Pago del tramite
     */
    public Pago getPago() {
        return pago;
    }

    /**
     * Metodo de acceso para establecer el pago del tramite
     *
     * @param pago Pago a establecer
     */
    public void setPago(Pago pago) {
        this.pago = pago;
    }

    /**
     * Hashcode de tramite
     *
     * @return Hashcode de tramite
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Se compara con otro tramite, tomando en cuenta el id
     *
     * @param object Tramite a comparar
     * @return Si son iguales los tramites
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tramite)) {
            return false;
        }
        Tramite other = (Tramite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Cadena de texto que contiene los atributos de tramite
     *
     * @return Cadena con atributos
     */
    @Override
    public String toString() {
        return "Tramite{" + "id=" + id + ", fechaEmision=" + fechaEmision + ", costo=" + costo + '}';
    }

}
