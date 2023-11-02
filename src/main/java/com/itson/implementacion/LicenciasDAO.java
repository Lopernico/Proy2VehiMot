/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.implementacion;

import com.itson.dominio.Licencia;
import com.itson.dominio.Persona;
import com.itson.excepciones.PersistenciaException;
import com.itson.interfaz.ILicenciasDAO;
import com.itson.recursos.clases.DuracionLicencia;
import com.itson.recursos.clases.Paginado;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

/**
 * Clase de acceso a datos para licencias
 * @author Sebastian
 */
public class LicenciasDAO implements ILicenciasDAO {
    
    /**
     * Generador de entitymanager para interactuar con la base de datos
     */
    private final EntityManagerFactory emf;
    /**
     * Logger de la clase LicenciasDAO
     */
    private static final Logger LOG = Logger.getLogger(LicenciasDAO.class.getName());

    /**
     * Constructor de la clase LicenciasDAO.
     *
     * @param emf El EntityManagerFactory utilizado para interactuar con la base
     * de datos.
     */
    public LicenciasDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * Inserta la licencia dada por el parámetro en la base de datos.
     *
     * @param lic La licencia a insertar.
     * @return La licencia insertada.
     * @throws com.itson.excepciones.PersistenciaException En caso de que la
     * licencia no este asociada a una persona
     */
    @Override
    public Licencia insertar(Licencia lic) throws PersistenciaException {
        EntityManager em = emf.createEntityManager();

        Calendar fechaMax = (Calendar) lic.getFechaExpedicion().clone();

        fechaMax.add(Calendar.MONTH, lic.getVigencia().getDuracionDias());

        lic.setFechaMax(fechaMax);

        Persona per = lic.getPersona();

        if (per != null) {

            em.getTransaction().begin();

            if (per.getLicencias() != null) {
                per.getLicencias().add(lic);
            } else {
                per.setLicencias(Arrays.asList(lic));
            }

            em.persist(lic);
            em.merge(per);
            em.getTransaction().commit();
            em.refresh(lic);
        } else {
            throw new PersistenceException("La licencia no cuenta con una persona asociada");
        }

        em.close();

        return lic;
    }

    /**
     * Busca las licencias asociadas a una persona en la base de datos.
     *
     * @param RFC El RFC de la persona.
     * @return Una lista de licencias asociadas a la persona, o null si no se
     * encuentran licencias.
     */
    @Override
    public List<Licencia> buscar(String RFC) {
        EntityManager em = emf.createEntityManager();

        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<Persona> criteria = builder.createQuery(Persona.class);
        Root<Persona> root = criteria.from(Persona.class);

        criteria = criteria.select(root).where(
                builder.like(root.get("RFC"), RFC)
        );

        TypedQuery<Persona> query = em.createQuery(criteria);

        List<Persona> personas = query.getResultList();
        if (!personas.isEmpty()) {
            return personas.get(0).getLicencias();
        } else {
            return null;
        }
    }

    /**
     * Busca en la base de datos las licencias de una persona en base a su RFC,
     * incluyendo una configuracion de paginado
     *
     * @param RFC RFC de la persona asociada a las licencias
     * @param pag Configuracion del paginado
     * @return Lista de licencias
     */
    @Override
    public List<Licencia> buscar(String RFC, Paginado pag) {
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Licencia> criteria = cb.createQuery(Licencia.class);
        Root<Licencia> rootPlaca = criteria.from(Licencia.class);
        Join<Licencia, Persona> joinLicencia = rootPlaca.join("persona");

        criteria = criteria.select(rootPlaca).where(
                cb.equal(joinLicencia.get("RFC"), RFC)
        );

        TypedQuery<Licencia> query = em.createQuery(criteria);

        query.setMaxResults(pag.getLimit());
        query.setFirstResult(pag.getOffset());

        List<Licencia> licencias = query.getResultList();
        if (!licencias.isEmpty()) {
            return licencias;
        } else {
            return null;
        }
    }

}
