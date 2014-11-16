package com.mycompany.pfinanzaspersonales;

import App.AgregarGastos;
import App.AgregarIngresos;
import App.DataUsuario;

import Extras.Config;
import Extras.JSON;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.controlsfx.control.PropertySheet.Item;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.json.JSONArray;
import org.json.JSONObject;


public class FXMLController implements Initializable {
    
    @FXML TableView tabla_gastos;
    
    @FXML TableColumn editar_tgastos;
    @FXML TableColumn eliminar_tgastos;
    @FXML TableColumn fecha_tgastos;
    @FXML TableColumn categoria_tgastos;
    @FXML TableColumn mpago_tgastos;
    @FXML TableColumn monto_tgastos;
    
    @FXML TableView tabla_ingresos;
    @FXML TableColumn editar_tingresos;
    @FXML TableColumn eliminar_tingresos;
    @FXML TableColumn fecha_tingresos;
    @FXML TableColumn categoria_tingresos;
    @FXML TableColumn mpago_tingresos;
    @FXML TableColumn monto_tingresos;
    
    @FXML Label lbl_total_saldo;
    @FXML Label lbl_total_gastos;
    
    @FXML Label bienvenido;
    
    @FXML Tab lbl_gasto_mensual;
    @FXML Tab lbl_ingreso_mensual;
    @FXML TitledPane lbl_info_mes;
    
    List<TablaGastosIngresos> tabla_ingresos_json;
    List<TablaGastosIngresos> tabla_gastos_json;
    
    @FXML
    private void menu_categoria_ingresos(ActionEvent event) throws IOException{
        
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/CategoriaIngresos.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    private void menu_categoria_gastos(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/CategoriaGastos.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    private void menu_add_gastos(ActionEvent event) throws IOException{
        AgregarGastos AG = AgregarGastos.getInstance();
        AG.setActualizacion(false);
        
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/AgregarGastos.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    private void menu_add_ingresos(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/AgregarIngresos.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show(); 
    }
    
    @FXML
    private void menu_buscador(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/Buscador.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    private void menu_actualizar(ActionEvent event) throws IOException{
        tabla_gastos.getItems().clear();
        cargarTablaGastos();
        cargarSaldos();
        cargarTablaIngresos();
    }
    
    @FXML
    private void menu_gastos_agrupados(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/GastosAgrupados.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    private void menu_ingresos_agrupados(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/IngresosAgrupados.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    private void menu_curvas(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/CurvaS.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    private void menu_balance(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/Balance.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        cargarSaldos();
        
        bienvenido.setText( "Bienvenido: "+DataUsuario.getInstance().getNombre() );
        
        lbl_gasto_mensual.setText("Gastos ("+Config.MesActual()+") ");
        lbl_ingreso_mensual.setText("Ingresos ("+Config.MesActual()+") ");
        lbl_info_mes.setText("Información mes de " + Config.MesActual());
        
        
        
        final Task<Void> tarea = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                
                cargarTablaGastos();
                cargarTablaIngresos();
                return null;
            }

            @Override protected void succeeded() {
                super.succeeded();
                
                eliminar_tgastos.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
                    @Override
                    public TableCell<String, String> call(TableColumn<String, String> p) {

                        return new TableCell<String, String>() {
                            @Override
                            public void updateItem(String item, boolean empty) {

                                super.updateItem(item, empty);
                                
                                if(!isEmpty() && !empty){

                                    final VBox vbox = new VBox(5);
                                    Image image = new Image(getClass().getResourceAsStream("/Imagenes/delete.png"));
                                    final Button boton = new Button("", new ImageView(image));
                                    boton.setOnAction(new EventHandler<ActionEvent>() {
                                          @Override
                                          public void handle(ActionEvent event) {

                                            Action response = Dialogs.create()
                                                    .title("Eliminar Gasto")
                                                    .message("Estás seguro que desaeas eliminar el gasto?")
                                                    .showConfirm();

                                            if (response == Dialog.ACTION_YES) {
                                                
                                                TablaGastosIngresos gastos = tabla_gastos_json.get(getTableRow().getIndex());
                                                
                                                List<NameValuePair> parametros = new ArrayList<NameValuePair>();
                                                parametros.add(new BasicNameValuePair("idgastos", gastos.getId() ));
                                                String url= Config.URL+"gastos/eliminar.json";
                                                HttpResponse responseJSON = JSON.request(url,parametros);
                                                JSONObject jObject = JSON.JSON(responseJSON);
                                                int code = Integer.parseInt(jObject.get("code").toString());
                                               
                                                if(code == 201){
                                                    int selectdIndex = getTableRow().getIndex();
                                                    tabla_gastos_json.remove(selectdIndex);
                                                    tabla_gastos.getItems().remove(selectdIndex);
                                                }else{
                                                    Dialogs.create()
                                                            .title("Error sincronización")
                                                            .message("Hubo un error al intentar eliminar el gasto. ERROR: 301")
                                                            .showInformation();
                                                }
                                                
                                                
                                            } 

                                          }
                                    });
                                    
                                    vbox.getChildren().add(boton);
                                    setGraphic(vbox);
                                }else{
                                    setGraphic(null);
                                }
                            }
                        };
                    }
                });
                
                
                editar_tgastos.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
                    @Override
                    public TableCell<String, String> call(TableColumn<String, String> p) {

                        return new TableCell<String, String>() {
                            @Override
                            public void updateItem(String item, boolean empty) {

                                super.updateItem(item, empty);
                                
                                if(!isEmpty() && !empty){

                                    final VBox vbox = new VBox(5);
                                    Image image = new Image(getClass().getResourceAsStream("/Imagenes/edit.png"));
                                    final Button boton = new Button("", new ImageView(image));
                                    boton.setOnAction(new EventHandler<ActionEvent>() {
                                          @Override
                                          public void handle(ActionEvent event) {
                                            
                                              
                                            TablaGastosIngresos gastos = tabla_gastos_json.get(getTableRow().getIndex());
                                            AgregarGastos AG = AgregarGastos.getInstance();
                                            
                                            AG.setID(gastos.getId());
                                            AG.setMonto(gastos.getMonto());
                                            AG.setCategoria(gastos.getCategoria());
                                            AG.setPago(gastos.getPago());
                                            AG.setActualizacion(true);
                                            
                                            Parent parent = null;
                                            try {
                                                parent = FXMLLoader.load(this.getClass().getResource("/fxml/AgregarGastos.fxml"));
                                            } catch (IOException ex) {
                                                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                            
                                            Stage stage = new Stage();
                                            Scene scene = new Scene(parent);
                                            stage.setScene(scene);
                                            stage.show();

                                          }
                                    });
                                    
                                    vbox.getChildren().add(boton);
                                    setGraphic(vbox);
                                }else{
                                    setGraphic(null);
                                }
                            }
                        };
                    }
                });
                
                
                
                eliminar_tingresos.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
                    @Override
                    public TableCell<String, String> call(TableColumn<String, String> p) {

                        return new TableCell<String, String>() {
                            @Override
                            public void updateItem(String item, boolean empty) {

                                super.updateItem(item, empty);
                                
                                if(!isEmpty() && !empty){

                                    final VBox vbox = new VBox(5);
                                    Image image = new Image(getClass().getResourceAsStream("/Imagenes/delete.png"));
                                    final Button boton = new Button("", new ImageView(image));
                                    boton.setOnAction(new EventHandler<ActionEvent>() {
                                          @Override
                                          public void handle(ActionEvent event) {

                                            Action response = Dialogs.create()
                                                    .title("Eliminar Gasto")
                                                    .message("Estás seguro que desaeas eliminar el ingreso?")
                                                    .showConfirm();

                                            if (response == Dialog.ACTION_YES) {
                                                
                                                TablaGastosIngresos ingresos = tabla_ingresos_json.get(getTableRow().getIndex());
                                                
                                                List<NameValuePair> parametros = new ArrayList<NameValuePair>();
                                                parametros.add(new BasicNameValuePair("idingresos", ingresos.getId() ));
                                                String url= Config.URL+"ingresos/eliminar.json";
                                                HttpResponse responseJSON = JSON.request(url,parametros);
                                                JSONObject jObject = JSON.JSON(responseJSON);
                                                int code = Integer.parseInt(jObject.get("code").toString());
                                               
                                                if(code == 201){
                                                    int selectdIndex = getTableRow().getIndex();
                                                    tabla_ingresos_json.remove(selectdIndex);
                                                    tabla_ingresos.getItems().remove(selectdIndex);
                                                }else{
                                                    Dialogs.create()
                                                            .title("Error sincronización")
                                                            .message("Hubo un error al intentar eliminar el ingreso. ERROR: 301")
                                                            .showInformation();
                                                }
                                                
                                            } 

                                          }
                                    });
                                    
                                    vbox.getChildren().add(boton);
                                    setGraphic(vbox);
                                }else{
                                    setGraphic(null);
                                }
                            }
                        };
                    }
                });
                
                
                editar_tingresos.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
                    @Override
                    public TableCell<String, String> call(TableColumn<String, String> p) {

                        return new TableCell<String, String>() {
                            @Override
                            public void updateItem(String item, boolean empty) {

                                super.updateItem(item, empty);
                                
                                if(!isEmpty() && !empty){

                                    final VBox vbox = new VBox(5);
                                    Image image = new Image(getClass().getResourceAsStream("/Imagenes/edit.png"));
                                    final Button boton = new Button("", new ImageView(image));
                                    boton.setOnAction(new EventHandler<ActionEvent>() {
                                          @Override
                                          public void handle(ActionEvent event) {
                                            
                                              
                                            TablaGastosIngresos ingresos = tabla_ingresos_json.get(getTableRow().getIndex());
                                            AgregarIngresos AI = AgregarIngresos.getInstance();
                                            
                                            AI.setID(ingresos.getId());
                                            AI.setMonto(ingresos.getMonto());
                                            AI.setCategoria(ingresos.getCategoria());
                                            AI.setPago(ingresos.getPago());
                                            AI.setActualizar(true);
                                            
                                            Parent parent = null;
                                            try {
                                                parent = FXMLLoader.load(this.getClass().getResource("/fxml/AgregarIngresos.fxml"));
                                            } catch (IOException ex) {
                                                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                            
                                            Stage stage = new Stage();
                                            Scene scene = new Scene(parent);
                                            stage.setScene(scene);
                                            stage.show();

                                          }
                                    });
                                    
                                    vbox.getChildren().add(boton);
                                    setGraphic(vbox);
                                }else{
                                    setGraphic(null);
                                }
                            }
                        };
                    }
                });
                
            }
        };
        new Thread(tarea).start();
    }
    
    
    private void cargarSaldos(){
        
        HttpResponse response;
        response = JSON.request(Config.URL+"usuarios/saldos.json");
        JSONObject jObject = JSON.JSON(response);
        try {
            JSONArray jsonArr = jObject.getJSONArray("data");
            JSONObject data_json = jsonArr.getJSONObject(0);
            
            if(data_json.get("total_ingresos").toString() != "null" && data_json.get("total_gastos").toString() != "null"){
                lbl_total_saldo.setText( saldos(data_json.get("total_ingresos").toString()) );
                lbl_total_gastos.setText( saldos(data_json.get("total_gastos").toString()) );
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String saldos(String monto){
        double myNum = Double.parseDouble(monto);  
        NumberFormat nf = NumberFormat.getCurrencyInstance( new Locale("es","AR") );  
        return nf.format(myNum);
    }
    
    private void cargarTablaIngresos(){
        HttpResponse response;
        response = JSON.request(Config.URL+"ingresos/ultimosingresos.json");
        JSONObject jObject = JSON.JSON(response);
        try {
            
            editar_tingresos.setCellValueFactory(new PropertyValueFactory<TablaGastosIngresos, String>("editar"));
            eliminar_tingresos.setCellValueFactory(new PropertyValueFactory<TablaGastosIngresos, String>("eliminar"));
            fecha_tingresos.setCellValueFactory(new PropertyValueFactory<TablaGastosIngresos, String>("fecha"));
            categoria_tingresos.setCellValueFactory(new PropertyValueFactory<TablaGastosIngresos, String>("categoria"));
            mpago_tingresos.setCellValueFactory(new PropertyValueFactory<TablaGastosIngresos, String>("pago"));
            monto_tingresos.setCellValueFactory(new PropertyValueFactory<TablaGastosIngresos, String>("monto"));
            
            
            tabla_ingresos_json = new ArrayList<TablaGastosIngresos>();
            
            if(!jObject.get("data").equals(null)){
                JSONArray jsonArr = jObject.getJSONArray("data");
                for(int i =0; i < jsonArr.length(); i++){
                    JSONObject data_json = jsonArr.getJSONObject(i);
                    tabla_ingresos_json.add(new TablaGastosIngresos(
                            data_json.get("idingresos").toString(),
                            data_json.get("idingresos").toString(),
                            data_json.get("idingresos").toString(),
                            data_json.get("monto").toString(),
                            data_json.get("fecha").toString(),
                            data_json.get("nom_mediopago").toString(),
                            data_json.get("nom_cat_ingresos").toString()


                    ));
                }
            }

            tabla_ingresos.setItems( FXCollections.observableArrayList(tabla_ingresos_json) );
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void cargarTablaGastos(){
        HttpResponse response;
        response = JSON.request(Config.URL+"gastos/ultimosgastos.json");
        JSONObject jObject = JSON.JSON(response);
        try {
            
            editar_tgastos.setCellValueFactory(new PropertyValueFactory<TablaGastosIngresos, String>("editar"));
            eliminar_tgastos.setCellValueFactory(new PropertyValueFactory<TablaGastosIngresos, String>("eliminar"));
            fecha_tgastos.setCellValueFactory(new PropertyValueFactory<TablaGastosIngresos, String>("fecha"));
            categoria_tgastos.setCellValueFactory(new PropertyValueFactory<TablaGastosIngresos, String>("categoria"));
            mpago_tgastos.setCellValueFactory(new PropertyValueFactory<TablaGastosIngresos, String>("pago"));
            monto_tgastos.setCellValueFactory(new PropertyValueFactory<TablaGastosIngresos, String>("monto"));
            
            tabla_gastos_json = new ArrayList<TablaGastosIngresos>();
            
            if(!jObject.get("data").equals(null)){

                JSONArray jsonArr = jObject.getJSONArray("data");
                
                for(int i =0; i < jsonArr.length(); i++){
                    JSONObject data_json = jsonArr.getJSONObject(i);
                    tabla_gastos_json.add(new TablaGastosIngresos(
                            data_json.get("idgastos").toString(),
                            data_json.get("idgastos").toString(),
                            data_json.get("idgastos").toString(),
                            data_json.get("monto").toString(),
                            data_json.get("fecha").toString(),
                            data_json.get("nom_mediopago").toString(),
                            data_json.get("nom_cat_gastos").toString()


                    ));
                }
            }

            tabla_gastos.setItems( FXCollections.observableArrayList(tabla_gastos_json) );
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static class TablaGastosIngresos {
        private String editar;
        private String eliminar;
        private String id;
        private String monto;
        private String fecha; 
        private String pago;
        private String categoria;
        
        
        public TablaGastosIngresos(String editar,String eliminar,String id,String monto,String fecha,String pago,String categoria) {
            this.editar = editar;
            this.eliminar = eliminar;
            this.id = id;
            this.monto = monto;
            this.fecha = fecha;
            this.pago = pago;
            this.categoria = categoria;
        }
        
        public String getEditar() {
            return editar;
        }

        public void setEditar(String editar) {
            this.editar = editar;
        }

        public String getEliminar() {
            return eliminar;
        }

        public void setEliminar(String eliminar) {
            this.eliminar = eliminar;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMonto() {
            
            double myNum = Double.parseDouble(monto);  
            NumberFormat nf = NumberFormat.getCurrencyInstance( new Locale("es","AR") );  
            return nf.format(myNum).replaceAll(",00", "");
        }

        public void setMonto(String monto) {
            this.monto = monto;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        public String getPago() {
            return pago;
        }

        public void setPago(String pago) {
            this.pago = pago;
        }

        public String getCategoria() {
            return categoria;
        }

        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }   
        
    }
    
}
