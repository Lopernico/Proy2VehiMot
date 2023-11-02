/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.itson.interfazGrafica;

import com.itson.dominio.Licencia;
import com.itson.dominio.Persona;
import com.itson.excepciones.PersistenciaException;
import com.itson.implementacion.LicenciasDAO;
import com.itson.implementacion.PersonasDAO;
import com.itson.interfaz.ILicenciasDAO;
import com.itson.interfaz.IPersonasDAO;
import com.itson.recursos.clases.DuracionLicencia;
import com.itson.recursos.clases.TipoLicencia;
import com.itson.recursos.validadores.Validadores;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Clase que representa la ventana para tramitar una nueva licencia.
 */
public class TramiteLicFrm extends javax.swing.JFrame {

    /**
     * DAO de licencias.
     */
    private ILicenciasDAO licDao;
    
    /**
     * DAO de personas.
     */
    private IPersonasDAO perDao;
    
    /**
     * Persona a registrar una nueva Licencia.
     */
    private Persona per;
    
    /**
     * Tipo de licencia a registrar.
     */
    private TipoLicencia tipo;
    
    /**
     * Duración de la licencia a ingresar.
     */
    private DuracionLicencia duracion;
    
    /**
     * Objeto para validar los datos de entrada de la ventana.
     */
    private Validadores validadores;
    
    /**
     * Referencia a la ventana anterior.
     */
    private InicioFrm anterior;

    /**
     * Constructor de la clase TramiteLicFrm.
     * @param emf EntityManagerFactory para la conexión de la base de datos.
     * @param anterior Referencia a la ventana anterior.
     */
    public TramiteLicFrm(EntityManagerFactory emf, InicioFrm anterior) {
        initComponents();
        this.licDao = new LicenciasDAO(emf);
        this.perDao = new PersonasDAO(emf);
        this.validadores = new Validadores();
        this.anterior = anterior;
        this.btmRealizarTra.setEnabled(false);
        gobIcon.setIcon(new ImageIcon("src/main/resources/LogoGob.png"));
    }
    
    /**
     * Método para realizar la búsqueda de una persona.
     */
    private void buscar() {
        String RFCb = txtRFCb.getText();

        if(RFCb.isEmpty()){
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, ingrese el RFC para continuar",
                    "Error: Campo vacío",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        if (!validadores.validaRfc(RFCb)) {
            mostrarMensajeErrorFormatoRFC();
            return;
        }

        this.per = perDao.buscar(RFCb);

        validarPersona();
    }

    /**
     * Método para validar una persona encontrada.
     */
    private void validarPersona() {
        if (per != null) {
            double costo = getCosto();
            String rfc = per.getRFC();
            String nombre = per.getNombres() + " " + per.getApellidoP() + " " + per.getApellidoM();
            String nacimiento = formatoFecha(per.getFechaNacimiento());
            String telefono = per.getTelefono();

            txtRFCr.setText(rfc);
            txtNombre.setText(nombre);
            txtNacimiento.setText(nacimiento);
            txtTelefono.setText(telefono);
            txtCosto.setText(costo + "");
            
            this.btmRealizarTra.setEnabled(true);
            
        } else {
            mostrarMensajeErrorNoEncontrado();
        }
    }

    /**
     * Método para obtener el costo de la licencia.
     * @return Costo de la licencia.
     */
    private double getCosto() {
        if (chbDiscapacidad.isSelected()) {
            tipo = TipoLicencia.discapacitado;
            switch (cbVigencia.getSelectedItem().toString()) {
                case "1 anio":
                    duracion = DuracionLicencia.UnAnio;
                    break;
                case "2 anios":
                    duracion = DuracionLicencia.dosAnios;
                    break;
                case "3 anios":
                    duracion = DuracionLicencia.tresAnios;
                    break;
            }
            return duracion.getCostoDiscapacitado();
        } else {
            tipo = TipoLicencia.normal;
            switch (cbVigencia.getSelectedItem().toString()) {
                case "1 anio":
                    duracion = DuracionLicencia.UnAnio;
                    break;
                case "2 anios":
                    duracion = DuracionLicencia.dosAnios;
                    break;
                case "3 anios":
                    duracion = DuracionLicencia.tresAnios;
                    break;
            }
            return duracion.getCostoNormal();
        }
    }

    /**
     * Método para establecer una fecha en formato dd/MM/yyyy.
     * @param fecha Fecha a formatear.
     * @return Fecha en formato dd/MM/yyyy.
     */
    private String formatoFecha(Calendar fecha) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date nacimiento = fecha.getTime();

        return formatter.format(nacimiento);
    }

    /**
     * Método para realizar el tramite de una licencia.
     */
    private void realizarTramite() {
        Licencia lic = new Licencia();

        lic.setCosto(getCosto());
        lic.setFechaEmision(Calendar.getInstance());
        lic.setFechaExpedicion(Calendar.getInstance());
        lic.setPersona(per);
        lic.setTipoLicencia(tipo);
        lic.setVigencia(duracion);

        if (per == null) {
            mostrarMensajeErrorNoEncontrado();
            return;
        }

        Licencia licN = null;
        try {
            licN = licDao.insertar(lic);
        } catch (PersistenciaException e) {
            JOptionPane.showMessageDialog(this, "Tramite fallo");
        }
        if (licN != null) {
            JOptionPane.showMessageDialog(this, "Tramite realizado con exito.\nCon folio: " + licN.getId());
            regresar();
        }
    }

    /**
     * Método para mostrar un mensaje de error de formato de RFC.
     */
    private void mostrarMensajeErrorFormatoRFC() {
        JOptionPane.showMessageDialog(
                this,
                "RFC inválido. Verifica que el RFC sea \n el correcto e ingréselo nuevamente",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Método para mostrar un mensaje de error de persona no encontrada.
     */
    private void mostrarMensajeErrorNoEncontrado() {
        JOptionPane.showMessageDialog(
                this,
                "Persona no encontrada",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Método para cambiar el estado del costo de la licencia.
     */
    private void cambiarEstadoCosto() {
        double costo = getCosto();
        txtCosto.setText(costo + "");
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
        txtRFCb = new javax.swing.JTextField();
        btmBuscar = new javax.swing.JButton();
        txtRFCr = new javax.swing.JTextField();
        lblRFCr = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtNacimiento = new javax.swing.JTextField();
        lblNacimiento = new javax.swing.JLabel();
        lblTelefono = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        cbVigencia = new javax.swing.JComboBox<>();
        lblVigencia = new javax.swing.JLabel();
        chbDiscapacidad = new javax.swing.JCheckBox();
        txtCosto = new javax.swing.JTextField();
        lblCosto = new javax.swing.JLabel();
        btmRealizarTra = new javax.swing.JButton();
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

        lblInicio.setFont(new java.awt.Font("Sitka Subheading", 1, 18)); // NOI18N
        lblInicio.setForeground(new java.awt.Color(255, 255, 255));
        lblInicio.setText("Tramitar licencia");

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

        lblRFCb.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblRFCb.setForeground(new java.awt.Color(0, 0, 0));
        lblRFCb.setText("RFC:");

        txtRFCb.setBackground(new java.awt.Color(255, 255, 255));
        txtRFCb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51), 2));

        btmBuscar.setText("Buscar");
        btmBuscar.setBackground(new java.awt.Color(255, 255, 255));
        btmBuscar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 3));
        btmBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btmBuscar.setFocusable(false);
        btmBuscar.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btmBuscar.setForeground(new java.awt.Color(157, 36, 73));
        btmBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmBuscarActionPerformed(evt);
            }
        });

        txtRFCr.setEditable(false);
        txtRFCr.setBackground(new java.awt.Color(204, 204, 204));
        txtRFCr.setForeground(new java.awt.Color(0, 0, 0));
        txtRFCr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51), 2));
        txtRFCr.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        lblRFCr.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblRFCr.setForeground(new java.awt.Color(0, 0, 0));
        lblRFCr.setText("RFC:");

        lblNombre.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblNombre.setForeground(new java.awt.Color(0, 0, 0));
        lblNombre.setText("Nombre:");

        txtNombre.setEditable(false);
        txtNombre.setBackground(new java.awt.Color(204, 204, 204));
        txtNombre.setForeground(new java.awt.Color(0, 0, 0));
        txtNombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51), 2));
        txtNombre.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        txtNacimiento.setEditable(false);
        txtNacimiento.setBackground(new java.awt.Color(204, 204, 204));
        txtNacimiento.setForeground(new java.awt.Color(0, 0, 0));
        txtNacimiento.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51), 2));
        txtNacimiento.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        lblNacimiento.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblNacimiento.setForeground(new java.awt.Color(0, 0, 0));
        lblNacimiento.setText("Nacimiento:");

        lblTelefono.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTelefono.setForeground(new java.awt.Color(0, 0, 0));
        lblTelefono.setText("Telefono:");

        txtTelefono.setEditable(false);
        txtTelefono.setBackground(new java.awt.Color(204, 204, 204));
        txtTelefono.setForeground(new java.awt.Color(0, 0, 0));
        txtTelefono.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51), 2));
        txtTelefono.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        cbVigencia.setBackground(new java.awt.Color(255, 255, 255));
        cbVigencia.setForeground(new java.awt.Color(0, 0, 0));
        cbVigencia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 anio", "2 anios", "3 anios" }));
        cbVigencia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        cbVigencia.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbVigenciaItemStateChanged(evt);
            }
        });

        lblVigencia.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblVigencia.setForeground(new java.awt.Color(0, 0, 0));
        lblVigencia.setText("Vigencia:");

        chbDiscapacidad.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        chbDiscapacidad.setForeground(new java.awt.Color(0, 0, 0));
        chbDiscapacidad.setText("¿Discapacidad?");
        chbDiscapacidad.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbDiscapacidadItemStateChanged(evt);
            }
        });

        txtCosto.setEditable(false);
        txtCosto.setBackground(new java.awt.Color(204, 204, 204));
        txtCosto.setForeground(new java.awt.Color(0, 0, 0));
        txtCosto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51), 2));
        txtCosto.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        lblCosto.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblCosto.setForeground(new java.awt.Color(0, 0, 0));
        lblCosto.setText("Costo:");

        btmRealizarTra.setText("Realizar tramite");
        btmRealizarTra.setBackground(new java.awt.Color(255, 255, 255));
        btmRealizarTra.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 3));
        btmRealizarTra.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btmRealizarTra.setFocusable(false);
        btmRealizarTra.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btmRealizarTra.setForeground(new java.awt.Color(157, 36, 73));
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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btmRealizarTra, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(lblVigencia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbVigencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(chbDiscapacidad)
                        .addGap(18, 18, 18)
                        .addComponent(lblCosto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(37, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTelefono, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblNombre, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblNacimiento, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblRFCr, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblRFCb, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRFCr, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtRFCb, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(btmBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(90, 90, 90))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblRFCb)
                        .addComponent(txtRFCb))
                    .addComponent(btmBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRFCr)
                    .addComponent(lblRFCr))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombre)
                    .addComponent(lblNombre))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNacimiento)
                    .addComponent(lblNacimiento))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTelefono)
                    .addComponent(txtTelefono))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chbDiscapacidad, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbVigencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblVigencia)
                        .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblCosto)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btmRealizarTra, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
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
                        .addGap(38, 38, 38)
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
                .addComponent(btmRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 400));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método de acción del botón para buscar a la persona.
     * @param evt Evento de acción.
     */
    private void btmBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmBuscarActionPerformed
        buscar();
    }//GEN-LAST:event_btmBuscarActionPerformed

    /**
     * Método de acción del botón para realizar trámite.
     * @param evt Evento de acción.
     */
    private void btmRealizarTraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmRealizarTraActionPerformed
        realizarTramite();
    }//GEN-LAST:event_btmRealizarTraActionPerformed

    /**
     * Método que cambia el estado de vigencia, si el valor seleccionado
     * en el ComboBox ha cambiado.
     * @param evt Evento de acción.
     */
    private void cbVigenciaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbVigenciaItemStateChanged
        if (evt.getStateChange() == evt.SELECTED) {
            cambiarEstadoCosto();
        }
    }//GEN-LAST:event_cbVigenciaItemStateChanged

    /**
     * Método que valida el CheckBox cuando es seleccionado.
     * @param evt Evento de acción.
     */
    private void chbDiscapacidadItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chbDiscapacidadItemStateChanged
        cambiarEstadoCosto();
    }//GEN-LAST:event_chbDiscapacidadItemStateChanged

    /**
     * Método de acción del botón "Regresar".
     * @param evt 
     */
    private void btmRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmRegresarActionPerformed
        regresar();
    }//GEN-LAST:event_btmRegresarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btmBuscar;
    private javax.swing.JButton btmRealizarTra;
    private javax.swing.JButton btmRegresar;
    private javax.swing.JComboBox<String> cbVigencia;
    private javax.swing.JCheckBox chbDiscapacidad;
    private javax.swing.JLabel gobIcon;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblCosto;
    private javax.swing.JLabel lblGobmx;
    private javax.swing.JLabel lblInicio;
    private javax.swing.JLabel lblNacimiento;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblRFCb;
    private javax.swing.JLabel lblRFCr;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JLabel lblVigencia;
    private javax.swing.JTextField txtCosto;
    private javax.swing.JTextField txtNacimiento;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtRFCb;
    private javax.swing.JTextField txtRFCr;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
