/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventario.adaptadores;
import com.google.gson.*;
import com.inventario.productos.Alimento;
import com.inventario.productos.Electrodomestico;
import com.inventario.productos.Producto;
import java.lang.reflect.Type;
/**
 *
 * @author nicol
 * Su función es deserializar los objetos subclase de Producto. Json no puede deserializar clases abstractas
 */
public class ProductoAdapter implements JsonDeserializer<Producto> {
    
    @Override
    public Producto deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // Obtener el objeto JSON
        JsonObject jsonObject = json.getAsJsonObject();
        
        // Leer el campo "tipo" que nos indica la subclase
        String tipo = jsonObject.get("tipo").getAsString();
        
        // Según el valor del campo "tipo", decidir qué clase instanciar
        if ("Alimento".equals(tipo)) {
            return context.deserialize(json, Alimento.class);  // Deserializar como Alimento
        } else if ("Electrodomestico".equals(tipo)) {
            return context.deserialize(json, Electrodomestico.class);  // Deserializar como Electrodomestico
        }
        
        throw new JsonParseException("Tipo de producto desconocido: " + tipo);
    }
    
}
