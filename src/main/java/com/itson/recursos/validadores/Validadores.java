/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.recursos.validadores;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase que incluye los métodos para validar distintas cadenas de texto.
 * @author Sebastian
 */
public class Validadores {
    
    /**
     * Método que valida si el número de placa ingresado es válido.
     * @param placa Placa a validar.
     * @return Valor true si la placa es válida, false en caso contrario.
     */
    public boolean validarPlaca(String placa) {
        String regex = "[a-zA-Z]{3}-\\d{3}"; 
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(placa);

        return matcher.matches();
    }
    
    /**
     * Método que valida si el RFC de la persona es válido.
     * @param rfc RFC a validar.
     * @return Valor true si el RFC es válido, false en caso contrario.
     */
    public boolean validaRfc(String rfc) {
        String regex = "^[a-zA-Z]{4}[0-9]{6}[a-zA-Z0-9]{3}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(rfc);

        return matcher.matches();
    }
    
}
