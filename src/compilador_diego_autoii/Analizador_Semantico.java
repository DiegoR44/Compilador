/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador_diego_autoii;
import java.util.Stack;
/**
 *
 * @author DIEKO
 */



import java.util.Stack;

public class Analizador_Semantico{
    Stack<Integer> Inicial= new Stack<Integer>();
    Stack<Integer> Invertida= new Stack<Integer>();
    Stack<Integer> Operadores= new Stack<Integer>();
    Stack<Integer> Salidas= new Stack<Integer>();
    Stack<Integer> AuxiliarS= new Stack<Integer>();
    
    
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
    
    public void  Push_pilaEntrada(int xtoken){
            Inicial.push(xtoken);
    }
      public void  get_Pop_pilaEntrada(){
            Inicial.pop();
    }
    
    public void  Push_pilaInvertida(int xtoken){
            Invertida.push(xtoken);
    }
      public void  get_Pop_pilaInvertida(){
            Invertida.pop();
    }
      public void  Push_pilaOperadores(int xtoken){
            Operadores.push(xtoken);
    }
   public void  get_Pop_pilaOperadores(){
            Operadores.pop();
    }
      public void  Peek_Operadores(){
            Operadores.peek();
    }
   
   
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
   
}
