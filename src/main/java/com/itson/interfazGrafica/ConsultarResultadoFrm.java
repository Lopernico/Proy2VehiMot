/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.itson.interfazGrafica;

import com.itson.dominio.Licencia;
import com.itson.dominio.Persona;
import com.itson.dominio.Placa;
import com.itson.implementacion.LicenciasDAO;
import com.itson.implementacion.PlacasDAO;
import com.itson.interfaz.ILicenciasDAO;
import com.itson.interfaz.IPersonasDAO;
import com.itson.interfaz.IPlacasDAO;
import com.itson.recursos.clases.Paginado;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.swing.table.DefaultTableModel;

/**
 * Clase que representa la ventana que muestra los resultados de la
 * busqueda de trámites realizados por una persona.
 */
public class ConsultarResultadoFrm extends javax.swing.JFrame {

    /**
     * DAO para licencias.
     */
    private ILicenciasDAO licDao;
    
    /**
     * DAO para placas.
     */
    private IPlacasDAO plaDao;
    
    /**
     * Persona de la cual se consultan los resultados.
     */
    private Persona per;
    
    /**
     * Información de paginación para la tabla de licencias.
     */
    private Paginado pagLicencia;
    
    /**
     * Información de paginación para la tabla de placas.
     */
    private Paginado pagPlacas;
    
    /**
     * Lista de licencias encontradas.
     */
    private List<Licencia> licencias;
    
    /**
     * Lista de placas encontradas.
     */
    private List<Placa> placas;
    
    /**
     * Referencia a la ventana anterior.
     */
    private ConsultaFrm anterior;

    /**
     * Constructor de la clase ConsultarResultadoFrm
     * @param emf EntityManagerFactory para la conexión a la base de datos.
     * @param per Referencia de la persona seleccionada en la ventana anterior.
     * @param anterior Referencia a la ventana anterior.
     */
    public ConsultarResultadoFrm(EntityManagerFactory emf, Persona per, ConsultaFrm anterior) {
        initComponents();
        licDao = new LicenciasDAO(emf);
        plaDao = new PlacasDAO(emf);
        this.anterior = anterior;
        this.per = per;
        this.lblNombre.setText(concatNombre(per));
        buscar();
    }

    /**
     * Realiza la búsqueda de licencias y placas asociadas a la persona actual.
     */
    private void buscar() {
        pagLicencia = new Paginado().setLimit(11);
        licencias = licDao.buscar(per.getRFC(), pagLicencia);
        if (licencias != null) {
            cargarTablaLicencias();
        }
        pagPlacas = new Paginado().setLimit(11);
        placas = plaDao.buscarPorRFC(per.getRFC(), pagPlacas);
        if (placas != null) {
            cargarTablaPlacas();
        }
    }

    /**
     * Avanza a la siguiente página de la tabla de licencias.
     */
    private void avanzarPagLicencia() {
        pagLicencia.avanzarPag();
        licencias = licDao.buscar(per.getRFC(), pagLicencia);
        if (licencias != null) {
            cargarTablaLicencias();
        } else {
            pagLicencia.retrocederPag();
        }
    }

    /**
     * Retrocede a la página anterior de la tabla de licencias.
     */
    private void retrocederPagLicencia() {
        pagLicencia.retrocederPag();
        licencias = licDao.buscar(per.getRFC(), pagLicencia);
        if (licencias != null) {
            cargarTablaLicencias();
        } else {
            pagLicencia.avanzarPag();
        }
    }

    /**
     * Avanza a la siguiente página de la tabla de placas.
     */
    private void avanzarPagPlacas() {
        pagPlacas.avanzarPag();
        placas = plaDao.buscarPorRFC(per.getRFC(), pagPlacas);
        if (placas != null) {
            cargarTablaPlacas();
        } else {
            pagPlacas.retrocederPag();
        }
    }

    /**
     * Retrocede a la página anterior de la tabla de placas.
     */
    private void retrocederPagPlacas() {
        pagPlacas.retrocederPag();
        placas = plaDao.buscarPorRFC(per.getRFC(), pagPlacas);
        if (placas != null) {
            cargarTablaPlacas();
        } else {
            pagPlacas.avanzarPag();
        }
    }

    /**
     * Formatea una fecha en un formato específico.
     * @param fecha La fecha a formatear.
     * @return La fecha formateada en formato específico.
     */
    private String formatoFecha(Calendar fecha) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date nacimiento = fecha.getTime();

        return formatter.format(nacimiento);
    }

    /**
     * Concatena el nombre completo de una persona.
     * @param per La persona de la cual se desea obtener el nombre completo.
     * @return El nombre completo de la persona concatenado.
     */
    private String concatNombre(Persona per) {
        return per.getNombres() + " " + per.getApellidoP() + " " + per.getApellidoM();
    }

    /**
     * Carga los datos de las licencias en una tabla.
     */
    private void cargarTablaLicencias() {
        if (licencias.isEmpty()) {
            if (pagLicencia.getOffset() != 0) {
                this.pagLicencia.retrocederPag();
                return;
            }
        }
        DefaultTableModel modeloTabla = (DefaultTableModel) this.tLicencias.getModel();
        modeloTabla.setRowCount(0);
        for (Licencia lic : licencias) {
            Object[] fila = {formatoFecha(lic.getFechaEmision()), lic.getCosto(), lic.getTipoLicencia()};
            modeloTabla.addRow(fila);
        }
    }

    /**
     * Carga los datos de las placas en una tabla.
     */
    private void cargarTablaPlacas() {

        if (placas.isEmpty()) {
            if (pagPlacas.getOffset() != 0) {
                this.pagPlacas.retrocederPag();
                return;
            }
        }
        DefaultTableModel modeloTabla = (DefaultTableModel) this.tPlacas.getModel();
        modeloTabla.setRowCount(0);
        for (Placa pla : placas) {
            String fechaRecepcion = pla.getFechaRecepcion() != null ? formatoFecha(pla.getFechaRecepcion()) : "Placa activa";
            Object[] fila = {formatoFecha(pla.getFechaEmision()), pla.getCosto(), fechaRecepcion};
            modeloTabla.addRow(fila);
        }

    }

    /**
     * Regresa a la ventana anterior y cierra la ventana actual.
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
        lblNombre = new javax.swing.JLabel();
        lblInicio3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tLicencias = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tPlacas = new javax.swing.JTable();
        lblInicio = new javax.swing.JLabel();
        lblInicio1 = new javax.swing.JLabel();
        btmLicRetroceder = new javax.swing.JButton();
        btmLicAvanzar = new javax.swing.JButton();
        btmPlaRetroceder = new javax.swing.JButton();
        btmPlaAvanzar = new javax.swing.JButton();
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

        lblNombre.setFont(new java.awt.Font("Sitka Subheading", 1, 18)); // NOI18N
        lblNombre.setForeground(new java.awt.Color(255, 255, 255));

        lblInicio3.setText("Consulta");
        lblInicio3.setFont(new java.awt.Font("Sitka Subheading", 1, 18)); // NOI18N
        lblInicio3.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addComponent(lblInicio3)
                    .addContainerGap(495, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblInicio3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        tLicencias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fecha de emision", "Costo", "Tipo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tLicencias.setBackground(new java.awt.Color(255, 255, 255));
        tLicencias.setForeground(new java.awt.Color(0, 0, 0));
        tLicencias.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tLicenciasFocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(tLicencias);

        tPlacas.setBackground(new java.awt.Color(255, 255, 255));
        tPlacas.setForeground(new java.awt.Color(0, 0, 0));
        tPlacas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fecha de emision", "Costo", "Fecha expedicion"
            }
        ));
        jScrollPane2.setViewportView(tPlacas);

        lblInicio.setText("Placas");
        lblInicio.setFont(new java.awt.Font("Sitka Subheading", 1, 18)); // NOI18N
        lblInicio.setForeground(new java.awt.Color(0, 0, 0));

        lblInicio1.setText("Licencias");
        lblInicio1.setFont(new java.awt.Font("Sitka Subheading", 1, 18)); // NOI18N
        lblInicio1.setForeground(new java.awt.Color(0, 0, 0));

        btmLicRetroceder.setText("<");
        btmLicRetroceder.setBackground(new java.awt.Color(255, 255, 255));
        btmLicRetroceder.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 3));
        btmLicRetroceder.setForeground(new java.awt.Color(157, 36, 73));
        btmLicRetroceder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmLicRetrocederActionPerformed(evt);
            }
        });

        btmLicAvanzar.setText(">");
        btmLicAvanzar.setBackground(new java.awt.Color(255, 255, 255));
        btmLicAvanzar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 3));
        btmLicAvanzar.setForeground(new java.awt.Color(157, 36, 73));
        btmLicAvanzar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmLicAvanzarActionPerformed(evt);
            }
        });

        btmPlaRetroceder.setText("<");
        btmPlaRetroceder.setBackground(new java.awt.Color(255, 255, 255));
        btmPlaRetroceder.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 3));
        btmPlaRetroceder.setForeground(new java.awt.Color(157, 36, 73));
        btmPlaRetroceder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmPlaRetrocederActionPerformed(evt);
            }
        });

        btmPlaAvanzar.setText(">");
        btmPlaAvanzar.setBackground(new java.awt.Color(255, 255, 255));
        btmPlaAvanzar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 3));
        btmPlaAvanzar.setForeground(new java.awt.Color(157, 36, 73));
        btmPlaAvanzar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmPlaAvanzarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addComponent(lblInicio1)
                        .addGap(36, 36, 36)
                        .addComponent(btmLicRetroceder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btmLicAvanzar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblInicio)
                        .addGap(52, 52, 52)
                        .addComponent(btmPlaRetroceder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btmPlaAvanzar))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btmPlaRetroceder)
                        .addComponent(btmPlaAvanzar))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblInicio)
                        .addComponent(lblInicio1)
                        .addComponent(btmLicRetroceder)
                        .addComponent(btmLicAvanzar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
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
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(gobIcon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblGobmx))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(btmRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btmRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 400));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tLicenciasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tLicenciasFocusGained

    }//GEN-LAST:event_tLicenciasFocusGained

    /**
     * Maneja el evento de acción del botón retroceder de licencias.
     * @param evt El evento de acción.
     */
    private void btmLicRetrocederActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmLicRetrocederActionPerformed
        retrocederPagLicencia();
    }//GEN-LAST:event_btmLicRetrocederActionPerformed

    /**
     * Maneja el evento de acción del botón avanzar de licencias.
     * @param evt El evento de acción.
     */
    private void btmLicAvanzarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmLicAvanzarActionPerformed
        avanzarPagLicencia();
    }//GEN-LAST:event_btmLicAvanzarActionPerformed

    /**
     * Maneja el evento de acción del botón retroceder de placas.
     * @param evt El evento de acción.
     */
    private void btmPlaRetrocederActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmPlaRetrocederActionPerformed
        retrocederPagPlacas();
    }//GEN-LAST:event_btmPlaRetrocederActionPerformed

    /**
     * Maneja el evento de acción del botón avanzar de placas.
     * @param evt El evento de acción.
     */
    private void btmPlaAvanzarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmPlaAvanzarActionPerformed
        avanzarPagPlacas();
    }//GEN-LAST:event_btmPlaAvanzarActionPerformed

    /**
     * Maneja el evento de acción del botón regresar.
     * @param evt El evento de acción.
     */
    private void btmRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmRegresarActionPerformed
        regresar();
    }//GEN-LAST:event_btmRegresarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btmLicAvanzar;
    private javax.swing.JButton btmLicRetroceder;
    private javax.swing.JButton btmPlaAvanzar;
    private javax.swing.JButton btmPlaRetroceder;
    private javax.swing.JButton btmRegresar;
    private javax.swing.JLabel gobIcon;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblGobmx;
    private javax.swing.JLabel lblInicio;
    private javax.swing.JLabel lblInicio1;
    private javax.swing.JLabel lblInicio3;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JTable tLicencias;
    private javax.swing.JTable tPlacas;
    // End of variables declaration//GEN-END:variables
}
