package com.mycompany.pfinanzaspersonales;

import Extras.Config;
import Extras.JSON;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class IngresosAgrupadosController implements Initializable {
    
    @FXML PieChart chart_ingresos_agrupados;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ingresos_agrupados();
    }
    
    private void ingresos_agrupados(){
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        
        HttpResponse response;
        response = JSON.request(Config.URL+"ingresos/ingresosagrupados.json");
        JSONObject jObject = JSON.JSON(response);
        String etiqueta = "";
        try {
            
            JSONArray jsonArr = jObject.getJSONArray("data");
            for(int i =0; i < jsonArr.length(); i++){
                JSONObject data_json = jsonArr.getJSONObject(i);
                etiqueta = data_json.get("nom_cat_ingresos").toString() + " "+ data_json.get("porcentaje") + "%";
                pieChartData.add(new PieChart.Data(etiqueta, Integer.parseInt(data_json.get("porcentaje").toString())) );
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        chart_ingresos_agrupados.setData(pieChartData);
    }
    
}
