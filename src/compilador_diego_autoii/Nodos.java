/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador_diego_autoii;

/**
 *
 * @author DIEKO
 */
public class Nodos {
   String lexema; 
    int token; 
    int linea; 
    Nodos sig = null;

    Nodos(String lexema, int token, int linea){
    this.lexema = lexema; 
    this.token = token; 
    this.linea = linea;
    };
}
