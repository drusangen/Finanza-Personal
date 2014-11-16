package com.mycompany.pfinanzaspersonales;

import App.DataUsuario;
import Extras.Config;
import Extras.JSON;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.controlsfx.dialog.Dialogs;
import org.json.JSONArray;
import org.json.JSONObject;

public class LoginController implements Initializable {
    
    @FXML private TextField txt_contrasena;
    @FXML private TextField txt_usuario;
    
    @FXML
    private void btnLoginAction(ActionEvent event) throws IOException {
        
        
        List<NameValuePair> parametros = new ArrayList<NameValuePair>();
        parametros.add(new BasicNameValuePair("usuario", txt_usuario.getText() ));
        parametros.add(new BasicNameValuePair("contrasena", txt_contrasena.getText() ));
        
        try{
            String url= Config.URL+"usuarios/login.json";
            HttpResponse responseJSON = JSON.request(url,parametros);
            JSONObject jObject = JSON.JSON(responseJSON);
            int code = Integer.parseInt(jObject.get("code").toString());

            if(code == 201){

                JSONArray jsonArr = jObject.getJSONArray("data");
                JSONObject data_json = jsonArr.getJSONObject(0);

                DataUsuario.getInstance().setID(data_json.get("id").toString());
                DataUsuario.getInstance().setEmail(data_json.get("email").toString());
                DataUsuario.getInstance().setNombre(data_json.get("nombre").toString());

                ((Node) (event.getSource())).getScene().getWindow().hide();
                Parent parent = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(parent);
                stage.setScene(scene);
                stage.show();
            }else{
                Dialogs.create()
                    .title("Error")
                    .message("Usuario y/o contraseña no existe")
                    .showInformation();
            }
        }catch(Exception ex){
                Dialogs.create()
                    .title("Error")
                    .message("Problemas al conectarse con el servidor")
                    .showInformation();
        }
        
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
}
