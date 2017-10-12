/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica3;

import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.AgentsConnection;

/**
 * Clase Client se encarga de inicializar los agentes y ejecutar la aplicación.
 * @author Concepcion Carcedo Carnero
 * @version 1.0
 */

public class Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        String Agente0 = "AGENTE0";
        String Agente1 = "AGENTE1";
        String Agente2 = "AGENTE2";
        String Agente3 = "AGENTE3";
        
        String mapa = "map1";
        
        AgentsConnection.connect("isg2.ugr.es", 6000, "Denebola", "Pamuk", "Eridano", false);
        Agente agente0 = new Agente(new AgentID(Agente0),new AgentID(Agente1),0 ,mapa);
        Agente agente1 = new Agente(new AgentID(Agente1),new AgentID(Agente2),1 ,null);
        Agente agente2 = new Agente(new AgentID(Agente2),new AgentID(Agente3),2 ,null);
        Agente agente3 = new Agente(new AgentID(Agente3),new AgentID(Agente0),3 ,null);
        
        agente0.start();
        agente1.start();
        agente2.start();
        agente3.start();
    }
    
}
