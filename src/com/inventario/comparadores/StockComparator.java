/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventario.comparadores;

import com.inventario.productos.Producto;
import java.util.Comparator;

/**
 *
 * @author nicol
 */
public class StockComparator implements Comparator<Producto> {
    
    //El metodo que se utiliza de la interfaz Comparator<Producto> es compare. Si queremos comparar dos campos
    //de tipo int se utiliza Integer.compare(int1, int2), devuelve -1 si el primer parametro de comparte es
    //más pequeño que el segúndo, 0 si son iguales, y 1 si es más grande
    @Override
    public int compare(Producto p1, Producto p2){
        return Integer.compare((int)p1.getStock(), (int)p2.getStock());
    }
    
}
