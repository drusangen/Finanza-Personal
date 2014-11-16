package com.mycompany.pfinanzaspersonales;

import App.AgregarIngresos;
import Extras.Config;
import Extras.JSON;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.controlsfx.dialog.Dialogs;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.json.JSONArray;
import org.json.JSONObject;


public class AgregarIngresosController  implements Initializable {
    
    @FXML private ComboBox cbb_categoria_ingresos;
    @FXML private ComboBox cbb_medio_pago;
    @FXML private TextField txt_monto;
    @FXML private Button btn_agregar;
    
    AgregarIngresos AI = AgregarIngresos.getInstance();
    List<ComboboxIngresos> pagos;
    List<ComboboxIngresos> categoria;
    
    
    @FXML private Label lbl_error_numero;
    //@FXML private Label lbl_error_combobox;    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarComboboxPagos();
        cargarComboboxCategoriaGastos();
        
        if(AI.isActualizar()){
            
            txt_monto.setText(AI.getMonto());
            cbb_medio_pago.getSelectionModel().select( new ComboboxIngresos().objectoPago(AI.getPago()) );
            cbb_categoria_ingresos.getSelectionModel().select( new ComboboxIngresos().objectoCategoria(AI.getCategoria()) );
            btn_agregar.setText("Actualizar");
        }else{
            
            cbb_medio_pago.getSelectionModel().selectFirst();
            cbb_categoria_ingresos.getSelectionModel().selectFirst();
            btn_agregar.setText("Agregar");
        }
    }
    
    @FXML
    public void btn_agregar(final ActionEvent event) throws IOException{
        
        ((Node) (event.getSource())).getScene().setCursor(Cursor.WAIT);
        Object item = cbb_categoria_ingresos.getSelectionModel().getSelectedItem();
        final String value = ((ComboboxIngresos)item).getId();    
        
        Object item_pagos = cbb_medio_pago.getSelectionModel().getSelectedItem();
        final String valor_pagos = ((ComboboxIngresos)item).getId();
        
        //ValidationSupport validationSupport = new ValidationSupport();
        //validationSupport.registerValidator(txt_monto, Validator.createEmptyValidator("Text is required"));
        
       
         final Task<Integer> tarea = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                
                List<NameValuePair> parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("idcategoria_ingresos", value ));
                parametros.add(new BasicNameValuePair("idusuarios", "1" ));
                parametros.add(new BasicNameValuePair("idmedio_de_pago", valor_pagos  ));
                parametros.add(new BasicNameValuePair("monto", txt_monto.getText() ));

                String url = "";
                
                if(AI.isActualizar()){
                    parametros.add(new BasicNameValuePair("idingresos", AI.getID() ));
                    url= Config.URL+"ingresos/actualizar.json";
                }else{
                    url= Config.URL+"ingresos/agregar.json";
                }
                

                HttpResponse response = JSON.request(url,parametros);
                JSONObject jObject = JSON.JSON(response);
                int code = Integer.parseInt(jObject.get("code").toString());
                
                ((Node) (event.getSource())).getScene().setCursor(Cursor.DEFAULT);
                return code;
            }
            
            @Override protected void succeeded() {
                super.succeeded();
                String mensaje = "";
                
                if (this.getValue()  == 201) {
                    if(AI.isActualizar()){
                        mensaje = "Se actualizó ingreso";
                    }else{
                        mensaje = "Se agregó un nuevo ingreso";
                    }
                }else{
                    if(AI.isActualizar()){
                        mensaje = "Ingreso no actualizado";
                    }else{
                        mensaje = "Hubo un error al añadir el ingreso";
                    }
                }
                 Dialogs.create()
                        .title("Mensaje")
                        .message(mensaje)
                        .showInformation();
                
            }
        };
        new Thread(tarea).start();
        
        
        
        
    }
    
    private void cargarComboboxPagos(){
        HttpResponse response = JSON.request(Config.URL+"gastos/listarpagos.json");
        JSONObject jObject = JSON.JSON(response);
        try {
            
            
            pagos = new ArrayList<ComboboxIngresos>();
            
            JSONArray jsonArr = jObject.getJSONArray("data");
            for(int i =0; i < jsonArr.length(); i++){
                JSONObject data_json = jsonArr.getJSONObject(i);
                pagos.add(new ComboboxIngresos( data_json.get("nom_mediopago").toString(), data_json.get("idmedio_de_pago").toString() ));
            }

            cbb_medio_pago.setItems( FXCollections.observableArrayList(pagos) );
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void cargarComboboxCategoriaGastos(){
        HttpResponse response;
        response = JSON.request(Config.URL+"catingresos/listar.json");
        JSONObject jObject = JSON.JSON(response);
        try {
            
            
            categoria = new ArrayList<ComboboxIngresos>();
            
            JSONArray jsonArr = jObject.getJSONArray("data");
            for(int i =0; i < jsonArr.length(); i++){
                JSONObject data_json = jsonArr.getJSONObject(i);
                categoria.add(new ComboboxIngresos( data_json.get("nom_cat_ingresos").toString(), data_json.get("idcategoria_ingresos").toString() ));
            }

            cbb_categoria_ingresos.setItems( FXCollections.observableArrayList(categoria) );
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public class ComboboxIngresos{
        private String nombre;
        private String id;
        
        
        public ComboboxIngresos(String nombre,String id){
            this.nombre = nombre;
            this.id = id;
        }
        
        public ComboboxIngresos(){
            
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
        
        public ComboboxIngresos objectoPago(String Pago){
            for (ComboboxIngresos comboPagos : pagos){
                if (comboPagos.getNombre().equals(Pago)){
                    return comboPagos;
                }
            }
            return null;
        }
        
        public ComboboxIngresos objectoCategoria(String Categoria){
            for (ComboboxIngresos comboPagos : categoria){
                if (comboPagos.getNombre().equals(Categoria)){
                    return comboPagos;
                }
            }
            return null;
        }
        
        
    }
}


