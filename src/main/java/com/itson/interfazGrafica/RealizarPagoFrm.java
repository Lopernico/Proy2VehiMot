/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.itson.interfazGrafica;

import com.itson.dominio.Pago;
import com.itson.dominio.Tramite;
import com.itson.excepciones.PersistenciaException;
import com.itson.implementacion.PagosDAO;
import com.itson.implementacion.TramitesDAO;
import com.itson.interfaz.IPagosDAO;
import com.itson.interfaz.ITramitesDAO;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Clase que representa la ventana para realizar el pago de trámites.

 */
public class RealizarPagoFrm extends javax.swing.JFrame {

    /**
     * DAO de tramites.
     */
    private ITramitesDAO traDao;
    
    /**
     * DAO de pagos.
     */
    private IPagosDAO paDao;
    
    /**
     * Referencia a la ventana anterior.
     */
    private InicioFrm anterior;
    
    /**
     * Objeto Tramite a pagar.
     */
    private Tramite tra;
    
    /**
     * Constructor de la clase RealizarPagoFrm.
     * @param emf EntityManagerFactory para la conexión de la base de datos.
     * @param anterior Objeto InicioFrm para establecer la ventana anterior.
     */
    public RealizarPagoFrm(EntityManagerFactory emf, InicioFrm anterior) {
        initComponents();
        paDao = new PagosDAO(emf);
        traDao = new TramitesDAO(emf);
        this.anterior = anterior;
        this.btmRealizarPago.setEnabled(false);
        gobIcon.setIcon(new ImageIcon("src/main/resources/LogoGob.png"));
    }

    /**
     * Método para buscar un trámite en la base de datos.
     */
    private void buscar() {
        if(txtId.getText().isBlank()){
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, ingrese el folio para continuar",
                    "Error: Folio no ingresado",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        long id = -1;
        try {
            id = Integer.valueOf(txtId.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un folio valido"
            );
            return;
        }
        try {
            this.tra = traDao.buscar(id);
        } catch (PersistenciaException e) {
            mostrarMensajeErrorNoEncontrado();
            return;
        }
        if (tra != null) {
            cargarDatosTramite();
            validarEstado();
        } else {
            mostrarMensajeErrorNoEncontrado();
        }
        
    }

    /**
     * Método para cargar los datos de un trámite en los campos de texto 
     * de la interfaz gráfica.
     */
    private void cargarDatosTramite() {
        if (tra != null) {
            txtFolio.setText(tra.getId() + "");
            txtFecha.setText(formatoFecha(tra.getFechaEmision()));
            txtCosto.setText(tra.getCosto() + "");
            txtEstado.setText(getEstado());
        }
    }

    /**
     * Método para obtener el estado de un trámite (pagado o pendiente).
     * @return Estado del trámite.
     */
    private String getEstado() {
        if (tra.getPago() != null) {
            return "pagado";
        } else {
            return "pendiente";
        }
    }

    /**
     * Método para validar el estado de un trámite y habilitar o deshabilitar el botón de realizar pago.
     */
    private void validarEstado() {
        if (tra.getPago() != null) {
            btmRealizarPago.setEnabled(false);
        } else {
            btmRealizarPago.setEnabled(true);
        }
    }

    /**
     * Método para realizar un pago asociado a un trámite en la base de datos.
     */
    private void realizarPago() {
        Pago pago = new Pago();
        if (tra != null) {
            pago.setCantidad(tra.getCosto());
            pago.setFechaPago(Calendar.getInstance());
            pago.setTramite(tra);
            try {
                Pago pa = paDao.insertar(pago);
                JOptionPane.showMessageDialog(this, "Pago realizado con exito\nFolio de pago: " + pa.getId());
                regresar();
            } catch (PersistenciaException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        } else {
            mostrarMensajeErrorNoEncontrado();
        }
    }

    /**
     * Método para formatear una fecha en formato dd/MM/yyyy.
     * @param fecha Objeto Calendar con la fecha a formatear.
     * @return Fecha formateada en formato dd/MM/yyyy.
     */
    private String formatoFecha(Calendar fecha) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date nacimiento = fecha.getTime();

        return formatter.format(nacimiento);
    }

    /**
     * Método para mostrar un mensaje de error cuando no se encuentra un trámite en la base de datos.
     */
    private void mostrarMensajeErrorNoEncontrado() {
        JOptionPane.showMessageDialog(
                this,
                "Tramite no encontrada",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Método para regresar a la ventana anterior.
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
        txtId = new javax.swing.JTextField();
        btmBuscar = new javax.swing.JButton();
        txtFolio = new javax.swing.JTextField();
        lblFolio = new javax.swing.JLabel();
        lblFechaEmision = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        txtEstado = new javax.swing.JTextField();
        lblEstado = new javax.swing.JLabel();
        txtCosto = new javax.swing.JTextField();
        lblCosto = new javax.swing.JLabel();
        btmRealizarPago = new javax.swing.JButton();
        gobIcon = new javax.swing.JLabel();
        btmRegresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(600, 400));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(11, 35, 30));

        lblGobmx.setFont(new java.awt.Font("Sitka Subheading", 1, 18)); // NOI18N
        lblGobmx.setForeground(new java.awt.Color(255, 255, 255));
        lblGobmx.setText("Gob.mx");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(600, 300));

        jPanel3.setBackground(new java.awt.Color(19, 50, 43));

        lblInicio.setText("Tramitar licencia");
        lblInicio.setFont(new java.awt.Font("Sitka Subheading", 1, 18)); // NOI18N
        lblInicio.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addComponent(lblInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        lblRFCb.setText("Folio:");
        lblRFCb.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblRFCb.setForeground(new java.awt.Color(0, 0, 0));

        txtId.setBackground(new java.awt.Color(255, 255, 255));
        txtId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51), 2));

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

        txtFolio.setEditable(false);
        txtFolio.setBackground(new java.awt.Color(153, 153, 153));
        txtFolio.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51), 2));
        txtFolio.setForeground(new java.awt.Color(0, 0, 0));

        lblFolio.setText("Folio:");
        lblFolio.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblFolio.setForeground(new java.awt.Color(0, 0, 0));

        lblFechaEmision.setText("Fecha de emision:");
        lblFechaEmision.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblFechaEmision.setForeground(new java.awt.Color(0, 0, 0));

        txtFecha.setEditable(false);
        txtFecha.setBackground(new java.awt.Color(153, 153, 153));
        txtFecha.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51), 2));
        txtFecha.setForeground(new java.awt.Color(0, 0, 0));

        txtEstado.setEditable(false);
        txtEstado.setBackground(new java.awt.Color(153, 153, 153));
        txtEstado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51), 2));
        txtEstado.setForeground(new java.awt.Color(0, 0, 0));

        lblEstado.setText("Estado:");
        lblEstado.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblEstado.setForeground(new java.awt.Color(0, 0, 0));

        txtCosto.setEditable(false);
        txtCosto.setBackground(new java.awt.Color(153, 153, 153));
        txtCosto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51), 2));
        txtCosto.setForeground(new java.awt.Color(0, 0, 0));

        lblCosto.setText("Costo:");
        lblCosto.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblCosto.setForeground(new java.awt.Color(0, 0, 0));

        btmRealizarPago.setBackground(new java.awt.Color(255, 255, 255));
        btmRealizarPago.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btmRealizarPago.setForeground(new java.awt.Color(157, 36, 73));
        btmRealizarPago.setText("Realizar pago");
        btmRealizarPago.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 3));
        btmRealizarPago.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btmRealizarPago.setFocusable(false);
        btmRealizarPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmRealizarPagoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblCosto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblEstado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblFechaEmision)
                            .addComponent(lblFolio))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtFolio, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblRFCb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btmBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(111, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btmRealizarPago, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtId)
                        .addComponent(btmBuscar))
                    .addComponent(lblRFCb))
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFolio)
                    .addComponent(lblFolio))
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFecha)
                    .addComponent(lblFechaEmision))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEstado)
                    .addComponent(lblEstado)
                    .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCosto))
                .addGap(32, 32, 32)
                .addComponent(btmRealizarPago, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        btmRegresar.setBackground(new java.awt.Color(255, 255, 255));
        btmRegresar.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btmRegresar.setForeground(new java.awt.Color(157, 36, 73));
        btmRegresar.setText("Regresar");
        btmRegresar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 3));
        btmRegresar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btmRegresar.setFocusable(false);
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
                        .addGap(26, 26, 26)
                        .addComponent(btmRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addComponent(btmRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 400));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método de acción del botón para buscar el trámite.
     * @param evt Evento de acción.
     */
    private void btmBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmBuscarActionPerformed
        buscar();
    }//GEN-LAST:event_btmBuscarActionPerformed

    /**
     * Método de acción del botón "Realizar pago".
     * @param evt Evento de acción.
     */
    private void btmRealizarPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmRealizarPagoActionPerformed
        realizarPago();
    }//GEN-LAST:event_btmRealizarPagoActionPerformed

    /**
     * Método de acción del botón "Regresar".
     * @param evt Evento de acción.
     */
    private void btmRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmRegresarActionPerformed
        regresar();
    }//GEN-LAST:event_btmRegresarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btmBuscar;
    private javax.swing.JButton btmRealizarPago;
    private javax.swing.JButton btmRegresar;
    private javax.swing.JLabel gobIcon;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblCosto;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblFechaEmision;
    private javax.swing.JLabel lblFolio;
    private javax.swing.JLabel lblGobmx;
    private javax.swing.JLabel lblInicio;
    private javax.swing.JLabel lblRFCb;
    private javax.swing.JTextField txtCosto;
    private javax.swing.JTextField txtEstado;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtFolio;
    private javax.swing.JTextField txtId;
    // End of variables declaration//GEN-END:variables
}
