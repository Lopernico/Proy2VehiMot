/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.recursos.clases;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Clase que incluye el método para generar el reporte de trámites.
 * @author Sebastian
 */
public class GenerarReporte {
    
    /**
     * Genera un informe de reporte de trámites utilizando JasperReports.
     * @param lista Lista de objetos ReporteTramitesDTO que se utilizarán 
     * como datos para el informe.
     * @param motivos Si ocurre un error durante la generación o visualización 
     * del informe.
     */
    public static void imprimirReporte(List<ReporteTramitesDTO> lista,String motivos){
        try {
            JasperReport report = (JasperReport) JRLoader.loadObjectFromFile("src/main/java/com/itson/recursos/visuales/reporte.jasper");
            HashMap motivo = new HashMap();
            motivo.put("parametros", motivos);
            
            JasperPrint jasP = JasperFillManager.fillReport(report, motivo,new JRBeanCollectionDataSource(lista));
            JasperViewer jv = new JasperViewer(jasP,false);
            jv.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(GenerarReporte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
