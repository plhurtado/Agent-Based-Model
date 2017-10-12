
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica3;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import static java.lang.Math.sqrt;

/**
 * Clase Heuristica General: posee diversos métodos comunes para su uso en las heurísticas específicas de 
 * cada rol.
 * @author Ernesto Gomez Leyva, Pedro Hurtado Gonzalez y Carmen Bueno Ben Boubker
 * @version 1.0
 */

public abstract class Heuristica {
    
    protected Sensores datosSensores;
    private JsonObject informacion;
    protected int[][] mapa;
    protected boolean estadoBusqueda;
    protected Coordenada[] meta;
    protected Coordenada goal;
    protected boolean todosMeta;
    protected double scanner[][];
/**
 * Constructor de la Heuristica General que inicializa las variables internas de la clase e inicializa un un objeto según
 * la vision y consumo que requiera las heuristicas que heredan de esta clase. (Dron, Coche o Camion)
 * @author Ernesto Gomez Leyva, Pedro Hurtado Gonzalez y Carmen Bueno Ben Boubker
 * @version 1.0
 */    
    public Heuristica(int vision, int consumo){
        
        this.datosSensores = new Sensores(vision, consumo);
        this.mapa = new int[1000][1000];
        this.estadoBusqueda = true;
        this.goal = new Coordenada(-1,-1);
        this.meta = new Coordenada[4];
        
        this.scanner = new double[3][3];
        
        for (int i = 0; i < 4; i++) {
            this.meta[i] = new Coordenada(-1, -1);
        }
        for(int i=0; i<1000; i++){
            for(int j=0; j<1000; j++){
                this.mapa[j][i]= 1;
            }
        }
    }
    
    public abstract String siguienteAccion();
    
 /**
 * Método actualizarScanner rellena una matriz con los valores relativos a la distancia que hay hasta el objetivo,
 * se trata de un scanner interno que utilizamos para la heuristica interna.
 * @author Ernesto Gomez Leyva, Pedro Hurtado Gonzalez y Carmen Bueno Ben Boubker
 * @version 1.0
 */  
    public void actualizarScanner(){
        System.out.println("\nACTUALIZADO SCANNER");

        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){

                scanner[i][j]= distancia(this.goal.getX(), this.goal.getY(), this.datosSensores.getGPS().getX()+i-1 ,this.datosSensores.getGPS().getY()+j-1);
        
                System.out.format("%.2f", scanner[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
    
 /**
 * Método distancia calcula la distancia entre dos puntos (x1,y1) y (x2,y2)
 * @author Ernesto Gomez Leyva, Pedro Hurtado Gonzalez y Carmen Bueno Ben Boubker
 * @version 1.0
 */  
    public double distancia(int x1, int y1, int x2, int y2){
             
        double distancia = Math.sqrt(Math.pow( (double)(x2-x1) , 2.0) + Math.pow( (double)(y2-y1), 2.0));
        
        return distancia;
    }

    public abstract double obtenerMejorCasilla(int i, int j);

    public abstract String movimiento(int i, int j);
    
/**
* Método setMeta recibe una coordenada que es introducida en el dato meta de la clase.
* @author Ernesto Gomez Leyva, Pedro Hurtado Gonzalez y Carmen Bueno Ben Boubker
* @version 1.0
*/  
    public void setMeta(Coordenada[] m){
        this.meta = m;
    }
/**
* Método setMeta recibe un numero entero que equivale a la posición en el array de meta y se actualiza los datos con el contenido
* del gps en ese momento.
* @author Ernesto Gomez Leyva, Pedro Hurtado Gonzalez y Carmen Bueno Ben Boubker
* @version 1.0
*/  
    public void setMeta(int i){
        if(this.datosSensores.getRadar()[this.datosSensores.getRadar().length/2][this.datosSensores.getRadar().length/2]==3){
            this.meta[i].setX(this.datosSensores.getGPS().getX());
            this.meta[i].setY(this.datosSensores.getGPS().getY());
            if (this.goal.getX()==-1){
                this.goal.setX(this.datosSensores.getGPS().getX());
                this.goal.setY(this.datosSensores.getGPS().getY());
            }
        }
    }
 /**
* Método setCoordenadaMeta recibe las coordenadas x e y y son almacenadas en el objetivo.
* @author Ernesto Gomez Leyva, Pedro Hurtado Gonzalez y Carmen Bueno Ben Boubker
* @version 1.0
*/    
    public void setCoordenadaMeta(int x, int y){
        this.goal.setX(x);
        this.goal.setY(y);
    }
/**
* Método getTodosMeta devuelve un booleano dependiendo de si todos los agentes han llegado
 * a la meta.
* @author Ernesto Gomez Leyva, Pedro Hurtado Gonzalez y Carmen Bueno Ben Boubker
* @version 1.0
*/    
    public boolean getTodosMeta(){
        int n=0;
        for(int i=0; i<4; i++){
            if(this.meta[i].getX() != -1){
                n++;
            }
        }
        
        if(n == 4)
            this.todosMeta=true;
        else
            this.todosMeta=false;
        
        return this.todosMeta;
    }
    
/**
 * Método getMapa devuelve una matriz con el mapa interno de la clase
 * @author Ernesto Gomez Leyva, Pedro Hurtado Gonzalez y Carmen Bueno Ben Boubker
 * @version 1.0
 */    
    public int[][] getMapa(){
        return this.mapa;
    }
/**
 * Método SetMapa recibe una matriz con un mapa que es almacenado en el mapa interno de la clase
 * y despues es mostrado.
 * @author Ernesto Gomez Leyva, Pedro Hurtado Gonzalez y Carmen Bueno Ben Boubker
 * @version 1.0
 */        
    public void setMapa(int[][] mapa){
        this.mapa = mapa;
        this.mostrarMapa();
    }
 /**
 * Método getInformacion almacena en un JsonObjet la información requerida por el siguiente agente.
 * Almacena el contenido de meta (booleanos) y las coordenadas del objetivo a continuación.
 * @author Ernesto Gomez Leyva, Pedro Hurtado Gonzalez y Carmen Bueno Ben Boubker
 * @version 1.0
 */       
      public JsonObject getInformacion(){
        
        JsonObject m = new JsonObject();
        JsonObject coordJson;
        JsonArray ja = new JsonArray();
        System.out.println(ja.toString());
        
        for(int i = 0; i < 4; i++){
            coordJson = new JsonObject();
            coordJson.add("x",this.meta[i].getX());
            coordJson.add("y",this.meta[i].getY());
            ja.add(coordJson);
        }
                    
        ja.add(this.goal.getX());
        ja.add(this.goal.getY());
        
        m.add("estadoMeta", ja);
        
        return m;
    }
///**
// * Método Actualizar Mapa
// * @author Concepcion Carcedo Carnero e Isidora Reina Molina
// * @version 1.0
// */        
//    public void actualizarMapa(){
//        
//    }
/**
* Método getEstadoMeta devuelve el estado del sensor, si está en el objetivo o no.
* @author Concepcion Carcedo Carnero e Isidora Reina Molina
* @version 1.0
*/ 
    public boolean getEstadoMeta(){
        return this.datosSensores.getGoal();
    }
    
/**
 * Método actualizar Sensores realiza una actualización de los datos del sensor por un Json de datos que se le pasa por parametro
 * @author Concepcion Carcedo Carnero e Isidora Reina Molina
 * @version 1.0
 */        
    public void actualizarSensores(JsonObject datos){
        this.datosSensores.actualizar(datos);
        //this.mapa[this.datosSensores.getGPS().getX()][this.datosSensores.getGPS().getY()]=4;
        //this.mostrarMapa();
    }
/**
 * Método Mostrar Mapa pinta por pantalla los valores del mapa interno de la clase.
 * @author Concepcion Carcedo Carnero e Isidora Reina Molina
 * @version 1.0
 */        
    public void mostrarMapa(){
        for(int i=0; i<1000; i++){
            for(int j=0; j<1000; j++){
                System.out.print(this.mapa[j][i]);
            }
            System.out.println();
        }
    }
    
    /**
 * Método Se Puede Aparcar
 * @author Concepcion Carcedo Carnero e Isidora Reina Molina
 * @version 1.0
 */
    public boolean sePuedeAparcar(int i, int j){
        int actualI = this.datosSensores.getRadar().length/2-1;
        int actualJ = this.datosSensores.getRadar().length/2-1;
        for (int k = 0; k < 4; k++) {
            if (i == actualI && j == actualJ){
                if(this.datosSensores.getGPS().getX() - 1 == this.meta[k].getX() && this.datosSensores.getGPS().getY() - 1 == this.meta[k].getY()){
                    return false;
                }
            }
            else if (i == actualI && j == actualJ+1){
                if(this.datosSensores.getGPS().getX()== this.meta[k].getX() &&  this.datosSensores.getGPS().getY() - 1 == this.meta[k].getY()){
                    return false;
                }
            }
            else if (i == actualI && j == actualJ+2){
                if(this.datosSensores.getGPS().getX() + 1 == this.meta[k].getX() &&  this.datosSensores.getGPS().getY() - 1 == this.meta[k].getY()){
                    return false;
                }
            }
            else if (i == actualI+1 && j == actualJ){
                if(this.datosSensores.getGPS().getX() - 1 == this.meta[k].getX() &&  this.datosSensores.getGPS().getY()== this.meta[k].getY()){
                    return false;
                }
            }
            else if (i == actualI+1 && j == actualJ+2){
                if(this.datosSensores.getGPS().getX() + 1 == this.meta[k].getX() && this.datosSensores.getGPS().getY()== this.meta[k].getY()){
                    return false;
                }
            }
            else if (i == actualI+2 && j == actualJ){
                if(this.datosSensores.getGPS().getX() - 1 == this.meta[k].getX() && this.datosSensores.getGPS().getY() + 1 == this.meta[k].getY()){
                    return false;
                }
            }
            else if (i == actualI+2 && j == actualJ+1){
                if(this.datosSensores.getGPS().getX()== this.meta[k].getX() &&  this.datosSensores.getGPS().getY() + 1== this.meta[k].getY()){
                    return false;
                }
            }
            else{
                if(this.datosSensores.getGPS().getX() + 1== this.meta[k].getX() &&  this.datosSensores.getGPS().getY() + 1== this.meta[k].getY()){
                    return false;
                }
            }      
        }
        return true;
    }
    
}

