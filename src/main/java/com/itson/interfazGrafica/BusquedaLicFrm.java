/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.itson.interfazGrafica;

import com.itson.dominio.Licencia;
import com.itson.dominio.Persona;
import com.itson.implementacion.LicenciasDAO;
import com.itson.implementacion.PersonasDAO;
import com.itson.interfaz.ILicenciasDAO;
import com.itson.interfaz.IPersonasDAO;
import com.itson.recursos.validadores.Validadores;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Clase que representa la ventana de búsqueda de licencias.
 */
public class BusquedaLicFrm extends javax.swing.JFrame {

    /**
     * EntityManagerFactory utilizada para la conexión a la base de datos.
     */
    private EntityManagerFactory emf;
    
    /**
     * Referencia a la interfaz ILicenciasDAO utilizada para acceder a la capa de acceso a datos de licencias.
     */
    private ILicenciasDAO licDao;
    
    /**
     * Referencia a interfaz IPersonasDAO, permite acceder a la capa de acceso a datos de personas.
     */
    private IPersonasDAO perDao;
    
    /**
     * Objeto Persona, representa la persona seleccionada en la ventana de búsqueda.
     */
    private Persona per;
    
    /**
     * Objeto Licencia, representa la licencia encontrada en la búsqueda.
     */
    private Licencia licMayor;
    
    /**
     * Referencia a la ventana anterior, la cual es de tipo InicioFrm.
     */
    private InicioFrm anterior;
    
    /**
     * Objeto Validadores, contiene métodos para validar datos de entrada de la ventana.
     */
    private Validadores validadores;

    /**
     * Constructor de la clase BusquedaLicFrm.
     * @param emf EntityManagerFactory utilizada para la conexión a la base de datos.
     * @param anterior Referencia a la ventana de inicio anterior.
     */
    public BusquedaLicFrm(EntityManagerFactory emf, InicioFrm anterior) {
        initComponents();
        this.emf = emf;
        this.licDao = new LicenciasDAO(emf);
        this.perDao = new PersonasDAO(emf);
        this.anterior = anterior;
        this.validadores = new Validadores();
        this.btmContinuar.setEnabled(false);
        gobIcon.setIcon(new ImageIcon("src/main/resources/LogoGob.png"));

    }

    /**
     * Método privado para realizar la búsqueda de una licencia.
     */
    private void buscar() {
        String RFCb = txtRFCb.getText();
        
        if(RFCb.isEmpty()){
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, indique el RFC para continuar",
                    "Error: Campo vacío",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if(!validadores.validaRfc(RFCb)){
            JOptionPane.showMessageDialog(
                    this, 
                    "Por favor, verifique el RFC e intente nuevamente",
                    "Error: RFC inválido",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        this.per = perDao.buscar(RFCb);

        if(per==null){
            JOptionPane.showMessageDialog(
                    this, 
                    "No se encontró el RFC en el sistema, intente nuevamente",
                    "Error: RFC no registrado",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        List<Licencia> licencias = ordenarLicencias(per.getLicencias());

        try{
            this.licMayor = licencias.get(0);
        } catch(ArrayIndexOutOfBoundsException ex){
            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró ninguna licencia relacionada\na este RFC en el sistema",
                    "Error: Licencias no encontradas",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        long vigencia = calcularVigencia(licMayor);

        establecerDatosPersona(licMayor.getPersona());
        txtVigencia.setText(generarTextoVigencia(vigencia));

        this.btmContinuar.setEnabled(true);
        
    }

    /**
     * Método privado para validar una licencia.
     */
    private void validarLicencia() {
        if (licMayor.getFechaMax().after(Calendar.getInstance())) {
            JOptionPane.showMessageDialog(this, "Licencia valida");
            continuar();
        } else {
            JOptionPane.showMessageDialog(this, "Licencia no valida");
        }
    }

    /**
     * Método privado para formatear una fecha en formato dd/MM/yyyy.
     * @param fecha Calendar con la fecha a formatear.
     * @return String con la fecha formateada.
     */
    private String formatoFecha(Calendar fecha) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date nacimiento = fecha.getTime();

        return formatter.format(nacimiento);
    }

    /**
     * Método privado para ordenar una lista de licencias según el tipo seleccionado.
     * @param lic lista de licencias a ordenar.
     * @return lista de licencias ordenadas.
     */
    private List<Licencia> ordenarLicencias(List<Licencia> lic) {
        Comparator<Licencia> mayorVigencia = new Comparator<Licencia>() {
            @Override
            public int compare(Licencia o1, Licencia o2) {
                return o2.getFechaMax().compareTo(o1.getFechaMax());
            }
        };

        Comparator<Licencia> ultimaTramitada = new Comparator<Licencia>() {
            @Override
            public int compare(Licencia o1, Licencia o2) {
                return o2.getFechaExpedicion().compareTo(o1.getFechaExpedicion());
            }
        };

        Comparator<Licencia> elegido = null;

        switch (cbTipo.getSelectedItem().toString()) {
            case "Mayor vigencia":
                elegido = mayorVigencia;
                break;
            case "Ultima tramitada":
                elegido = ultimaTramitada;
                break;
        }
        lic.sort(elegido);

        return lic;
    }

    /**
     * Método privado para concatenar el nombre completo de una persona.
     * @param per Persona de la cual se desea concatenar el nombre.
     * @return String con el nombre completo de la persona.
     */
    private String concatNombre(Persona per) {
        return per.getNombres() + " " + per.getApellidoP() + " " + per.getApellidoM();
    }

    /**
     * Método privado para calcular la vigencia de una licencia en días.
     * @param lic Licencia objeto de Licencia para la cual se desea calcular la vigencia.
     * @return long con la vigencia de la licencia en días.
     */
    private long calcularVigencia(Licencia lic) {
        long vigencia = (lic.getFechaMax().getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / 86400000;
        return vigencia;
    }

    /**
     * Método privado para establecer los datos de una persona en los campos correspondientes.
     * @param per Persona objeto de Persona para la cual se desean establecer los datos.
     */
    private void establecerDatosPersona(Persona per) {
        txtRFCr.setText(per.getRFC());
        txtNombre.setText(concatNombre(per));
        txtNacimiento.setText(formatoFecha(per.getFechaNacimiento()));
        txtTelefono.setText(per.getTelefono());
    }

    /**
     * Método privado para generar el texto que muestre la vigencia de una licencia.
     * @param vigencia long con la vigencia de la licencia en días.
     * @return String con el texto de vigencia de la licencia.
     */
    private String generarTextoVigencia(long vigencia) {
        String vigenciaTxt;
        System.out.println(vigencia);
        if (vigencia <= 0) {
            vigencia = vigencia * -1;
            vigenciaTxt = "Vencida por " + vigencia + " dias";
        } else {
            vigenciaTxt = vigencia + " dias";
        }

        return vigenciaTxt;
    }

    /**
     * Método privado para regresar a la ventana de inicio anterior.
     */
    private void regresar() {
        this.anterior.setVisible(true);
        this.dispose();
    }
    
    /**
     * Método privado para continuar a la siguiente ventana de selección de automóvil.
     */
    private void continuar() {
        new SelectAutoFrm(this, licMayor, emf).setVisible(true);
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
        lblInicio1 = new javax.swing.JLabel();
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
        txtVigencia = new javax.swing.JTextField();
        lblVigencia = new javax.swing.JLabel();
        btmContinuar = new javax.swing.JButton();
        cbTipo = new javax.swing.JComboBox<>();
        lblRFCb1 = new javax.swing.JLabel();
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
        lblInicio.setText("Tramite de placas");

        lblInicio1.setFont(new java.awt.Font("Sitka Subheading", 1, 18)); // NOI18N
        lblInicio1.setForeground(new java.awt.Color(255, 255, 255));
        lblInicio1.setText("Buscar licencia");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblInicio1)
                .addGap(15, 15, 15))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblInicio1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblRFCb.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblRFCb.setForeground(new java.awt.Color(0, 0, 0));
        lblRFCb.setText("RFC:");

        txtRFCb.setBackground(new java.awt.Color(255, 255, 255));
        txtRFCb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51), 2));

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

        txtRFCr.setEditable(false);
        txtRFCr.setBackground(new java.awt.Color(204, 204, 204));
        txtRFCr.setForeground(new java.awt.Color(0, 0, 0));
        txtRFCr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51), 2));

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

        txtNacimiento.setEditable(false);
        txtNacimiento.setBackground(new java.awt.Color(204, 204, 204));
        txtNacimiento.setForeground(new java.awt.Color(0, 0, 0));
        txtNacimiento.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51), 2));

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

        txtVigencia.setEditable(false);
        txtVigencia.setBackground(new java.awt.Color(204, 204, 204));
        txtVigencia.setForeground(new java.awt.Color(0, 0, 0));
        txtVigencia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51), 2));

        lblVigencia.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblVigencia.setForeground(new java.awt.Color(0, 0, 0));
        lblVigencia.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblVigencia.setText("Vigencia:");

        btmContinuar.setBackground(new java.awt.Color(255, 255, 255));
        btmContinuar.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btmContinuar.setForeground(new java.awt.Color(157, 36, 73));
        btmContinuar.setText("Continuar");
        btmContinuar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 3));
        btmContinuar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btmContinuar.setFocusable(false);
        btmContinuar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmContinuarActionPerformed(evt);
            }
        });

        cbTipo.setBackground(new java.awt.Color(255, 255, 255));
        cbTipo.setForeground(new java.awt.Color(0, 0, 0));
        cbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mayor vigencia", "Ultima tramitada" }));
        cbTipo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        cbTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTipoActionPerformed(evt);
            }
        });

        lblRFCb1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblRFCb1.setForeground(new java.awt.Color(0, 0, 0));
        lblRFCb1.setText("Tipo:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(lblRFCb)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRFCb, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblRFCb1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbTipo, 0, 125, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btmBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(84, 84, 84)
                                .addComponent(lblNombre)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(119, 119, 119)
                                .addComponent(lblRFCr)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtRFCr, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblVigencia, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblNacimiento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(lblTelefono)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(163, 163, 163)
                        .addComponent(txtVigencia, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btmContinuar, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(38, 38, 38))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtRFCb)
                        .addComponent(cbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblRFCb1)
                        .addComponent(btmBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblRFCb))
                .addGap(15, 15, 15)
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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtVigencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblVigencia)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(btmContinuar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28))
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
                        .addGap(30, 30, 30)
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
                .addComponent(btmRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 400));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
    * Método invocado cuando se hace clic en el botón "Buscar".
    * Realiza la búsqueda de licencias en la base de datos.
    * @param evt Objeto ActionEvent que representa el evento de acción.
    */
    private void btmBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmBuscarActionPerformed
        buscar();
    }//GEN-LAST:event_btmBuscarActionPerformed
    
    /**
    * Método invocado cuando se hace clic en el botón "Continuar".
    * Valida la licencia seleccionada y realiza una acción específica.
    * @param evt Objeto ActionEvent que representa el evento de acción.
    */
    private void btmContinuarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmContinuarActionPerformed
        validarLicencia();
    }//GEN-LAST:event_btmContinuarActionPerformed

    private void cbTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbTipoActionPerformed
    
    /**
    * Método invocado cuando se hace clic en el botón "Regresar".
    * Realiza una acción específica para regresar a la ventana anterior.
    * @param evt Objeto ActionEvent que representa el evento de acción.
    */
    private void btmRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmRegresarActionPerformed
        regresar();
    }//GEN-LAST:event_btmRegresarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btmBuscar;
    private javax.swing.JButton btmContinuar;
    private javax.swing.JButton btmRegresar;
    private javax.swing.JComboBox<String> cbTipo;
    private javax.swing.JLabel gobIcon;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblGobmx;
    private javax.swing.JLabel lblInicio;
    private javax.swing.JLabel lblInicio1;
    private javax.swing.JLabel lblNacimiento;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblRFCb;
    private javax.swing.JLabel lblRFCb1;
    private javax.swing.JLabel lblRFCr;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JLabel lblVigencia;
    private javax.swing.JTextField txtNacimiento;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtRFCb;
    private javax.swing.JTextField txtRFCr;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtVigencia;
    // End of variables declaration//GEN-END:variables
}
