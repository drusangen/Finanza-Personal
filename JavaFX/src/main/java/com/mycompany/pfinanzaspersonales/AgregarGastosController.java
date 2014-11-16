package com.mycompany.pfinanzaspersonales;

import App.AgregarGastos;
import Extras.Config;
import Extras.JSON;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.controlsfx.dialog.Dialogs;
import org.json.JSONArray;
import org.json.JSONObject;


public class AgregarGastosController implements Initializable {
    
    @FXML private ComboBox cbb_categoria_gastos;
    @FXML private ComboBox cbb_medio_pago;
    @FXML private TextField txt_monto;
    @FXML private Button btn_agregar;
    
    @FXML private Label lbl_error_numero;
    //@FXML private Label lbl_error_combobox;
    
    AgregarGastos AG = AgregarGastos.getInstance();
    List<ComboboxGastos> pagos;
    List<ComboboxGastos> categoria;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarComboboxPagos();
        cargarComboboxCategoriaGastos();
        
        
        if(AG.getActualizacion()){
            
            txt_monto.setText(AG.getMonto());
            cbb_medio_pago.getSelectionModel().select( new ComboboxGastos().objectoPago(AG.getPago()) );
            cbb_categoria_gastos.getSelectionModel().select( new ComboboxGastos().objectoCategoria(AG.getCategoria()) );
            
            btn_agregar.setText("Actualizar");
        }else{
            cbb_medio_pago.getSelectionModel().selectFirst();
            cbb_categoria_gastos.getSelectionModel().selectFirst();
            btn_agregar.setText("Agregar");
        }
    }
    
    @FXML
    public void btn_agregar(ActionEvent event) throws IOException{

        
        ((Node) (event.getSource())).getScene().setCursor(Cursor.WAIT);
        Object item = cbb_categoria_gastos.getSelectionModel().getSelectedItem();
        String value = ((ComboboxGastos)item).getId();    
        
        
        Object item_pagos = cbb_medio_pago.getSelectionModel().getSelectedItem();
        String valor_pagos = ((ComboboxGastos)item).getId();
        
        
        final Task<Integer> tarea = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                
                
                
                List<NameValuePair> parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("idcategoria_gastos", value ));
                parametros.add(new BasicNameValuePair("idusuarios", "1" ));
                parametros.add(new BasicNameValuePair("idmedio_de_pago", valor_pagos  ));
                parametros.add(new BasicNameValuePair("monto", txt_monto.getText() ));

                String url = "";
                
                if(AG.getActualizacion()){
                    
                    parametros.add(new BasicNameValuePair("idgastos", AG.getID() ));
                    url= Config.URL+"gastos/actualizar.json";
                }else{
                    url= Config.URL+"gastos/agregar.json";
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
                    if(AG.getActualizacion()){
                        mensaje = "Se actualizó gasto";
                    }else{
                        mensaje = "Se agregado un nuevo gasto";
                    }
                    
                }else{
                    if(AG.getActualizacion()){
                        mensaje = "Gasto no actualizado";
                    }else{
                        mensaje = "Hubo un error al añadir el gasto";
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

            pagos = new ArrayList<ComboboxGastos>();
            JSONArray jsonArr = jObject.getJSONArray("data");
            for(int i =0; i < jsonArr.length(); i++){
                JSONObject data_json = jsonArr.getJSONObject(i);
                pagos.add(new ComboboxGastos( data_json.get("nom_mediopago").toString(), data_json.get("idmedio_de_pago").toString() ));
            }
            cbb_medio_pago.setItems( FXCollections.observableArrayList(pagos) );
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void cargarComboboxCategoriaGastos(){
        HttpResponse response;
        response = JSON.request(Config.URL+"catgastos/listar.json");
        JSONObject jObject = JSON.JSON(response);
        try {
            
            
            categoria = new ArrayList<ComboboxGastos>();
            
            JSONArray jsonArr = jObject.getJSONArray("data");
            for(int i =0; i < jsonArr.length(); i++){
                JSONObject data_json = jsonArr.getJSONObject(i);
                
                categoria.add(new ComboboxGastos( data_json.get("nom_cat_gastos").toString(), data_json.get("idcategoria_gastos").toString() ));
            }

            cbb_categoria_gastos.setItems( FXCollections.observableArrayList(categoria) );
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public class ComboboxGastos{
        private String nombre;
        private String id;
        
        
        public ComboboxGastos(String nombre,String id){
            this.nombre = nombre;
            this.id = id;
        }

        public ComboboxGastos() {
            
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
        
        public ComboboxGastos objectoPago(String Pago){
            for (ComboboxGastos comboPagos : pagos){
                if (comboPagos.getNombre().equals(Pago)){
                    return comboPagos;
                }
            }
            return null;
        }
        
        public ComboboxGastos objectoCategoria(String Categoria){
            for (ComboboxGastos comboPagos : categoria){
                if (comboPagos.getNombre().equals(Categoria)){
                    return comboPagos;
                }
            }
            return null;
        }
        
    }
}


