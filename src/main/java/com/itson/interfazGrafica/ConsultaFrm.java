/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.itson.interfazGrafica;

import com.itson.dominio.Persona;
import com.itson.implementacion.PersonasDAO;
import com.itson.interfaz.IPersonasDAO;
import com.itson.recursos.clases.Paginado;
import com.itson.recursos.clases.ParametrosBusqueda;
import com.itson.recursos.validadores.Validadores;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * Clase que representa la ventana de consulta de personas.
 */
public class ConsultaFrm extends javax.swing.JFrame {

    /**
     * Objeto para cceder a los datos de personas en la base de datos.
     */
    private IPersonasDAO perDao;
    
    /**
     * Objeto para almacenar los datos de la persona seleccionada.
     */
    private Persona per;
    
    /**
     * Objeto para almacenar los parámetros de búsqueda de personas.
     */
    private ParametrosBusqueda parBus;
    
    /**
     * Lista de personas encontradas en la búsqueda.
     */
    private List<Persona> personas;
    
    /**
     * EntityManagerFactory para la conexión a la base de datos
     */
    private EntityManagerFactory emf;
    
    /**
     * Referencia a la ventana anterior.
     */
    private InicioFrm anterior;
    
    /**
     * Objeto para validar datos de entrada de la ventana.
     */
    private Validadores validadores;

    /**
     * Constructor de la clase ConsultaFrm.
     * @param emf EntityManagerFactory para la conexión a la base de datos.
     * @param anterior Referencia a la ventana anterior.
     */
    public ConsultaFrm(EntityManagerFactory emf, InicioFrm anterior) {
        initComponents();
        this.perDao = new PersonasDAO(emf);
        this.emf = emf;
        this.anterior = anterior;
        this.validadores = new Validadores();
        this.btmContinuar.setEnabled(false);
        dpNacimiento.getComponentDateTextField().setEditable(false);
        jScrollPane1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tPersonas.getSelectedRow();
                if (row >= 0) {
                    String rfc = tPersonas.getValueAt(row, 0).toString();

                    txtSeleccionado.setText(rfc);
                }
            }
        });

        tPersonas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = tPersonas.getSelectedRow();
                    if (selectedRow != -1) {
                        String rfc = (String) tPersonas.getValueAt(selectedRow, 0);
                        txtSeleccionado.setText(rfc);
                    }
                }
            }
        });

        gobIcon.setIcon(new ImageIcon("src/main/resources/LogoGob.png"));

    }

    /**
     * Método privado para realizar la búsqueda de personas según los parámetros de búsqueda ingresados en la GUI.
     */
    private void buscar() {
        ParametrosBusqueda parBus = new ParametrosBusqueda();

        if (!txtRFCb.getText().isBlank()) {
            if(!validadores.validaRfc(txtRFCb.getText())){
                JOptionPane.showMessageDialog(
                    this, 
                    "Verifique el RFC e intente nuevamente",
                    "Error: RFC inválido",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }else{
                parBus.setRFC(txtRFCb.getText());
            }
        }
        if (!txtNombre.getText().isBlank()) {
            parBus.setNombreSimilar(txtNombre.getText());
        }
        if (dpNacimiento.getDate() != null) {
            LocalDate localDate = dpNacimiento.getDate();
            ZoneId zoneId = ZoneId.systemDefault();
            Date date = Date.from(localDate.atStartOfDay(zoneId).toInstant());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            parBus.setFechaNacimiento(calendar);
            System.out.println(calendar);
        }

        Paginado pag = new Paginado().setLimit(9);
        parBus.setConfPag(pag);
        this.parBus = parBus;

        personas = this.perDao.buscar(parBus);

        if (!personas.isEmpty()) {
            cargarTablaPersonas();
        } else {
            JOptionPane.showMessageDialog(this, "No encontrado");
        }
        
        btmContinuar.setEnabled(true);
        
    }

    /**
     * Método privado para avanzar a la siguiente página de resultados de búsqueda.
     */
    private void avanzarPag() {
        parBus.getConfPag().avanzarPag();
        personas = this.perDao.buscar(parBus);
        if (!personas.isEmpty()) {
            cargarTablaPersonas();
        } else {
            parBus.getConfPag().retrocederPag();
        }
    }

    /**
     * Método privado para retroceder a la página anterior de resultados de búsqueda.
     */
    private void retrocederPag() {
        parBus.getConfPag().retrocederPag();
        personas = this.perDao.buscar(parBus);
        if (!personas.isEmpty()) {
            cargarTablaPersonas();
        }
    }

    /**
     * Método privado para formatear una fecha en formato dd/MM/yyyy.
     * @param fecha Fecha a formatear.
     * @return Fecha formateada en formato dd/MM/yyyy.
     */
    private String formatoFecha(Calendar fecha) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date nacimiento = fecha.getTime();

        return formatter.format(nacimiento);
    }

    /**
     * Método privado para concatenar el nombre completo de una persona en un solo String.
     * @param per Persona de la cual se desea obtener el nombre completo.
     * @return Nombre completo de la persona.
     */
    private String concatNombre(Persona per) {
        return per.getNombres() + " " + per.getApellidoP() + " " + per.getApellidoM();
    }

    /**
     * Método privado para cargar los datos de las personas encontradas en 
     * la tabla de la ventana.
     */
    private void cargarTablaPersonas() {

        if (personas.isEmpty()) {
            this.parBus.getConfPag().retrocederPag();
            return;
        }
        DefaultTableModel modeloTabla = (DefaultTableModel) this.tPersonas.getModel();
        modeloTabla.setRowCount(0);
        for (Persona per : personas) {
            Object[] fila = {per.getRFC(), concatNombre(per), formatoFecha(per.getFechaNacimiento())};
            modeloTabla.addRow(fila);
        }

    }

    /**
     * Método privado para manejar la acción de continuar después de seleccionar una persona en la GUI.
     */
    private void continuar() {
        per = perDao.buscar(txtSeleccionado.getText());
        new ConsultarResultadoFrm(emf, per, this).setVisible(true);
        this.setVisible(false);
    }

    /**
     * Método privado para manejar la acción de regresar a la ventana anterior-
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
        lblInicio1 = new javax.swing.JLabel();
        lblRFCb = new javax.swing.JLabel();
        txtRFCb = new javax.swing.JTextField();
        btmBuscar = new javax.swing.JButton();
        lblRFCr = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lblVigencia = new javax.swing.JLabel();
        lblNombreS = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tPersonas = new javax.swing.JTable();
        btmContinuar = new javax.swing.JButton();
        txtSeleccionado = new javax.swing.JTextField();
        btmRetroceder = new javax.swing.JButton();
        btmAvanzar = new javax.swing.JButton();
        dpNacimiento = new com.github.lgooddatepicker.components.DatePicker();
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

        lblInicio.setText("Consulta");
        lblInicio.setFont(new java.awt.Font("Sitka Subheading", 1, 18)); // NOI18N
        lblInicio.setForeground(new java.awt.Color(255, 255, 255));

        lblInicio1.setText("Busqueda de persona");
        lblInicio1.setFont(new java.awt.Font("Sitka Subheading", 1, 18)); // NOI18N
        lblInicio1.setForeground(new java.awt.Color(255, 255, 255));

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

        lblRFCb.setText("RFC:");
        lblRFCb.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblRFCb.setForeground(new java.awt.Color(0, 0, 0));

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

        lblRFCr.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRFCr.setText("Nacimiento:");
        lblRFCr.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblRFCr.setForeground(new java.awt.Color(0, 0, 0));

        txtNombre.setBackground(new java.awt.Color(255, 255, 255));
        txtNombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51), 2));
        txtNombre.setForeground(new java.awt.Color(0, 0, 0));
        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });

        lblVigencia.setText("RFC seleccionado");
        lblVigencia.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblVigencia.setForeground(new java.awt.Color(0, 0, 0));

        lblNombreS.setText("Nombre similar:");
        lblNombreS.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblNombreS.setForeground(new java.awt.Color(0, 0, 0));

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

        txtSeleccionado.setEditable(false);
        txtSeleccionado.setBackground(new java.awt.Color(255, 255, 255));
        txtSeleccionado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51), 2));
        txtSeleccionado.setForeground(new java.awt.Color(0, 0, 0));

        btmRetroceder.setBackground(new java.awt.Color(255, 255, 255));
        btmRetroceder.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btmRetroceder.setForeground(new java.awt.Color(157, 36, 73));
        btmRetroceder.setText("<");
        btmRetroceder.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 3));
        btmRetroceder.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btmRetroceder.setFocusable(false);
        btmRetroceder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmRetrocederActionPerformed(evt);
            }
        });

        btmAvanzar.setBackground(new java.awt.Color(255, 255, 255));
        btmAvanzar.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btmAvanzar.setForeground(new java.awt.Color(157, 36, 73));
        btmAvanzar.setText(">");
        btmAvanzar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(157, 36, 73), 3));
        btmAvanzar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btmAvanzar.setFocusable(false);
        btmAvanzar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmAvanzarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(lblRFCb)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtRFCb, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblNombreS)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblRFCr, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dpNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btmRetroceder, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btmAvanzar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btmBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btmContinuar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSeleccionado, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblVigencia))
                        .addGap(28, 28, 28))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtRFCb)
                            .addComponent(lblNombreS)
                            .addComponent(lblRFCb)
                            .addComponent(txtNombre))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblRFCr)
                                .addComponent(dpNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btmRetroceder, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btmAvanzar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(btmBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)
                        .addComponent(lblVigencia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSeleccionado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btmContinuar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(228, 228, 228))
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
                        .addGap(26, 26, 26)
                        .addComponent(btmRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addComponent(btmRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 400));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método de acción para el evento de acción del botón "Buscar".
     * @param evt Evento de acción.
     */
    private void btmBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmBuscarActionPerformed
        buscar();
    }//GEN-LAST:event_btmBuscarActionPerformed

    /**
     * Método de acción para el evento de acción del botón "Continuar".
     * @param evt Evento de acción.
     */
    private void btmContinuarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmContinuarActionPerformed
        continuar();
    }//GEN-LAST:event_btmContinuarActionPerformed

    private void tPersonasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tPersonasFocusGained
    }//GEN-LAST:event_tPersonasFocusGained

    /**
     * Método de acción para el evento de acción del botón "Retroceder".
     * @param evt Evento de acción.
     */
    private void btmRetrocederActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmRetrocederActionPerformed
        retrocederPag();
    }//GEN-LAST:event_btmRetrocederActionPerformed

    /**
     * Método de acción para el evento de acción del botón "Avanzar".
     * @param evt Evento de acción.
     */
    private void btmAvanzarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmAvanzarActionPerformed
        avanzarPag();
    }//GEN-LAST:event_btmAvanzarActionPerformed

    /**
     * Método de acción para el evento de acción del botón "Regresar".
     * @param evt Evento de acción.
     */
    private void btmRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmRegresarActionPerformed
        regresar();
    }//GEN-LAST:event_btmRegresarActionPerformed

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btmAvanzar;
    private javax.swing.JButton btmBuscar;
    private javax.swing.JButton btmContinuar;
    private javax.swing.JButton btmRegresar;
    private javax.swing.JButton btmRetroceder;
    private com.github.lgooddatepicker.components.DatePicker dpNacimiento;
    private javax.swing.JLabel gobIcon;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblGobmx;
    private javax.swing.JLabel lblInicio;
    private javax.swing.JLabel lblInicio1;
    private javax.swing.JLabel lblNombreS;
    private javax.swing.JLabel lblRFCb;
    private javax.swing.JLabel lblRFCr;
    private javax.swing.JLabel lblVigencia;
    private javax.swing.JTable tPersonas;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtRFCb;
    private javax.swing.JTextField txtSeleccionado;
    // End of variables declaration//GEN-END:variables
}
