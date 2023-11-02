/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.itson.interfaz;

import com.itson.dominio.Licencia;
import com.itson.excepciones.PersistenciaException;
import com.itson.recursos.clases.Paginado;
import java.util.List;

/**
 * Interfaz de acceso a datos de licencias
 * @author Sebastian
 */
public interface ILicenciasDAO {

    /**
     * Inserta la licencia dada por el parámetro en la base de datos.
     *
     * @param lic La licencia a insertar.
     * @return La licencia insertada.
     * @throws com.itson.excepciones.PersistenciaException En caso de que la
     * licencia no este asociada a una persona
     */
    public Licencia insertar(Licencia lic) throws PersistenciaException;

    /**
     * Busca las licencias asociadas a una persona en la base de datos.
     *
     * @param RFC El RFC de la persona.
     * @return Una lista de licencias asociadas a la persona, o null si no se
     * encuentran licencias.
     */
    public List<Licencia> buscar(String RFC);

    /**
     * Busca en la base de datos las licencias de una persona en base a su RFC,
     * incluyendo una configuracion de paginado
     *
     * @param RFC RFC de la persona asociada a las licencias
     * @param pag Configuracion del paginado
     * @return Lista de licencias
     */
    public List<Licencia> buscar(String RFC, Paginado pag);
}
