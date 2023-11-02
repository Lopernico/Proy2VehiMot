/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.implementacion;

import com.itson.dominio.Automovil;
import com.itson.excepciones.PersistenciaException;
import com.itson.interfaz.IAutomovilesDAO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Clase de acceso a datos para automoviles
 * @author Sebastian
 */
public class AutomovilesDAO implements IAutomovilesDAO{
    
    /**
     * Generador de entitymanager para interactuar con la base de datos
     */
    private final EntityManagerFactory emf;

    /**
     * Constructor de la clase AutomovilesDAO
     * @param emf instancia EntityManagerFactoy utilizada para 
     * interactuar con la base de datos.
     */
    public AutomovilesDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * Inserta un objeto Automovil en la base de datos.
     * @param auto Objeto Automovil a insertar.
     * @return El objeto Automovil insertado en la base de datos
     * @throws PersistenciaException  Si ocurre un error al interactuar
     * con la base de datos.
     */
    @Override
    public Automovil insertar(Automovil auto) throws PersistenciaException {
        EntityManager em=emf.createEntityManager();
        
        em.getTransaction().begin();

        em.persist(auto);

        em.getTransaction().commit();

        em.refresh(auto);

        em.close();

        return auto;
    }

    /**
     * Busca un Automovil en la base de datos por su número de serie.
     * @param numSerie El número de serie del Automovil a buscar.
     * @return El objeto Automovil que coincide con el número de serie, 
     * o null si no se encuentra.
     */
    @Override
    public Automovil buscar(String numSerie) {
        EntityManager em = emf.createEntityManager();

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Automovil> criteria = cb.createQuery(Automovil.class);
        Root<Automovil> root = criteria.from(Automovil.class);

        criteria = criteria.select(root).where(
                cb.like(root.get("numSerie"), numSerie)
        );

        TypedQuery<Automovil> query = em.createQuery(criteria);

        List<Automovil> autos = query.getResultList();
        if (!autos.isEmpty()) {
            return autos.get(0);
        } else {
            return null;
        }
    }

    /**
     * Busca un Automovil en la base de datos por su número de placa.
     *
     * @param numPlaca El número de placa del Automovil a buscar.
     * @return El objeto Automovil que coincide con el número de placa, o null si no se encuentra.
     */
    @Override
    public Automovil buscarPorPlaca(String numPlaca) {
        EntityManager em = emf.createEntityManager();
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        
        CriteriaQuery<Automovil> criteria = cb.createQuery(Automovil.class);
        Root<Automovil> root = criteria.from(Automovil.class);
        
        criteria = criteria.select(root).where(
                cb.equal(root.get("placas").get("numPlaca"), numPlaca)
        );
        
        TypedQuery<Automovil> query = em.createQuery(criteria);
        
        List<Automovil> autos = query.getResultList();
        if(!autos.isEmpty()){
            return autos.get(0);
        } else{
            return null;
        }
        
    }
    
    
    
}
