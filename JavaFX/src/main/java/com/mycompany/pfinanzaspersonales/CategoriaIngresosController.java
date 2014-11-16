package com.mycompany.pfinanzaspersonales;

import Extras.Config;
import Extras.JSON;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;


public class CategoriaIngresosController implements Initializable {
    
    @FXML private TableView tabla_ingresos;
    @FXML private TableColumn descripcion;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        HttpResponse response;
        response = JSON.request(Config.URL+"catingresos/listar.json");
        JSONObject jObject = JSON.JSON(response);
        try {
            
            descripcion.setCellValueFactory(new PropertyValueFactory<CategoriaProductos, String>("descripcion"));
            List<CategoriaProductos> categoria = new ArrayList<CategoriaProductos>();
            
            JSONArray jsonArr = jObject.getJSONArray("data");
            for(int i =0; i < jsonArr.length(); i++){
                JSONObject data_json = jsonArr.getJSONObject(i);
                categoria.add(new CategoriaProductos( data_json.get("nom_cat_ingresos").toString() ));
            }

            tabla_ingresos.setItems( FXCollections.observableArrayList(categoria) );
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public static class CategoriaProductos {
        private String descripcion;
        
        public CategoriaProductos(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
    }
}