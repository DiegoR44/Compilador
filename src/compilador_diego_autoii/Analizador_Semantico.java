/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador_diego_autoii;
import java.util.ArrayList;
import java.util.Stack;
/**
 *
 * @author DIEKO
 */



import java.util.Stack;

public class Analizador_Semantico{
  
    Nodos p;
    Stack<Integer> Inicial= new Stack<Integer>();
    Stack<Integer> Invertida= new Stack<Integer>();
    Stack<Integer> Operadores= new Stack<Integer>();
    Stack<Integer> Salidas= new Stack<Integer>();
    Stack<Integer> AuxSalida= new Stack<Integer>();

     Stack<Integer> ETokenI= new Stack<Integer>();//tokeninvertidos
    Stack<Integer> EToken= new Stack<Integer>();//tokens
    Stack<Integer> ETokenO= new Stack<Integer>();//tokenoperandos

    Stack<String> ELexemasI= new Stack<String>();//
    Stack<String> ELexemas= new Stack<String>();
    Stack<String> ELexemasO= new Stack<String>();
     
    ListaObjPolish cabezaPol= null,pPol,rPol;
    
    Nodos_Variables cabeza_variable = null, p_variable, Nodos; 
    //lista de variables declaradas
    
    public void insertarnodos_Variables(String xlexemas, int xtipo_token) {
      
        Nodos_Variables nodo_variable = new Nodos_Variables(xlexemas,xtipo_token);

        if (cabeza_variable == null) {
            cabeza_variable = nodo_variable;
            p_variable = cabeza_variable;
            Nodos = cabeza_variable;
            
        }else {
            p_variable.sig = nodo_variable;
            p_variable = p_variable.sig;
        }
    }

    
    public void imprimirLista_Variables() {
        
        p_variable = cabeza_variable;

        System.out.println("LISTA DE VARIABLES:" );
        while (p_variable != null) {
            if(p_variable.lexemas_variables==null){
                    
           System.out.println("[VARIABLE: sin datos " + " TIPO:"+ p_variable.tipos + "]");
            
            }else{
            System.out.println("[VARIABLE: " + p_variable.lexemas_variables + " |" + " TIPO:"+ p_variable.tipos + "]");
            
            }
                
                
            p_variable = p_variable.sig;
        }
    }
  public static int Jerarquias(int op) {
        int prf = 9;
        if ((op == 106) || (op == 107)) {
            prf = 8;  //--> */
        }
        if ((op == 104) || (op == 105)) {
            prf = 7;//--> +-
        }
        if ((op == 108) || (op == 109) || (op == 110) || (op == 111) || (op == 112) || (op == 113)) {
            prf = 6;// relacionales
        }
        if ((op == 200)) {
            prf = 5;//NOT
        }
        if ((op == 201) || (op == 202)) {
            prf = 4;//AND OR
        }
        if ((op == 115)) {
            prf = 3;//)
        }
        if ((op == 114)) {
            prf = 2;//(
        }
        if ((op == 119) || (op == 215) || (op == 216)){prf = 1;} 
        return prf;
    }
 
    public void  Push_pilaInicial(int xtoken){
            Inicial.push(xtoken);
    }
    
  public void  Push_pilaSalidas(int xtoken){
            Salidas.push(xtoken);
    }
  
    
    public void  Push_pilaInvertida(int xtoken){
            Invertida.push(xtoken);
    }
     
      public void  Push_pilaOperadores(int xtoken){
            Operadores.push(xtoken);
    }
      
    public  void insertarNodoPol(String xlexema, Integer xtoken) {
           ListaObjPolish NodoPol = new ListaObjPolish(xlexema, xtoken);
        
        if (cabezaPol == null) {
            cabezaPol = NodoPol;
            pPol = cabezaPol;
        } else {
            pPol.sig = NodoPol;
            pPol = NodoPol;
        }    
    }
    public void imprimirNodospol() {
       rPol=cabezaPol;
        while (rPol != null){

                System.out.println("[ Lexema : " + rPol.lexema_polish + "| Token: " + rPol.token+"]");
           
          rPol= rPol.sig;
        }
    }
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
   
}
