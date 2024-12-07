/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventario.productos;

/**
 *
 * @author nicol
 */
public class Electrodomestico extends Producto {
    
    private int garantiaMeses;
    private CategoriasEnergeticas categoriaEnergetica;
    private static final long serialVersionUID = 2L;

    public Electrodomestico(String tipo, String nombre, double precio, int stock, int garantiaMeses, CategoriasEnergeticas categoriaEnergetica) {
        super(tipo, nombre, precio, stock);
        this.garantiaMeses = garantiaMeses;
        this.categoriaEnergetica = categoriaEnergetica;
    }
    
    
    @Override
    public String mostrarDetalles(){
        StringBuilder sb = new StringBuilder();
        sb.append("Tipo: Electrodomestico")
                .append("\nCodigo: ").append(this.getCodigo())
                .append("\nNombre: ").append(this.getNombre())
                .append("\nPrecio: ").append(this.getPrecio())
                .append("\nStock: ").append(this.getStock())
                .append("\nMeses de garantía: ").append(garantiaMeses)
                .append("\nCategoría energetica: ").append(categoriaEnergetica);
        return sb.toString();
    }
    
    
    /*
    Metodo encargado de aplicar el valor pasado por parametro a la variable garantiaMeses, con previa validación
    de que no se trate de un campo negativo o 0
    */
    public void setGarantiaMeses(int garantiaMeses) {
        if(garantiaMeses <= 0){
            throw new IllegalArgumentException("ERROR, NO SE PUEDE INGRESAR UNA GARANTÍA MENOR O IGUAL A 0");
        }
        this.garantiaMeses = garantiaMeses;
    }

    /*
    Metodo encargado de aplicar el valor pasado por parametro a la variable categoriaEnergetica
    */
    public void setCategoriaEnergetica(CategoriasEnergeticas categoriaEnergetica) {
        this.categoriaEnergetica = categoriaEnergetica;
    }

    public int getGarantiaMeses() {
        return garantiaMeses;
    }

    public CategoriasEnergeticas getCategoriaEnergetica() {
        return categoriaEnergetica;
    }
    
    
    
    
    
    
    
}
