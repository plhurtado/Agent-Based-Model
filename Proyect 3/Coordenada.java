/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica3;

/**
 * Clase Coordenada
 * @author Isidora Reina Molina 
 */
public class Coordenada {
        
    private int x, y;
    
    Coordenada(int x, int y){
        this.x = x;
        this.y = y;
    }

/*
 * Métodos getter y setter devuelve o almacena los datos privados de la clase Coordenada
 * que son dos numeros enteros: X e Y.
 * @author Isidora Reina Molina 
 * @version 1.0
*/
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
    

