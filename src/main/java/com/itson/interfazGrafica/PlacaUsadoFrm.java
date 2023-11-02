/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.itson.interfazGrafica;

import com.itson.dominio.Automovil;
import com.itson.dominio.Licencia;
import com.itson.dominio.Placa;
import com.itson.excepciones.PersistenciaException;
import com.itson.implementacion.AutomovilesDAO;
import com.itson.implementacion.PlacasDAO;
import com.itson.interfaz.IAutomovilesDAO;
import com.itson.interfaz.IPlacasDAO;
import com.itson.recursos.clases.TextPrompt;
import com.itson.recursos.validadores.Validadores;
import java.util.Calendar;
import javax.persistence.EntityManagerFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Clase que representa la ventana para el registro de una nueva placa para
 * un auto usado.
 */
public class PlacaUsadoFrm extends javax.swing.JFrame {

    /**
     * EntityManagerFactory para la conexión de la base de datos.
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
     * Objeto para validar datos de entrada de la ventana.
     */
    private final Validadores validadores;
    
    /**
     * Automovil usado al que se asigna la nueva placa.
     */
    private Automovil auto;
    
    /**
     * Licencia relacionada al automovil.
     */
    private Licencia lic;
    
    /**
     * Referencia a la ventana anterior.
     */
    private SelectAutoFrm anterior;
    
    /**
     * Constructor de la clase PlacaUsadoFrm.
     * @param emf El EntityManagerFactory para la conexión a la base de datos.
     * @param lic La licencia asociada al trámite de registro de placas.
     * @param saf La ventana SelectAutoFrm que precede a esta ventana.
     */
    public PlacaUsadoFrm(EntityManagerFactory emf, Licencia lic, SelectAutoFrm saf) {
        initComponents();
        TextPrompt texp = new TextPrompt("Formato: XXX-999", txfNumPlaca);
        this.emf = emf;
        this.autoDao = new AutomovilesDAO(emf);
        this.plaDao = new PlacasDAO(emf);
        this.validadores = new Validadores();
        this.auto = null;
        this.lic = lic;
        this.anterior = saf;
        this.btnRealizarTramite.setEnabled(false);
        gobIcon.setIcon(new ImageIcon("src/main/resources/LogoGob.png"));
    }

    /**
     * Método que busca un automóvil por su número de placa.
     * @param numPlaca El número de placa a buscar.
     * @return El automóvil encontrado, o null si no se encuentra o hay errores.
     */
    private Automovil buscarAutoPorPlaca(String numPlaca){
        
        if(numPlaca.isEmpty()){
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, indique el número de placa para continuar",
                    "Error: Campo vacío",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }
        
        if(!validadores.validarPlaca(numPlaca)){
            JOptionPane.showMessageDialog(
                    this, 
                    "Verifique el numero de placa e intente nuevamente",
                    "Error: Numero de placa inválido",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        Placa placaBuscada = plaDao.buscarPorNumPlaca(numPlaca);
        
        if(placaBuscada == null){
            JOptionPane.showMessageDialog(
                    this,
                    "La placa no se encuentra registrada en el sistema.\n"
                            + "Por favor, ingrese nuevamente",
                    "Error: Placa no encontrada",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }
        
        if(placaBuscada.getFechaRecepcion() != null){
            JOptionPane.showMessageDialog(
                    this,
                    "La placa indicada es una placa antigua del vehículo.\n"
                            + "Por favor, ingrese la placa activa e intente nuevamente",
                    "Error: Placa antigua",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }
        
        auto = autoDao.buscarPorPlaca(numPlaca);
        
        if(auto == null){
            JOptionPane.showMessageDialog(
                this,
                "No se pudo encontrar el auto, intente nuevamente",
                "Error: Auto no encontrado",
                JOptionPane.ERROR_MESSAGE
            );
            return null;
        }
        
        txfNumSerie.setText(auto.getNumSerie());
        txfMarca.setText(auto.getMarca());
        txfLinea.setText(auto.getLinea());
        
        btnRealizarTramite.setEnabled(true);
        
        return auto;
    }
    
    /**
     * Método que guarda una nueva placa en la base de datos.
     */
    private void guardarNuevaPlaca(){
        String numPlaca = txfNumPlaca.getText();
        
        Calendar fechaEmision = Calendar.getInstance();
        
        String numPlacaNueva = plaDao.generarNumPlaca();
        
        Placa placaNueva = new Placa();
        placaNueva.setNumPlaca(numPlacaNueva);
        placaNueva.setFechaEmision(fechaEmision);
        placaNueva.setCosto(1000);
        placaNueva.setAutomovil(auto);
        placaNueva.setLicencia(lic);
        
        Placa placaFolio = null;
        
        try{
            plaDao.actualizarPlacaAnterior(numPlaca, fechaEmision);
            placaFolio = plaDao.insertar(placaNueva);
            JOptionPane.showMessageDialog(
                    this, 
                    "La placa para el auto "+auto.getNumSerie()+" ha sido "
                    + "\nregistrada con éxito\n"
                    +"Con el número '"+numPlacaNueva+"'\n"
                    +"Y el numero de folio '"+placaFolio.getId()+"'",
                    "Placa registrada",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch(PersistenciaException ex){
            JOptionPane.showMessageDialog(
                    this, 
                    "No se pudieron guardar las placas. \n"
                            + "Por favor, intente nuevamente",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        anterior.setVisible(true);
        dispose();
    }
    
    /**
     * Método que regresa a la ventana anterior.
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
        lblUsado = new javax.swing.JLabel();
        lblVentana = new javax.swing.JLabel();
        lblNumPlaca = new javax.swing.JLabel();
        txfNumPlaca = new javax.swing.JTextField();
        btnBuscarPlaca = new javax.swing.JButton();
        lblNumSerie = new javax.swing.JLabel();
        lblMarca = new javax.swing.JLabel();
        lblLinea = new javax.swing.JLabel();
        lblCosto = new javax.swing.JLabel();
        txfNumSerie = new javax.swing.JTextField();
        txfMarca = new javax.swing.JTextField();
        txfLinea = new javax.swing.JTextField();
        txfCosto = new javax.swing.JTextField();
        btnRealizarTramite = new javax.swing.JButton();
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

        lblUsado.setFont(new java.awt.Font("Sitka Subheading", 1, 18)); // NOI18N
        lblUsado.setForeground(new java.awt.Color(255, 255, 255));
        lblUsado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUsado.setText("Auto usado");

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
                .addComponent(lblUsado, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblVentana, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(lblUsado, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        lblNumPlaca.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblNumPlaca.setForeground(new java.awt.Color(0, 0, 0));
        lblNumPlaca.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNumPlaca.setText("Numero de placa");
        lblNumPlaca.setToolTipText("");

        txfNumPlaca.setBackground(new java.awt.Color(255, 255, 255));
        txfNumPlaca.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txfNumPlaca.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfNumPlaca.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnBuscarPlaca.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarPlaca.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnBuscarPlaca.setForeground(new java.awt.Color(157, 36, 73));
        btnBuscarPlaca.setText("Buscar");
        btnBuscarPlaca.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 3));
        btnBuscarPlaca.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarPlaca.setFocusable(false);
        btnBuscarPlaca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarPlacaActionPerformed(evt);
            }
        });

        lblNumSerie.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblNumSerie.setForeground(new java.awt.Color(0, 0, 0));
        lblNumSerie.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNumSerie.setText("Numero de serie");
        lblNumSerie.setToolTipText("");

        lblMarca.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblMarca.setForeground(new java.awt.Color(0, 0, 0));
        lblMarca.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMarca.setText("Marca");
        lblMarca.setToolTipText("");

        lblLinea.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblLinea.setForeground(new java.awt.Color(0, 0, 0));
        lblLinea.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLinea.setText("Linea");
        lblLinea.setToolTipText("");

        lblCosto.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblCosto.setForeground(new java.awt.Color(0, 0, 0));
        lblCosto.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCosto.setText("Costo");
        lblCosto.setToolTipText("");

        txfNumSerie.setBackground(new java.awt.Color(204, 204, 204));
        txfNumSerie.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        txfNumSerie.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txfNumSerie.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txfNumSerie.setEnabled(false);

        txfMarca.setBackground(new java.awt.Color(204, 204, 204));
        txfMarca.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        txfMarca.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txfMarca.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txfMarca.setEnabled(false);

        txfLinea.setBackground(new java.awt.Color(204, 204, 204));
        txfLinea.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        txfLinea.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txfLinea.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txfLinea.setEnabled(false);

        txfCosto.setBackground(new java.awt.Color(204, 204, 204));
        txfCosto.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        txfCosto.setForeground(new java.awt.Color(0, 0, 0));
        txfCosto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfCosto.setText("$1,000");
        txfCosto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txfCosto.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txfCosto.setEnabled(false);
        txfCosto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfCostoActionPerformed(evt);
            }
        });

        btnRealizarTramite.setBackground(new java.awt.Color(255, 255, 255));
        btnRealizarTramite.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnRealizarTramite.setForeground(new java.awt.Color(157, 36, 73));
        btnRealizarTramite.setText("Realizar trámite");
        btnRealizarTramite.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 3));
        btnRealizarTramite.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRealizarTramite.setFocusable(false);
        btnRealizarTramite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizarTramiteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblNumPlaca)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txfNumPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnBuscarPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(100, 100, 100))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblMarca)
                            .addComponent(lblNumSerie)
                            .addComponent(lblLinea)
                            .addComponent(lblCosto))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txfNumSerie, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txfMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txfLinea, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(100, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txfCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnRealizarTramite, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(73, 73, 73))))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txfNumPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNumPlaca))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNumSerie)
                    .addComponent(txfNumSerie, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txfMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMarca))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txfLinea, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLinea))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCosto)
                            .addComponent(txfCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnRealizarTramite, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 26, Short.MAX_VALUE))
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
                    .addComponent(btmRetroceder, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(gobIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblGobmx)
                        .addGap(6, 6, 6)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(lblGobmx))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(gobIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btmRetroceder, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método de acción del botón para buscar placas.
     * @param evt Evento de acción.
     */
    private void btnBuscarPlacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPlacaActionPerformed
        this.buscarAutoPorPlaca(txfNumPlaca.getText());
    }//GEN-LAST:event_btnBuscarPlacaActionPerformed

    private void txfCostoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfCostoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfCostoActionPerformed

    /**
     * Método de acción del botón "Realizar trámite".
     * @param evt Evento de acción.
     */
    private void btnRealizarTramiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarTramiteActionPerformed
        // TODO add your handling code here:
        this.guardarNuevaPlaca();
    }//GEN-LAST:event_btnRealizarTramiteActionPerformed

    /**
     * Método de acción del botón "Regresar".
     * @param evt 
     */
    private void btmRetrocederActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmRetrocederActionPerformed
        // TODO add your handling code here:
        this.regresar();
    }//GEN-LAST:event_btmRetrocederActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btmRetroceder;
    private javax.swing.JButton btnBuscarPlaca;
    private javax.swing.JButton btnRealizarTramite;
    private javax.swing.JLabel gobIcon;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblCosto;
    private javax.swing.JLabel lblGobmx;
    private javax.swing.JLabel lblLinea;
    private javax.swing.JLabel lblMarca;
    private javax.swing.JLabel lblNumPlaca;
    private javax.swing.JLabel lblNumSerie;
    private javax.swing.JLabel lblUsado;
    private javax.swing.JLabel lblVentana;
    private javax.swing.JTextField txfCosto;
    private javax.swing.JTextField txfLinea;
    private javax.swing.JTextField txfMarca;
    private javax.swing.JTextField txfNumPlaca;
    private javax.swing.JTextField txfNumSerie;
    // End of variables declaration//GEN-END:variables
}
