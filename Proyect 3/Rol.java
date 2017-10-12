/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica3;

import com.eclipsesource.json.JsonObject;

/**
 * Clase Abstracta ROL: representa el rol de un agente y define su comportamiento. 
 * Además contiene la parte común de la heurística.
 * @author Martin Zumarraga Uribe
 * @version 1.0
 */    
public abstract class Rol {
    
    protected int vision;
    protected int consumo;
    
    public Rol(){}
        
    public abstract boolean getEstado();
    
    public abstract String realizarMovimiento();
    
    public abstract JsonObject getInformacion();
     
    public abstract void actualizarSensores(JsonObject datos);
    
    public abstract void actualizarEstado(JsonObject informacion);
    
    public abstract void setEstadoMeta(int i);
    
    public abstract boolean getTodosMeta();
}
