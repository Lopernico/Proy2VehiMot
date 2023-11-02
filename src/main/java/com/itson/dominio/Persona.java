/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.dominio;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.persistence.*;

/**
 * Clase entidad de persona
 *
 * @author Sebastian
 */
@Entity
@Table(name = "personas")
public class Persona implements Serializable {

    /**
     * id de persona
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * RFC de la persona
     */
    @Column(name = "RFC", nullable = false, unique = true, length = 13)
    private String RFC;

    /**
     * Nombres de la persona
     */
    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    /**
     * Apellido paterno de la persona
     */
    @Column(name = "apellidoP", nullable = false, length = 50)
    private String apellidoP;

    /**
     * Apellido materno de la persona
     */
    @Column(name = "apellidoM", nullable = false, length = 50)
    private String apellidoM;

    /**
     * Fecha de nacimiento de la persona
     */
    @Column(name = "fechaNacimiento", nullable = false)
    @Temporal(TemporalType.DATE)
    private Calendar fechaNacimiento;

    /**
     * Telefono de la persona
     */
    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    /**
     * Relacion uno a muchos con licencia
     */
    @OneToMany(mappedBy = "persona", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Licencia> licencias;

    /**
     * Relacion uno a muchos con automovil
     */
    @OneToMany(mappedBy = "persona", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Automovil> automoviles;

    /**
     * Metodo de acceso para obtener el id de la persona
     *
     * @return id de la persona
     */
    public Long getId() {
        return id;
    }

    /**
     * Metodo de acceso para establecer el id de persona
     *
     * @param id id a establecer
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Metodo de acceso para obtener el RFC de la persona
     *
     * @return RFC de la persona
     */
    public String getRFC() {
        return RFC;
    }

    /**
     * Metodo de acceso para establecer el RFC
     *
     * @param RFC RFC a establecer
     */
    public void setRFC(String RFC) {
        this.RFC = RFC;
    }

    /**
     * Metodo de acceso para obtener los nombres de la persona
     *
     * @return Nombres de la persona
     */
    public String getNombres() {
        return nombres;
    }

    /**
     * Metodo de acceso para establecer los nombres de la persona
     *
     * @param nombres Nombres a establecer
     */
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    /**
     * Metodo de acceso para obtener el apellido paterno de la persona
     *
     * @return Apellido paterno de la persona
     */
    public String getApellidoP() {
        return apellidoP;
    }

    /**
     * Metodo de acceso para establecer el apellido paterno de la persona
     *
     * @param apellidoP Apellido paterno a establecer
     */
    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    /**
     * Metodo de acceso para obtener el apellido materno de la persona
     *
     * @return Apellido materno de la persona
     */
    public String getApellidoM() {
        return apellidoM;
    }

    /**
     * Metodo de acceso para establecer el apellido materno de la persona
     *
     * @param apellidoM Apellido materno a establecer
     */
    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    /**
     * Metodo de acceso para obtener la fecha de nacimiento de la persona
     *
     * @return Fecha de nacimiento de la persona
     */
    public Calendar getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Metodo de acceso para establecer la fecha de nacimiento
     *
     * @param fechaNacimiento Fecha de nacimiento a establecer
     */
    public void setFechaNacimiento(Calendar fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Metodo de acceso para obtener el telefono de la persona
     *
     * @return Telefono de la persona
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Metodo de acceso para establecer el telefono de la persona
     *
     * @param telefono Telefono a establecerr
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Metodo de acceso para obtener las licencias relacionadas a la persona
     *
     * @return Lista de licencias de la persona
     */
    public List<Licencia> getLicencias() {
        return licencias;
    }

    /**
     * Metodo de acceso para establecer las listas relacionadas a la persona
     *
     * @param licencias Lista de licencias a establecer
     */
    public void setLicencias(List<Licencia> licencias) {
        this.licencias = licencias;
    }

    /**
     * Metodo de acceso para obtener los automoviles relacionados a la persona
     *
     * @return Lista de automoviles de la persona
     */
    public List<Automovil> getAutomoviles() {
        return automoviles;
    }

    /**
     * Metodo de acceso para establecer los automoviles relacionados a la
     * persona
     *
     * @param automoviles lista de automoviles a establecer
     */
    public void setAutomoviles(List<Automovil> automoviles) {
        this.automoviles = automoviles;
    }

    /**
     * Hashcode de la persona
     *
     * @return Hashcode de la persona
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Se compara con otra persona, tomando en cuenta el id
     *
     * @param object Persona a comparar
     * @return Si son la misma persona
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Cadena de texto con los atributos de persona
     *
     * @return Cadena con los atributos de persona
     */
    @Override
    public String toString() {
        return "Persona{" + "id=" + id + ", RFC=" + RFC + ", nombres=" + nombres + ", apellidoP=" + apellidoP + ", apellidoM=" + apellidoM + ", fechaNacimiento=" + fechaNacimiento + ", telefono=" + telefono;
    }

}
