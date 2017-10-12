/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica3;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase Agente representa al agente dentro de la sociedad y se encarga de las comunicaciones y control del token
 * @author Concepción Carcedo Carnero, Martin Zumarraga Uribe e Isidora Reina Molina
 * @version 1.0
 */

public class Agente extends SingleAgent {
    
    private AgentID siguienteAgente;
    private int numeroAgent;
    
    private Rol rol;
    private boolean token;
    private String conversationID, reply, mapa;
    private Integer rastreado;
    private JsonObject informacion,answer, msg, traza;
    private ACLMessage inbox, outbox;
    int fulrate=0;
    
/**
 * Constructor de la Clase Agente crea la instancia de la clase inicializando las variables internas de la clase.
 * @author Concepción Carcedo Carnero, Martin Zumarraga Uribe e Isidora Reina Molina
 * @version 1.0
 */
    public Agente(AgentID aid, AgentID siguiente, int n, String mapa) throws Exception {
        
        super(aid);
        
        this.siguienteAgente = siguiente;
        this.numeroAgent = n;
        this.token = false;
        this.mapa = mapa;
        this.informacion = new JsonObject();
        this.answer = new JsonObject();
        this.msg = new JsonObject();
        this.traza = new JsonObject();
        
        this.inbox = new ACLMessage();
        this.outbox = new ACLMessage();
        
    }
    
/**
 * Método Suscribir realiza la operación de enviar al servidor un mensaje en Json para logearse
 * en un mundo (mapa), dependiendo de la respuesta de servidor, monstrará por pantalla el id de conversación
 * o un mensaje de error.
 * @author Concepción Carcedo Carnero, Martin Zumarraga Uribe e Isidora Reina Molina
 * @version 1.0
 */
    public void subscribe() {
        
        this.msg.add("world", this.mapa);
        this.outbox.setSender(this.getAid());
        this.outbox.setReceiver(new AgentID("Denebola"));
        this.outbox.setPerformative(ACLMessage.SUBSCRIBE);
        this.outbox.setContent(msg.toString());
        this.send(outbox);
        
        this.inbox = null;
        try {
            this.inbox = this.receiveACLMessage();
        } catch (InterruptedException ex) {
            Logger.getLogger(Agente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(inbox.getPerformativeInt() == ACLMessage.INFORM){
            this.conversationID = this.inbox.getConversationId();
            System.out.println("\nCONVERSATION-ID : "+ this.conversationID);
        }else{
            System.out.println("\nERROR EN EL SUBSCRIBE : " + inbox.getPerformative());
        }
    }

/**
 * Método Registrar realiza la operación de enviar al servidor un mensaje en Json para que un vehiculo sea
 * registrado en el mundo. Si el servidor no devuelve un mensaje de error, con la información recivida
 * realizaremos la creación de los diferentes roles que podemos obtener, para ello tendremos en cuenta en valor de 
 * la variable 'fuelrate' e imprimimos por pantalla el nombre del agente y el rol que ha recibido.
 * @author Concepción Carcedo Carnero, Martin Zumarraga Uribe e Isidora Reina Molina
 * @version 1.0
 */    
    public void registrar(){
        
        this.msg = new JsonObject();
        this.msg.add("command", "checkin");
        this.outbox.setSender(this.getAid());
        this.outbox.setReceiver(new AgentID("Denebola"));
        this.outbox.setPerformative(ACLMessage.REQUEST);
        this.outbox.setConversationId(this.conversationID);
        this.outbox.setContent(msg.toString());
        this.send(outbox);
        
        this.inbox = null;
        try {
            this.inbox = this.receiveACLMessage();
        } catch (InterruptedException ex) {
            Logger.getLogger(Agente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(this.inbox.getPerformativeInt() == ACLMessage.INFORM){
            this.answer = Json.parse(this.inbox.getContent()).asObject();
            this.reply = inbox.getReplyWith();
            
            JsonObject capabilities = this.answer.get("capabilities").asObject();
            System.out.println("\nRECIBIENDO CAPABILITIES 'FUELRATE': " + capabilities.get("fuelrate").asInt() 
                    + " 'RANGE': " + capabilities.get("range").asInt() + " 'FLY': " + capabilities.get("fly").asBoolean());
            
            fulrate= capabilities.get("fuelrate").asInt();
            switch(capabilities.get("fuelrate").asInt()){
                case (1):
                    System.out.println("\nEl " + this.getName() + " es un COCHE.");
                    this.rol = new Coche(capabilities.get("range").asInt(),capabilities.get("fuelrate").asInt());
                    break;
                case (2):
                    System.out.println("\nEl " + this.getName() + " es un DRON.");
                    this.rol = new Dron(capabilities.get("range").asInt(),capabilities.get("fuelrate").asInt());
                    break;
                case (4):
                    System.out.println("\nEl " + this.getName() + " es un CAMION.");
                    this.rol = new Camion(capabilities.get("range").asInt(),capabilities.get("fuelrate").asInt());  
                    break;
            }
        }else{
            System.out.println("\nERROR EN EL REQUEST : " + this.inbox.getPerformative());
        }
    }

/**
 * Método Cancelar realiza la operación de enviar al servidor un mensaje en Json con el cierre de sesión.
 * Del servidor recibimos dos mensajes de respuesta. El primero con el resultado de la operación, y el segundo 
 * con la traza de la sesión de trabajo generada. Seguidamente convertimos la traza en una imagen llamando a una
 * función auxiliar.
 * @author Concepción Carcedo Carnero, Martin Zumarraga Uribe e Isidora Reina Molina
 * @version 1.0
 */    
    public void cancel(){
       
        //Mandamos cancel para cerrar sesion
        this.outbox.setSender(this.getAid());
        this.outbox.setReceiver(new AgentID("Denebola"));
        this.outbox.setPerformative(ACLMessage.CANCEL);
        this.outbox.setContent("");
        this.send(outbox);
        
        //Recibimos en el siguiente mensaje que se ha cerrado la sesion correctamente
        this.inbox = null;
        try {
            this.inbox = this.receiveACLMessage();
        } catch (InterruptedException ex) {
            Logger.getLogger(Agente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(inbox.getPerformativeInt() == ACLMessage.AGREE){
            this.traza = Json.parse(this.inbox.getContent()).asObject();
            System.out.println("\nCERRANDO SESION :" + traza.get("result").asString());
        }
        
        //Y en el siguiente mensaje recibimos la traza  
        this.inbox = null;
        try {
            this.inbox = this.receiveACLMessage();
        } catch (InterruptedException ex) {
            Logger.getLogger(Agente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(inbox.getPerformativeInt() == ACLMessage.INFORM){
            this.traza = Json.parse(this.inbox.getContent()).asObject();
            System.out.println("\nGENERANDO TRAZA...");
            this.convertirTraza();
        }
        
    }
 
/**
 * Método Mandar Clave realiza un mensaje en Json que es enviado al siguiente agente que tenga asignado y el 
 * contenido del mensaje será el identificador de conversación que se utilizará para comunicarse con el servidor.
 * @author Ernesto Gomez Leyva
 * @version 1.0
 */
    public void mandarClave(){

        this.outbox.setSender(this.getAid());
        this.outbox.setReceiver(this.siguienteAgente);
        this.outbox.setPerformative(ACLMessage.INFORM);
        JsonObject auxJson = new JsonObject();
        auxJson.add("conversationID", this.conversationID);
        this.outbox.setContent(auxJson.toString());
        this.send(outbox);
    }
/**
 * Método Recibir Clave recibe un mensaje cuyo contenido es el id de conversación y lo mostramos por pantalla.
 * @author Ernesto Gomez Leyva
 * @version 1.0
 */  
    public void recibirClave(){
        
        this.inbox = null;
        try {
            this.inbox = this.receiveACLMessage();
        } catch (InterruptedException ex) {
            Logger.getLogger(Agente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(inbox.getPerformativeInt() == ACLMessage.INFORM){
            JsonObject auxJson = new JsonObject();
            auxJson =  Json.parse(this.inbox.getContent()).asObject();
            this.conversationID = auxJson.getString("conversationID", "");
            System.out.println("\n"+ this.getName()+ " RECIBIENDO CONVERSATION-ID :" + this.conversationID);
            
        }
    }  

/**
 * Método Mandar Acción realiza un comunicado con el servidor dependiendo de la acción que se quiera realizar.
 * Envía un mensaje en Json al servidor y puede ser pedir datos, realizar refuel o movimiento y recoge los datos
 * recividos según lo que se haya pedido, en el caso de que no se pueda realizar la accion solicitada, muestra el error
 * y se cierra sesión.
 * @author Concepción Carcedo Carnero, Martin Zumarraga Uribe e Isidora Reina Molina
 * @version 1.0
 */
    public void mandarAccion(String accion){
        
        this.outbox.setSender(this.getAid());
        this.outbox.setReceiver(new AgentID("Denebola"));
        
        if(accion == "pedir")
            this.outbox.setPerformative(ACLMessage.QUERY_REF);
        else{
            this.outbox.setPerformative(ACLMessage.REQUEST);
            this.msg = new JsonObject();
            this.msg.add("command", accion);
            this.outbox.setContent(this.msg.toString());
        }
        
        this.outbox.setConversationId(this.conversationID);
        this.outbox.setInReplyTo(this.reply);
        this.send(outbox);
        
        this.inbox = null;
        try {
            this.inbox = this.receiveACLMessage();
            this.reply = this.inbox.getReplyWith();
        } catch (InterruptedException ex) {
            Logger.getLogger(Agente.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(inbox.getPerformativeInt() == ACLMessage.INFORM){
            this.answer = new JsonObject();
            this.answer = Json.parse(this.inbox.getContent()).asObject();
            
            if(accion == "pedir"){
                JsonObject result = this.answer.get("result").asObject();
                System.out.println("\n"+ this.getName()+ " RECIBIENDO QUERY_REF");
                this.rol.actualizarSensores(result);
            }
            else if(accion == "refuel"){
                System.out.println("REFUEL :" + this.answer.get("result".toString()));
            }
            else{
                System.out.println("MOVE :" + this.answer.get("result".toString()));
            }
        }else{
            System.out.println("ERROR MANDAR ACCION : " + this.inbox.getPerformative());
            this.answer = Json.parse(this.inbox.getContent()).asObject();
            System.out.println("\nCERRANDO SESION :" + answer.get("details").asString());
            this.cancel();
        }
    }
    
/**
 * Método Esperar Token donde el agente espera recibir un mensaje de otro agente cuyo contenido es la infomación relativa
 * al estado de la meta, si se ha llegado al objetivo y donde se encuentra. Una vez recibido, el agente actualiza sus datos y pone su 
 * token a true. En el caso de haber un error, es mostrado por pantalla.
 * @author Concepción Carcedo Carnero, Martin Zumarraga Uribe e Isidora Reina Molina
 * @version 1.0
 */    
    private void esperarToken() {
        this.inbox = null;
        try {
            this.inbox = this.receiveACLMessage();
        } catch (InterruptedException ex) {
            Logger.getLogger(Agente.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        if(inbox.getPerformativeInt() == ACLMessage.INFORM){
           this.informacion = new JsonObject();
           this.informacion = Json.parse(this.inbox.getContent()).asObject();

           System.out.println("INFORMACION :" + this.informacion.get("estadoMeta").toString());
           this.rol.actualizarEstado(informacion);
           this.token = true;
       }else{
           System.out.println("ERROR RECIBIR TOKEN: "+this.numeroAgent + " "+ this.inbox.getPerformative());
        }
    }
/**
 * Método Mandar Token realiza un envío de un mensaje en Json para el siguiente agente, el contenido es la información recabada
 * hasta ahora. Su token es puesto a false.
 * @author Concepción Carcedo Carnero, Martin Zumarraga Uribe e Isidora Reina Molina
 * @version 1.0
 */  
    private void mandarToken() {
        
        this.informacion = new JsonObject();
        this.informacion = this.rol.getInformacion();
        
        this.outbox.setSender(this.getAid());
        this.outbox.setReceiver(this.siguienteAgente);
        this.outbox.setPerformative(ACLMessage.INFORM);
        this.outbox.setContent(this.informacion.toString());
        
        this.token = false;
        this.send(outbox);
    }

/**
 * Método Convertir Traza convierte la traza recibida por el servidor (JsonArray) en un array de bytes que
 * se guardará en un archivo .png con el nombre del mapa que se ha explorado.
 * @author Carmen Bueno Ben Boubker
 * @version 1.0
 */
    public void convertirTraza(){
        
        JsonArray ja = traza.get("trace").asArray();
        System.out.println(ja.toString());
        
        byte data[] = new byte [ja.size()];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) ja.get(i).asInt();           
        }
        try {         
            FileOutputStream fos = new FileOutputStream(mapa+".png");
            fos.write(data);
            fos.close();
            System.out.println("TRAZA GUARDADA");         
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Agente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Agente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

/**
 * Método EXECUTE es el método que ejecuta cada agente y realiza las operaciones necesarias, dependiendo de qué
 * agente se trate tendrá unas funciones u otras. El agente 0 se subscribe y se registra hasta que sea un dron y a continuación 
 * lo hacen los siguientes agentes. El token no se pasa hasta que el dron encuentre el objetivo. Y pasa el token al siguiente agente para
 * encontrar el objetivo y así sucesivamente hasta que todos lleguen a la meta y se cierra la sesión.
 * @author Carmen Bueno Ben Boubker
 * @version 1.0
 */
    public void execute(){
//        if (this.numeroAgent==0)
//            this.cancel();
        if(this.getName() == "AGENTE0"){
            do{
                
                //this.cancel();
                this.subscribe();
                this.token = true;
                
                this.registrar();
                
                if(this.fulrate !=2)
                    this.cancel();
                
            }while(this.fulrate != 2);
            
            //Pido informacion al servidor
            this.mandarAccion("pedir");
            this.mandarClave();
        }
        
        if(this.getName() != "AGENTE0"){
            this.recibirClave();
            if(this.getName() != "AGENTE3"){
                this.mandarClave();
            }
            //Se registra en el servidor
            this.registrar();
        }
                
        
        
        if(this.getName() == "AGENTE0"){
            do{
                //Realizo movimiento
                String accion = this.rol.realizarMovimiento();
                System.out.println("\nMOVIMIENTO : " + this.getName() + " "+ accion);

                //Mando la accion
                System.out.println("\nACCION " + accion);
                this.mandarAccion(accion);

                //Actualizo mi posicion para saber si estoy en la meta
                this.mandarAccion("pedir");

            }while(!this.rol.getEstado());
            
            this.rol.setEstadoMeta(numeroAgent);
            this.mandarToken(); 
            
            //Mientras no están todos en la meta y estoy en la meta paso el token
            while(!this.rol.getTodosMeta()){
                while(!token)
                    this.esperarToken();

                if(this.rol.getEstado())
                    this.mandarToken();
            }
            //Cuando estan todos en la meta cierro sesion
            this.cancel();
            
        }else{
            do{
                while(!token)
                    this.esperarToken();
                //Si no estoy en la meta hago movimiento sino paso el token
                if(!this.rol.getEstado()){
                    this.mandarAccion("pedir");
                    //Realizo movimiento
                    String accion = this.rol.realizarMovimiento();
                    System.out.println("\nMOVIMIENTO : " + this.getName() + " "+ accion);

                    //Mando la accion
                    System.out.println("\nACCION " + accion);
                    this.mandarAccion(accion);

                    //Actualizo mi posicion para saber si estoy en la meta
                    this.mandarAccion("pedir");
                    this.rol.setEstadoMeta(numeroAgent);
                }
                this.mandarToken();

            }while(!this.rol.getTodosMeta());
            
        }
               
    }

 }

