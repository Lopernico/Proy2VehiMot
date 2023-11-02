/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.recursos.clases;

import com.itson.dominio.Persona;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/**
 * Clase donde se encuentran las personas a insertar.
 * @author Sebastian
 */
import org.apache.commons.lang3.RandomStringUtils;

public class VeintePersonas {
    
    /**
     * Constructor de la clase VeintePersonas.
     */
    public VeintePersonas() {
        cambiarFechas();
        RFC = generarRFC();

    }
    
    /**
     * Arreglo de String con los nombres de las personas.
     */
    private String[] nombres = {"Juan Jose", "Jose Juan", "Jorge Luis", "Karla", "Roberto",
        "Pedro Antonio", "Javier Roberto", "Luis Antonio", "Pablo", "Norma",
        "Harbor", "Samuel Luis", "Damian", "Ignacio Luis", "Luis Armando",
        "Carmen", "Elmer Daniel", "Fabian", "Martha", "Guadalupe"};

    /**
     * Arreglo de String con los apellidos paternos de las personas.
     */
    private String[] apellidoP = {"Mendoza", "Paredes", "Sanchez", "Jackson", "Molina",
        "Santos", "Araujo", "Sousa", "Silva", "Pereira",
        "Costa", "Ferreira", "Barbosa", "Pinto", "Castro",
        "Carvalho", "Oliveira", "Almeida", "Rocha", "Lima"};
    
    /**
     * Arreglo de String con los apellidos maternos de las personas.
     */
    private String[] apellidoM = {"Castro", "Barbosa", "Quezada", "Ribeiro", "Melo",
        "Correia", "Goncalves", "Sousa", "Rocha", "Arellano",
        "Aponte", "Romero", "Verduzco", "Barela", "Cavazos",
        "Mesa", "Varela", "Orozco", "Orozco", "Archuleta"};

    /**
     * Arreglo Calendar con la fecha de nacimiento de cada persona.
     */
    private Calendar[] fechaNacimiento = {Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(),
        Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(),
        Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(),
        Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance()};

    /**
     * Arreglo de String con el número de teléfono de cada persona.
     */
    private String[] telefono = {"8428930423", "9482734928", "3894857210", "0956892710", "9483230142",
        "0973824758", "9673412456", "9103940149", "9098923491", "9019341301",
        "9812351891", "1045192734", "9814352538", "1425821302", "1458295234",
        "8917845135", "9104819572", "5175175917", "9018575182", "5183715193"};

    /**
     * Arreglo de String con los RFC.
     */
    private String[] RFC;

    /**
     * Método para cambiar la fecha de nacimiento de las personas automáticamente.
     */
    private void cambiarFechas() {
        for (int i = 0; i < fechaNacimiento.length; i++) {
            int dias = (int) (Math.random() * 18250 + 6570);
            fechaNacimiento[i].add(Calendar.DAY_OF_YEAR, -1 * dias);
        }
    }

    /**
     * Método que genera el RFC de cada persona.
     * @return Arreglo de String con los RFC generados.
     */
    public String[] generarRFC() {
        String[] RFCa = new String[20];
        SimpleDateFormat anio = new SimpleDateFormat("yy");

        for (int i = 0; i < nombres.length; i++) {
            StringBuffer RFC = new StringBuffer();
            RFC.append(apellidoP[i].substring(0, 2));
            RFC.append(apellidoM[i].substring(0, 1));
            RFC.append(nombres[i].substring(0, 1));
            RFC.append(anio.format(fechaNacimiento[i].getTime()));
            RFC.append(mes(fechaNacimiento[i].get(Calendar.MONTH)+1));
            RFC.append(mes(fechaNacimiento[i].get(Calendar.DAY_OF_MONTH)));
            RFC.append(RandomStringUtils.randomAlphanumeric(3));
            RFCa[i] = RFC.toString().toUpperCase();
            RFC = null;
        }
        return RFCa;
    }

    /**
     * Método que establece el mes en un mejor formato.
     * @param i Mes.
     * @return String con el mes en mejor formato.
     */
    private String mes(int i) {
        if (i < 10) {
            return "0" + i;
        } else {
            return i + "";
        }
    }

    /**
     * Método para obtener las personas.
     * @return Lista con las personas.
     */
    public List<Persona> obtenerPersonas() {
        List<Persona> personas = new ArrayList<>();
        for (int i = 0; i < RFC.length; i++) {
            Persona per = new Persona();
            per.setNombres(nombres[i]);
            per.setApellidoP(apellidoP[i]);
            per.setApellidoM(apellidoM[i]);
            per.setFechaNacimiento(fechaNacimiento[i]);
            per.setRFC(RFC[i]);
            per.setTelefono(telefono[i]);
            personas.add(per);
        }
        return personas;
    }
}
