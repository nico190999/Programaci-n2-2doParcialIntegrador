/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventario.excepciones;

/**
 *
 * @author nicol
 */
public class ProductoNoEncontradoException extends Exception {
    
    /*
    Excepción utilizada para los casos en los que no encuentre el producto a través del metodo buscar
    */
    public ProductoNoEncontradoException(String message){
        super(message);
    }
    
}
