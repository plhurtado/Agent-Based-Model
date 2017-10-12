/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica3;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

/**
 * Clase Dron
 * @author Carmen Bueno Ben Boubker y Martin Zumarraga Uribe
 * @version 1.0
 */

public class Dron extends Rol{
    
    private HeuristicaDron heuristica;
/**
 * Constructor de la clase Dron crea el objeto dron que hereda de la clase Rol e inicializa la heurísitca del dron
 * @author Carmen Bueno Ben Boubker y Martin Zumarraga Uribe
 * @version 1.0
 */
    public Dron(int vision, int consumo) {
        super();
        System.out.println("Dron con vision " + vision + " y consumo " + consumo);
        this.heuristica = new HeuristicaDron(vision, consumo);
    }
/**
 * Método sobreescrito GetEstado devuelve el estado de la meta, que se encuentra en la clase heurísitca.
 * @author Carmen Bueno Ben Boubker y Martin Zumarraga Uribe
 * @version 1.0
 */
    @Override
    public boolean getEstado() {
        return this.heuristica.getEstadoMeta();
    }
/**
 * Método sobreescrito Realizar Movimiento llama al método siguiente acción de la heurísitica
 * @author Carmen Bueno Ben Boubker y Martin Zumarraga Uribe
 * @version 1.0
 */
    @Override
    public String realizarMovimiento() {

        return this.heuristica.siguienteAccion();        
    }
/**
 * Método sobreescrito GetInformacion devuelve la información relativa resultante de la heurística
 * @author Carmen Bueno Ben Boubker y Martin Zumarraga Uribe
 * @version 1.0
 */
    @Override
    public JsonObject getInformacion() {
        return this.heuristica.getInformacion();
    }
/**
 * Método sobreescrito Actualizar Sensores recibe un objeto Json con datos que serán actualizados
 * por la heuristica.
 * @author Carmen Bueno Ben Boubker y Martin Zumarraga Uribe
 * @version 1.0
 */
    @Override
    public void actualizarSensores(JsonObject datos) {
        this.heuristica.actualizarSensores(datos);
    }
    
    /**
 * Método sobreescrito ActualizarEstado recibe por parametro un Json con la información relativa a la meta
 * si se ha llegado a ella o no y en las dos ultimas posiciones se almacena las coordenadas x e y del objetivo.
 * @author Carmen Bueno Ben Boubker y Martin Zumarraga Uribe
 * @version 1.0
 */
    
    @Override
    public void actualizarEstado(JsonObject informacion) {
              
       JsonArray rad=informacion.get("estadoMeta").asArray();
       JsonObject coordJson;
       System.out.println("ACTUALIZANDO META");
       Coordenada [] meta = new Coordenada[4];
       int j=0,k=0;
       for(int i=0;i<rad.size()-2;i++){
           coordJson = new JsonObject();
           coordJson = rad.get(i).asObject();
           meta[i]= new Coordenada(coordJson.getInt("x", -1), coordJson.getInt("y", -1));
       }
       
       this.heuristica.setCoordenadaMeta(rad.get(4).asInt(), rad.get(5).asInt());
       this.heuristica.setMeta(meta);
    }

    /**
 * Método sobreescrito setEstadoMeta invoca la función setMeta de heuristica pasandole el parámetro
 * recibido.
 * @author Carmen Bueno Ben Boubker y Martin Zumarraga Uribe
 * @version 1.0
 */
    @Override
    public void setEstadoMeta(int i) {
        this.heuristica.setMeta(i);
    }
     
    
    /**
 * Método sobreescrito getTodosMeta devuelve un booleano en funcion de si todos los agentes
 * se encuentran en la meta.
 * @author Carmen Bueno Ben Boubker y Martin Zumarraga Uribe
 * @version 1.0
 */
    @Override
    public boolean getTodosMeta() {
        return this.heuristica.getTodosMeta();
    }
}
