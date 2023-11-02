/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.implementacion;

import com.itson.dominio.Pago;
import com.itson.dominio.Tramite;
import com.itson.excepciones.PersistenciaException;
import com.itson.interfaz.IPagosDAO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;

/**
 * Clase de acceso a datos de pagos
 *
 * @author Sebastian
 */
public class PagosDAO implements IPagosDAO {

    /**
     * Generador de entitymanager para interactuar con la base de datos
     */
    private final EntityManagerFactory emf;

    /**
     * Constructor que recibe el EntityManagerFactory y lo establece en los
     * atributos
     *
     * @param emf EntityManagerFactory
     */
    public PagosDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * Inserta en la base de datos el pago dado en el parametro
     *
     * @param pago Pago a insertar
     * @return El pago que fue insertado
     * @throws PersistenciaException Si la entidad ya existe en la base de datos
     */
    @Override
    public Pago insertar(Pago pago) throws PersistenciaException {
        EntityManager em = emf.createEntityManager();
        Tramite tra = null;
        if (pago != null) {
            tra = pago.getTramite();
            tra.setPago(pago);
        }
        try {
            em.getTransaction().begin();

            em.persist(pago);
            em.merge(tra);

            em.getTransaction().commit();

            em.refresh(pago);
        } catch (EntityExistsException e) {
            Logger.getLogger(PersonasDAO.class.getName()).log(Level.SEVERE, null, e);
            throw new PersistenciaException("La persona a insertar ya existe");
        } finally {
            em.close();
        }
        return pago;
    }

    /**
     * Busca y trae de la base de datos un pago que coincida con el id dado en
     * el parametro
     *
     * @param id id del pago a buscar
     * @return Pago encontrado en la base
     * @throws PersistenciaException Si la entidad no se encontro
     */
    @Override
    public Pago buscar(long id) throws PersistenciaException {
        EntityManager em = emf.createEntityManager();
        Pago pa = null;
        try {
            pa = em.find(Pago.class, id);
        } catch (EntityNotFoundException e) {
            throw new PersistenciaException("No se encontro el pago");
        }
        return pa;

    }

}
