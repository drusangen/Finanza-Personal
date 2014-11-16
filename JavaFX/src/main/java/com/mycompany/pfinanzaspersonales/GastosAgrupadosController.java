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

public class GastosAgrupadosController implements Initializable {
    
    @FXML PieChart chart_gastos_agrupados;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gastos_agrupados();
    }
    
    private void gastos_agrupados(){
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        
        HttpResponse response;
        response = JSON.request(Config.URL+"gastos/gastosagrupados.json");
        JSONObject jObject = JSON.JSON(response);
        String etiqueta = "";
        try {
            
            JSONArray jsonArr = jObject.getJSONArray("data");
            for(int i =0; i < jsonArr.length(); i++){
                JSONObject data_json = jsonArr.getJSONObject(i);
                etiqueta = data_json.get("nom_cat_gastos").toString() + " "+ data_json.get("porcentaje") + "%";
                pieChartData.add(new PieChart.Data(etiqueta, Integer.parseInt(data_json.get("porcentaje").toString())) );
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        chart_gastos_agrupados.setData(pieChartData);
    }
    
}
