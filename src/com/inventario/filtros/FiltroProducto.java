/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.inventario.filtros;

import com.inventario.productos.Producto;

/**
 *
 * @author nicol
 * @param <T>
 * Al ser FunctionalInterface cuenta unicamente con un unico metodo abstracto, 
 * que se utiliza en funciones lambda (función anonima que se define en el momento de la implementación)
 */
@FunctionalInterface
public interface FiltroProducto<T extends Producto> {
    boolean filtrar(T producto);
}
