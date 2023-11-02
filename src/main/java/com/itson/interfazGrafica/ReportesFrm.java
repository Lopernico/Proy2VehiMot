/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.itson.interfazGrafica;

import com.itson.dominio.Persona;
import com.itson.implementacion.LicenciasDAO;
import com.itson.implementacion.PersonasDAO;
import com.itson.interfaz.ILicenciasDAO;
import com.itson.interfaz.IPersonasDAO;
import com.itson.recursos.clases.GenerarReporte;
import com.itson.recursos.clases.ParametrosBusqueda;
import com.itson.recursos.clases.ReporteTramitesDTO;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 * Clase que representa la ventana para generar reportes de trámites.
 */
public class ReportesFrm extends javax.swing.JFrame {

    /**
     * DAO de personas.
     */
    private IPersonasDAO perDao;
    
    /**
     * Parámetros para buscar la persona.
     */
    private ParametrosBusqueda parBus;
    
    /**
     * EntityManagerFactory para la conexión de la base de datos.
     */
    private EntityManagerFactory emf;
    
    /**
     * Lista que trae el reporte de trámites.
     */
    private List<ReporteTramitesDTO> reporte;
    
    /**
     * Referencia a la ventana anterior.
     */
    private InicioFrm anterior;

    /**
     * Constructor de la clase ReportesFrm.
     * @param emf EntityManagerFactory para la conexión de la base de datos.
     * @param anterior Ventana anterior.
     */
    public ReportesFrm(EntityManagerFactory emf, InicioFrm anterior) {
        initComponents();
        this.perDao = new PersonasDAO(emf);
        this.emf = emf;
        gobIcon.setIcon(new ImageIcon("src/main/resources/LogoGob.png"));
        this.anterior = anterior;
        this.btmGenerarPdf.setEnabled(false);
        this.dpPeriodoIn.getComponentDateTextField().setEditable(false);
        this.dpPeriodoFin.getComponentDateTextField().setEditable(false);

    }

    /**
     * Método privado para realizar la búsqueda de reportes.
     */
    private void buscar() {

        parBus = new ParametrosBusqueda();
        convertirFecha();
        parBus.setNombreSimilar(txtNombre.getText());
        parBus.setTipo(cbTipo.getSelectedItem().toString());

        reporte = perDao.buscarTramites(parBus);

        if (!reporte.isEmpty()) {
            cargarTabla();
        }
        
    }

    /**
     * Método privado para escribir una fecha en formato dd/MM/yyyy.
     * @param fecha Fecha a formatear.
     * @return Fecha en formato dd/MM/yyyy.
     */
    private String formatoFecha(Date fecha) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(fecha);
    }
    
    /**
     * Método privado para concatenar el nombre completo de una persona.
     * @param per Objeto ReporteTramitesDTO que contiene la información de una persona.
     * @return Nombre completo de la persona concatenado.
     */
    private String concatNombre(ReporteTramitesDTO per) {
        return per.getNombresSolicitante() + " " + per.getApellidoPSolicitante() + " " + per.getApellidoMSolicitante();
    }

    /**
     * Método privado para convertir las fechas seleccionadas en el 
     * componente de DatePicker.
     */
    private void convertirFecha() {
        if (dpPeriodoIn.getDate() != null) {
            LocalDate localDate = dpPeriodoIn.getDate();
            ZoneId zoneId = ZoneId.systemDefault();
            Date date = Date.from(localDate.atStartOfDay(zoneId).toInstant());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            parBus.setPeriodo1(calendar);

        }
        if (dpPeriodoFin.getDate() != null) {
            LocalDate localDate = dpPeriodoFin.getDate();
            ZoneId zoneId = ZoneId.systemDefault();
            Date date = Date.from(localDate.atStartOfDay(zoneId).toInstant());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            parBus.setPeriodo2(calendar);
        }
    }

    /**
     * Método privado para cargar los datos en la tabla de reportes.
     */
    private void cargarTabla() {
        DefaultTableModel modeloTabla = (DefaultTableModel) this.tReporte.getModel();
        modeloTabla.setRowCount(0);
        for (ReporteTramitesDTO rep : reporte) {
            Object[] fila = {formatoFecha(rep.getFechaEmision()), concatNombre(rep), rep.getTipoTramite(), rep.getCosto()};
            modeloTabla.addRow(fila);
        }
        
        this.btmGenerarPdf.setEnabled(true);
        
    }

    /**
     * Método privado para generar el motivo de búsqueda de reportes.
     * @return Motivo de búsqueda de reportes
     */
    private String generarMotivo() {
        boolean hayNombreSimilar = !parBus.getNombreSimilar().isBlank();
        boolean hayPeriodo = parBus.getPeriodo1() != null && parBus.getPeriodo2() != null;
        boolean isLicencia = parBus.getTipo().equalsIgnoreCase("licencia");
        boolean isPlaca = parBus.getTipo().equalsIgnoreCase("placa");

        StringBuffer motivo = new StringBuffer();

        if (hayNombreSimilar) {
            motivo.append("Nombres similares a: " + parBus.getNombreSimilar() + "\t");
        }
        if (hayPeriodo) {
            motivo.append("Periodo del " + formatoFecha(parBus.getPeriodo1().getTime()) + " al " + formatoFecha(parBus.getPeriodo2().getTime()) + "\t");
        }
        if (isLicencia) {
            motivo.append("Busqueda de licencias" + "\t");
        } else if (isPlaca) {
            motivo.append("Busqueda de placa" + "\t");
        } else {
            motivo.append("Todos los tipos de tramites");
        }
        System.out.println(motivo.toString());
        return motivo.toString();
    }

    /**
     * Método privado para regresar a la ventana anterior.
     */
    private void regresar() {
        this.anterior.setVisible(true);
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblGobmx = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lblInicio = new javax.swing.JLabel();
        lblRFCb = new javax.swing.JLabel();
        btmBuscar = new javax.swing.JButton();
        lblRFCr = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lblNombreS = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tReporte = new javax.swing.JTable();
        dpPeriodoIn = new com.github.lgooddatepicker.components.DatePicker();
        dpPeriodoFin = new com.github.lgooddatepicker.components.DatePicker();
        cbTipo = new javax.swing.JComboBox<>();
        btmGenerarPdf = new javax.swing.JButton();
        gobIcon = new javax.swing.JLabel();
        btmRegresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(600, 400));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(11, 35, 30));

        lblGobmx.setText("Gob.mx");
        lblGobmx.setFont(new java.awt.Font("Sitka Subheading", 1, 18)); // NOI18N
        lblGobmx.setForeground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(600, 300));

        jPanel3.setBackground(new java.awt.Color(19, 50, 43));

        lblInicio.setText("Reportes");
        lblInicio.setFont(new java.awt.Font("Sitka Subheading", 1, 18)); // NOI18N
        lblInicio.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(424, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblRFCb.setText("Tipo de tramite:");
        lblRFCb.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblRFCb.setForeground(new java.awt.Color(0, 0, 0));

        btmBuscar.setBackground(new java.awt.Color(255, 255, 255));
        btmBuscar.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btmBuscar.setForeground(new java.awt.Color(157, 36, 73));
        btmBuscar.setText("Buscar");
        btmBuscar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 3));
        btmBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btmBuscar.setFocusable(false);
        btmBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmBuscarActionPerformed(evt);
            }
        });

        lblRFCr.setText("Periodo:");
        lblRFCr.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblRFCr.setForeground(new java.awt.Color(0, 0, 0));

        txtNombre.setBackground(new java.awt.Color(255, 255, 255));
        txtNombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51), 2));
        txtNombre.setForeground(new java.awt.Color(0, 0, 0));

        lblNombreS.setText("Nombre similar:");
        lblNombreS.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblNombreS.setForeground(new java.awt.Color(0, 0, 0));

        tReporte.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "fechaEmision", "Nombre", "Tipo", "Costo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tReporte.setBackground(new java.awt.Color(255, 255, 255));
        tReporte.setForeground(new java.awt.Color(0, 0, 0));
        tReporte.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tReporteFocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(tReporte);

        cbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "todos", "licencia", "placa" }));

        btmGenerarPdf.setText("Generar PDF");
        btmGenerarPdf.setBackground(new java.awt.Color(255, 255, 255));
        btmGenerarPdf.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 3));
        btmGenerarPdf.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btmGenerarPdf.setFocusable(false);
        btmGenerarPdf.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btmGenerarPdf.setForeground(new java.awt.Color(157, 36, 73));
        btmGenerarPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmGenerarPdfActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblRFCb)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblNombreS)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblRFCr)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dpPeriodoIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dpPeriodoFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btmGenerarPdf, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(btmBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(28, 28, 28))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombreS)
                    .addComponent(lblRFCb)
                    .addComponent(txtNombre)
                    .addComponent(cbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRFCr)
                    .addComponent(dpPeriodoIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dpPeriodoFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btmBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btmGenerarPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)))
                .addGap(277, 277, 277))
        );

        btmRegresar.setText("Regresar");
        btmRegresar.setBackground(new java.awt.Color(255, 255, 255));
        btmRegresar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 3));
        btmRegresar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btmRegresar.setFocusable(false);
        btmRegresar.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btmRegresar.setForeground(new java.awt.Color(157, 36, 73));
        btmRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmRegresarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(gobIcon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblGobmx))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(btmRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(gobIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblGobmx)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btmRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 400));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método de acción del botón "Buscar".
     * @param evt Evento de acción.
     */
    private void btmBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmBuscarActionPerformed
        buscar();
    }//GEN-LAST:event_btmBuscarActionPerformed

    private void tReporteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tReporteFocusGained

    }//GEN-LAST:event_tReporteFocusGained

    /**
     * Método de acción del botón para generar el reporte en PDF.
     * @param evt Evento de acción.
     */
    private void btmGenerarPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmGenerarPdfActionPerformed
        GenerarReporte.imprimirReporte(reporte, generarMotivo());
    }//GEN-LAST:event_btmGenerarPdfActionPerformed

    /**
     * Método de acción para el botón "Regresar".
     * @param evt Evento de acción.
     */
    private void btmRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmRegresarActionPerformed
        regresar();
    }//GEN-LAST:event_btmRegresarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btmBuscar;
    private javax.swing.JButton btmGenerarPdf;
    private javax.swing.JButton btmRegresar;
    private javax.swing.JComboBox<String> cbTipo;
    private com.github.lgooddatepicker.components.DatePicker dpPeriodoFin;
    private com.github.lgooddatepicker.components.DatePicker dpPeriodoIn;
    private javax.swing.JLabel gobIcon;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblGobmx;
    private javax.swing.JLabel lblInicio;
    private javax.swing.JLabel lblNombreS;
    private javax.swing.JLabel lblRFCb;
    private javax.swing.JLabel lblRFCr;
    private javax.swing.JTable tReporte;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
