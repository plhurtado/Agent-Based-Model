/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica3;

/**
 * Clase Heuristica Coche
 * @author Pedro Hurtado Gonzalez y Ernesto Gomez Leyva
 * @version 1.0
 */    

public class HeuristicaCoche extends Heuristica{

    private double minValueFind;
    private String accionAnterior;
    
    public HeuristicaCoche(int vision, int consumo) {
        super(vision, consumo);
        this.minValueFind= Double.POSITIVE_INFINITY;
        this.accionAnterior = "";
    }

/**
* Metodo SiguienteAccio. El coche mandará la acción refuel si la bateria es inferior a 2, sino la acción a desempeñar será
 * la de movimiento. Si se puede iniciar el algoritmo de la mano derecha, lanzará dicha heuristica. De no ser así, se 
 * lanzará la heurística básica que consiste en un algoritmo greedy con pesos.
* @author Pedro Hurtado Gonzalez y Ernesto Gomez Leyva
* @version 1.0
*/ 
    @Override
    public String siguienteAccion() {
        
        if(super.datosSensores.getBattery() < 2){
            System.out.println("CONTROLLER: REFUEL");
            return "refuel";
        }
        
        super.actualizarScanner();
        
        if(this.iniciarManoDerecha()){
            System.out.println("\nHEURISTICA 2");

            this.accionAnterior= manoDerecha();
            return this.accionAnterior;
        }
        else{
            
            System.out.println("\nHEURISTICA 1");
             
            double mejorOpcion = 1000000.0;
            int mejorI = 0;
            int mejorJ = 0;
            int borde = ((super.datosSensores.getRadar().length-1)/2)-1;
            int mitad = (super.datosSensores.getRadar().length-1)/2;

            for (int i=borde; i<mitad+2;i++){
                    for(int j=borde; j<mitad+2;j++){
                        if(!(i==mitad && j==mitad)){
                            if(super.datosSensores.getRadar()[i][j]==3 && super.sePuedeAparcar(i, j) ){
                                return this.movimiento(i,j);
                            }else if (super.datosSensores.getRadar()[i][j]==0){
                                if(this.obtenerMejorCasilla(i,j)<mejorOpcion){
                                    mejorOpcion = this.obtenerMejorCasilla(i,j);
                                    mejorI = i;
                                    mejorJ = j;
                                } 
                            }
                        }
                    }
            }
            String accion = this.movimiento(mejorI,mejorJ);
            return accion;
        } 
    }

 /**
 * Metodo iniciar mano derecha donde se realiza una valoración para realizar el algoritmo de la mano derecha, en el cual
 * vamos a utilizar un scanner creado internamente para ayudarnos con la orientación hacia el objetivo una vez sabemos donde
 * se encuentra. La función devuelve true o false dependiendo de si vamos a lanzar el algoritmo o no.
 * @author Pedro Hurtado Gonzalez y Ernesto Gomez Leyva
 * @version 1.0
 */ 
    public boolean iniciarManoDerecha(){
        
        double minObst= Double.POSITIVE_INFINITY;
        double minVoid= Double.POSITIVE_INFINITY;

        for(int i=1; i<4; i++){
            for(int j=1; j<4; j++){
                if((i!=2 && j!=2) && (this.datosSensores.getRadar()[i][j] == 1 || this.datosSensores.getRadar()[i][j] == 2)){
                    if(super.scanner[i-1][j-1]<minObst)
                        minObst= super.scanner[i-1][j-1];
                }
                else if((i!=2 && j!=2) && (this.datosSensores.getRadar()[i][j] != 1 && this.datosSensores.getRadar()[i][j] != 2)){
                    if(super.scanner[i-1][j-1]<minVoid)
                        minVoid= super.scanner[i-1][j-1];
                }
            }
        }
        
        System.out.println("minVoid: " + minVoid + " --- minObst: " + minObst);
        
        if(minObst<minVoid && minVoid != Double.POSITIVE_INFINITY){
            if(this.minValueFind>super.scanner[1][1]){
                this.minValueFind= super.scanner[1][1];
            }
        }
        
        System.out.println("minValue= " + this.minValueFind + " --- scanner: " + scanner[1][1] + " --- minVoid: " + minVoid);
        
        if(minObst == Double.POSITIVE_INFINITY || this.minValueFind>minVoid)
            return false;
        else if(this.minValueFind<minVoid)
            return true;
        else
            return true;
    }
 /**
 * Metodo mano derecha implementación del algoritmo de la mano derecha que consiste en comprobar en el radar los muros circundantes así como
 * la acción anterior elegida y en función de esto elegir la siguiente acción más apropiada para bordear muros pegados a ellos a la vez que nos acercamos
 * al objetivo sin crear lagunas en el recorrido. Una vez elegida la acción, se actualiza el mapa interno y se manda la acción.
 * @author Pedro Hurtado Gonzalez y Ernesto Gomez Leyva
 * @version 1.0
 */     
    public String manoDerecha(){
        
        String accion = new String();
        boolean accionElegida = false;
        
        if(!this.isMovE() && !this.isMovW() && !accionElegida){
            if(this.accionAnterior == "moveSW" || this.accionAnterior == "moveS" || this.accionAnterior == "moveSE"){
                if(this.isMovSW()){
                    accion = "moveSW";
                    accionElegida = true;
                }
                else if(this.isMovS()){
                    accion = "moveS";
                    accionElegida = true;
                }
            }
            else if(this.accionAnterior == "moveNW" || this.accionAnterior == "moveN" || this.accionAnterior == "moveNE"){
                if(this.isMovNE()){
                    accion = "moveNE";
                    accionElegida = true;
                }
                else if(this.isMovN()){
                    accion = "moveN";
                    accionElegida = true;
                }
            }
        }
            
        if(!this.isMovN() && !this.isMovS() && !accionElegida){ 
            if(this.accionAnterior=="moveSE" || this.accionAnterior=="moveE" || this.accionAnterior=="moveNE"){
                if(this.isMovSE()){
                    accion= "moveSE";
                    accionElegida=true;
                }
                else if(this.isMovE()){
                    accion= "moveE";
                    accionElegida=true;
                }
            }
            else if(this.accionAnterior=="moveSW" || this.accionAnterior=="moveW" || this.accionAnterior=="moveNW"){
                if(this.isMovSW()){
                    accion= "moveSW";
                    accionElegida=true;
                }
                else if(isMovW()){
                    accion = "moveW";
                    accionElegida=true;
                }
            }
        }
            
        if(!this.isMovE() && !accionElegida){
            if(this.isMovNE()){
                accion = "moveNE";
                accionElegida=true;
            }
            else if(this.isMovN()){
                accion = "moveN";
                accionElegida=true;
            }
        }
        if(!this.isMovN() && !accionElegida){
            if(this.isMovNW()){
                accion = "moveNW";
                accionElegida=true;
            }
            else if(this.isMovW()){
                accion = "moveW";
                accionElegida=true;
            }
        }
        if(!this.isMovW() && !accionElegida){
            if(this.isMovSW()){
                accion= "moveSW";
                accionElegida=true;
            }
            else if(this.isMovS()){
                accion= "moveS";
                accionElegida=true;
            }
        }
        if(!this.isMovS() && !accionElegida){
            if(this.isMovSE()){
                accion= "moveSE";
                accionElegida=true;
            }
            else if(this.isMovE()){
                accion = "moveE";
                accionElegida=true;
            }
        }
        if(!this.isMovSE() && !accionElegida){
            if(this.isMovE()){
                accion = "moveE";
                accionElegida=true;
            }
            else if(this.isMovNE()){
                accion = "moveNE";
                accionElegida=true;
            }
        }
        if(!this.isMovSW() && !accionElegida){
            if(this.isMovS()){
                accion = "moveS";
                accionElegida=true;
            }
            else if(this.isMovSE()){
                accion = "moveSE";
                accionElegida=true;
            }
        }
        if(!this.isMovNW() && !accionElegida){
            if(this.isMovW()){
                accion = "moveW";
                accionElegida=true;
            }
            else if(this.isMovSW()){
                accion = "moveSW";
                accionElegida=true;
            }
        }
        if(!accionElegida){
            if(this.isMovN())
                accion = "moveN";
            else if(this.isMovNW())
                accion = "moveNW";
        }
        
        this.incrementarMapa(accion);
        
        return accion;
    }
  /**
 * Metodo incrementar mapa recibe una acción y dependiendo de esta, se incrementa en uno la posición del mapa a realizar la acción.
 * @author Pedro Hurtado Gonzalez y Ernesto Gomez Leyva
 * @version 1.0
 */    
    public void incrementarMapa(String accion){ 
        
        if(accion == "moveNW")
            this.mapa[this.datosSensores.getGPS().getX() - 1][this.datosSensores.getGPS().getY() - 1]++;
        else if (accion == "moveN")
            this.mapa[this.datosSensores.getGPS().getX()][this.datosSensores.getGPS().getY() - 1]++;
        else if (accion == "moveNE")
            this.mapa[this.datosSensores.getGPS().getX() + 1][this.datosSensores.getGPS().getY() - 1]++;
        else if (accion == "moveW")
            this.mapa[this.datosSensores.getGPS().getX() - 1][this.datosSensores.getGPS().getY()]++;
        else if (accion == "moveE")
            this.mapa[this.datosSensores.getGPS().getX() + 1][this.datosSensores.getGPS().getY()]++;
        else if (accion == "moveSW")
            this.mapa[this.datosSensores.getGPS().getX() - 1][this.datosSensores.getGPS().getY() + 1]++;
        else if (accion == "moveS")
            this.mapa[this.datosSensores.getGPS().getX()][this.datosSensores.getGPS().getY() + 1]++;
        else
            this.mapa[this.datosSensores.getGPS().getX() + 1][this.datosSensores.getGPS().getY() + 1]++;
    }

/**
 * Metodo isMov Dirección, dependiendo de la dirección a consultar, devuelve un booleano de si la posición a donde
 * se dirige en el radar es distinto de paredes(1) o muro de alrededor(2)
 * @author Pedro Hurtado Gonzalez y Ernesto Gomez Leyva
 * @version 1.0
 */ 
    public boolean isMovNW(){
        return this.datosSensores.getRadar()[1][1]!=1 && this.datosSensores.getRadar()[1][1]!=2;
    }
    
    public boolean isMovN(){
        return this.datosSensores.getRadar()[1][2]!=1 && this.datosSensores.getRadar()[1][2]!=2;
    }
    
    public boolean isMovNE(){
        return this.datosSensores.getRadar()[1][3]!=1 && this.datosSensores.getRadar()[1][3]!=2;
    }
    
    public boolean isMovW(){
        return this.datosSensores.getRadar()[2][1]!=1 && this.datosSensores.getRadar()[2][1]!=2;
    }
    
    public boolean isMovE(){
        return this.datosSensores.getRadar()[2][3]!=1 && this.datosSensores.getRadar()[2][3]!=2;
    }
    
    public boolean isMovSW(){
        return this.datosSensores.getRadar()[3][1]!=1 && this.datosSensores.getRadar()[3][1]!=2;
    }
    
    public boolean isMovS(){
        return this.datosSensores.getRadar()[3][2]!=1 && this.datosSensores.getRadar()[3][2]!=2;
    }
    
    public boolean isMovSE(){
        return this.datosSensores.getRadar()[3][3]!=1 && this.datosSensores.getRadar()[3][3]!=2;
    }
    
/**
* Metodo obtenerMejorCasilla recibe como parámetro una posición, dependiendo de cual sea ésta, realizará un calculo
 * para darle un valor ('peso') a dicha posición.
* @author Pedro Hurtado Gonzalez y Ernesto Gomez Leyva
* @version 1.0
*/ 
    @Override
    public double obtenerMejorCasilla(int i, int j) {
        if (i == 1 && j == 1){
            return this.mapa[this.datosSensores.getGPS().getX() - 1][this.datosSensores.getGPS().getY() - 1]*this.distancia(this.goal.getX(), this.goal.getY(), this.datosSensores.getGPS().getX() - 1, this.datosSensores.getGPS().getY() - 1);
        }
        else if (i == 1 && j == 2){
            return this.mapa[this.datosSensores.getGPS().getX()][this.datosSensores.getGPS().getY() - 1]*this.distancia(this.goal.getX(), this.goal.getY(), this.datosSensores.getGPS().getX(), this.datosSensores.getGPS().getY() - 1);
        }
        else if (i == 1 && j == 3){
            return this.mapa[this.datosSensores.getGPS().getX() + 1][this.datosSensores.getGPS().getY() - 1]*this.distancia(this.goal.getX(), this.goal.getY(), this.datosSensores.getGPS().getX() + 1, this.datosSensores.getGPS().getY() - 1);
        }
        else if (i == 2 && j == 1){
            return this.mapa[this.datosSensores.getGPS().getX() - 1][this.datosSensores.getGPS().getY()]*this.distancia(this.goal.getX(), this.goal.getY(), this.datosSensores.getGPS().getX() - 1, this.datosSensores.getGPS().getY());
        }
        else if (i == 2 && j == 3){
            return this.mapa[this.datosSensores.getGPS().getX() + 1][this.datosSensores.getGPS().getY()]*this.distancia(this.goal.getX(), this.goal.getY(), this.datosSensores.getGPS().getX() + 1, this.datosSensores.getGPS().getY());
        }
        else if (i == 3 && j == 1){
            return this.mapa[this.datosSensores.getGPS().getX() - 1][this.datosSensores.getGPS().getY() + 1]*this.distancia(this.goal.getX(), this.goal.getY(), this.datosSensores.getGPS().getX() - 1, this.datosSensores.getGPS().getY() + 1);
        }
        else if (i == 3 && j == 2){
            return this.mapa[this.datosSensores.getGPS().getX()][this.datosSensores.getGPS().getY() + 1]*this.distancia(this.goal.getX(), this.goal.getY(), this.datosSensores.getGPS().getX(), this.datosSensores.getGPS().getY() + 1);
        }
        else{
            return this.mapa[this.datosSensores.getGPS().getX() + 1][this.datosSensores.getGPS().getY() + 1]*this.distancia(this.goal.getX(), this.goal.getY(), this.datosSensores.getGPS().getX() + 1, this.datosSensores.getGPS().getY() + 1);
        }
    }

/**
 * Metodo movimiento recibe dos parámetros (posiciones) y dependiendo de éstas corresponderá a una acción de movimiento hacia una dirección
 * y se incrementará previamente el valor de esa casilla em uno.
 * @author Pedro Hurtado Gonzalez y Ernesto Gomez Leyva
 * @version 1.0
 */ 
    @Override
    public String movimiento(int i, int j) {
        
            switch(i){
                case 1: 
                    if(j==1){
                        this.mapa[this.datosSensores.getGPS().getX() - 1][this.datosSensores.getGPS().getY() - 1]++; 
                        return "moveNW";
                    }else if (j==2){
                        this.mapa[this.datosSensores.getGPS().getX()][this.datosSensores.getGPS().getY() - 1]++; 
                        return "moveN";
                    }else{
                        this.mapa[this.datosSensores.getGPS().getX() + 1][this.datosSensores.getGPS().getY() - 1]++; 
                        return "moveNE";
                    }                        
                case 2: 
                    if(j==1){
                        this.mapa[this.datosSensores.getGPS().getX() - 1][this.datosSensores.getGPS().getY()]++; 
                        return "moveW";
                    }else{
                        this.mapa[this.datosSensores.getGPS().getX() + 1][this.datosSensores.getGPS().getY() - 1]++; 
                        return "moveE";
                    }
                case 3: 
                    if(j==1){
                        this.mapa[this.datosSensores.getGPS().getX() - 1][this.datosSensores.getGPS().getY() + 1]++; 
                        return "moveSW";
                    }else if (j==2){
                        this.mapa[this.datosSensores.getGPS().getX()][this.datosSensores.getGPS().getY() + 1]++; 
                        return "moveS";
                    }else{
                        this.mapa[this.datosSensores.getGPS().getX()][this.datosSensores.getGPS().getY() + 1]++; 
                        return "moveSE";
                    } 
            }
            
            return "";
    }
    
    
}
