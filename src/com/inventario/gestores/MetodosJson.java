/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.inventario.gestores;

/**
 *
 * @author nicol
 * Interfaz con metodos exclusivos encargados de lectura y escritura en formato json, es decir
 * de la Serialización y Deserialización de json
 */
public interface MetodosJson {
    
    void loadFromJson();
    void saveToJson();
}
