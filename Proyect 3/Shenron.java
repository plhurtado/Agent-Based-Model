import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;

public class Shenron extends SingleAgent{
    
        ACLMessage outbox, inbox;
        JsonObject injson, outjson;
        String controller, user, password;

    public Shenron(AgentID aid, String c, String u, String p) throws Exception {
        super(aid);
        controller = c;
        user = u;
        password = p;
    }
    
    /*@Override
    public void execute()  {
        System.out.println("Ejecutando a Mutenroshi");
        Reboot();
        System.out.println("Fin de Mutenroshi");
    }*/
    
    void Reboot()  {
        //Configuramos el paquete con los datos de emisor, receptor, performativa y contenido (usuario, contraseña y controlador)
        System.out.println("Reseteando server "+controller);
        outbox = new ACLMessage();
        outbox.setSender(this.getAid());
        outbox.setReceiver(new AgentID("Shenron"));
        outbox.setPerformative(ACLMessage.REQUEST);
        outjson = new JsonObject();
        outjson.add("controller",controller);
        outjson.add("user",user);
        outjson.add("password", password);
        outbox.setContent(outjson.toString());
        
        this.send(outbox);
        try {
            System.out.println("Obteniendo respuesta");
            inbox = this.receiveACLMessage();
        } catch (InterruptedException ex) {
            System.out.println("Error en la recepcion del mensaje");
        }
        //Caso en el que el servidor devuelva error en respuesta al mensaje enviado
        if (inbox.getPerformativeInt() == ACLMessage.FAILURE)
            System.out.println("El servidor devolvio Error en respuesta al mensaje enviado \n");
        //En caso de que devuelva un INFORM podemos seguir con la comunicación
        else if (inbox.getPerformativeInt() == ACLMessage.INFORM)
            System.out.println("El reinicio del servidor se ha realizado correctamente \n");
        else
            System.out.println("Not understood \n");
    }
    void Log() throws InterruptedException{
        
        System.out.println("Pidiendo ultimos datos");
        outbox = new ACLMessage();
        outbox.setSender(this.getAid());
        outbox.setReceiver(new AgentID("Shenron"));
        outbox.setPerformative(ACLMessage.QUERY_REF);
        outjson = new JsonObject();
        outjson.add("controller",controller);
        outjson.add("user",user);
        outjson.add("password", password);
        outbox.setContent(outjson.toString());
        
        this.send(outbox);
        try {
            System.out.println("Obteniendo respuesta del servidor \n");
            inbox = this.receiveACLMessage();
        } catch (InterruptedException ex) {
            System.out.println("Error en la recepcion del mensaje \n");
        }
        
        if (inbox.getPerformativeInt() == ACLMessage.FAILURE)
            System.out.println("El servidor devolvio Error en respuesta al mensaje enviado. (NOTIFICAR AL PROFESOR)\n ");
        else if (inbox.getPerformativeInt() == ACLMessage.INFORM){
            System.out.println("La información facilitada por el servidor es: \n");
            JsonObject message = Json.parse( inbox.getContent() ).asObject();
            System.out.println("Fecha: " + message.getString("date", "Por defecto") + "\n");
            System.out.println("Valor: " + message.getString("value", "Por defecto") + "\n");
            System.out.println("Conversacion: " + message.getString("conversation", "Por defecto") + "\n");
            System.out.println("Contenido: " + message.getString("content", "Por defecto") + "\n"); //Puede que este no este bien
        }
        else
            System.out.println("Not understood");
    }
}