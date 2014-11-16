/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

/**
 *
 * @author diegovalladares
 */
public class DataUsuario {
    
    String ID;
    String Email;
    String Nombre;
    
    private static DataUsuario instance = null;
    
    protected DataUsuario() {
        
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }
    
    
    
    public static DataUsuario getInstance() {
        if(instance == null) {
            instance = new DataUsuario();
        }
        return instance;
    }
}
