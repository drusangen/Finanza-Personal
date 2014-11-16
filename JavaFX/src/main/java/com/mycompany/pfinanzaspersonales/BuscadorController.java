package com.mycompany.pfinanzaspersonales;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import App.AgregarGastos;
import App.AgregarIngresos;
import Extras.Config;
import Extras.JSON;
import com.mycompany.pfinanzaspersonales.AgregarIngresosController.ComboboxIngresos;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author diegovalladares
 */
public class BuscadorController implements Initializable {
    
    @FXML private ComboBox cbb_tipo;
    @FXML private ComboBox cbb_pago;
    @FXML private Button btn_buscar;
    
    @FXML private DatePicker input_desde;
    @FXML private DatePicker input_hasta;
    
    @FXML TableView tabla_buscador;
    @FXML TableColumn editar;
    @FXML TableColumn eliminar;
    @FXML TableColumn fecha;
    @FXML TableColumn categoria;
    @FXML TableColumn tpago;
    @FXML TableColumn tmonto;
    @FXML TableColumn ttipo;
    
    List<TablaBuscar> tabla_json;
    
    List<Combobox> tipo = new ArrayList<Combobox>();
    List<Combobox> pago = new ArrayList<Combobox>();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        tipo.add(new Combobox( "Todo", "0" ));
        tipo.add(new Combobox( "Ingresos", "Ingresos" ));
        tipo.add(new Combobox( "Gastos", "Gasto" ));
        cbb_tipo.setItems( FXCollections.observableArrayList(tipo) );
        cargarComboboxPagos();
        
        
        cbb_tipo.getSelectionModel().selectFirst();
        cbb_pago.getSelectionModel().selectFirst();
        
    }    
    
    @FXML
    private void btnBuscar(ActionEvent event) throws IOException {
        
        String desde_t = "",hasta_t = "";

        if(input_desde.getValue() != null){
            desde_t = input_desde.getValue().toString();
        }
        
        if(input_hasta.getValue() != null){
            hasta_t = input_hasta.getValue().toString();
        }
        
        final String desde = desde_t;
        final String hasta = hasta_t;
        
        final Task<Void> tarea = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                
                HttpResponse response;
                List<NameValuePair> parametros = new ArrayList<NameValuePair>();
                
                
                parametros.add(new BasicNameValuePair("tipo", cbb_tipo.getValue().toString() ));
                parametros.add(new BasicNameValuePair("pago", cbb_pago.getValue().toString() ));
                parametros.add(new BasicNameValuePair("desde", desde ));
                parametros.add(new BasicNameValuePair("hasta", hasta ));
                
                System.out.println(desde);
                response = JSON.request(Config.URL+"usuarios/buscar.json",parametros);
                
                
                JSONObject jObject = JSON.JSON(response);
                tabla_json = new ArrayList<TablaBuscar>();
                
                try {

                    editar.setCellValueFactory(new PropertyValueFactory<TablaBuscar, String>("editar"));
                    eliminar.setCellValueFactory(new PropertyValueFactory<TablaBuscar, String>("eliminar"));
                    fecha.setCellValueFactory(new PropertyValueFactory<TablaBuscar, String>("fecha"));
                    categoria.setCellValueFactory(new PropertyValueFactory<TablaBuscar, String>("categoria"));
                    tpago.setCellValueFactory(new PropertyValueFactory<TablaBuscar, String>("pago"));
                    tmonto.setCellValueFactory(new PropertyValueFactory<TablaBuscar, String>("monto"));
                    ttipo.setCellValueFactory(new PropertyValueFactory<TablaBuscar, String>("tipo"));
                    
                    tabla_json = new ArrayList<TablaBuscar>();

                    if(!jObject.get("data").equals(null)){
                        JSONArray jsonArr = jObject.getJSONArray("data");
                        for(int i =0; i < jsonArr.length(); i++){
                            JSONObject data_json = jsonArr.getJSONObject(i);
                            tabla_json.add(new TablaBuscar(
                                    data_json.get("idgastos").toString(),
                                    data_json.get("idgastos").toString(),
                                    data_json.get("idgastos").toString(),
                                    data_json.get("monto").toString(),
                                    data_json.get("fecha").toString(),
                                    data_json.get("nom_mediopago").toString(),
                                    data_json.get("categoria").toString(),
                                    data_json.get("tipo").toString()
                            ));
                        }
                    }
                    tabla_buscador.setItems( FXCollections.observableArrayList(tabla_json) );

                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                return null;
            }

            @Override protected void succeeded() {
                super.succeeded();
                
                eliminar.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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
                                                
                                                TablaBuscar tabla = tabla_json.get(getTableRow().getIndex());
                                                
                                                List<NameValuePair> parametros = new ArrayList<NameValuePair>();
                                                
                                                String url = "";
                                                if(tabla.getTipo() == "Gastos"){
                                                    parametros.add(new BasicNameValuePair("idgastos", tabla.getId() ));
                                                    url= Config.URL+"gastos/eliminar.json";
                                                }else{
                                                    parametros.add(new BasicNameValuePair("idingresos", tabla.getId() ));
                                                     url= Config.URL+"ingresos/eliminar.json";
                                                }
                                                
                                                
                                                
                                                HttpResponse responseJSON = JSON.request(url,parametros);
                                                JSONObject jObject = JSON.JSON(responseJSON);
                                                int code = Integer.parseInt(jObject.get("code").toString());
                                               
                                                if(code == 201){
                                                    int selectdIndex = getTableRow().getIndex();
                                                    tabla_json.remove(selectdIndex);
                                                    tabla_buscador.getItems().remove(selectdIndex);
                                                }else{
                                                    Dialogs.create()
                                                            .title("Error sincronización")
                                                            .message("Hubo un error al intentar eliminar . ERROR: 301")
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
                
                
                editar.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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
           
                                            TablaBuscar tabla = tabla_json.get(getTableRow().getIndex());
                                            
                                            Parent parent = null;
                                            if(tabla.getTipo() == "Gastos"){
                                                AgregarGastos AG = AgregarGastos.getInstance();
                                                 
                                                AG.setID(tabla.getId());
                                                AG.setMonto(tabla.getMonto());
                                                AG.setCategoria(tabla.getCategoria());
                                                AG.setPago(tabla.getPago());
                                                AG.setActualizacion(true);

                                                try {
                                                    parent = FXMLLoader.load(this.getClass().getResource("/fxml/AgregarGastos.fxml"));
                                                } catch (IOException ex) {
                                                    Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            }else{
                                                AgregarIngresos AI = AgregarIngresos.getInstance();

                                                AI.setID(tabla.getId());
                                                AI.setMonto(tabla.getMonto());
                                                AI.setCategoria(tabla.getCategoria());
                                                AI.setPago(tabla.getPago());
                                                AI.setActualizar(true);

                                                
                                                try {
                                                    parent = FXMLLoader.load(this.getClass().getResource("/fxml/AgregarIngresos.fxml"));
                                                } catch (IOException ex) {
                                                    Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                                                }
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
    
    private void cargarComboboxPagos(){
        HttpResponse response = JSON.request(Config.URL+"gastos/listarpagos.json");
        JSONObject jObject = JSON.JSON(response);
        pago.add(new Combobox( "Todo", "0" ));
        try {
            
            JSONArray jsonArr = jObject.getJSONArray("data");
            for(int i =0; i < jsonArr.length(); i++){
                JSONObject data_json = jsonArr.getJSONObject(i);
                pago.add(new Combobox( data_json.get("nom_mediopago").toString(), data_json.get("idmedio_de_pago").toString() ));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        cbb_pago.setItems( FXCollections.observableArrayList(pago) );
    }
    
    public class Combobox{
        private String nombre;
        private String id;
        
        
        public Combobox(String nombre,String id){
            this.nombre = nombre;
            this.id = id;
        }
        
        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
        
        @Override
        public String toString() {
            return nombre;
        }    
        
    }
    
    public static class TablaBuscar {
        private String editar;
        private String eliminar;
        private String id;
        private String monto;
        private String fecha; 
        private String pago;
        private String categoria;
        private String tipo;
        
        public TablaBuscar(String editar,String eliminar,String id,String monto,String fecha,String pago,String categoria,String tipo) {
            this.editar = editar;
            this.eliminar = eliminar;
            this.id = id;
            this.monto = monto;
            this.fecha = fecha;
            this.pago = pago;
            this.categoria = categoria;
            this.tipo = tipo;
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

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }
        
        
    }
}
