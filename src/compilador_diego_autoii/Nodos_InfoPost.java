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
public class Nodos_InfoPost {
     String lexema;
    int token;
    Nodos_InfoPost sig = null;
    
    Nodos_InfoPost(String lexemasInPost, int tiposInPost){
        this.lexema = lexemasInPost;
        this.token = tiposInPost;
    };
}


    

