/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Extras;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

/**
 *
 * @author Diegou
 */
public class JSON {
    public static HttpResponse request(String URL,List parametros){
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(URL);
        HttpResponse response = null;
        try {
            httppost.setEntity(new UrlEncodedFormEntity(parametros));
            response = httpclient.execute(httppost);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        return response;
    }

    public static HttpResponse request(String URL){
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(URL);
        HttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
        }catch(Exception e){ }
        return response;
    }

    public static JSONObject JSON(HttpResponse response){
        String result = "";
        JSONObject jObject = null;
        try{
                HttpEntity entity = response.getEntity();
        InputStream inputStream = entity.getContent();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
        StringBuilder sb = new StringBuilder();

        String line = null;
        while ((line = reader.readLine()) != null)
        {
            sb.append(line + "\n");
        }

        result = sb.toString();
        jObject = new JSONObject(result);
        }catch(Exception ex){}
        return jObject;
    }

    public static HttpResponse request(URL url, List<NameValuePair> parametros) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
