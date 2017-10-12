/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica3;

/**
 * Clase Heuristica Dron
 * @author Carmen Bueno Ben Boubker y Martin Zumarraga Uribe
 * @version 1.0
 */    

public class HeuristicaDron extends Heuristica{

      private boolean subir= false;
      private boolean drcha= true;
      private boolean vertical= false;
      private boolean primera_vez= true;
      private int filas= 0;
      
      
      public HeuristicaDron(int vision, int consumo) {
        super(vision, consumo);
    }
/**
 * Método sobreescrito siguiente Acción. El dron realizará refel si su bateria es inferior a 4. En otro caso la acción a realizar
 * será de movimiento. El dron tiene dos heuristicas a aplicar, si es el primer dron (Buscador) realizará un barrido del mapa hasta encontrar
 * el objetivo. Sin embargo, si ya se ha encontrado la meta previamente, el dron aplicará la heurística del algoritmo greedy por pesos
 * para llegar por el camino más corto a la meta.
 * @author Carmen Bueno Ben Boubker y Martin Zumarraga Uribe
 * @version 1.0
 */    
    @Override
    public String siguienteAccion() {
              
        if(super.datosSensores.getBattery() < 4){
            System.out.println("CONTROLLER: REFUEL");
            return "refuel";
        }
        
        if(super.meta[0].getX() == -1){
            for (int i = 0; i < this.datosSensores.getRadar().length; i++) {
               for (int j = 0; j < this.datosSensores.getRadar().length; j++) {
                   if(super.datosSensores.getRadar()[i][j] == 3){
                       //Marcamos el objetivo
                        return this.movimiento(i, j);
                   }
               }
            }

            if (primera_vez){
                primera_vez= false;
                if(super.datosSensores.getRadar()[0][1] == 2){
                    subir= false;
                    return this.movimiento(2,1);
                }
                else{
                    subir= true;
                    return this.movimiento(0,1);
                }       
            }
            else{
                if(vertical && filas < 5){
                    if(subir){
                        filas++;
                        return this.movimiento(0, 1);   //Subir un paso
                    }
                    else{
                        filas++;
                        return this.movimiento(2, 1);   //Bajar un paso
                    }
                }
                else if(filas == 5){
                    filas= 0;
                    vertical= false;
                    drcha= !drcha;
                    if(drcha){
                        return this.movimiento(1, 2);
                    }else{
                        return this.movimiento(1, 0);
                    }        
                }
                else{
                    if(drcha){
                        if(super.datosSensores.getRadar()[1][2] == 2){
                            vertical= true;
                            if(subir){
                                filas++;
                                return this.movimiento(0, 1);   //Subir un paso
                            }
                            else{
                                filas++;
                                return this.movimiento(2, 1);   //Bajar un paso
                            }
                        }
                        else{
                            return this.movimiento(1, 2);
                        }
                    }
                    else{   //vamos a la izq
                        if(super.datosSensores.getRadar()[1][0]== 2){
                            System.out.println("activo vertical");
                            vertical= true;
                            if(subir){
                                filas++;
                                return this.movimiento(0, 1);   //Subir un paso
                            }
                            else{
                                filas++;
                                return this.movimiento(2, 1);   //Bajar un paso
                            }
                        }
                        else{
                            return this.movimiento(1, 0);
                        }
                    }
                }

            }
            
       }else{
            
            double mejorOpcion = 1000000.0;
            int mejorI = 0;
            int mejorJ = 0;
            int borde = ((super.datosSensores.getRadar().length-1)/2)-1;
            int mitad = (super.datosSensores.getRadar().length-1)/2;

            for (int i=borde; i<mitad+2;i++){
                    for(int j=borde; j<mitad+2;j++){
                        if(!(i==mitad && j==mitad)){
                            if(super.datosSensores.getRadar()[i][j]==3 && sePuedeAparcar(i, j)){
                                return this.movimiento(i,j);
                            }else if (super.datosSensores.getRadar()[i][j]==0 || super.datosSensores.getRadar()[i][j]==1){
                                if(this.obtenerMejorCasilla(i,j)<mejorOpcion){
                                    mejorOpcion = this.obtenerMejorCasilla(i,j);
                                    mejorI = i;
                                    mejorJ = j;
                                } 
                            }
                        }
                    }
            }

            return this.movimiento(mejorI,mejorJ);
        }
    }


 /**
 * Metodo obtenerMejorCasilla recibe como parámetro una posición, dependiendo de cual sea ésta, realizará un calculo
 * para darle un valor ('peso') a dicha posición.
 * @author Carmen Bueno Ben Boubker y Martin Zumarraga Uribe
 * @version 1.0
 */   
    @Override
    public double obtenerMejorCasilla(int i, int j) {
        if (i == 0 && j == 0){
            return this.distancia(this.goal.getX(), this.goal.getY(), this.datosSensores.getGPS().getX() - 1, this.datosSensores.getGPS().getY() - 1);
        }
        else if (i == 0 && j == 1){
            return this.distancia(this.goal.getX(), this.goal.getY(), this.datosSensores.getGPS().getX(), this.datosSensores.getGPS().getY() - 1);
        }
        else if (i == 0 && j == 2){
            return this.distancia(this.goal.getX(), this.goal.getY(), this.datosSensores.getGPS().getX() + 1, this.datosSensores.getGPS().getY() - 1);
        }
        else if (i == 1 && j == 0){
            return this.distancia(this.goal.getX(), this.goal.getY(), this.datosSensores.getGPS().getX() - 1, this.datosSensores.getGPS().getY());
        }
        else if (i == 1 && j == 2){
            return this.distancia(this.goal.getX(), this.goal.getY(), this.datosSensores.getGPS().getX() + 1, this.datosSensores.getGPS().getY());
        }
        else if (i == 2 && j == 0){
            return this.distancia(this.goal.getX(), this.goal.getY(), this.datosSensores.getGPS().getX() - 1, this.datosSensores.getGPS().getY() + 1);
        }
        else if (i == 2 && j == 1){
            return this.distancia(this.goal.getX(), this.goal.getY(), this.datosSensores.getGPS().getX(), this.datosSensores.getGPS().getY() + 1);
        }
        else{
            return this.distancia(this.goal.getX(), this.goal.getY(), this.datosSensores.getGPS().getX() + 1, this.datosSensores.getGPS().getY() + 1);
        }
    }
/**
 * Metodo movimiento recibe una posición y dependiendo de cual sea esta, corresponderá a un movimiento.
 * @author Carmen Bueno Ben Boubker y Martin Zumarraga Uribe
 * @version 1.0
 */   
    @Override
    public String movimiento(int i, int j) {
      
            switch(i){
                case 0: 
                    if(j==0){
                        return "moveNW";
                    }else if (j==1){
                        return "moveN";
                    }else{
                        return "moveNE";
                    }                        
                case 1: 
                    if(j==0){
                        return "moveW";
                    }else{
                        return "moveE";
                    }
                case 2: 
                    if(j==0){
                        return "moveSW";
                    }else if (j==1){
                        return "moveS";
                    }else{
                        return "moveSE";
                    } 
            }
            
            return "";
    }
    
}
