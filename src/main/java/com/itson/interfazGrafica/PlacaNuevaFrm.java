/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.itson.interfazGrafica;

import com.itson.dominio.Automovil;
import com.itson.dominio.Licencia;
import com.itson.dominio.Persona;
import com.itson.dominio.Placa;
import com.itson.excepciones.PersistenciaException;
import com.itson.implementacion.AutomovilesDAO;
import com.itson.implementacion.PersonasDAO;
import com.itson.implementacion.PlacasDAO;
import com.itson.interfaz.IAutomovilesDAO;
import com.itson.interfaz.IPersonasDAO;
import com.itson.interfaz.IPlacasDAO;
import com.itson.recursos.clases.TextPrompt;
import com.itson.recursos.validadores.Validadores;
import java.util.Calendar;
import javax.persistence.EntityManagerFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Clase que representa la ventana para el registro de una placa para un auto nuevo.
 */
public class PlacaNuevaFrm extends javax.swing.JFrame {
    
    /**
     * EntityManagerFactory para la conexión a la base de datos.
     */
    private EntityManagerFactory emf;
    
    /**
     * DAO de automoviles.
     */
    private IAutomovilesDAO autoDao;
    
    /**
     * DAO de placas.
     */
    private IPlacasDAO plaDao;
    
    /**
     * DAO de personas.
     */
    private IPersonasDAO perDao;
    
    /**
     * Objeto para validar datos de entrada de la ventana.
     */
    private final Validadores validadores;
    
    /**
     * Automovil a ingresar.
     */
    private Automovil auto;
    
    /**
     * Persona a buscar en la base de datos.
     */
    private Persona per;
    
    /**
     * Licencia a la cual se atribuye la nueva la placa.
     */
    private Licencia lic;
    
    /**
     * Placa a ingresar.
     */
    private Placa pla;
    
    /**
     * Referencia a la clase anterior.
     */
    private SelectAutoFrm anterior;
    
    /**
     * Constructor de la clase PlacaNuevaFrm.
     * @param emf EntityManagerFactory para la conexión de la base de datos.
     * @param lic Licencia que será asociada a la placa.
     * @param saf Ventana SelectAutoFrm anterior.
     */
    public PlacaNuevaFrm(EntityManagerFactory emf, Licencia lic, SelectAutoFrm saf) {
        initComponents();
        TextPrompt texp = new TextPrompt("Formato: XXX-999", txfNumSerie);
        this.emf = emf;
        this.autoDao = new AutomovilesDAO(emf);
        this.plaDao = new PlacasDAO(emf);
        this.perDao = new PersonasDAO(emf);
        this.lic = lic;
        this.validadores = new Validadores();
        this.anterior = saf;
        gobIcon.setIcon(new ImageIcon("src/main/resources/LogoGob.png"));
    }
    
    /**
     * Método para guardar un nuevo automóvil en la base de datos.
     * @return Automovil objeto del automóvil guardado.
     */
    private Automovil guardarAutomovil(){
        String numSerie = txfNumSerie.getText();
        String marca = txfMarca.getText();
        String linea = txfLinea.getText();
        String color = txfColor.getText();
        String modelo = txfModelo.getText();
        
        if(numSerie.isEmpty() || marca.isEmpty() || linea.isEmpty() ||
                color.isEmpty() || modelo.isEmpty()){
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, complete todos los campos para continuar",
                    "Error: Campos vacíos",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        if(!validadores.validarPlaca(numSerie)){
            JOptionPane.showMessageDialog(
                    this, 
                    "Verifique el numero de serie e intente nuevamente",
                    "Error: Numero de serie inválido",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        per = perDao.buscar(lic.getPersona().getRFC());
        
        if(autoDao.buscar(numSerie)!=null){
            JOptionPane.showMessageDialog(
                    this, 
                    "El número de serie ingresado ya está\n"
                            + "registrado en el sistema.\n"
                            + "Por favor, verifique e intenten nuevamente",
                    "Advertencia: Numero de serie ya registrado",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }
        
        this.auto = new Automovil();
        auto.setNumSerie(numSerie);
        auto.setMarca(marca);
        auto.setLinea(linea);
        auto.setColor(color);
        auto.setModelo(modelo);
        auto.setPersona(per);
        
        try{
            autoDao.insertar(auto);
        } catch(PersistenciaException e){
            JOptionPane.showMessageDialog(
                    this, 
                    "No se pudo registrar el auto",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        auto = autoDao.buscar(numSerie);
        return auto;
    }
    
    /**
     * Método para guardar una nueva placa en la base de datos.
     */
    private void guardarPlacas(){
        
        auto = this.guardarAutomovil();
        
        if(auto == null){
            return;
        }
        
        Calendar fechaEmision = Calendar.getInstance();
        
        String numPlaca = plaDao.generarNumPlaca();
        
        pla = new Placa();
        pla.setNumPlaca(numPlaca);
        pla.setFechaEmision(fechaEmision);
        pla.setCosto(1500);
        pla.setAutomovil(auto);
        pla.setLicencia(lic);
        
        Placa placaNueva = null;
        
        try{
            placaNueva = plaDao.insertar(pla);
            JOptionPane.showMessageDialog(
                    this, 
                    "La placa para el auto "+auto.getNumSerie()+" ha sido "
                        +"\nregistrada con éxito\n"
                        +"Con el número '"+numPlaca+"'\n"
                        +"Y el número de folio '"+placaNueva.getId()+"'",
                    "Placa registrada",
                    JOptionPane.INFORMATION_MESSAGE
                            );
        } catch(PersistenciaException ex){
            JOptionPane.showMessageDialog(
                    this, 
                    "No se pudieron guardar las placas. \n"
                        + "Por favor, intente nuevamente",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        new InicioFrm(emf).setVisible(true);
        dispose();
    }
    
    /**
     * Método para regresar a la ventana anterior.
     */
    private void regresar() {
        BusquedaLicFrm blf = new BusquedaLicFrm(emf, new InicioFrm(emf));
        new SelectAutoFrm(blf, lic, emf).setVisible(true);
        this.dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblGobmx = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lblNuevo = new javax.swing.JLabel();
        lblVentana = new javax.swing.JLabel();
        lblNumSerie = new javax.swing.JLabel();
        lblMarca = new javax.swing.JLabel();
        lblLinea = new javax.swing.JLabel();
        lblColor = new javax.swing.JLabel();
        lblModelo = new javax.swing.JLabel();
        lblCosto = new javax.swing.JLabel();
        txfNumSerie = new javax.swing.JTextField();
        txfMarca = new javax.swing.JTextField();
        txfLinea = new javax.swing.JTextField();
        txfColor = new javax.swing.JTextField();
        txfModelo = new javax.swing.JTextField();
        txfCosto = new javax.swing.JTextField();
        btmRealizarTra = new javax.swing.JButton();
        btmRetroceder = new javax.swing.JButton();
        gobIcon = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(600, 400));

        jPanel1.setBackground(new java.awt.Color(11, 35, 30));
        jPanel1.setPreferredSize(new java.awt.Dimension(600, 400));

        lblGobmx.setFont(new java.awt.Font("Sitka Subheading", 1, 18)); // NOI18N
        lblGobmx.setForeground(new java.awt.Color(255, 255, 255));
        lblGobmx.setText("Gob.mx");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(19, 50, 43));

        lblNuevo.setFont(new java.awt.Font("Sitka Subheading", 1, 18)); // NOI18N
        lblNuevo.setForeground(new java.awt.Color(255, 255, 255));
        lblNuevo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNuevo.setText("Auto nuevo");

        lblVentana.setFont(new java.awt.Font("Sitka Subheading", 1, 18)); // NOI18N
        lblVentana.setForeground(new java.awt.Color(255, 255, 255));
        lblVentana.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblVentana.setText("Trámite de placas");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(lblVentana, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 300, Short.MAX_VALUE)
                .addComponent(lblNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblVentana, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(lblNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        lblNumSerie.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblNumSerie.setForeground(new java.awt.Color(0, 0, 0));
        lblNumSerie.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNumSerie.setText("Num. serie");
        lblNumSerie.setToolTipText("");

        lblMarca.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblMarca.setForeground(new java.awt.Color(0, 0, 0));
        lblMarca.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMarca.setText("Marca");
        lblMarca.setToolTipText("");

        lblLinea.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblLinea.setForeground(new java.awt.Color(0, 0, 0));
        lblLinea.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLinea.setText("Línea");
        lblLinea.setToolTipText("");

        lblColor.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblColor.setForeground(new java.awt.Color(0, 0, 0));
        lblColor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblColor.setText("Color");
        lblColor.setToolTipText("");

        lblModelo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblModelo.setForeground(new java.awt.Color(0, 0, 0));
        lblModelo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblModelo.setText("Modelo");
        lblModelo.setToolTipText("");

        lblCosto.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblCosto.setForeground(new java.awt.Color(0, 0, 0));
        lblCosto.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCosto.setText("Costo");
        lblCosto.setToolTipText("");

        txfNumSerie.setBackground(new java.awt.Color(255, 255, 255));
        txfNumSerie.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txfNumSerie.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txfNumSerie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfNumSerieActionPerformed(evt);
            }
        });

        txfMarca.setBackground(new java.awt.Color(255, 255, 255));
        txfMarca.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txfMarca.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txfLinea.setBackground(new java.awt.Color(255, 255, 255));
        txfLinea.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txfLinea.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txfColor.setBackground(new java.awt.Color(255, 255, 255));
        txfColor.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txfColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txfModelo.setBackground(new java.awt.Color(255, 255, 255));
        txfModelo.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txfModelo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txfCosto.setBackground(new java.awt.Color(204, 204, 204));
        txfCosto.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        txfCosto.setForeground(new java.awt.Color(0, 0, 0));
        txfCosto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfCosto.setText("$1,500");
        txfCosto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txfCosto.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txfCosto.setEnabled(false);
        txfCosto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfCostoActionPerformed(evt);
            }
        });

        btmRealizarTra.setBackground(new java.awt.Color(255, 255, 255));
        btmRealizarTra.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btmRealizarTra.setForeground(new java.awt.Color(157, 36, 73));
        btmRealizarTra.setText("Realizar tramite");
        btmRealizarTra.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 3));
        btmRealizarTra.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btmRealizarTra.setFocusable(false);
        btmRealizarTra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmRealizarTraActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txfCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(112, 112, 112)
                        .addComponent(btmRealizarTra, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(lblModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txfModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(lblColor, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txfColor, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(lblLinea, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txfLinea, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(lblMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txfMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(lblNumSerie, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txfNumSerie, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(lblNumSerie, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txfNumSerie, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txfMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblLinea, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txfLinea, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblColor))
                    .addComponent(txfColor, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblModelo)
                    .addComponent(txfModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblCosto)
                            .addComponent(txfCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btmRealizarTra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 31, Short.MAX_VALUE))
        );

        btmRetroceder.setBackground(new java.awt.Color(255, 255, 255));
        btmRetroceder.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btmRetroceder.setForeground(new java.awt.Color(157, 36, 73));
        btmRetroceder.setText("Regresar");
        btmRetroceder.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 3));
        btmRetroceder.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btmRetroceder.setFocusable(false);
        btmRetroceder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmRetrocederActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(gobIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblGobmx))
                    .addComponent(btmRetroceder, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblGobmx)
                    .addComponent(gobIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btmRetroceder, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txfCostoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfCostoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfCostoActionPerformed

    /**
     * Método de acción del botón "Realizar trámite".
     * @param evt Evento de acción.
     */
    private void btmRealizarTraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmRealizarTraActionPerformed
        this.guardarPlacas();
    }//GEN-LAST:event_btmRealizarTraActionPerformed

    /**
     * Método de acción del botón "Regresar".
     * @param evt Evento de acción.
     */
    private void btmRetrocederActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmRetrocederActionPerformed
        // TODO add your handling code here:
        this.regresar();
    }//GEN-LAST:event_btmRetrocederActionPerformed

    private void txfNumSerieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfNumSerieActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfNumSerieActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btmRealizarTra;
    private javax.swing.JButton btmRetroceder;
    private javax.swing.JLabel gobIcon;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblColor;
    private javax.swing.JLabel lblCosto;
    private javax.swing.JLabel lblGobmx;
    private javax.swing.JLabel lblLinea;
    private javax.swing.JLabel lblMarca;
    private javax.swing.JLabel lblModelo;
    private javax.swing.JLabel lblNuevo;
    private javax.swing.JLabel lblNumSerie;
    private javax.swing.JLabel lblVentana;
    private javax.swing.JTextField txfColor;
    private javax.swing.JTextField txfCosto;
    private javax.swing.JTextField txfLinea;
    private javax.swing.JTextField txfMarca;
    private javax.swing.JTextField txfModelo;
    private javax.swing.JTextField txfNumSerie;
    // End of variables declaration//GEN-END:variables
}
