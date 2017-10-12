/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica3;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

/**
 * Clase Sensores: Agente encargado de recibir la información del servidor con los datos de los sensores.
 * @author Ernesto Gomez Leyva
 * @version 1.0
 */    

class Sensores {
    
    private Coordenada gps;
    private int[][] radar;
    private int battery;
    private int batteryGlobal;
    private int[] batteryAgents;
    private boolean goal;
    
    private int vision;

 /**
 * Constructor de la Clase Sensores inicializa todas las variables privadas de la clase.
 * @author Ernesto Gomez Leyva
 * @version 1.0
 */  

    Sensores(int vision, int consumo) {
       
        this.gps = new Coordenada(0,0);
        this.radar = new int[vision][vision];
        this.battery = 0;
        this.batteryGlobal = 0;
        this.batteryAgents = new int[4];
        this.goal = false;
        
        this.vision = vision;
    }

/**
 * Metodo getBattery devuelve el numero de batería restante
 * @author Ernesto Gomez Leyva
 * @version 1.0
 */
    public int getBattery(){
        return this.battery;
    }
    
 /**
 * Metodo setBattery inserta el numero de batería recibido por parámetro.
 * @author Ernesto Gomez Leyva
 * @version 1.0
 */  
    public void setBattery(int b){
        this.battery = b;
    }
    
 /**
 * Metodo getGPS devuelve el dato Coordenada de la posición donde se encuentre.
 * @author Ernesto Gomez Leyva
 * @version 1.0
 */  
    public Coordenada getGPS(){
        return this.gps;
    }
 /**
 * Metodo setGPS introduce los valores x e y recibidos por parámetro en el dato privado gps.
 * @author Ernesto Gomez Leyva
 * @version 1.0
 */    
    public void setGPS(int x, int y){
        this.gps.setX(x);
        this.gps.setY(y);
    }
 /**
 * Metodo getBatteryGlobal devuelve el numero de batería global que hay.
 * @author Ernesto Gomez Leyva
 * @version 1.0
 */  
    public int getBatteryGlobal(){
        return this.batteryGlobal;
    }
 /**
 * Metodo setBatteryGlobal introduce el numero entero recibido por parámetro en el dato privado de la clase.
 * @author Ernesto Gomez Leyva
 * @version 1.0
 */      
    public void setBatteryGlobal(int n){
        this.batteryGlobal = n;
    }
 /**
 * Metodo getBatteryAgentes devuelve un array de enteros, cada posición corresponde a la bateria de cada agente.
 * @author Ernesto Gomez Leyva
 * @version 1.0
 */      
    public int[] getBatteryAgentes(){
        return this.batteryAgents;
    }
 /**
 * Metodo setBatteryAgentes según el id del agente, se introduce la bateria que tiene en la posición correspondiente.
 * @author Ernesto Gomez Leyva
 * @version 1.0
 */      
    public void setBatteryAgente(int id, int b){
        this.batteryAgents[id] = b;
    }
 /**
 * Metodo getRadar devuelve un array de enteros, con los datos del radar.
 * @author Ernesto Gomez Leyva
 * @version 1.0
 */      
    public int[][] getRadar(){
        return radar;
    }
 /**
 * Metodo getGoal devuelve un booleano según esté en el objetivo o no.
 * @author Ernesto Gomez Leyva
 * @version 1.0
 */      
    public boolean getGoal(){
        return this.goal;
    }
 /**
 * Metodo actualizar sobre un Json recibido por parámetro, actualiza las variables internas de la clase según la información recibida.
 * @author Ernesto Gomez Leyva
 * @version 1.0
 */      
    void actualizar(JsonObject datos) {
       
       this.battery= datos.get("battery").asInt();
       System.out.println("Bateria :" + this.battery);
        
       this.gps.setX(datos.get("x").asInt());
       System.out.println("X :" + this.gps.getX());
       this.gps.setY(datos.get("y").asInt());
       System.out.println("Y :" + this.gps.getY());
       
       JsonArray rad=datos.get("sensor").asArray();
       System.out.println("RADAR :" + rad.toString());
       int j=0,k=0;
       for(int i=0;i<rad.size();i++){
           this.radar[j][k] = rad.get(i).asInt();
           k++;
           if(k==this.vision){
               j++;
               k=0;
           }
       }
       
       this.batteryGlobal= datos.get("energy").asInt();
       System.out.println("ENERGY :" + this.batteryGlobal);
       
       this.goal = datos.get("goal").asBoolean();
       System.out.println("GOAL :" + this.goal);
    }
    
}
