/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.itson.baseaproyecto2;

import com.itson.interfazGrafica.BusquedaLicFrm;
import com.itson.interfazGrafica.InicioFrm;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Sebastian
 */
public class BaseAProyecto2 {

    public static void main(String[] args) {

        EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory("com.itson.BaseAProyecto2");
        new InicioFrm(managerFactory).setVisible(true);
   
    }
}
