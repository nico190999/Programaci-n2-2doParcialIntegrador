/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventario.productos;
/**
 *
 * @author nicol
 */
public class Alimento extends Producto {
    
    private String fechaVencimiento;
    private String refrigeracionRequerida;
    private static final long serialVersionUID = 3L;

    public Alimento(String tipo, String nombre, double precio, int stock, String fechaVencimiento, String refrigeracionRequerida) {
        super(tipo, nombre, precio, stock);
        this.fechaVencimiento = fechaVencimiento;
        this.refrigeracionRequerida = refrigeracionRequerida;
    }
    
    
    @Override
    public String mostrarDetalles(){
        StringBuilder sb = new StringBuilder();
        sb.append("Tipo: Alimento")
                .append("\nCodigo: ").append(this.getCodigo())
                .append("\nNombre: ").append(this.getNombre())
                .append("\nPrecio: ").append(this.getPrecio())
                .append("\nStock: ").append(this.getStock())
                .append("\nFecha de vencimiento: ").append(fechaVencimiento)
                .append("\nRequiere refrigeración: ").append(refrigeracionRequerida);
        return sb.toString();
    }
    
    /*
    Establece el parametro al valor de la variable refrigeracionRequerida
    */
    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    
    /*
    Establece el parametro al valor de la variable refrigeracionRequerida, con previa validación que
    se encuentre entre las respuestas validas
    */
    public void setRefrigeracionRequerida(String refrigeracionRequerida) {
        if(refrigeracionRequerida.equals("SI") || refrigeracionRequerida.equals("SI")){
            this.refrigeracionRequerida = refrigeracionRequerida;
        }else{
            throw new IllegalArgumentException("ERROR, RESPUESTA NO VALIDA");
        }
    }

    public String getRefrigeracionRequerida() {
        return refrigeracionRequerida;
    }
    
    
    
    
    
    
    
    
}
