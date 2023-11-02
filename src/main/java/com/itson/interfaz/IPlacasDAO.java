/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.itson.interfaz;

import com.itson.dominio.Placa;
import com.itson.excepciones.PersistenciaException;
import com.itson.recursos.clases.Paginado;
import java.util.Calendar;
import java.util.List;

/**
 * Interfaz de acceso a datos de placas
 * @author Sebastian
 */
public interface IPlacasDAO {

    /**
     * Método que inserta una nueva placa, dada por el parámetro, en la base de
     * datos.
     *
     * @param placa La placa a insertar.
     * @return La placa insertada.
     * @throws PersistenciaException Si ocurre un error durante la inserción.
     */
    public Placa insertar(Placa placa) throws PersistenciaException;

    /**
     * Busca en la base de datos una placa con el mismo numero de placa dado en
     * el parametro
     *
     * @param numPlaca Numero de placa a buscar
     * @return Placa encontrada
     */
    public Placa buscarPorNumPlaca(String numPlaca);

    /**
     * Método que busca placas en la base de datos por el número de serie dado
     * por el parámetro.
     *
     * @param numSerie El número de serie a buscar.
     * @return Una lista de placas que coinciden con el número de serie.
     */
    public List<Placa> buscar(String numSerie);

    /**
     * Método que busca placas en la base de datos por RFC de persona dada por
     * el parámetro.
     *
     * @param rfc El RFC de la persona a buscar.
     * @return Una lista de placas asociadas a la persona con el RFC
     * especificado.
     */
    public List<Placa> buscarPorRFC(String rfc);

    /**
     * Método que busca placas en la base de datos por RFC de persona dada por
     * el parametro, ademas de utilizar una configuracion de paginado
     *
     * @param rfc RFC de la persona a buscar
     * @param pag Configuracion de paginado
     * @return Lista de placas
     */
    public List<Placa> buscarPorRFC(String rfc, Paginado pag);

    /**
     * Genera el numero de placa de 7 caracteres
     *
     * @return Cadena de texto con el numero de placa
     */
    public String generarNumPlaca();

    /**
     * Actualiza el numero de placa
     *
     * @param numPlaca numero de placa
     * @param fechaRecepcion Fecha de recepcion de la placa
     * @return Placa actualizada
     */
    public Placa actualizarPlacaAnterior(String numPlaca, Calendar fechaRecepcion);

}
