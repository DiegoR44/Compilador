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
public class Nodos_Variables {
    
        String lexemas_variables;

     int tipos;
     Nodos_Variables sig = null;
     
     Nodos_Variables(String xlexema, int xtipo){
         
         this.lexemas_variables = xlexema;
         this.tipos = xtipo;
        
     }
    
}
