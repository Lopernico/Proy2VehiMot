/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.itson.interfaz;

import com.itson.dominio.Automovil;
import com.itson.dominio.Placa;
import com.itson.excepciones.PersistenciaException;

/**
 * Interfaz de acceso a datos de automoviles
 * @author Sebastian
 */
public interface IAutomovilesDAO {

    /**
     * Inserta un objeto Automovil en la base de datos.
     *
     * @param auto Objeto Automovil a insertar.
     * @return El objeto Automovil insertado en la base de datos
     * @throws PersistenciaException Si ocurre un error al interactuar con la
     * base de datos.
     */
    public Automovil insertar(Automovil auto) throws PersistenciaException;

    /**
     * Busca un Automovil en la base de datos por su número de serie.
     *
     * @param numSerie El número de serie del Automovil a buscar.
     * @return El objeto Automovil que coincide con el número de serie, o null
     * si no se encuentra.
     */
    public Automovil buscar(String numSerie);

    /**
     * Busca un Automovil en la base de datos por su número de placa.
     *
     * @param numPlaca El número de placa del Automovil a buscar.
     * @return El objeto Automovil que coincide con el número de placa, o null
     * si no se encuentra.
     */
    public Automovil buscarPorPlaca(String numPlaca);

}
