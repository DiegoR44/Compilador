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
public class Analizador_sintactico extends Analizador_Semantico {

    Nodos p;

    boolean errorEncontrado = false;
    //String nombre_ID;

    String lexemaAux;

    int tipo;

    String Id_Algoritmo;

    String erroresSintacticos[][] = {
        /*0*/{"Simbolo no valido", "500"},
        /*1*/ {"Se espera cierre de comentario", "501"},
        /*2*/ {"Se espera un digito despues del punto", "502"},
        /*3*/ {"Se espera cierre de cadena", "503"},
        /*4*/ {"Debe empezar con ALGORITMO", "504"},
        /*5*/ {"Se espera el nombre del algoritmo", "505"},
        /*6*/ {"Se espera inicio del parentesis", "506"},
        /*7*/ {"Se espera final del parentesis", "507"},
        /*8*/ {"Se espera la palabra INICIO", "508"},
        /*9*/ {"Se espera la palabra FIN", "509"},
        /*10*/ {"Se espera la palabra ES", "510"},
        /*11*/ {"Se espera un FIN_SI", "511"},
        /*12*/ {"Se espera un operador relacional", "512"},
        /*13*/ {"Se espera la palabra ENTONCES", "513"},
        /*14*/ {"Se espera nombre tipo simple", "514"},
        /*15*/ {"Se espera declaracion de variable", "515"},
        /*16*/ {"Se espera FIN_MIENTRAS", "516"},
        {"Se esperaba asignacion", "600"},
      {"Se esperaba algo que Escribir", "601"},
    };

    String erroresSemanticos[][] = {
        /*17*/{"El nombre de la variable es igual al ID", "517"},
        /*18*/ {"El nombre de la variable es repetida", "518"},
        /*19*/ {"La variable no esta asignada", "519"},
        /*20*/ {"Incompatibilidad de datos", "520"}

    };

    private void imprimirMensajeError_semantico(int num_error) {

        for (String[] errore : erroresSemanticos) {
            if (num_error == Integer.valueOf(errore[1])) {
                System.out.println(ANSI_RED + " " + "ERROR: " + errore[0] + " (" + p.lexema + ") " + "/ Nº. ERROR: " + num_error + ":  " + "EN LA LINEA: " + " " + p.linea + ANSI_RESET);
            }
        }
        errorEncontrado = true;

    }

    private void imprimirMensajeError(int num_error) {

        for (String[] errore : erroresSintacticos) {
            if (num_error == Integer.valueOf(errore[1])) {
                System.out.println(ANSI_RED + " " + "ERROR: " + errore[0] + "/ Nº. ERROR: " + num_error + " " + "EN LA LINEA: " + " " + p.linea + ANSI_RESET);
            }
        }
        errorEncontrado = true;

    }

    Analizador_sintactico(Nodos cabeza) {
        p = cabeza;
        try {
            while (p != null) {
                if (p.token == 212) {
                    p = p.sig;
                    if (p.token == 100) { //id
                        Id_Algoritmo = p.lexema;
                        p = p.sig;
                        if (p.token == 114) {
                            p = p.sig;
                            if (p.token == 115) {
                                p = p.sig;
                                if (p.token == 217) {//ES
                                    p = p.sig;
                                    Dec_variable();
                                    if (p.token == 213) { //INICIO
                                        p = p.sig;
                                        imprimirLista_Variables();
                                        Accion();

                                        if (p.token == 214) { //FIN
                                            break;
                                        } else {
                                            imprimirMensajeError(509);
                                            break;
                                        }
                                    } else {
                                        imprimirMensajeError(508);
                                        break;
                                    }
                                } else {
                                    imprimirMensajeError(510);
                                    break;
                                }
                            } else {
                                imprimirMensajeError(507);
                                break;
                            }
                        } else {
                            imprimirMensajeError(506);
                            break;
                        }
                    } else {
                        imprimirMensajeError(505);
                        break;
                    }
                } else {
                    imprimirMensajeError(504);
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Fin de archivo inesperado");
            errorEncontrado = true;
        }
    }
    private void Accion() {
        switch (p.token) {
            case 100:
                Validar_variable_noDeclarada();

                p = p.sig;
                Asignacion();
                break;
            case 215:
                p = p.sig;
                Leer();
                if (p.token == 118) {
                    p = p.sig;
                    Accion();
                }
                break;
            case 216:
                p = p.sig;
                Escribir();
                if (p.token == 118) {
                    p = p.sig;
                    Accion();
                }
                break;
            case 205:
                Si();
                if (p.token == 118) {
                    p = p.sig;
                    Accion();
                }
                break;
            case 209:
                p = p.sig;
                Mientras();
                if (p.token == 118) {
                    p = p.sig;

                    Accion();
                }
            default:
                break;
        }
    }

    private void Asignacion() {
        if (p.token == 119) {//:=
            p = p.sig;

            exprecion_numerica();

            if (p.token == 118) {//;
                p = p.sig;
                Accion();
            }

        } else {
            imprimirMensajeError(600);

        }

    }

    private void Mientras() {

        Exp_logica();

        if (p.token == 210) {
            p = p.sig;

            Accion();

            if (p.token == 211) {

                p = p.sig;
            } else {
                imprimirMensajeError(516);
            }
        }
    }

    private void Si() {

        p = p.sig;

        Exp_logica();

        if (p.token == 206) { //ENTONCES
            p = p.sig;
            Accion();

            if (p.token == 208) { //SINO
                p = p.sig;

                Accion();
            }
            if (p.token == 207) { //FIN_SI

                p = p.sig;
            } else {
                imprimirMensajeError(511);
            }
        } else {
            imprimirMensajeError(513);
        }
    }

    private void Exp_logica() {
        switch (p.token) {
            case 114://(
                p = p.sig;
                Exp_logica();
                if (p.token == 115) {//)
                    p = p.sig;
                    Exp_logica_1();
                }
                break;
            case 200://NOT

                p = p.sig;
                Exp_logica();
                Exp_logica_1();
                break;
            case 100://identificador
                if ((p.sig.token == 108) || (p.sig.token == 109) || (p.sig.token == 110)
                        || (p.sig.token == 111) || (p.sig.token == 112) || (p.sig.token == 113)) {//relacionales
                    exprecion_relacional();
                } else {
                    Exp_logica_1();
                }
                break;
            case 203://true

                Exp_logica_1();
                break;
            case 204://false

                Exp_logica_1();
                break;
            default:
                exprecion_relacional();
                Exp_logica_1();
                break;
        }
    }

    private void exprecion_relacional() {
        exprecion_numerica();
        if (p.token >= 108 && p.token <= 113) {//relacionales

            p = p.sig;
            exprecion_numerica();
        } else {
            imprimirMensajeError(512);
        }
    }

    private void Exp_logica_1() {
        if (p.token >= 201 && p.token <= 202) {//AND, OR

            p = p.sig;

            Exp_logica();
            Exp_logica_1();
        }
    }

    private void Escribir() {
        if (p.token == 100) {

            p = p.sig;
            if (p.token == 116) {
                p = p.sig;

                Escribir();
            }
        }else{
            imprimirMensajeError(601);
        
        }
    }

    private void Leer() {
        if (p.token == 100) {

            p = p.sig;
            if (p.token == 116) {
                p = p.sig;

                Leer();
            }
        }
    }

    private void exprecion_numerica() {
        switch (p.token) {
            case 114:

                exprecion_numerica();
                if (p.token == 115) {

                    exprecion_numerica1();
                }
                break;
            case 105://-

                exprecion_numerica();
                exprecion_numerica1();
                break;
            case 100:
                Validar_variable_noDeclarada();

                p = p.sig;
                exprecion_numerica1();

                break;
            case 203://TRUE

                p = p.sig;
                Exp_logica_1();
                break;
            case 204://FALSE

                p = p.sig;
                Exp_logica_1();
                break;
            case 103:

                p = p.sig;
                exprecion_numerica1();
                break;
            case 102:

                p = p.sig;
                exprecion_numerica1();
                break;
            case 101:

                p = p.sig;
                exprecion_numerica1();
                break;
            default:
                break;
        }
    }

    private void exprecion_numerica1() {
        if (p.token >= 104 && p.token <= 107) {

            p = p.sig;
            exprecion_numerica();
            exprecion_numerica1();
        }
    }

    private void Dec_variable() {
        if (p.token == 100) {
            if (p.lexema.equals(Id_Algoritmo)) {
                imprimirMensajeError_semantico(517);
            } else {
                Validar_variableRepetida();
                lexemaAux = p.lexema;
            }
            p = p.sig;
            if (p.token == 117) {//:
                p = p.sig;
                Nombre_tipo_simple();
                insertarnodos_Variables(lexemaAux, tipo);

                if (p.token == 118) {//;
                    p = p.sig;

                    Dec_variable();
                }
            } else {
                imprimirMensajeError(515);
            }
        } else {
            imprimirMensajeError(515);
        }
    }

    private void Nombre_tipo_simple() {
        switch (p.token) {

            case 218:
                p.token = 101;
                tipo = p.token;

                p = p.sig;

                break;
            case 219:

                p.token = 102;
                tipo = p.token;

                p = p.sig;
                break;
            case 220:
                p.token = 103;
                tipo = p.token;
                p = p.sig;
                break;
            case 221:

                tipo = p.token;

                p = p.sig;
                break;
            default:
                imprimirMensajeError(514);
                break;
        }
    }

    public void Validar_variableRepetida() {
        Nodos = cabeza_variable;
        while (Nodos != null) {
            if (p.lexema.equals(Nodos.lexemas_variables)) {
                imprimirMensajeError_semantico(518);
            }
            Nodos = Nodos.sig;
        }
        Nodos = cabeza_variable;

    }

    public void Validar_variable_noDeclarada() {
        Nodos = cabeza_variable;
        boolean validacionVariable = false;

        while (Nodos != null) {
            if (p.lexema.equals(Nodos.lexemas_variables)) {
                validacionVariable = true;
                p.token = Nodos.tipos;
                break;
            }
            Nodos = Nodos.sig;

        }
        if (validacionVariable == false) {
            imprimirMensajeError_semantico(519);
        }

    }

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

}
