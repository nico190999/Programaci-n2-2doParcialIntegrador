/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventario.productos;

import com.inventario.excepciones.StockInsuficienteException;
import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author nicol
 */
public abstract class Producto implements Serializable, Comparable<Producto> {
 
    private String codigo;
    private String tipo;
    private String nombre;
    private double precio;
    private int stock;
    private static final long serialVersionUID = 1L;

    public Producto(String tipo, String nombre, double precio, int stock) {
        this.tipo = tipo;
        this.codigo = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }
    
    /*
    Metodo abstracto que será heredada de las clases hijas para devolver en formato String los datos
    del producto 
    */
    public abstract String mostrarDetalles();
    
    /*
    Metodo encargado si la cantidad tipo int ingresada por parametro supera al stock del producto, en caso
    que si, lanzar la excepción personalizada de tipo StockInsuficienteException. Sin embargo en caso que 
    no, devolver el boolean true
    */
    public boolean verificarDisponibilidad(int cantidad) throws StockInsuficienteException{
        if(cantidad < stock){
            return true;
        }
        throw new StockInsuficienteException("LA CANTIDAD INGRESADA SUPERA EL STOCK DEL PRODUCTO");
    }

    
    /*
    Devolver el valor del atributo nombre
    */
    public String getNombre() {return nombre;}

    /*
    Devolver el valor del atributo precio
    */
    public double getPrecio() {return precio;}

    /*
    Devolver el valor del atributo stock
    */
    public int getStock() {return stock;}
    
    /*
    Devolver el valor del atributo codigo
    */
    public String getCodigo(){return codigo;}

    /*
    Devolver el valor del atributo tipo
    */
    public String getTipo() {return tipo;}

    /*
    Metodo encargado de aplicar el valor pasado por parametro a la variable tipo, con previa validación
    de que no se trate de un campo diferente a Alimento o Electrodomestico
    */
    public void setTipo(String tipo) {
        if(tipo.equals("Alimento") || tipo.equals("Electrodomestico")){
            this.tipo= tipo;
        }else{
            throw new IllegalArgumentException("ERROR, NO ADMITE EL VALOR BRINDADO");
        }
    }

    
    /*
    Metodo encargado de aplicar el valor pasado por parametro a la variable tipo, con previa validación
    de que no se trate de un campo diferente a Alimento o Electrodomestico
    */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("ERROR, NO ADMITE NOMBRE NULO"); 
        }
        this.nombre = nombre;
    }

    
    /*
    Metodo encargado de aplicar el valor pasado por parametro a la variable precio, con previa validación
    de que no se trate de un campo negativo o 0
    */
    public void setPrecio(double precio) {
        if (precio <= 0){
            throw new IllegalArgumentException("ERROR, NO SE ADMITE VALORES MENOR O IGUAL A 0");
        }
    }

    
    /*
    Metodo encargado de aplicar el valor pasado por parametro a la variable stock, con previa validación
    de que no se trate de un campo negativo o 0
    */
    public void setStock(int stock) {
        if (stock <= 0) {
            throw new IllegalArgumentException("ERROR, NO SE ADMITE VALORES MENOR O IGUAL A 0");
        }
        this.stock = stock;
    }
    
    
    /*
    Compara 2 String de forma alfabetica, este metodo se llama desde GestorInventario
    para ordenar productos a través de collections
    */
    @Override
    public int compareTo(Producto producto){
        return this.nombre.compareTo(producto.nombre);
    }
    
    
    
    
}
