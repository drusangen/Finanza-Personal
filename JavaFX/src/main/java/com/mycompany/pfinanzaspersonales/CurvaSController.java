package com.mycompany.pfinanzaspersonales;

import Extras.Config;
import Extras.JSON;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;



public class CurvaSController implements Initializable {
    
    @FXML LineChart chart_linechart;
    @FXML ComboBox combobox_mes;
    @FXML ImageView loading_img;
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        CargarComboboxCurvaS();
        
    }
    
    @FXML
    private void combobox_cambiar(final ActionEvent event){
        
       ((Node) (event.getSource())).getScene().setCursor(Cursor.WAIT);
        
        Object item = combobox_mes.getSelectionModel().getSelectedItem();
        String value = ((ComboboxCurvaS)item).getId();
        
        chart_linechart.getData().clear();
        chart_linechart.getData().addAll(
                                            gastos(value),
                                            Ingresos(value)
                                        );
        
        
        ((Node) (event.getSource())).getScene().setCursor(Cursor.DEFAULT);

    }
    
    private Series gastos(String fecha){
        
        XYChart.Series series = new XYChart.Series();
        series.setName("Gastos");
        
        
        List<NameValuePair> parametros = new ArrayList<NameValuePair>();
        parametros.add(new BasicNameValuePair("fecha", fecha ));
        
        HttpResponse response;
        response = JSON.request(Config.URL+"gastos/ingresosvsgastos.json",parametros);
        JSONObject jObject = JSON.JSON(response);
        String etiqueta = "";
        try {
            
            JSONArray jsonArr = jObject.getJSONArray("data");
            for(int i =0; i < jsonArr.length(); i++){
                JSONObject data_json = jsonArr.getJSONObject(i);
                etiqueta = data_json.get("fechas").toString();
                series.getData().add(new XYChart.Data(etiqueta, Integer.parseInt(data_json.get("monto").toString())) );
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return series;
    }
    
    private Series Ingresos(String fecha){
       
        XYChart.Series series_b = new XYChart.Series();
        series_b.setName("Ingresos");
        
        List<NameValuePair> parametros = new ArrayList<NameValuePair>();
        parametros.add(new BasicNameValuePair("fecha", fecha ));
        
        HttpResponse response;
        response = JSON.request(Config.URL+"ingresos/ingresosvsgastos.json",parametros);
        JSONObject jObject = JSON.JSON(response);
        String etiqueta = "";
        try {
            
            JSONArray jsonArr = jObject.getJSONArray("data");
            for(int i =0; i < jsonArr.length(); i++){
                JSONObject data_json = jsonArr.getJSONObject(i);
                etiqueta = data_json.get("fechas").toString();
                series_b.getData().add(new XYChart.Data(etiqueta, Integer.parseInt(data_json.get("monto").toString())) );
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return series_b;
    }
    
    private void CargarComboboxCurvaS(){
        HttpResponse response;
        response = JSON.request(Config.URL+"usuarios/comboboxfechasmensual.json");
        JSONObject jObject = JSON.JSON(response);
        try {
            
            
            List<ComboboxCurvaS> categoria = new ArrayList<ComboboxCurvaS>();
            
            JSONArray jsonArr = jObject.getJSONArray("data");
            String etiqueta = "";
            for(int i =0; i < jsonArr.length(); i++){
                JSONObject data_json = jsonArr.getJSONObject(i);
                
                etiqueta = Config.ComboboxFechas(data_json.get("mes").toString(), data_json.get("anio").toString());
                categoria.add(new ComboboxCurvaS( etiqueta, data_json.get("fecha").toString() ));
            }

            combobox_mes.setItems( FXCollections.observableArrayList(categoria) );
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public class ComboboxCurvaS{
        private String nombre;
        private String id;
        
        
        public ComboboxCurvaS(String nombre,String id){
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
    
}
