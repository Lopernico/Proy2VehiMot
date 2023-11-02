/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.itson.interfazGrafica;

import com.itson.dominio.Persona;
import com.itson.excepciones.PersistenciaException;
import com.itson.implementacion.PersonasDAO;
import com.itson.interfaz.IPersonasDAO;
import com.itson.recursos.clases.Paginado;
import com.itson.recursos.clases.VeintePersonas;
import javax.persistence.EntityManagerFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Clase que representa la ventana de inicio de la aplicación.
 */
public class InicioFrm extends javax.swing.JFrame {

    /**
     * EntityManagerFactory para la conexión a la base de datos.
     */
    private EntityManagerFactory emf;
    
    /**
     * Variable para indicar si se han insertado las personas.
     */
    private static boolean insertados = false;

    /**
     * Constructor de la clase InicioFrm.
     * @param emf Objeto EntityManagerFactory para gestionar la conexión a la base de datos.
     */
    public InicioFrm(EntityManagerFactory emf) {
        initComponents();
        this.emf = emf;
        
        this.validaPersonasInsertadas();
        
        gobIcon.setIcon(new ImageIcon("src/main/resources/LogoGob.png"));
    
    }

    /**
     * Método privado que valida que las personas ya hayan sido insertadas
     * en la base de datos.
     */
    private void validaPersonasInsertadas(){
        IPersonasDAO perDao = new PersonasDAO(emf);
        Paginado pag = new Paginado();
        pag.setLimit(2);
        pag.setOffset(2);

        try{
            perDao.buscar(pag).get(0);
            insertados = true;
        } catch(Exception ex){}
        
        this.btmConsultar.setEnabled(insertados);
        this.btmRealizarPago.setEnabled(insertados);
        this.btmReportes.setEnabled(insertados);
        this.btmTramitarLic.setEnabled(insertados);
        this.btmTramitarPla.setEnabled(insertados);
        this.btmVerPersonas.setEnabled(insertados);
    }
    
    /**
     * Método privado para insertar personas en la base de datos.
     * Si ya se han insertado personas previamente, muestra mensaje de error.
     * Si no, inserta personas en la base de datos y muestra mensaje de éxito.
     */
    private void insertarPersonas() {
        if (insertados) {
            mostrarMensajeFallo2OPersonas();
            return;
        } else {
            insertados = true;
            this.btmConsultar.setEnabled(true);
            this.btmRealizarPago.setEnabled(true);
            this.btmReportes.setEnabled(true);
            this.btmTramitarLic.setEnabled(true);
            this.btmTramitarPla.setEnabled(true);
            this.btmVerPersonas.setEnabled(true);
        }

        IPersonasDAO pers = new PersonasDAO(emf);
        VeintePersonas person = new VeintePersonas();
        try {
            pers.insertar20Personas(person.obtenerPersonas());
            mostrarMensajeExito20Personas();
        } catch (PersistenciaException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    /**
     * Método privado para mostrar un mensaje de éxito al insertar 20 personas en la base de datos.
     */
    private void mostrarMensajeExito20Personas() {
        JOptionPane.showMessageDialog(this, "Personas insertadas");

    }

    /**
     * Método privado para mostrar un mensaje de error al intentar insertar personas cuando ya se han insertado previamente.
     */
    private void mostrarMensajeFallo2OPersonas() {
        JOptionPane.showMessageDialog(this, "Ya se han insertado las personas");

    }

    /**
     * Método privado para abrir la ventana de Trámite de Licencia.
     */
    private void abrirTramiteLic() {
        new TramiteLicFrm(emf, this).setVisible(true);
        this.setVisible(false);
    }

    /**
     * Método privado para abrir la ventana de Consultas.
     */
    private void abrirConsultas() {
        new ConsultaFrm(emf, this).setVisible(true);
        this.setVisible(false);
    }

    /**
     * Método privado para abrir la ventana de Búsqueda de Placas.
     */
    private void abrirTramitePlacas() {
        new BusquedaLicFrm(emf, this).setVisible(true);
        this.setVisible(false);
    }

    /**
     * Método para abrir la ventana de reportes.
     */
    private void abrirReportes() {
        new ReportesFrm(emf, this).setVisible(true);
        this.setVisible(false);
    }

    /**
     * Método para abrir la ventana de personas.
     */
    private void abrirPersonas() {
        new PersonasFrm(emf, this).setVisible(true);
        this.setVisible(false);
    }
    
    /**
     * Método para abrir la ventana de pagos.
     */
    private void abrirPagos(){
        new RealizarPagoFrm(emf, this).setVisible(true);
        this.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblGobmx = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lblInicio = new javax.swing.JLabel();
        btmInsertPer = new javax.swing.JButton();
        btmTramitarLic = new javax.swing.JButton();
        btmConsultar = new javax.swing.JButton();
        btmTramitarPla = new javax.swing.JButton();
        btmReportes = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        btmVerPersonas = new javax.swing.JButton();
        gobIcon = new javax.swing.JLabel();
        btmRealizarPago = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(600, 400));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(11, 35, 30));

        lblGobmx.setFont(new java.awt.Font("Sitka Subheading", 1, 18)); // NOI18N
        lblGobmx.setForeground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(19, 50, 43));

        lblInicio.setFont(new java.awt.Font("Sitka Subheading", 1, 18)); // NOI18N
        lblInicio.setForeground(new java.awt.Color(255, 255, 255));
        lblInicio.setText("Inicio");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblInicio)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addComponent(lblInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btmInsertPer.setBackground(new java.awt.Color(157, 36, 73));
        btmInsertPer.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btmInsertPer.setForeground(new java.awt.Color(255, 255, 255));
        btmInsertPer.setText("Insertar personas");
        btmInsertPer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 5));
        btmInsertPer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btmInsertPer.setFocusable(false);
        btmInsertPer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmInsertPerActionPerformed(evt);
            }
        });

        btmTramitarLic.setBackground(new java.awt.Color(157, 36, 73));
        btmTramitarLic.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btmTramitarLic.setForeground(new java.awt.Color(255, 255, 255));
        btmTramitarLic.setText("Tramitar licencia");
        btmTramitarLic.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 5));
        btmTramitarLic.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btmTramitarLic.setFocusable(false);
        btmTramitarLic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmTramitarLicActionPerformed(evt);
            }
        });

        btmConsultar.setBackground(new java.awt.Color(157, 36, 73));
        btmConsultar.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btmConsultar.setForeground(new java.awt.Color(255, 255, 255));
        btmConsultar.setText("Consultar");
        btmConsultar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 5));
        btmConsultar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btmConsultar.setFocusable(false);
        btmConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmConsultarActionPerformed(evt);
            }
        });

        btmTramitarPla.setBackground(new java.awt.Color(157, 36, 73));
        btmTramitarPla.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btmTramitarPla.setForeground(new java.awt.Color(255, 255, 255));
        btmTramitarPla.setText("Tramitar placa");
        btmTramitarPla.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 5));
        btmTramitarPla.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btmTramitarPla.setFocusable(false);
        btmTramitarPla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmTramitarPlaActionPerformed(evt);
            }
        });

        btmReportes.setBackground(new java.awt.Color(157, 36, 73));
        btmReportes.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btmReportes.setForeground(new java.awt.Color(255, 255, 255));
        btmReportes.setText("Reportes");
        btmReportes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 5));
        btmReportes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btmReportes.setFocusable(false);
        btmReportes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmReportesActionPerformed(evt);
            }
        });

        btmVerPersonas.setBackground(new java.awt.Color(157, 36, 73));
        btmVerPersonas.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btmVerPersonas.setForeground(new java.awt.Color(255, 255, 255));
        btmVerPersonas.setText("Consultar personas");
        btmVerPersonas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 5));
        btmVerPersonas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btmVerPersonas.setFocusable(false);
        btmVerPersonas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmVerPersonasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btmInsertPer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addComponent(btmReportes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btmConsultar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btmTramitarPla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btmTramitarLic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btmVerPersonas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(402, 402, 402))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btmTramitarLic, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btmTramitarPla, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btmConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btmReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btmInsertPer, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btmVerPersonas, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        gobIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/LogoGob.png"))); // NOI18N

        btmRealizarPago.setBackground(new java.awt.Color(157, 36, 73));
        btmRealizarPago.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btmRealizarPago.setForeground(new java.awt.Color(255, 255, 255));
        btmRealizarPago.setText("Realizar pago");
        btmRealizarPago.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 5));
        btmRealizarPago.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btmRealizarPago.setFocusable(false);
        btmRealizarPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmRealizarPagoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(gobIcon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblGobmx)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btmRealizarPago)
                .addContainerGap())
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
                .addComponent(btmRealizarPago)
                .addGap(13, 13, 13))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 400));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método que se ejecuta al hacer clic en el botón de "Tramitar Licencia".
     * Abre la ventana de trámite de licencia.
     * @param evt Objeto ActionEvent que representa el evento de clic en el botón.
     */
    private void btmTramitarLicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmTramitarLicActionPerformed
        abrirTramiteLic();
    }//GEN-LAST:event_btmTramitarLicActionPerformed

    /**
     * Método que se ejecuta al hacer clic en el botón de "Consultar".
     * Abre la ventana de consultas.
     * @param evt Objeto ActionEvent que representa el evento de clic en el botón.
     */
    private void btmConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmConsultarActionPerformed
        abrirConsultas();
    }//GEN-LAST:event_btmConsultarActionPerformed

    /**
     * Método que se ejecuta al hacer clic en el botón de "Tramitar Placas".
     * Abre la ventana de trámite de placas.
     * @param evt Objeto ActionEvent que representa el evento de clic en el botón.
     */
    private void btmTramitarPlaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmTramitarPlaActionPerformed
        abrirTramitePlacas();
    }//GEN-LAST:event_btmTramitarPlaActionPerformed

    /**
     * Método que se ejecuta al hacer clic en el botón de "Reportes".
     * Abre la ventana de reportes.
     * @param evt Objeto ActionEvent que representa el evento de clic en el botón.
     */
    private void btmReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmReportesActionPerformed
        abrirReportes();
    }//GEN-LAST:event_btmReportesActionPerformed

    /**
     * Método que se ejecuta al hacer clic en el botón de "Insertar Personas".
     * Inserta personas en la base de datos.
     * @param evt Objeto ActionEvent que representa el evento de clic en el botón.
     */
    private void btmInsertPerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmInsertPerActionPerformed
        insertarPersonas();
    }//GEN-LAST:event_btmInsertPerActionPerformed

    /**
     * Método que se ejecuta al hacer clic en el botón de "Ver Personas".
     * Abre la ventana de personas.
     * @param evt Objeto ActionEvent que representa el evento de clic en el botón.
     */
    private void btmVerPersonasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmVerPersonasActionPerformed
        abrirPersonas();
    }//GEN-LAST:event_btmVerPersonasActionPerformed

    /**
     * Método que se ejecuta al hacer clic en el botón de "Realizar Pago".
     * Abre la ventana de pagos.
     * @param evt Objeto ActionEvent que representa el evento de clic en el botón.
     */
    private void btmRealizarPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmRealizarPagoActionPerformed
        abrirPagos();
    }//GEN-LAST:event_btmRealizarPagoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btmConsultar;
    private javax.swing.JButton btmInsertPer;
    private javax.swing.JButton btmRealizarPago;
    private javax.swing.JButton btmReportes;
    private javax.swing.JButton btmTramitarLic;
    private javax.swing.JButton btmTramitarPla;
    private javax.swing.JButton btmVerPersonas;
    private javax.swing.JLabel gobIcon;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblGobmx;
    private javax.swing.JLabel lblInicio;
    // End of variables declaration//GEN-END:variables
}
