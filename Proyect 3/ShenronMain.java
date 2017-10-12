package practica3;

import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.AgentsConnection;

/**
 *
 * @author Pedro Hurtado
 */
public class ShenronMain {
    public static void main(String[] args) {
        String controller = "Denebola",
                user="Pamuk",
                password="Eridano";
        
        System.out.println("Conectando ...");
        AgentsConnection.connect("isg2.ugr.es",6000, "test", user, password, false);
        
        try {
            System.out.println("Invocando a Shenron " + "...");
            Shenron s = new Shenron(new AgentID("Pamuk"), controller, user, password);
            s.Reboot(); //En caso de que queramos reiniciar el servidor
            //s.Log(); //En caso de que queramos obtener informacion de la ultima conexion
        } catch (Exception ex) {
        }
        
    }
}
