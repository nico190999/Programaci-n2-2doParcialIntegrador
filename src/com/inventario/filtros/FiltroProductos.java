/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventario.filtros;

import com.inventario.productos.Producto;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicol
 * Función encargada de filtrado de productos. Se aplica un filtro y devuelve la lista con los productos 
 * ya filtrados. Al ser estatico, se tiene que aclarar en el metodo luego de static
 */
public class FiltroProductos {
    
    public static<T extends Producto> List<T> filtrarProductos(List<T> productos, FiltroProducto filtro) {
        List<T> resultado = new ArrayList<>();
        for (T producto : productos) {
            if (filtro.filtrar(producto)) {
                resultado.add(producto);
            }
        }
        return resultado;
    }
    
}
