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
public class AgregarIngresos {
    
    private static AgregarIngresos instance = null;
    
    String ID;
    String monto;
    String categoria;
    String pago;
    boolean actualizar;

    protected AgregarIngresos() {
        
    }
    
    
    public String getMonto() {
        return monto.replaceAll("[^0-9]+", "");
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPago() {
        return pago;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }

    public boolean isActualizar() {
        return actualizar;
    }

    public void setActualizar(boolean actualizar) {
        this.actualizar = actualizar;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
    
   
    public static AgregarIngresos getInstance() {
        if(instance == null) {
            instance = new AgregarIngresos();
        }
        return instance;
    }
    
}
