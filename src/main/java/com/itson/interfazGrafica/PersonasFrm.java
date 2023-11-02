/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.itson.interfazGrafica;

import com.itson.dominio.Persona;
import com.itson.implementacion.PersonasDAO;
import com.itson.interfaz.IPersonasDAO;
import com.itson.recursos.clases.Paginado;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.swing.table.DefaultTableModel;

/**
 * Clase que representa la ventana que muestra las personas a insertar en el sistema.
 */
public class PersonasFrm extends javax.swing.JFrame {
    
    /**
     * DAO para personas.
     */
    private IPersonasDAO perDao;
    
    /**
     * Lista de personas a insertar.
     */
    private List<Persona> personas;
    
    /**
     * Información de paginado para la tabla de personas.
     */
    private Paginado pag;
    
    /**
     * Referencia a la ventana anterior.
     */
    private InicioFrm anterior;
    
    /**
     * Constructor de la clase PersonasFrm.
     * @param emf EntityManagerFactory para conexión a la base de datos.
     * @param anterior Referencia a la ventana anterior.
     */
    public PersonasFrm(EntityManagerFactory emf, InicioFrm anterior) {
        initComponents();
        this.anterior = anterior;
        perDao = new PersonasDAO(emf);
        buscar();
    }
    
    /**
     * Método privado para realizar una búsqueda de personas en la base de datos y cargar los resultados en la tabla.
     */
    private void buscar() {
        pag = new Paginado().setLimit(10);
        personas = perDao.buscar(pag);
        if (personas != null) {
            cargarTabla();
        }
    }
    
    /**
     * Método privado para avanzar a la siguiente página de resultados en la búsqueda de personas.
     */
    private void avanzarPag() {
        pag.avanzarPag();
        personas = perDao.buscar(pag);
        if (personas != null) {
            cargarTabla();
        } else {
            pag.retrocederPag();
        }
    }
    
    /**
     * Método privado para retroceder a la página anterior de resultados en la búsqueda de personas.
     */
    private void retrocederPag() {
        pag.retrocederPag();
        personas = perDao.buscar(pag);
        if (personas != null) {
            cargarTabla();
        } else {
            pag.avanzarPag();
        }
    }
    
    /**
     * Método privado para formatear una fecha en el formato "dd/MM/yyyy".
     * @param fecha Objeto Calendar que representa la fecha a formatear.
     * @return Cadena de caracteres que representa la fecha formateada.
     */
    private String formatoFecha(Calendar fecha) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date nacimiento = fecha.getTime();
        
        return formatter.format(nacimiento);
    }
    
    /**
     * Método privado para concatenar el nombre completo de una persona en formato "nombres apellidoP apellidoM".
     * 
     * @param per Objeto Persona del cual se obtendrán los nombres y apellidos.
     * @return Cadena de caracteres que representa el nombre completo de la persona concatenado.
     */
    private String concatNombre(Persona per) {
        return per.getNombres() + " " + per.getApellidoP() + " " + per.getApellidoM();
    }
    
    /**
     * Método privado para cargar los datos de las personas en una tabla.
     */
    private void cargarTabla() {
        if (personas.isEmpty()) {
            if (pag.getOffset() != 0) {
                this.pag.retrocederPag();
                return;
            }
        }
        DefaultTableModel modeloTabla = (DefaultTableModel) this.tPersonas.getModel();
        modeloTabla.setRowCount(0);
        for (Persona per : personas) {
            Object[] fila = {per.getRFC(), concatNombre(per), formatoFecha(per.getFechaNacimiento())};
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
        lblInicio3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tPersonas = new javax.swing.JTable();
        lblInicio1 = new javax.swing.JLabel();
        btmLicRetroceder = new javax.swing.JButton();
        btmLicAvanzar = new javax.swing.JButton();
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

        lblInicio3.setFont(new java.awt.Font("Sitka Subheading", 1, 18)); // NOI18N
        lblInicio3.setForeground(new java.awt.Color(255, 255, 255));
        lblInicio3.setText("Consulta");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(lblInicio3)
                .addContainerGap(495, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblInicio3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tPersonas.setBackground(new java.awt.Color(255, 255, 255));
        tPersonas.setForeground(new java.awt.Color(0, 0, 0));
        tPersonas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "RFC", "Nombre", "Nacimiento"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tPersonas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tPersonasFocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(tPersonas);

        lblInicio1.setFont(new java.awt.Font("Sitka Subheading", 1, 18)); // NOI18N
        lblInicio1.setForeground(new java.awt.Color(0, 0, 0));
        lblInicio1.setText("Personas");

        btmLicRetroceder.setBackground(new java.awt.Color(255, 255, 255));
        btmLicRetroceder.setForeground(new java.awt.Color(157, 36, 73));
        btmLicRetroceder.setText("<");
        btmLicRetroceder.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 3));
        btmLicRetroceder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmLicRetrocederActionPerformed(evt);
            }
        });

        btmLicAvanzar.setBackground(new java.awt.Color(255, 255, 255));
        btmLicAvanzar.setForeground(new java.awt.Color(157, 36, 73));
        btmLicAvanzar.setText(">");
        btmLicAvanzar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 3));
        btmLicAvanzar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmLicAvanzarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addComponent(lblInicio1)
                        .addGap(36, 36, 36)
                        .addComponent(btmLicRetroceder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btmLicAvanzar))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblInicio1)
                    .addComponent(btmLicRetroceder)
                    .addComponent(btmLicAvanzar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addGap(22, 22, 22)
                        .addComponent(btmRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addComponent(btmRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 400));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Maneja el evento de acción del botón "Regresar".
     * @param evt Evento de acción del botón.
     */
    private void btmRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmRegresarActionPerformed
        regresar();
    }//GEN-LAST:event_btmRegresarActionPerformed

    /**
     * Maneja el evento de acción del botón para avanzar página.
     * @param evt Evento de acción del botón.
     */
    private void btmLicAvanzarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmLicAvanzarActionPerformed
        avanzarPag();
    }//GEN-LAST:event_btmLicAvanzarActionPerformed

    /**
     * Maneja el evento de acción del botón para retroceder página.
     * @param evt Evento de acción del botón.
     */
    private void btmLicRetrocederActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmLicRetrocederActionPerformed
        retrocederPag();
    }//GEN-LAST:event_btmLicRetrocederActionPerformed

    private void tPersonasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tPersonasFocusGained

    }//GEN-LAST:event_tPersonasFocusGained

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btmLicAvanzar;
    private javax.swing.JButton btmLicRetroceder;
    private javax.swing.JButton btmRegresar;
    private javax.swing.JLabel gobIcon;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblGobmx;
    private javax.swing.JLabel lblInicio1;
    private javax.swing.JLabel lblInicio3;
    private javax.swing.JTable tPersonas;
    // End of variables declaration//GEN-END:variables
}
