/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.dominio;

import com.itson.recursos.clases.DuracionLicencia;
import com.itson.recursos.clases.TipoLicencia;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.persistence.*;

/**
 * Clase entidad de licencia
 *
 * @author Sebastian
 */
@Entity
@Table(name = "licencias")
public class Licencia extends Tramite implements Serializable {

    /**
     * Fecha de expedicion de la licencia
     */
    @Column(name = "fechaExpedicion", nullable = false)
    @Temporal(TemporalType.DATE)
    private Calendar fechaExpedicion;

    /**
     * Fecha maxima de validez de la licencia, calculado mediante fecha de
     * expedicion y la duracion de la licencia
     */
    @Column(name = "fechaMax", nullable = false)
    @Temporal(TemporalType.DATE)
    private Calendar fechaMax;

    /**
     * El tipo de licencia. Normal y discapacitado
     */
    @Column(name = "tipoLicencia", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TipoLicencia tipoLicencia;

    /**
     * La duracion de la licencia. 1, 2 y 3 anios
     */
    @Column(name = "vigencia", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private DuracionLicencia vigencia;

    /**
     * Relacion uno a muchos con placas
     */
    @OneToMany(mappedBy = "licencia", cascade = CascadeType.PERSIST)
    private List<Placa> placas;

    /**
     * Relacion muchos a uno con persona
     */
    @ManyToOne
    @JoinColumn(name = "id_persona")
    private Persona persona;

    /**
     * Metodo de acceso para obtener la fecha de expedicion de la licencia
     *
     * @return Fecha de expedicion de la licencia
     */
    public Calendar getFechaExpedicion() {
        return fechaExpedicion;
    }

    /**
     * Metodo de acceso para establecer la fecha de expedicion de la licencia
     *
     * @param fechaExpedicion Fecha de expedicion a establecer
     */
    public void setFechaExpedicion(Calendar fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    /**
     * Metodo de acceso para obtener el tipo de licencia.
     *
     * Normal y discapacitado
     *
     * @return Tipo de licencia
     */
    public TipoLicencia getTipoLicencia() {
        return tipoLicencia;
    }

    /**
     * Metodo de acceso para establecer el tipo de licencia.
     *
     * Normal y discapacitado
     *
     * @param tipoLicencia Tipo de licencia a establecer
     */
    public void setTipoLicencia(TipoLicencia tipoLicencia) {
        this.tipoLicencia = tipoLicencia;
    }

    /**
     * Metodo de acceso para obtener la vigencia de la licencia.
     *
     * 1, 2 y 3 anios
     *
     * @return La vigencia de la licencia
     */
    public DuracionLicencia getVigencia() {
        return vigencia;
    }

    /**
     * Metodo de acceso para establecer la vigencia de la licencia.F
     *
     * 1, 2 y 3 anios
     *
     * @param vigencia Vigencia a establecer
     */
    public void setVigencia(DuracionLicencia vigencia) {
        this.vigencia = vigencia;
    }

    /**
     * Metodo de acceso para obtener la lista de placas relacionadas a la
     * licencia
     *
     * @return Lista de placas relacionadas a la licencia
     */
    public List<Placa> getPlacas() {
        return placas;
    }

    /**
     * Metodo de acceso para establecer la lista de placas relacionadas a la
     * licencia
     *
     * @param placas Lista de placas a establecer
     */
    public void setPlacas(List<Placa> placas) {
        this.placas = placas;
    }

    /**
     * Metodo de acceso para obtener la persona relacionada a la licencia
     *
     * @return Persona relacionada a la licencia
     */
    public Persona getPersona() {
        return persona;
    }

    /**
     * Metodo de acceso para establecer la persona relacionada a la licencia
     *
     * @param persona Persona a establecer
     */
    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    /**
     * Metodo de acceso para obtener la fecha maxima de vigencia de la licencia
     *
     * @return Fecha maxima de la licencia
     */
    public Calendar getFechaMax() {
        return fechaMax;
    }

    /**
     * Metodo de acceso para establecer la fecha maxima de vigencia de la
     * licencia
     *
     * @param fechaMax Fecha maxima a establecer
     */
    public void setFechaMax(Calendar fechaMax) {
        this.fechaMax = fechaMax;
    }

    /**
     * hashcode de la licencia
     *
     * @return hashcode de la licencia
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Metodo para compararse con otra licencia, tomando en cuenta el id de la
     * licencia
     *
     * @param object Licencia a comparar
     * @return Si son iguales
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Licencia)) {
            return false;
        }
        Licencia other = (Licencia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Cadena de texto con los atributos de la licencia
     *
     * @return Cadena con los atributos
     */
    @Override
    public String toString() {
        return "Licencia{" + "fechaExpedicion=" + fechaExpedicion + ", fechaMax=" + fechaMax + ", tipoLicencia=" + tipoLicencia + ", vigencia=" + vigencia + ", persona=" + persona.getNombres();
    }

}
