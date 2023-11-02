/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.implementacion;

import com.itson.dominio.Tramite;
import com.itson.excepciones.PersistenciaException;
import com.itson.interfaz.ITramitesDAO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;

/**
 * Clase de acceso a datos de tramites
 *
 * @author Sebastian
 */
public class TramitesDAO implements ITramitesDAO {

    /**
     * Generador de entitymanager para interactuar con la base de datos
     */
    private final EntityManagerFactory emf;

    /**
     * Constructor que recibe el EntityManagerFactory y lo establece en el
     * atributo
     *
     * @param emf EntityManagerFactory a establecer
     */
    public TramitesDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * Busca y trae de la base de datos el tramite que tenga el id dado en el
     * parametro, regresa null si no encuentra un tramite con id
     *
     * @param id id del tramite a buscar
     * @return Tramite encontrado
     * @throws PersistenciaException Si la entidad no se encontro
     */
    @Override
    public Tramite buscar(long id) throws PersistenciaException {
        EntityManager em = emf.createEntityManager();
        Tramite tra = null;
        try {
            tra = em.find(Tramite.class, id);
        } catch (EntityNotFoundException e) {
            throw new PersistenciaException("No se encontro el tramite");

        }
        return tra;
    }

}
