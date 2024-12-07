/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventario.gestores;

import com.inventario.productos.Alimento;
import com.inventario.productos.CategoriasEnergeticas;
import com.inventario.productos.Electrodomestico;
import com.inventario.productos.Producto;
import com.inventario.comparadores.PriceComparator;
import com.inventario.comparadores.StockComparator;
import com.inventario.excepciones.ProductoNoEncontradoException;
import com.inventario.excepciones.StockInsuficienteException;
import com.inventario.filtros.FiltroProductos;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 *
 * @author nicol
 */
public class ControladorInventario extends Application {
    
    private Stage primaryStage;
    private GestorInventario gestor = new GestorInventario();
    private ListView<Producto> viewListProductos = new ListView<>();
    private FiltroProductos filtrador;
    private static final int WIDTH = 460;
    private static final int HEIGHT = 520;
    
    
    
    @Override
    public void start(Stage primaryStage) {
        /*Inicializa la interfaz grafica*/
        
        this.primaryStage = primaryStage;
        primaryStage.setTitle("GESTIÓN INVENTARIO");

        // Mostrar la pantalla principal
        pantallaPrincipal();
    }

    
    
    
    /*
    La función se encarga de mostrar la pantalla principal de la interfaz grafica,
    en esta permite interactuar con las diferentes opciones
    */
    private void pantallaPrincipal() {
        
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));
        
        //Botones
        ////////////////////////////////////////////////////////////////////////////////
        
        Button buttonCargarInventarioBinario = new Button("Cargar inventario binario");
        Button buttonCargarInventarioJson = new Button("Cargar inventario Json");
        Button buttonAgregarProducto = new Button("Agregar producto");
        Button buttonMostrarTodosLosProductos = new Button("Mostrar todos los productos");
        Button buttonBusquedaYModificacion = new Button("Busqueda y modificación");
        Button buttonGenerarArchivoTxt = new Button("Generar archivo formato txt");
        Button buttonLeearArchivoTxt = new Button("Leer archivo txt");
        Button buttonRetirarStockDeProducto = new Button("Retirar stock de producto");
        Button buttonFiltro = new Button("Filtrado de productos");
        Button buttonOrdenar = new Button("Ordenamiento");
        Button btnSalir = new Button("Salir");
        
        ////////////////////////////////////////////////////////////////////////////////

        // Acciones de los botones
        ////////////////////////////////////////////////////////////////////////////////
        buttonAgregarProducto.setOnAction(e -> pantallaAgregarProductos());
        //La primera letra hace referencia al EVENTO QUE OCURRE cuando se presiona el botón
        buttonMostrarTodosLosProductos.setOnAction(e -> pantallaVisualizadorProductos());
        buttonBusquedaYModificacion.setOnAction(e -> pantallaBuscarYModificarProductoPorCodigo());
        buttonOrdenar.setOnAction(e -> pantallaOrdenamiento());
        buttonRetirarStockDeProducto.setOnAction(e -> pantallaRetirarStockDeProducto());
        btnSalir.setOnAction(e -> primaryStage.close());
        buttonFiltro.setOnAction(e -> pantallaFiltrado());
        buttonLeearArchivoTxt.setOnAction(e -> pantallaLecturaTxt());
        
        
        buttonCargarInventarioBinario.setOnAction(e -> {
        try{
            gestor.readBinary();
            showAlertConfirmation(STYLESHEET_MODENA, "ARCHIVO BINARIO CARGADO CORRECTAMENTE");
        }catch(Exception ex){
            showAlertError(STYLESHEET_MODENA, "ERROR AL CARGAR EL ARCHIVO BINARIO");
        }
        });
        
        
        buttonCargarInventarioJson.setOnAction(e -> {
        try{
            gestor.loadFromJson();
            showAlertConfirmation(STYLESHEET_MODENA, "ARCHIVO JSON CARGADO CORRECTAMENTE");
        }catch(Exception ex){
            showAlertError(STYLESHEET_MODENA, "ERROR AL CARGAR JSON");
        }
        });
        
        
        buttonGenerarArchivoTxt.setOnAction(e -> {
        try{
            gestor.guardarProductosEnTxt();
            showAlertConfirmation("Se ha generado correctamente el archivo", "EL ARCHIVO SE HA GENERADO CORRECTAMENTE BAJO LA LEYENDA 'productos.txt'");
        }catch(Exception ex){
            showAlertError("ERROR", ex.getMessage());
        }
        });
        
        ////////////////////////////////////////////////////////////////////////////////
        
        

        // Diseño
        //////////////////////////////////////////////////////////////////////////////

        HBox botonesFila1 = new HBox(10, buttonCargarInventarioBinario, buttonCargarInventarioJson);
        HBox botonesFila2 = new HBox(10, buttonAgregarProducto, buttonMostrarTodosLosProductos, buttonBusquedaYModificacion);
        HBox botonesFila3 = new HBox(10, buttonGenerarArchivoTxt, buttonLeearArchivoTxt);
        HBox botonesFila4 = new HBox(10, buttonRetirarStockDeProducto, buttonFiltro, buttonOrdenar);
        HBox botonesFila5 = new HBox(10, btnSalir);

        //Posicionar en el centro
        botonesFila1.setAlignment(Pos.CENTER);
        botonesFila2.setAlignment(Pos.CENTER);
        botonesFila3.setAlignment(Pos.CENTER);
        botonesFila4.setAlignment(Pos.CENTER);
        botonesFila5.setAlignment(Pos.CENTER);

        
        mainLayout = new VBox(20, botonesFila1, botonesFila2, botonesFila3, botonesFila4, botonesFila5);


        mainLayout.setAlignment(Pos.CENTER);

        
        Scene scene = new Scene(mainLayout, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    
    /*
    La función se encarga de mostrar la pantalla de agregación de productos,
    en esta se permite ingresar nuevos productos a la lista completando los campos
    correspondientes
    */
    private void pantallaAgregarProductos() {
        
        // Contenedor principal
        ////////////////////////////////////////////////////////////////////////////////
        
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));
        
        ////////////////////////////////////////////////////////////////////////////////

        // ComboBox para seleccionar tipo de producto
        ComboBox<String> comboTipoProducto = new ComboBox<>();
        comboTipoProducto.getItems().addAll("Alimento", "Electrodomestico");
        comboTipoProducto.setPromptText("Seleccionar tipo de producto");

        // Campos comunes

        TextField campoNombre = new TextField();
        campoNombre.setPromptText("Nombre");

        TextField campoPrecio = new TextField();
        campoPrecio.setPromptText("Precio");
        
        TextField campoStock = new TextField();
        campoStock.setPromptText("Stock");

        // Campos específicos para "Alimento"
        DatePicker campoFechaVencimiento = new DatePicker();
        campoFechaVencimiento.setPromptText("Fecha de vencimiento (YYYY-MM-DD)");

        ComboBox<String> campoRefrigeracion = new ComboBox<>();
        campoRefrigeracion.getItems().addAll("SI", "NO");
        campoRefrigeracion.setPromptText("¿Requiere refrigeración?");

        // Campos específicos para "Electrodoméstico"
        TextField campoGarantia = new TextField();
        campoGarantia.setPromptText("Garantía (en meses)");

        ComboBox<CategoriasEnergeticas> campoCategoriaEnergetica = new ComboBox<>();
        campoCategoriaEnergetica.getItems().addAll(CategoriasEnergeticas.A, CategoriasEnergeticas.B, CategoriasEnergeticas.C);
        campoCategoriaEnergetica.setPromptText("Seleccionar categoría energetica");

        // Contenedor para campos dinámicos
        VBox contenedorCamposDinamicos = new VBox(10);

        // Listener para el ComboBox
        comboTipoProducto.setOnAction(e -> {
            String seleccion = comboTipoProducto.getValue();
            contenedorCamposDinamicos.getChildren().clear();

            if ("Alimento".equals(seleccion)) {
                contenedorCamposDinamicos.getChildren().addAll(campoFechaVencimiento, campoRefrigeracion);
            } else if ("Electrodomestico".equals(seleccion)) {
                contenedorCamposDinamicos.getChildren().addAll(campoGarantia, campoCategoriaEnergetica);
            }
        });

        // Botón para guardar el producto
        Button botonGuardar = new Button("Guardar producto");
        botonGuardar.setOnAction(e -> {
        try{
            String nombre = campoNombre.getText();
            double precio = Double.parseDouble(campoPrecio.getText());
            int stock = Integer.parseInt(campoStock.getText());
            
            if ("Alimento".equals(comboTipoProducto.getValue())) {
                
                LocalDate selectedDate = campoFechaVencimiento.getValue();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String dateString = selectedDate.format(formatter);
                
                String refrigeracion = campoRefrigeracion.getValue();
                Alimento alimento = new Alimento("Alimento", nombre, precio, stock, dateString, refrigeracion);
                gestor.save(alimento);
            } else if ("Electrodomestico".equals(comboTipoProducto.getValue())) {
                int garantia = Integer.parseInt(campoGarantia.getText());
                CategoriasEnergeticas categoriaEnergetica = campoCategoriaEnergetica.getValue();
                Electrodomestico electrodomestico = new Electrodomestico("Electrodomestico", nombre, precio, stock, garantia, categoriaEnergetica);
                gestor.save(electrodomestico);
            }
            
            showAlertConfirmation("Agregación exitosa",
                    "El producto se agrego correctamente a la lista\nYa se encuentra actualizado en Json y Archivo Binario");
            
        }catch(NumberFormatException ex){
            showAlertError("ERROR", "Ingreso invalido\nIntente nuevamente");
        }catch (Exception ex){
            showAlertError("ERROR", "Error, intente nuevamente");
        }finally{
            campoNombre.clear();
            campoPrecio.clear();
            campoStock.clear();
            campoGarantia.clear();
        }
        });

        // Botón para volver al menú principal
        Button botonBackMainMenu = new Button("Volver al menú principal");
        
        
        botonBackMainMenu.setOnAction(b -> pantallaPrincipal());
        

        // Diseño
        mainLayout.getChildren().addAll(new Label("Agregar producto"),
            comboTipoProducto,
            campoNombre,
            campoPrecio,
            campoStock,
            contenedorCamposDinamicos,
            new HBox(10, botonGuardar, botonBackMainMenu)
        );

        // Configurar escena
        Scene scene = new Scene(mainLayout, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
    }
    
    
    /*
    La función se encarga de mostrar la pantalla en la que se visuzalizan los detalles de los productos
    */
    private void pantallaVisualizadorProductos(){
        // Crear botones
        Button actualizar = new Button("Actualizar");
        Button botonElementoStock = new Button("Grafico estadistico <elemento, stock>");
        Button returnToMainMenu = new Button("Volver al menu principal");
        
        // ListView
        viewListProductos.setCellFactory(param -> new ListCell<Producto>() {
            @Override
            protected void updateItem(Producto producto, boolean empty) {
                super.updateItem(producto, empty);
                if (producto != null) {
                    setText(producto.mostrarDetalles());
                } else {
                    setText(null);
                }
            }
    });

        // Acciones de los botones
        actualizar.setOnAction(e -> {
            // Limpiar la lista actual
            viewListProductos.getItems().clear();

            // Agregar todos los productos al ListView
            viewListProductos.getItems().addAll(gestor.getProductos());
        });
        
        botonElementoStock.setOnAction(e -> pantallaEstadisticaElemntoStock());
        
        returnToMainMenu.setOnAction(e -> pantallaPrincipal());
        
        

        // Diseño
        VBox root = new VBox(10, actualizar,botonElementoStock, returnToMainMenu, viewListProductos);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Crear escena y mostrarla
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    
    /*
    La función se encarga de mostrar la pantalla de modificación de productos, en esta se puede buscar,
    modificar o eliminar de nuestra lista de productos
    */
    private void pantallaBuscarYModificarProductoPorCodigo() {        
                            
                            //CONTENEDOR
        /////////////////////////////////////////////////////////
        
        VBox mainLoyout = new VBox(10); //instancia una caja vertical
        mainLoyout.setPadding(new Insets(10));
        
        /////////////////////////////////////////////////////////
        
        
        //CAMPOS DE ENTRADA     
        TextField campoCodigo = new TextField();
        campoCodigo.setPromptText("Codigo de producto"); 
        
        TextField campoNombre = new TextField();
        campoNombre.setPromptText("Nuevo nombre del producto");

        TextField campoPrecio = new TextField();
        campoPrecio.setPromptText("Nuevo precio del producto");

        TextField campoStock = new TextField();
        campoStock.setPromptText("Nuevo stock del producto");
        
        //Campos de Alimento
        DatePicker campoFechaVencimiento = new DatePicker();
        campoFechaVencimiento.setPromptText("Fecha de vencimiento (YYYY-MM-DD)");
        
        ComboBox<String> campoRefrigeracion = new ComboBox<>();
        campoRefrigeracion.getItems().addAll("SI", "NO");
        campoRefrigeracion.setPromptText("¿Requiere refrigeración?");
        
        //Campos de Electrodomestico
        TextField campoGarantia = new TextField();
        campoGarantia.setPromptText("Garantía (en meses)");

        ComboBox<CategoriasEnergeticas> campoCategoriaEnergetica = new ComboBox<>();
        campoCategoriaEnergetica.getItems().addAll(CategoriasEnergeticas.A, CategoriasEnergeticas.B, CategoriasEnergeticas.C);
        campoCategoriaEnergetica.setPromptText("Seleccionar categoría energetica");

        // CAMPOS INICIALES OCULTOS
        campoNombre.setVisible(false);
        campoPrecio.setVisible(false);
        campoStock.setVisible(false);
        
        //CAMPOS DE ALIMENTO OCULTO
        campoFechaVencimiento.setVisible(false);
        campoRefrigeracion.setVisible(false);
        
        //CANPOS DE ELECTRODOMESTICO OCULTO
        campoGarantia.setVisible(false);
        campoCategoriaEnergetica.setVisible(false);
        

        // BOTÓN PARA MODIFICAR
        Button botonModificar = new Button("Guardar cambios");
        botonModificar.setVisible(false);
        
        //BOTON ELIMINAR
        Button botonEliminar = new Button("Eliminar producto");
        botonEliminar.setVisible(false);
        
        //BOTON BUSCAR
        Button botonBuscar = new Button("Buscar");
        
        
        /////////////////////////////////////////////////////////
        
        
        
                        //ACCIÓN BOTON BUSCAR
        /////////////////////////////////////////////////////////
        
        Label resultadoBusqueda = new Label();
        
        Producto[] productoEncontrado = {null};
        
        botonBuscar.setOnAction((ActionEvent e) -> {
        try{
            String codigo = campoCodigo.getText();
            Producto producto = gestor.buscarProductoPorCodigo(codigo);
            if(producto != null){
                resultadoBusqueda.setText("Producto encontrado: \n" + producto.mostrarDetalles());
                
                productoEncontrado[0] = producto;

 
                campoNombre.setText(producto.getNombre());
                campoPrecio.setText(String.valueOf(producto.getPrecio()));
                campoStock.setText(String.valueOf(producto.getStock()));
                
                if (producto instanceof Alimento) {
                    campoFechaVencimiento.setVisible(true);
                    campoRefrigeracion.setVisible(true);
                    campoGarantia.setVisible(false);
                    campoCategoriaEnergetica.setVisible(false);
                    campoRefrigeracion.setValue(((Alimento) producto).getRefrigeracionRequerida());
                } else if (producto instanceof Electrodomestico) {
                    campoGarantia.setVisible(true);
                    campoCategoriaEnergetica.setVisible(true);
                    campoFechaVencimiento.setVisible(false);
                    campoRefrigeracion.setVisible(false);
                    campoGarantia.setText(String.valueOf(((Electrodomestico) producto).getGarantiaMeses()));
                    campoCategoriaEnergetica.setValue(((Electrodomestico) producto).getCategoriaEnergetica());
                }
                
                campoNombre.setVisible(true);
                campoPrecio.setVisible(true);
                campoStock.setVisible(true);
                botonModificar.setVisible(true);
                botonEliminar.setVisible(true);
            }
        }catch(ProductoNoEncontradoException ex){
            showAlertError(STYLESHEET_MODENA, ex.getMessage());
        }
        });
        
        /////////////////////////////////////////////////////////
        
                        //ACCIÓN BOTON ELIMINAR
        /////////////////////////////////////////////////////////
        botonEliminar.setOnAction(e -> {
        try{
            gestor.eliminarProductoDesdeConsola(productoEncontrado[0].getCodigo());
            
            showAlertConfirmation("ELIMINACIÓN PRODUCTO", "PRODUCTO ELIMINADO CORRECTAMENTE");
            resultadoBusqueda.setText("Producto eliminado correctamente");
            
            campoCategoriaEnergetica.setVisible(false);
            campoFechaVencimiento.setVisible(false);
            campoGarantia.setVisible(false);
            campoNombre.setVisible(false);
            campoPrecio.setVisible(false);
            campoRefrigeracion.setVisible(false);
            campoStock.setVisible(false);
            botonModificar.setVisible(false);
            botonEliminar.setVisible(false);
        }catch(Exception ex){
            showAlertError("ERROR ELIMINACIÓN PRODUCTO", "Error: " + ex.getMessage());
        }
        });
        
        /////////////////////////////////////////////////////////
        
                        //ACCIÓN BOTON MODIFICAR
        /////////////////////////////////////////////////////////
        
        botonModificar.setOnAction(e -> {
        try{
            productoEncontrado[0].setNombre(campoNombre.getText());
            productoEncontrado[0].setPrecio(Double.parseDouble(campoPrecio.getText()));
            productoEncontrado[0].setStock(Integer.parseInt(campoStock.getText()));
            
            if (productoEncontrado[0] instanceof Alimento) {
                Alimento alimento = (Alimento) productoEncontrado[0];
                alimento.setRefrigeracionRequerida(campoRefrigeracion.getValue());

                LocalDate selectedDate = campoFechaVencimiento.getValue();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String dateString = selectedDate.format(formatter);

                alimento.setFechaVencimiento(dateString);
            } else if (productoEncontrado[0] instanceof Electrodomestico) {
                Electrodomestico electrodomestico = (Electrodomestico) productoEncontrado[0];
                electrodomestico.setGarantiaMeses(Integer.parseInt(campoGarantia.getText()));
                electrodomestico.setCategoriaEnergetica(campoCategoriaEnergetica.getValue());
            }
            
            gestor.update(productoEncontrado[0]);
            
            showAlertConfirmation("MODIFICACIÓN PRODUCTO", "Producto modificado correctamente");
            resultadoBusqueda.setText("Producto modificado:\n" + productoEncontrado[0].mostrarDetalles());
            
            campoCategoriaEnergetica.setVisible(false);
            campoFechaVencimiento.setVisible(false);
            campoGarantia.setVisible(false);
            campoNombre.setVisible(false);
            campoPrecio.setVisible(false);
            campoRefrigeracion.setVisible(false);
            campoStock.setVisible(false);
            botonModificar.setVisible(false);
            botonEliminar.setVisible(false);
            
        }catch(Exception ex){
            showAlertError("ERROR AL MODIFICAR PRODUCTO", "Error: " + ex.getMessage());
        }
        });
        
        /////////////////////////////////////////////////////////



        
                    //BOTON VOLVER AL MENU PRINCIPAL
        /////////////////////////////////////////////////////////
        
        Button botonBackMainMenu = new Button("Volver al menu principal");
        botonBackMainMenu.setOnAction(b -> pantallaPrincipal());
        
        /////////////////////////////////////////////////////////
        
        
        
        
        
                //CONFIGURACIÓN VISTA/DISEÑO DE FORMULARIO
        /////////////////////////////////////////////////////////    
        
        GridPane inputgrid = new GridPane(); 
        inputgrid.setHgap(10);
        inputgrid.setVgap(10);
        inputgrid.addRow(0, new Label("Ingresar código de producto:"), campoCodigo);
        
        GridPane editGrid = new GridPane();
        editGrid.setHgap(10);
        editGrid.setVgap(10);
        editGrid.addRow(0, new Label("Nombre:"), campoNombre);
        editGrid.addRow(1, new Label("Precio:"), campoPrecio);
        editGrid.addRow(2, new Label("Stock:"), campoStock);
        editGrid.addRow(3, new Label("F. Vencimiento:"), campoFechaVencimiento);
        editGrid.addRow(4, new Label("Refrigeracion:"), campoRefrigeracion);
        editGrid.addRow(5, new Label("Garantia:"), campoGarantia);
        editGrid.addRow(6, new Label("Categoria electrica:"), campoCategoriaEnergetica);
        
        
        /////////////////////////////////////////////////////////
        
        
        
                    //CONTENEDOR DE BOTONES
        /////////////////////////////////////////////////////////
        
        HBox contenedorBotonesBuscarYBackMainMenu = new HBox(5,botonBuscar, botonBackMainMenu);
        HBox contenedorModificarYEliminar = new HBox(5, botonEliminar, botonModificar);
        
        /////////////////////////////////////////////////////////
        
        
        
                //AGREGAR COMPONENTES AL LAYOUT PRINCIPAL
        /////////////////////////////////////////////////////////
        
        mainLoyout.getChildren().addAll(inputgrid, contenedorBotonesBuscarYBackMainMenu, 
                resultadoBusqueda, editGrid, contenedorModificarYEliminar);
        
        /////////////////////////////////////////////////////////
        
        
        
                    //CONFIGURACIÓN ESCENA
        /////////////////////////////////////////////////////////
        
        Scene scene = new Scene(mainLoyout, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        
        /////////////////////////////////////////////////////////
    }
    
    
    /*
    La función se encarga de mostrar la pantalla principal de la interfaz grafica,
    en esta permite interactuar con las diferentes opciones
    */
    private void pantallaOrdenamiento(){
        
        Button buttonOrdenarNaturalmente = new Button("Ordenar por nombre naturalmente");
        Button buttonOrdenarPrecio = new Button("Ordenar por precio");
        Button buttonOrdenarStock = new Button("Ordenar por stock");
        Button backMainMenu = new Button("Volver al menu principal");

      
        buttonOrdenarNaturalmente.setOnAction(e -> {
        try{
            gestor.sortNaturalName();
            showAlertConfirmation(STYLESHEET_MODENA, "PRODUCTOS ORDENADOS POR NOMBRE CORRECTAMENTE");
        }catch(Exception ex){
            showAlertError(STYLESHEET_MODENA, "Error");
        }});
        
        buttonOrdenarStock.setOnAction(e -> {
        try{
            gestor.sort(new StockComparator());
            showAlertConfirmation(STYLESHEET_MODENA, "PRODUCTOS ORDENADOS POR STOCK CORRECTAMENTE");
        }catch(Exception ex){
            showAlertError(STYLESHEET_MODENA, "Error");
        }
        });
        
        buttonOrdenarPrecio.setOnAction(e -> {
        try{
            gestor.sort(new PriceComparator());
            showAlertConfirmation(STYLESHEET_MODENA, "PRODUCTOS ORDENADOS POR PRECIO CORRECTAMENTE");
        }catch(Exception ex){
            showAlertError(STYLESHEET_MODENA, "Error");
        }
        });
        
        backMainMenu.setOnAction(e -> pantallaPrincipal());
        
        

      
        VBox root = new VBox(10, buttonOrdenarNaturalmente,buttonOrdenarPrecio, buttonOrdenarStock, backMainMenu);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

       
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    
    /*
    La función se encarga de mostrar la pantalla que permite retirar productos del stock del producto.
    En primera instancia lo busca, una vez encontrado se debe agregar la cantidad a retirar
    */
    private void pantallaRetirarStockDeProducto(){
        
        VBox layoutPrincipal = new VBox(10);
        layoutPrincipal.setPadding(new Insets(10));
        
     
        TextField campoCodigo = new TextField();
        campoCodigo.setPromptText("Codigo del producto");
        
        TextField campoStockARetirar = new TextField();
        campoStockARetirar.setPromptText("Cantidad a retirar");
        
        
        
        //Botones
        Button botonBuscar = new Button("Buscar");
        Button botonRestar = new Button("Restar");
        botonRestar.setVisible(false);
        Button botonBackMainMenu = new Button("Volver al menu principal");
        
        

        
        
        botonBackMainMenu.setOnAction(e -> pantallaPrincipal());
        
        // Variable para almacenar el producto encontrado en el botón BUSCAR
        Producto[] productoEncontrado = {null};
        
        Label resultadoBusqueda = new Label();
        botonBuscar.setOnAction(e -> {
        try{
            String codigo = campoCodigo.getText();
            Producto producto = gestor.buscarProductoPorCodigo(codigo);
            if(producto != null){
                resultadoBusqueda.setText("Producto encontrado: \n" + producto.getNombre() + "\nStock: " + producto.getStock());
                botonRestar.setVisible(true); 
                productoEncontrado[0] = producto;
            }    
        }catch(ProductoNoEncontradoException ex){
            showAlertError(STYLESHEET_MODENA, "Error: " + ex.getMessage());
        }
        });
        
        Operacion resta = (a, b) -> a - b;
        botonRestar.setOnAction(e -> {
            try {
                
                int stockARetirar = Integer.parseInt(campoStockARetirar.getText());
                
                if(productoEncontrado[0].verificarDisponibilidad(stockARetirar)){
                    productoEncontrado[0].setStock(resta.ejecutar(productoEncontrado[0].getStock(), stockARetirar));
                    gestor.update(productoEncontrado[0]);
                    showAlertConfirmation("ACTUALIZACIÓN PRODUCTO", "Se actualizo el producto correctamente");
                    resultadoBusqueda.setText("Producto actualizado:\nNombre: " + productoEncontrado[0].getNombre() + "\nStock: " + productoEncontrado[0].getStock());
                }
            } catch (StockInsuficienteException ex) {
                showAlertError("ERROR", "Error: " + ex.getMessage());
            }finally{
                campoCodigo.clear();
                campoStockARetirar.clear();
                botonRestar.setVisible(false);
            }
        });
        
        
        
        // CONTENEDOR DE CAMPOS
        GridPane gridEntrada = new GridPane();
        gridEntrada.setHgap(10);
        gridEntrada.setVgap(10);
        gridEntrada.addRow(0, new Label("Código:"), campoCodigo);
        gridEntrada.addRow(1, new Label("Cantidad a retirar:"), campoStockARetirar);
        
        
       // Contenedor de botones
        HBox contenedorBotones = new HBox(10,botonBuscar,botonRestar, botonBackMainMenu);
        
        //LAYOUT PRINCIPAL
        layoutPrincipal.getChildren().addAll(
            gridEntrada,
            contenedorBotones,
            resultadoBusqueda
        );

        // Crear escena y mostrarla
        Scene scene = new Scene(layoutPrincipal, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    
    /*
    La función se encarga de mostrar la pantalla donde se pueden visuzlizar diversas opciones de filtrado,
    una vez hecho el filtro, se actualiza en nuestra lista
    */
    private void pantallaFiltrado(){
        // Crear botones
        Button filtrarProductosBaratos = new Button("Productos baratos (menos de 10.000 pesos)");
        Button filtrarProductosCaros = new Button("Productos caros (mas de 10.000 pesos)");
        Button hashMap = new Button("HashMap 'Código, Nombre'");
        Button returnToMainMenu = new Button("Volver al menu principal");
        
        // Configurar el ListView
        viewListProductos.setCellFactory(param -> new ListCell<Producto>() {
            @Override
            protected void updateItem(Producto producto, boolean empty) {
                super.updateItem(producto, empty);
                if (producto != null) {
                    setText(producto.mostrarDetalles());
                } else {
                    setText(null);
                }
            }
    });

        
        
        viewListProductos.setVisible(false);
        viewListProductos.setManaged(false);
        
        Label resultadoBusqueda = new Label();
        
        filtrarProductosBaratos.setOnAction(e -> {
            resultadoBusqueda.setText("");
            viewListProductos.setVisible(true);
            viewListProductos.setManaged(true);
            viewListProductos.getItems().clear();
            List<Producto> productosBaratos = 
                    filtrador.filtrarProductos(gestor.getProductos(), p -> p.getPrecio() < 10000);
            viewListProductos.getItems().addAll(productosBaratos);
        });      
        
        filtrarProductosCaros.setOnAction(e -> {
            resultadoBusqueda.setText("");
            viewListProductos.setVisible(true);
            viewListProductos.setManaged(true);
            viewListProductos.getItems().clear();
            List<Producto> productosCaros = 
                    filtrador.filtrarProductos(gestor.getProductos(), p -> p.getPrecio() > 10000);
            viewListProductos.getItems().addAll(productosCaros);
        });
        
        returnToMainMenu.setOnAction(e -> pantallaPrincipal());
        
        
        hashMap.setOnAction(e -> {
            viewListProductos.getItems().clear();
            viewListProductos.setVisible(false);
            viewListProductos.setManaged(false);
            HashMap <String, String> productosHashMap = new HashMap<>();
            Iterator<Producto> iterator = gestor.getProductos().iterator(); //iterator permite recorrer
            //la lista
            while (iterator.hasNext()) { //mientras tenga otro elemento
                Producto producto = iterator.next(); //obtiene el metodo de la coleccion y pasa al siguiente
                productosHashMap.put(producto.getCodigo(), producto.getNombre());
            }
            StringBuilder sb = new StringBuilder();
            for (var entry : productosHashMap.entrySet()) {
                String item = "Código: " + entry.getKey() + ", Nombre: " + entry.getValue();
                sb.append(item).append("\n");
            }
            resultadoBusqueda.setText(sb.toString());
        });
        
        

        // Crear diseño
        VBox root = new VBox(10, filtrarProductosBaratos, filtrarProductosCaros, hashMap,
                returnToMainMenu, resultadoBusqueda, viewListProductos);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Crear escena y mostrarla
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    
    /*
    La función se encarga de mostrar la pantalla donde podemos visualizar estadisticas con el uso de 
    elementos Chart
    */
    private void pantallaEstadisticaElemntoStock(){
        
        Map<String, Integer> inventario = new HashMap<>();
        for (Producto producto : gestor.getProductos()) {
            inventario.put(producto.getNombre(), producto.getStock());
        }
        
        Button botonReturn = new Button("Volver");
        botonReturn.setOnAction(e -> pantallaVisualizadorProductos());

        //Ejes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Producto");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Stock");
        
        
        //Configuracion grafico de barras
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Inventario de Productos <elemento,Stock>");
        
        //Datos del grafico
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Stock Actual");
        
        for (Map.Entry<String, Integer> entry : inventario.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        
        barChart.getData().add(series);
        
        VBox root = new VBox(10, barChart, botonReturn);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");
        
        
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Gráfico de Inventario");
        primaryStage.setScene(scene);
        primaryStage.show();
        
    } 
    
    
    /*
    La función se encarga de mostrar la pantalla donde podemos visualizar el texto del archivo formato t
    */
    private void pantallaLecturaTxt(){
        // Crear botones
        Button botonMostrar = new Button("Mostrar");
        Button returnToMainMenu = new Button("Volver al menu principal");
        

    

        
        
        
        
        Label resultadoBusqueda = new Label();
        
        ScrollPane scrollPane = new ScrollPane(resultadoBusqueda);
        scrollPane.setFitToWidth(true); //ancho, se ajusta automaticamente
        scrollPane.setPrefHeight(200); //altura
        
        botonMostrar.setOnAction(e -> resultadoBusqueda.setText(gestor.leerArchivoTxt()));
        
        
        
        returnToMainMenu.setOnAction(e -> pantallaPrincipal());
        
        
        
        
        

        // Crear diseño
        VBox root = new VBox(10, botonMostrar, scrollPane, returnToMainMenu);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Crear escena y mostrarla
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    
    
    /*
    La función se encarga enseñar una alerta de error, se pasa por parametro el titulo y 
    contenido que deseemos aparezca en la alerta
    */
    private void showAlertError(String titulo, String contenido){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(contenido);
        alert.show();
    }
    
    /*
    La función se encarga enseñar una alerta de Confirmación, se pasa por parametro el titulo y 
    contenido que deseemos aparezca en la alerta
    */
    private void showAlertConfirmation(String titulo, String contenido){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setContentText(contenido);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
