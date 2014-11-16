/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Extras;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Diegou
 */
public class Config {
    public static String URL = "http://localhost/api_fxfinanzas/";
    
    public static String ComboboxFechas(String mes,String anio){
        
        String tag = "";
        switch(mes){
            case "01":
                tag = "Enero ";
                break;
                
            case "02":
                tag = "Febrero ";
                break;
            case "03":
                tag = "Marzo ";
                break;
            case "04":
                tag = "Abril ";
                break;
            case "05":
                tag = "Mayo ";
                break;
            case "06":
                tag = "Junio ";
                break;
            case "07":
                tag = "Julio ";
                break;
            case "08":
                tag = "Agosto ";
                break;
            case "09":
                tag = "Septiembre ";
                break;
            case "10":
                tag = "Octubre ";
                break;
            case "11":
                tag = "Noviembre ";
                break;
            case "12":
                tag = "Diciembre ";
                break;
        }
        
        return tag + "" + anio;
    }
    
    public static String MesActual(){
        Calendar c = GregorianCalendar.getInstance();
        String mes = "Enero";
        

        switch(c.get(Calendar.MONTH) + 1){
            
            case 1:
                mes = "Enero";
                break;
            
            case 2:
                mes = "Febrero";
                break;
            
            case 3:
                mes = "Marzo";
                break;
            
            case 4:
                mes = "Abril";
                break;
            
            case 5:
                mes = "Mayo";
                break;
            
            case 6:
                mes = "Junio";
                break;
            
            case 7:
                mes = "Julio";
                break;
            
            case 8:
                mes = "Agosto";
                break;
            
            case 9:
                mes = "Septiembre";
                break;
            
            case 10:
                mes = "Octubre";
                break;
                
            case 11:
                mes = "Noviembre";
                break;
            
            case 12:
                mes = "Diciembre";
                break;
        }
        return mes;
    }
}
