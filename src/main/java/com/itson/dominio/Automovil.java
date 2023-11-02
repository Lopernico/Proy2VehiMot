package com.itson.dominio;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 * Clase entidad de automovil
 *
 * @author Sebastian
 */
@Entity
@Table(name = "automoviles")
public class Automovil implements Serializable {

    /**
     * Identificador en la base de datos
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Numero de serie del automovil
     */
    @Column(name = "numSerie", nullable = false, unique = true, length = 10)
    private String numSerie;

    /**
     * Marca del automovil
     */
    @Column(name = "marca", nullable = false, length = 30)
    private String marca;
    /**
     * Linea del automovil
     */
    @Column(name = "linea", nullable = false, length = 30)
    private String linea;

    /**
     * Color del automovil
     */
    @Column(name = "color", nullable = false, length = 30)
    private String color;

    /**
     * Modelo del automovil
     */
    @Column(name = "modelo", nullable = false, length = 30)
    private String modelo;

    /**
     * Relacion uno a muchos con placas
     */
    @OneToMany(mappedBy = "automovil")
    private List<Placa> placas;

    /**
     * Relacion muchos a uno con persona
     */
    @ManyToOne
    @JoinColumn(name = "id_persona")
    private Persona persona;

    /**
     * Metodo de acceso para obtener el id del automovil
     *
     * @return id del automovil
     */
    public Long getId() {
        return id;
    }

    /**
     * Metodo de acceso para establecer el id
     *
     * @param id id a establecer
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Metodo de acceso para obtener el numero de serie del automovil
     *
     * @return Numero de serie del automovil
     */
    public String getNumSerie() {
        return numSerie;
    }

    /**
     * Metodo de acceso para establecer el numero de serie del automovil
     *
     * @param numSerie Numero de serie a establecer
     */
    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    /**
     * Metodo de acceso para obtener la marca del automovil
     *
     * @return Marca del automovil
     */
    public String getMarca() {
        return marca;
    }

    /**
     * Metodo de acceso para establecer la marca del automovil
     *
     * @param marca Marca a establecer
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * Metodo de acceso para obtener la linea del automovil
     *
     * @return Linea del automovil
     */
    public String getLinea() {
        return linea;
    }

    /**
     * Metodo de acceso para establecer la linea del automovil
     *
     * @param linea Linea a establecer
     */
    public void setLinea(String linea) {
        this.linea = linea;
    }

    /**
     * Metodo de acceso para obtener el color del automovil
     *
     * @return Color del automovil
     */
    public String getColor() {
        return color;
    }

    /**
     * Metodo de acceso para establecer el color del automovil
     *
     * @param color Color a establecer
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Metodo de acceso para obtener el modelo del automovil
     *
     * @return Modelo del automovil
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Metodo de acceso para establecer el modelo del automovil
     *
     * @param modelo Modelo a establecer
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * Metodo de acceso para obtener las placas relacionadas con el automovil
     *
     * @return Placas del automovil
     */
    public List<Placa> getPlacas() {
        return placas;
    }

    /**
     * Metodo de acceso para establecer las placas relacionadas con el automovil
     *
     * @param placas Placas a establecer
     */
    public void setPlacas(List<Placa> placas) {
        this.placas = placas;
    }

    /**
     * Metodo de acceso para obtener la persona relacionada con el automovil
     *
     * @return Persona del automovil
     */
    public Persona getPersona() {
        return persona;
    }

    /**
     * Metodo de acceso para establecer la persona relacionada del automovil
     *
     * @param persona Persona a establecer
     */
    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    /**
     * Hashcode de automovil
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Metodo para compararse con otro automovil, tomando en cuenta el id del
     * automovil
     *
     * @param object Objeto a comparar
     * @return Si son iguales
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Automovil)) {
            return false;
        }
        Automovil other = (Automovil) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Cadena de texto con los atributos del automovil
     *
     * @return Atributos en texto
     */
    @Override
    public String toString() {
        return "Automovil{" + "id=" + id + ", numSerie=" + numSerie + ", marca=" + marca + ", linea=" + linea + ", color=" + color + ", modelo=" + modelo + ", placas=" + placas + ", persona=" + persona + '}';
    }

}
