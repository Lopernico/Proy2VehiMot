package com.itson.implementacion;

import com.itson.dominio.Persona;
import com.itson.excepciones.PersistenciaException;
import com.itson.interfaz.IPersonasDAO;
import com.itson.recursos.clases.Paginado;
import com.itson.recursos.clases.ParametrosBusqueda;
import com.itson.recursos.clases.ReporteTramitesDTO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.eclipse.persistence.exceptions.DatabaseException;

/**
 *
 * @author Sebastian
 */
public class PersonasDAO implements IPersonasDAO {

    /**
     * Generadora de entityManagers
     */
    private final EntityManagerFactory emf;
    /**
     * Logger de la clase PersonasDAO
     */
    private static final Logger LOG = Logger.getLogger(PersonasDAO.class.getName());

    /**
     * Constructor que recibe el EntityManagerFactory, lo que permite ser
     * dinamico con el archivo persitence.xml
     *
     * @param emf Generador de EntityManagers
     */
    public PersonasDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * Inserta la persona dada en el parametro en la base de datos
     *
     * @param per Persona a insertar
     * @return Persona que se inserto
     * @throws com.itson.excepciones.PersistenciaException Si la entidad ya
     * existe
     */
    @Override
    public Persona insertar(Persona per) throws PersistenciaException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            em.persist(per);

            em.getTransaction().commit();

            em.refresh(per);
        } catch (EntityExistsException e) {
            Logger.getLogger(PersonasDAO.class.getName()).log(Level.SEVERE, null, e);
            throw new PersistenciaException("La persona a insertar ya existe");
        } finally {
            em.close();

        }

        return per;
    }

    /**
     * Regresa a una persona que coincida con el RFC dado en el parametro Si no
     * se encuentra a la persona regresa null
     *
     * @param RFC RFC de la persona
     * @return Persona encontrada
     */
    @Override
    public Persona buscar(String RFC) {
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
            return personas.get(0);
        } else {
            return null;
        }
    }

    /**
     * Busca personas con nombres similares a los dados en el parametro, regresa
     * null si no existe alguna persona con el nombre similar
     *
     * @param nombre nombre similar
     * @return Lista de personas con nombres similares
     */
    @Override
    public List<Persona> buscarPorNombre(String nombre) {
        EntityManager em = emf.createEntityManager();

        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<Persona> criteria = builder.createQuery(Persona.class);
        Root<Persona> root = criteria.from(Persona.class);

        /*
        Concatena el nombre de la persona en una sola expresion
         */
        Expression<String> exN = builder.concat(root.<String>get("nombres"), " ");
        Expression<String> exAP = builder.concat(root.<String>get("apellidoP"), " ");
        Expression<String> expression1 = builder.concat(exN, exAP);
        Expression<String> exNombreCompleto = builder.concat(expression1, root.<String>get("apellidoM"));

        criteria = criteria.select(root).where(
                builder.like(exNombreCompleto, "%" + nombre + "%")
        );

        TypedQuery<Persona> query = em.createQuery(criteria);

        List<Persona> personas = query.getResultList();

        if (!personas.isEmpty()) {
            return personas;
        } else {
            return null;
        }
    }

    /**
     * Busca a personas que tengan la misma fecha de nacimiento, regresa null si
     * no existe alguna persona con la misma fecha de nacimiento
     *
     * @param fechaNacimiento fecha a comparar
     * @return lista de personas con misma fecha de nacimiento
     */
    @Override
    public List<Persona> buscar(Calendar fechaNacimiento) {
        EntityManager em = emf.createEntityManager();

        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<Persona> criteria = builder.createQuery(Persona.class);
        Root<Persona> root = criteria.from(Persona.class);

        criteria = criteria.select(root).where(
                builder.between(root.get("fechaNacimiento"), fechaNacimiento, fechaNacimiento)
        );

        TypedQuery<Persona> query = em.createQuery(criteria);

        List<Persona> personas = query.getResultList();

        if (!personas.isEmpty()) {
            return personas;
        } else {
            return null;
        }
    }

    /**
     * Busca personas en la base de datos que contengan los parametros de
     * busqueda indicados, regresa null en caso de que no exista alguna persona
     * que cumpla con los parametros indicados
     *
     * @param parBus Parametros de busqueda
     * @return Lista de personas
     */
    @Override
    public List<Persona> buscar(ParametrosBusqueda parBus) {
        EntityManager em = emf.createEntityManager();

        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<Persona> criteria = builder.createQuery(Persona.class);
        Root<Persona> root = criteria.from(Persona.class);
        List<Expression> expressions = new ArrayList<>();

        if (parBus.getRFC() != null) {
            Expression exp1 = builder.like(root.get("RFC"), parBus.getRFC());
            expressions.add(exp1);
        }

        if (parBus.getNombreSimilar() != null) {
            Expression<String> exN = builder.concat(root.<String>get("nombres"), " ");
            Expression<String> exAP = builder.concat(root.<String>get("apellidoP"), " ");
            Expression<String> expression1 = builder.concat(exN, exAP);
            Expression<String> exNombreCompleto = builder.concat(expression1, root.<String>get("apellidoM"));

            Expression exp2 = builder.like(exNombreCompleto, "%" + parBus.getNombreSimilar() + "%");

            expressions.add(exp2);
        }

        if (parBus.getFechaNacimiento() != null) {
            Expression exp3 = builder.between(root.get("fechaNacimiento"), parBus.getFechaNacimiento(), parBus.getFechaNacimiento());
            expressions.add(exp3);
        }
        criteria = criteria.select(root).where(
                builder.and(expressions.toArray(new Predicate[0]))
        );

        TypedQuery<Persona> query = em.createQuery(criteria);
        query.setMaxResults(parBus.getConfPag().getLimit());
        query.setFirstResult(parBus.getConfPag().getOffset());

        List<Persona> personas = query.getResultList();

        return personas;
    }

    /**
     * Busca en la base de datos los tramites asociados a la persona en base a
     * los parametros especificados
     *
     * @param parBus Parametros de busqueda
     * @return Lista de tramites
     */
    @Override
    public List<ReporteTramitesDTO> buscarTramites(ParametrosBusqueda parBus) {
        EntityManager em = emf.createEntityManager();
        boolean hayNombreSimilar = !parBus.getNombreSimilar().isBlank();
        boolean hayPeriodo = parBus.getPeriodo1() != null && parBus.getPeriodo2() != null;

        String nomSimilar = null;
        String periodo = null;

        String quer1 = "SELECT new com.itson.recursos.clases.ReporteTramitesDTO("
                + "l.fechaEmision, 'licencia',p.nombres,p.apellidoP,p.apellidoM,l.costo"
                + ") "
                + "FROM Persona p "
                + "INNER JOIN p.licencias l INNER JOIN Tramite t "
                + "ON l.id = t.id";

        String quer2 = "SELECT new com.itson.recursos.clases.ReporteTramitesDTO("
                + "pl.fechaEmision, 'placa',p.nombres,p.apellidoP,p.apellidoM, pl.costo"
                + ") "
                + "FROM Persona p "
                + "INNER JOIN p.licencias l INNER JOIN l.placas pl INNER JOIN Tramite t "
                + "ON pl.id = t.id";

        if (hayNombreSimilar || hayPeriodo) {
            quer1 = quer1.concat(" WHERE ");
            quer2 = quer2.concat(" WHERE ");
        }

        if (hayPeriodo) {
            periodo = " t.fechaEmision BETWEEN :fecha1 AND :fecha2 ";
            quer1 = quer1.concat(periodo);
            quer2 = quer2.concat(periodo);
        }

        if (hayNombreSimilar && hayPeriodo) {
            quer1 = quer1.concat(" AND ");
            quer2 = quer2.concat(" AND ");
        }

        if (hayNombreSimilar) {
            nomSimilar = " CONCAT(p.nombres,' ',p.apellidoP,' ',p.apellidoM) LIKE :nombre ";
            quer1 = quer1.concat(nomSimilar);
            quer2 = quer2.concat(nomSimilar);
        }

        Query query = em.createQuery(quer1);

        Query query2 = em.createQuery(quer2);

        if (hayPeriodo) {
            query.setParameter("fecha1", parBus.getPeriodo1());
            query.setParameter("fecha2", parBus.getPeriodo2());
            query2.setParameter("fecha1", parBus.getPeriodo1());
            query2.setParameter("fecha2", parBus.getPeriodo2());
        }

        if (hayNombreSimilar) {
            query.setParameter("nombre", "%" + parBus.getNombreSimilar() + "%");
            query2.setParameter("nombre", "%" + parBus.getNombreSimilar() + "%");
        }

        List<ReporteTramitesDTO> resultList = query.getResultList();
        List<ReporteTramitesDTO> resultList2 = query2.getResultList();

        resultList.addAll(resultList2);
        String eliminar = null;
        if (parBus.getTipo().equalsIgnoreCase("licencia")) {
            eliminar = "placa";
        } else if (parBus.getTipo().equalsIgnoreCase("placa")) {
            eliminar = "licencia";
        }

        for (Iterator<ReporteTramitesDTO> i = resultList.iterator(); i.hasNext() && eliminar != null;) {
            ReporteTramitesDTO siguiente = i.next();
            if (eliminar.equalsIgnoreCase(siguiente.getTipoTramite())) {
                i.remove();
            }
        }

        return resultList;
    }

    /**
     * Trae de la base de datos a todas las personas, con una configuracion de
     * paginado de por medio
     *
     * @param pag Configuracion de paginado
     * @return Lista de personas
     */
    @Override
    public List<Persona> buscar(Paginado pag) {
        EntityManager em = emf.createEntityManager();

        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<Persona> criteria = builder.createQuery(Persona.class);
        Root<Persona> root = criteria.from(Persona.class);
        TypedQuery<Persona> query = em.createQuery(criteria);
        query.setMaxResults(pag.getLimit());
        query.setFirstResult(pag.getOffset());

        List<Persona> personas = query.getResultList();
        return personas;
    }

    /**
     * Inserta en la base de datos a 20 personas dadas en el parametro
     *
     * @param personas Lista de personas a insertar
     * @throws PersistenciaException En caso de que exista algun error al
     * insertar a las personas
     */
    @Override
    public void insertar20Personas(List<Persona> personas) throws PersistenciaException {
        for (Persona per : personas) {
            try {
                this.insertar(per);
            } catch (PersistenciaException ex) {
                Logger.getLogger(PersonasDAO.class.getName()).log(Level.SEVERE, null, ex);
                throw new PersistenciaException("Error al insertar las personas");
            } catch (EntityExistsException e) {
                Logger.getLogger(PersonasDAO.class.getName()).log(Level.SEVERE, null, e);
                throw new PersistenciaException("Personas repetidas");
            }
        }
    }

}
