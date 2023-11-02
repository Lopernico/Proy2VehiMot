package com.itson.interfaz;

import com.itson.dominio.Persona;
import com.itson.excepciones.PersistenciaException;
import com.itson.recursos.clases.Paginado;
import com.itson.recursos.clases.ParametrosBusqueda;
import com.itson.recursos.clases.ReporteTramitesDTO;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Sebastian
 */
public interface IPersonasDAO {

    /**
     * Inserta la persona dada en el parametro en la base de datos
     *
     * @param per Persona a insertar
     * @return Persona que se inserto
     * @throws com.itson.excepciones.PersistenciaException Si la entidad ya
     * existe
     */
    public Persona insertar(Persona per) throws PersistenciaException;

    /**
     * Regresa a una persona que coincida con el RFC dado en el parametro Si no
     * se encuentra a la persona regresa null
     *
     * @param RFC RFC de la persona
     * @return Persona encontrada
     */
    public Persona buscar(String RFC);

    /**
     * Busca personas con nombres similares a los dados en el parametro, regresa
     * null si no existe alguna persona con el nombre similar
     *
     * @param nombre nombre similar
     * @return Lista de personas con nombres similares
     */
    public List<Persona> buscarPorNombre(String nombre);

    /**
     * Busca a personas que tengan la misma fecha de nacimiento, regresa null si
     * no existe alguna persona con la misma fecha de nacimiento
     *
     * @param fechaNacimiento fecha a comparar
     * @return lista de personas con misma fecha de nacimiento
     */
    public List<Persona> buscar(Calendar fechaNacimiento);

    /**
     * Busca personas en la base de datos que contengan los parametros de
     * busqueda indicados, regresa null en caso de que no exista alguna persona
     * que cumpla con los parametros indicados
     *
     * @param parBus Parametros de busqueda
     * @return Lista de personas
     */
    public List<Persona> buscar(ParametrosBusqueda parBus);

    /**
     * Busca en la base de datos los tramites asociados a la persona en base a
     * los parametros especificados
     *
     * @param parBus Parametros de busqueda
     * @return Lista de tramites
     */
    public List<ReporteTramitesDTO> buscarTramites(ParametrosBusqueda parBus);

    /**
     * Inserta en la base de datos a 20 personas dadas en el parametro
     *
     * @param personas Lista de personas a insertar
     * @throws PersistenciaException En caso de que exista algun error al
     * insertar a las personas
     */
    public void insertar20Personas(List<Persona> personas) throws PersistenciaException;

    /**
     * Trae de la base de datos a todas las personas, con una configuracion de
     * paginado de por medio
     *
     * @param pag Configuracion de paginado
     * @return Lista de personas
     */
    public List<Persona> buscar(Paginado pag);
}
