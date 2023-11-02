/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.itson.interfaz;

import com.itson.dominio.Pago;
import com.itson.excepciones.PersistenciaException;

/**
 * Interfaz de acceso a datos de pagos
 *
 * @author Sebastian
 */
public interface IPagosDAO {

    /**
     * Inserta en la base de datos el pago dado en el parametro
     *
     * @param pago Pago a insertar
     * @return El pago que fue insertado
     * @throws PersistenciaException Si la entidad ya existe en la base de datos
     */
    public Pago insertar(Pago pago) throws PersistenciaException;

    /**
     * Busca y trae de la base de datos un pago que coincida con el id dado en
     * el parametro
     *
     * @param id id del pago a buscar
     * @return Pago encontrado en la base
     * @throws PersistenciaException Si la entidad no se encontro
     */
    public Pago buscar(long id) throws PersistenciaException;
}
