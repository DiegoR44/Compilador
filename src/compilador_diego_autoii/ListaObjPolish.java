/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compilador_diego_autoii;

/**
 *
 * @author Dieko
 */
public class ListaObjPolish {
 String lexema_polish;
    int token ;
     ListaObjPolish sig = null;
    public ListaObjPolish(String xlexema_polish, int xtoken) {
        this.lexema_polish=xlexema_polish;
        this.token=xtoken;
        
    }
    
}
