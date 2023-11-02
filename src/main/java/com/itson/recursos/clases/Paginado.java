/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.recursos.clases;

/**
 * Clase para manejar la paginación de resultados de una lista.
 * @author Sebastian
 */
public class Paginado {

    /**
     * Límite de resultados por página.
     */
    private int limit;
    
    /**
     * Desplazamiento de resultados.
     */
    private int offset;
    
    /**
     * Número de página actual.
     */
    private int pagina;
    
    /**
     * Constructor por defecto de la clase Paginado.
     * Inicializa el offset y la página en 0.
     */
    public Paginado() {
        offset = 0;
        pagina = 0;
    }
    
    /**
     * Obtiene el límite de resultados por página.
     * @return El límite de resultados por página.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Establece el límite de resultados por página.
     * @param limit El límite de resultados por página.
     * @return Una referencia a la instancia de Paginado para permitir el encadenamiento de métodos.
     */
    public Paginado setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Obtiene el desplazamiento de resultados.
     * @return El desplazamiento de resultados.
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Establece el desplazamiento de resultados.
     * @param offset El desplazamiento de resultados.
     * @return Una referencia a la instancia de Paginado para permitir el encadenamiento de métodos.
     */
    public Paginado setOffset(int offset) {
        this.offset = offset;
        return this;
    }
    
    /**
     * Calcula el desplazamiento de resultados en base al límite y la página actual.
     */
    private void calcOffset() {
        this.offset = (this.limit * this.pagina);
    }

    /**
     * Avanza a la siguiente página de resultados.
     */
    public void avanzarPag() {
        pagina++;
        calcOffset();
    }

    /**
     * Retrocede a la página anterior de resultados, si es posible.
     */
    public void retrocederPag() {
        if (this.pagina != 0) {
            this.pagina--;
            calcOffset();
        }
    }
    
}
