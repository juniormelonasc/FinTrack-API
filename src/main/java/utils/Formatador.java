/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author Júnior Melo
 */

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Formatador {

    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatarValor(double valor) {
        return String.format("R$ %.2f", valor);
    }

    public static String formatarData(LocalDate data) {
        return data.format(FORMATADOR_DATA);
    }
}