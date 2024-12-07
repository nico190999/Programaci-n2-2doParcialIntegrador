/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.inventario.gestores;

/**
 *
 * @author nicol
 * 
 * Interfaz en cargada de realizar operaciónes con los parametros que se pasen
 * 
 */
@FunctionalInterface
public interface Operacion {
    int ejecutar(int a, int b);
}
