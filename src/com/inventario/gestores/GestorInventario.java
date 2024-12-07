/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventario.gestores;

import com.inventario.productos.Producto;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.inventario.adaptadores.ProductoAdapter;
import com.inventario.excepciones.ProductoNoEncontradoException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;


/**
 *
 * @author nicol
 */
public class GestorInventario implements MetodosJson, MetodosBinario{
    
    private List<Producto> productos;
    private static final String JSON_FILE = "productos.json";
    private static final String BINARY_FILE = "productos.dat";
    private static final String TXT_FILE = "productos.txt";
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    
    /*
    Constructor de la clase, se inicializa el adaptador
    */
    public GestorInventario() {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Producto.class, new ProductoAdapter())  // Registrar el adaptador
            .create();
        this.productos = new ArrayList<>();
    }

    
    /*
    Función encargada de agregar el producto a la lista. Una vez agregado, guardar los nuevos datos de
    la lista en formato Json y Binario a través de sus funciones correspondientes
    */
    public void save(Producto producto){
        productos.add(producto);
        saveToJson();
        writeBinary();
    }
    
    /*
    Metodo encargado de devolver el producto que corresponda al código que se pase por parametro, se busca
    a este producto a través de la lista.
    En caso de no encontrar el producto, devolver la excepción personalizada ProductoNoEncontradoException
    */
    public Producto buscarProductoPorCodigo(String codigo) throws ProductoNoEncontradoException{ //findByID
        for (Producto producto : productos) {
            if(codigo.equals(producto.getCodigo())){
                return producto;
            }
        }
        throw new ProductoNoEncontradoException("No se encontro un producto con el codigo proporcionado. Intente nuevamente");
    }
    
    
    /*
    Metodo encargado de iterar la lista y eliminar al poducto que coincida con el código que se pase por
    parametro. Luego guardar en archivo json y binario
    removeif elimina TODOS los elementos de la lista que cumplan con la condición que se le pase por parametro.
    "p" representa cada elemento de la lista
    */
    public void eliminarProductoDesdeConsola(String codigo){
        productos.removeIf(p -> p.getCodigo().equals(codigo));
        saveToJson();
        writeBinary();
    }
    
    
    /*
    Función encargada de reemplazar CADA elemento de la lista con el producto que se pasa por parametro,
    de la condición especificada en replaceAll. Si el código del producto p.getCodigo() es igual al
    código del producto que se pasa por parametro, el producto que se pasa por parametro reemplaza a p,
    caso contrariosigue todo igual y devuelve p
    Luego guardar los nuevos datos de la lista en formatoJson y Binario a través de sus funciones 
    correspondientes
    */
    public void update(Producto producto){ //Modifica el objeto de la lista que coincida con el ID
        productos.replaceAll(p -> p.getCodigo().equals(producto.getCodigo()) ? producto : p);
        saveToJson();
        writeBinary();
    }

    
    /*
    Función encargada de retornar la lista de productos
    */
    public List<Producto> getProductos() {
        return productos;
    }
    
    
    /*
    Se pasa el contenido de la lista a nuestro archivo Json. Serializar
    Lo que se encuentra entre parentesís del try es el RECURSO, que se cierra al finalizar el mismo
    FileWriter se usa para escribir texto en un archivo que se pase por parametro.
    Writer maneja operaciones de escritura
    */
    @Override
    public void saveToJson(){
        try(Writer writer = new FileWriter(JSON_FILE)){
            gson.toJson(productos, writer);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    
    
    /*
    Lector de archivo json, encargado de pasar los datos a mi lista. Deserializar
    */
    @Override
    public void loadFromJson() {
        try (Reader lector = new FileReader(JSON_FILE)) {
            Type tipoListaProductos = new TypeToken<ArrayList<Producto>>(){}.getType();
            productos = gson.fromJson(lector, tipoListaProductos);
            if (productos == null) productos = new ArrayList<>();
        } catch (IOException e) {
            productos = new ArrayList<>();
        }
    }
    
    
    
    /*
    Función encargada de la escritura y serialización de nuestra List, añadiendo cada
    producto al archivo binario
    */
    @Override
    public void writeBinary() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(BINARY_FILE))) {
            for (Producto producto : productos) {
                out.writeObject(producto);  // Guarda cada objeto por separado
            }
            System.out.println("Productos guardados correctamente.");
        } catch (IOException e) {
            System.out.println("ERROR al guardar el archivo binario: " + e.getMessage());
        }
    }
    
    
    
    /*
    Función encargada de la lectura y deserialización del archivo binario, añadiendo cada
    producto a la lista
    */
    @Override
    public void readBinary() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(BINARY_FILE))) {
            while (true) {
                try {
                    Producto producto = (Producto) in.readObject();  // Lee un objeto
                    productos.add(producto);  // Agrega el objeto deserializado a la lista
                } catch (EOFException e) {
                    break;  // Fin del archivo
                }
            }
            System.out.println("Productos cargados correctamente.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("ERROR al leer el archivo binario: " + e.getMessage());
        }
    }
    
    
    /*
    Función encargada de guardar nuestra lista en formato TXT
    */
    public void guardarProductosEnTxt() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(TXT_FILE))) {
        int cantidadProductosAlimento = 0;
        int cantidadProductosElectrodomestico = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("REPORTE INVENTARIO: \n\nDetalle de productos:\n\n");
        for (Producto producto : productos) {
            switch(producto.getClass().getSimpleName()){
                case "Alimento":
                    cantidadProductosAlimento++;
                    break;
                case "Electrodomestico":
                    cantidadProductosElectrodomestico++;
                    break;
            }
            sb.append(producto.mostrarDetalles()).append("\n\n");
        }
        sb.append("CANTIDAD PRODUCTOS TOTALES: ").append(cantidadProductosAlimento + cantidadProductosElectrodomestico)
            .append("\nCANTIDAD PRODUCTOS DE TIPO ALIMENTO: ").append(cantidadProductosAlimento)
            .append("\nCANTIDAD PRODUCTOS DE TIPO ELECTRODOMESTICO: ").append(cantidadProductosElectrodomestico);
        writer.write(sb.toString());
        System.out.println("Productos guardados exitosamente en el archivo.");
    } catch (IOException e) {
        System.out.println("ERROR al guardar productos en archivo TXT: " + e.getMessage());
    }
    
}
    
    /*
    Función encargada de la lectura de nuestro archivo txt
    */
    public String leerArchivoTxt() {
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(TXT_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return contenido.toString();
    }
    
    /*Ordenar por nombre natural.
    Llama al metodo comparteTo a través de collections.
    Eñ primer metodo mencionado se encuentra en en Producto*/
    public void sortNaturalName() {
        
        Collections.sort(productos);
        saveToJson();
        writeBinary();
    }
    
    
    
    /*Se encarga de ordenar la lista en base al comparador que se pase por parametro,
    estos se encuentran en difrentes clases y permiten el ordenamiento de menor a 
    mayor precio y stock.
    @param comparator comparador a utilizar
    */
    public void sort(Comparator<Producto> comparator) {
        Collections.sort(productos, comparator);
        saveToJson();
        writeBinary();
    }
    

    
    
    
    
    
}
