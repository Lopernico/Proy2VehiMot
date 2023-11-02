package com.itson.dominio;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.*;

/**
 * Clase entidad de pago
 *
 * @author Sebastian
 */
@Entity
@Table(name = "pagos")
public class Pago implements Serializable {

    /**
     * id de el pago
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Cantidad del pago realizado
     */
    @Column(name = "cantidad", nullable = false)
    private double cantidad;

    /**
     * fecha de pago del tramite
     */
    @Column(name = "fechaPago", nullable = false)
    @Temporal(TemporalType.DATE)
    private Calendar fechaPago;

    /**
     * Relacion uno a uno con tramite
     */
    @OneToOne
    @JoinColumn(name = "id_tramite")
    private Tramite tramite;

    /**
     * Metodo de acceso para obtener el id
     *
     * @return id del pago
     */
    public Long getId() {
        return id;
    }

    /**
     * Metodo de acceso para establecer el id del pago
     *
     * @param id id a establecer
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Metodo de acceso para obtener la cantidad monetaria del pago
     *
     * @return cantidad monetaria del pago
     */
    public double getCantidad() {
        return cantidad;
    }

    /**
     * Metodo de acceso para establecer la cantidad monetaria del pago
     *
     * @param cantidad Cantida monetaria a establecer
     */
    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Metodo de acceso para obtener la fecha de pago
     *
     * @return Fecha de pago
     */
    public Calendar getFechaPago() {
        return fechaPago;
    }

    /**
     * Metodo de acceso para establecer la fecha de pago
     *
     * @param fechaPago Fecha de pago a establecer
     */
    public void setFechaPago(Calendar fechaPago) {
        this.fechaPago = fechaPago;
    }

    /**
     * Metodo de acceso para obtener el tramite pagado
     *
     * @return Tramite pagado
     */
    public Tramite getTramite() {
        return tramite;
    }

    /**
     * Metodo de acceso para establecer el tramite pagado
     *
     * @param tramite Tramite a establecer
     */
    public void setTramite(Tramite tramite) {
        this.tramite = tramite;
    }

    /**
     * Hashcode de pago
     *
     * @return Hashcode de pago
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Metodo para compararse con otro pago, tomando en cuenta el id
     *
     * @param object Pago a comparar
     * @return Si el pago es igual al otro
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pago)) {
            return false;
        }
        Pago other = (Pago) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Cadena de texto que contiene los atributos de pago
     *
     * @return Cadena con los atributos de pago
     */
    @Override
    public String toString() {
        return "Pago{" + "id=" + id + ", cantidad=" + cantidad + ", fechaPago=" + fechaPago + ", tramite=" + tramite + '}';
    }

}
