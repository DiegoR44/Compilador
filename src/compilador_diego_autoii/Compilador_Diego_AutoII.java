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
public class Compilador_Diego_AutoII {

    /**
     * @param args the command line arguments
     */
    Nodos p;

    public static void main(String[] args) {
        // TODO code application logic here
        try {
            //Ponemos a "Dormir" el programa durante los ms que queremos
           
                //Ponemos a "Dormir" el programa durante los ms que queremos
               
               System.out.println(" %COMPILANDO.......%");

          

            Thread.sleep(5 * 1000);
            Analizador_lexico Lexico = new Analizador_lexico();

            //Analizador_Semantico semantica = new Analizador_Semantico();
            Analizador_sintactico sintaxis = new Analizador_sintactico(Lexico.cabeza);

            if (!Lexico.errorEncontrado) {
                System.out.println("---------------------------");
                System.out.println("Analisis Lexico Terminado");

                if (!sintaxis.errorEncontrado) {
                    System.out.println("Analisis Sintactico Terminado");

                } else {

                    System.out.println(ANSI_RED + "Analisis Sintactico No Terminado" + ANSI_RESET);
                }
            } else {
                System.out.println(ANSI_RED + "Analisis Lexico No Terminado" + ANSI_RESET);
            }

            
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
}
