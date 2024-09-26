/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import utilidades.Util;

/**
 *
 * @author 2dam
 */
public enum Dificultad {
    ALTA, MEDIA, BAJA;
    public Dificultad validarDificultad() {
        String str;
        Dificultad contenido = Dificultad.BAJA;
        boolean error;
        do {
            System.out.println("Introduzca una dificultad: (ALTA - MEDIA - BAJA)");
            str = Util.introducirCadena();
            if (str != null) {
                try {
                    contenido = Dificultad.valueOf(str.toUpperCase());
                    error = false;
                } catch (IllegalArgumentException e) {
                    System.out.println("Dificultad no válida. Por favor, introduzca uno de los valores permitidos.");
                    error = true;
                }
            } else {
                System.out.println("La entrada no puede estar vacía. Por favor, introduzca un valor.");
                error = true;
            }
        } while (error);
        return contenido;
    }
}
