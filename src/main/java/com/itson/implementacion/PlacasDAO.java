/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.implementacion;

import com.itson.dominio.Automovil;
import com.itson.dominio.Licencia;
import com.itson.dominio.Persona;
import com.itson.dominio.Placa;
import com.itson.excepciones.PersistenciaException;
import com.itson.interfaz.IPlacasDAO;
import com.itson.recursos.clases.Paginado;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

/**
 * Clase de acceso a datos de placas
 *
 * @author Sebastian
 */
public class PlacasDAO implements IPlacasDAO {

    /**
     * Generador de entitymanager para interactuar con la base de datos
     */
    private final EntityManagerFactory emf;

    /**
     * Constructor de la clase PlacasDAO que recibe una instancia de
     * EntityManagerFactory.
     *
     * @param emf La instancia de EntityManagerFactory a utilizar.
     */
    public PlacasDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * Método que inserta una nueva placa, dada por el parámetro, en la base de
     * datos.
     *
     * @param placa La placa a insertar.
     * @return La placa insertada.
     * @throws PersistenciaException Si ocurre un error durante la inserción.
     */
    @Override
    public Placa insertar(Placa placa) throws PersistenciaException {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Automovil auto = placa.getAutomovil();

        if (auto != null) {
            if (auto.getPlacas() != null) {
                auto.getPlacas().add(placa);
            } else {
                auto.setPlacas(Arrays.asList(placa));
            }
        }

        em.persist(placa);
        em.merge(auto);

        em.getTransaction().commit();

        em.close();

        return placa;
    }

    /**
     * Busca en la base de datos una placa con el mismo numero de placa dado en
     * el parametro
     *
     * @param numPlaca Numero de placa a buscar
     * @return Placa encontrada
     */
    @Override
    public Placa buscarPorNumPlaca(String numPlaca) {
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Placa> criteria = cb.createQuery(Placa.class);
        Root<Placa> root = criteria.from(Placa.class);

        criteria = criteria.select(root).where(
                cb.like(root.get("numPlaca"), numPlaca)
        );

        TypedQuery<Placa> query = em.createQuery(criteria);

        List<Placa> placas = query.getResultList();
        if (!placas.isEmpty()) {
            return placas.get(0);
        } else {
            return null;
        }
    }

    /**
     * Método que busca placas en la base de datos por el número de serie dado
     * por el parámetro.
     *
     * @param numSerie El número de serie a buscar.
     * @return Una lista de placas que coinciden con el número de serie.
     */
    @Override
    public List<Placa> buscar(String numSerie) {
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
            return autos.get(0).getPlacas();
        } else {
            return null;
        }
    }

    /**
     * Método que busca placas en la base de datos por RFC de persona dada por
     * el parámetro.
     *
     * @param rfc El RFC de la persona a buscar.
     * @return Una lista de placas asociadas a la persona con el RFC
     * especificado.
     */
    @Override
    public List<Placa> buscarPorRFC(String rfc) {
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Placa> criteria = cb.createQuery(Placa.class);
        Root<Placa> rootPlaca = criteria.from(Placa.class);
        Join<Placa, Licencia> joinLicencia = rootPlaca.join("licencia");
        Join<Licencia, Persona> joinPersona = joinLicencia.join("persona");

        criteria = criteria.select(rootPlaca).where(
                cb.equal(joinPersona.get("RFC"), rfc)
        );

        TypedQuery<Placa> query = em.createQuery(criteria);

        List<Placa> placas = query.getResultList();
        if (!placas.isEmpty()) {
            return placas;
        } else {
            return null;
        }
    }

    /**
     * Método que busca placas en la base de datos por RFC de persona dada por
     * el parametro, ademas de utilizar una configuracion de paginado
     *
     * @param rfc RFC de la persona a buscar
     * @param pag Configuracion de paginado
     * @return Lista de placas
     */
    @Override
    public List<Placa> buscarPorRFC(String rfc, Paginado pag) {
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Placa> criteria = cb.createQuery(Placa.class);
        Root<Placa> rootPlaca = criteria.from(Placa.class);
        Join<Placa, Licencia> joinLicencia = rootPlaca.join("licencia");
        Join<Licencia, Persona> joinPersona = joinLicencia.join("persona");

        criteria = criteria.select(rootPlaca).where(
                cb.equal(joinPersona.get("RFC"), rfc)
        );

        TypedQuery<Placa> query = em.createQuery(criteria);

        query.setMaxResults(pag.getLimit());
        query.setFirstResult(pag.getOffset());

        List<Placa> placas = query.getResultList();
        if (!placas.isEmpty()) {
            return placas;
        } else {
            return null;
        }
    }
    
    /**
     * Genera el numero de placa de 7 caracteres
     * @return Cadena de texto con el numero de placa
     */
    @Override
    public String generarNumPlaca() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        boolean placaRepetida = true;

        while (placaRepetida) {
            for (int i = 0; i < 3; i++) {
                char letra = (char) (random.nextInt(26) + 'A');
                sb.append(letra);
            }

            sb.append("-");

            for (int i = 0; i < 3; i++) {
                int num = random.nextInt(10);
                sb.append(num);
            }

            String numeroPlaca = sb.toString();

            Placa placaExist = this.buscarPorNumPlaca(numeroPlaca);
            if (placaExist == null) {
                placaRepetida = false;
            } else {
                sb.setLength(0);
            }

        }

        return sb.toString();
    }
    
    /**
     * Actualiza el numero de placa
     * @param numPlaca numero de placa
     * @param fechaRecepcion Fecha de recepcion de la placa
     * @return Placa actualizada
     */
    @Override
    public Placa actualizarPlacaAnterior(String numPlaca, Calendar fechaRecepcion) {
        EntityManager em = emf.createEntityManager();
        Placa placa = null;
        try {
            em.getTransaction().begin();
            placa = buscarPorNumPlaca(numPlaca);
            if (placa != null) {
                placa.setFechaRecepcion(fechaRecepcion);
                em.merge(placa);
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return placa;

    }

}
