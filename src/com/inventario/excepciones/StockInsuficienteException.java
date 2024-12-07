/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventario.excepciones;

/**
 *
 * @author nicol
 */
public class StockInsuficienteException extends Exception {
    
    /*
    Excepción utilizada para los casos en los que en el metodo que se implementa, 
    se le pase un número mayor al stock pertinente del producto
    */
    public StockInsuficienteException(String message){
        super(message);
    }
    
}
