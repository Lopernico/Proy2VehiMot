/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.itson.interfaz;

import com.itson.dominio.Tramite;
import com.itson.excepciones.PersistenciaException;

/**
 * Interfaz de acceso a datos de tramites
 * @author Sebastian
 */
public interface ITramitesDAO {

    /**
     * Busca y trae de la base de datos el tramite que tenga el id dado en el
     * parametro, regresa null si no encuentra un tramite con id
     *
     * @param id id del tramite a buscar
     * @return Tramite encontrado
     * @throws PersistenciaException Si la entidad no se encontro
     */
    public Tramite buscar(long id) throws PersistenciaException;
}
