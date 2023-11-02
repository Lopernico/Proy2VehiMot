package com.itson.excepciones;

/**
 * Clase de excepcion en persistencia
 *
 * @author Sebastian
 */
public class PersistenciaException extends Exception {

    /**
     * Constructor de excepcion con mensaje
     *
     * @param message Mensaje de excepcion
     */
    public PersistenciaException(String message) {
        super(message);
    }

    /**
     * Obtener la causa de la excepcion
     *
     * @return throwable
     */
    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }

    /**
     * Obtiene el mensaje de la excepcion
     *
     * @return Mensaje de excepcion
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
