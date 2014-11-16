/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pfinanzaspersonales;

import Extras.Config;
import Extras.JSON;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author diegovalladares
 */
public class BalanceController implements Initializable {
    
    
    @FXML TableView tabla_balance;
    @FXML TableColumn c_mes;
    @FXML TableColumn c_ingresos;
    @FXML TableColumn c_gastos;
    @FXML TableColumn c_resumen;
    @FXML private ComboBox cbb_anio;
    
    List<Combobox> combobox;
    List<TablaBalance> tabla_balance_;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       cargarAnio();
       cbb_anio.getSelectionModel().selectFirst();
    }
    
    @FXML
    private void seleccionar_balance(ActionEvent event) throws IOException{
       cargarTablaBalance();
    }
    
    private void cargarTablaBalance(){
        
        List<NameValuePair> parametros = new ArrayList<NameValuePair>();
        
        Object item_anio = cbb_anio.getSelectionModel().getSelectedItem();
        String anio = ((Combobox)item_anio).getId();
        
        parametros.add(new BasicNameValuePair("anio", anio ));
        HttpResponse responseJSON = JSON.request(Config.URL+"usuarios/balance.json",parametros);
        JSONObject jObject = JSON.JSON(responseJSON);
        try {
            
            c_mes.setCellValueFactory(new PropertyValueFactory<TablaBalance, String>("mes"));
            c_ingresos.setCellValueFactory(new PropertyValueFactory<TablaBalance, String>("ingreso"));
            c_gastos.setCellValueFactory(new PropertyValueFactory<TablaBalance, String>("gasto"));
            c_resumen.setCellValueFactory(new PropertyValueFactory<TablaBalance, String>("resumen"));
            
            tabla_balance_ = new ArrayList<TablaBalance>();
            
            if(!jObject.get("data").equals(null)){
                JSONArray jsonArr = jObject.getJSONArray("data");
                for(int i =0; i < jsonArr.length(); i++){
                    JSONObject data_json = jsonArr.getJSONObject(i);
                    tabla_balance_.add(new TablaBalance(
                            data_json.get("mes").toString(),
                            data_json.get("total_ingresos").toString(),
                            data_json.get("total_gastos").toString(),
                            data_json.get("resumen").toString()
                    ));
                }
            }

            tabla_balance.setItems( FXCollections.observableArrayList(tabla_balance_) );
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void cargarAnio(){
        HttpResponse response = JSON.request(Config.URL+"usuarios/anios.json");
        JSONObject jObject = JSON.JSON(response);
        try {

            combobox = new ArrayList<Combobox>();
            JSONArray jsonArr = jObject.getJSONArray("data");
            for(int i =0; i < jsonArr.length(); i++){
                JSONObject data_json = jsonArr.getJSONObject(i);
                combobox.add(new Combobox( data_json.get("anio").toString(), data_json.get("anio").toString() ));
            }
            cbb_anio.setItems( FXCollections.observableArrayList(combobox) );
            
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    
    public static class TablaBalance {
        
        String mes;
        String ingreso;
        String gasto;
        String resumen;
        
        public TablaBalance(String mes,String ingreso,String gasto,String resumen) {
            this.mes = mes;
            this.ingreso = ingreso;
            this.gasto = gasto;
            this.resumen = resumen;
        }

        public String getMes() {
            return mes;
        }

        public void setMes(String mes) {
            this.mes = mes;
        }

        public String getIngreso() {
            double myNum = Double.parseDouble(ingreso);  
            NumberFormat nf = NumberFormat.getCurrencyInstance( new Locale("es","AR") );  
            return nf.format(myNum).replaceAll(",00", "");
        }

        public void setIngreso(String ingreso) {
            this.ingreso = ingreso;
        }

        public String getGasto() {
            double myNum = Double.parseDouble(gasto);  
            NumberFormat nf = NumberFormat.getCurrencyInstance( new Locale("es","AR") );  
            return nf.format(myNum).replaceAll(",00", "");
        }

        public void setGasto(String gasto) {
            this.gasto = gasto;
        }

        public String getResumen() {
            double myNum = Double.parseDouble(resumen);  
            NumberFormat nf = NumberFormat.getCurrencyInstance( new Locale("es","AR") );  
            return nf.format(myNum).replaceAll(",00", "");
        }

        public void setResumen(String resumen) {
            this.resumen = resumen;
        } 
        
    }
}
