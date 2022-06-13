/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador_diego_autoii;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author DIEKO
 */
public class Analizador_sintactico extends Analizador_Semantico {

    Nodos p;
    public  ArrayList<String> Vars = new ArrayList();
  
    String lexemaError;
    int EtiquetaIf;
    int EtiquetaWhile;
    boolean errorEncontrado = false;
    //String nombre_ID;
    String op1;
    String op2;
    String ASMText="";
      String Operador="";
    String resultadoASM;
  
    int contador = 0;
    Stack<Integer> If = new Stack<Integer>();
    Stack<Integer> While = new Stack<Integer>();
    String lexemaAux;
    Stack<String> SearchPolishh = new <String> Stack();
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
        {"Se esperaba algo que Escribir", "601"},};

    String erroresSemanticos[][] = {
        /*17*/{"No puedes declarar una variable con el nombre del algoritmo", "517"},
        /*18*/ {"Variable repetida", "518"},
        /*19*/ {"Variable no asignada", "519"},
        /*20*/ {"Incompatabilidad de tipos", "520"}

    };
    SistemaDe_Tipos Tipos = new SistemaDe_Tipos();

    private void imprimirMensajeError_semantico(int num_error) {

        for (String[] errore : erroresSemanticos) {
            if (num_error == Integer.valueOf(errore[1])) {
                System.out.println(ANSI_RED + " " + "ERROR: " + errore[0] + " (" + lexemaError + ") " + "/ Nº. ERROR: " + num_error + ":  " + "EN LA LINEA: " + " " + p.linea + ANSI_RESET);
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
                                        imprimirNodospol();
                                        CodigoObjetoANDarchivoASM();

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
                Push_pilaInicial(p.token);
                EToken.push(p.token);
                ELexemas.push(p.lexema);
                p = p.sig;
                Asignacion();
                break;
            case 215:
                EToken.push(p.token);
                ELexemas.push(p.lexema);
                p = p.sig;
                Leer();
                Codigo_IntermedioPolish();
                if (p.token == 118) {
                    p = p.sig;
                    Accion();
                }
                break;
            case 216:
                EToken.push(p.token);
                ELexemas.push(p.lexema);

                p = p.sig;
                Escribir();
                Codigo_IntermedioPolish();
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
            Push_pilaInicial(p.token);
            EToken.push(p.token);
            ELexemas.push(p.lexema);
            p = p.sig;
            exprecion_numerica();
            Evaluar_Infijo_Post();
            Codigo_IntermedioPolish();
            if (p.token == 118) {//;
                p = p.sig;
                Accion();
            }

        } else {
            imprimirMensajeError(600);

        }

    }

    private void Mientras() {
        EtiquetaWhile++;
        While.push(EtiquetaWhile);
        Exp_logica();
        Evaluar_Infijo_Post();
        insertarNodoPol("D" + (While.peek()), 0);

        Codigo_IntermedioPolish();
        insertarNodoPol("Brf C" + (While.peek()), 0);

        if (p.token == 210) {
            p = p.sig;

            Accion();
            insertarNodoPol("Bri D" + (While.peek()), 0);

            if (p.token == 211) {
                insertarNodoPol("C" + (While.peek()), 0);

                While.pop();
                p = p.sig;
            } else {
                imprimirMensajeError(516);
            }
        }
    }

    private void Si() {
        EtiquetaIf++;
        If.push(EtiquetaIf);
        p = p.sig;
        lexemaError = p.lexema;
        Exp_logica();
        Evaluar_Infijo_Post();
        Codigo_IntermedioPolish();
        insertarNodoPol("Brf A" + (If.peek()), 0);

        if (p.token == 206) { //ENTONCES
            p = p.sig;
            Accion();
            insertarNodoPol("Bri B" + (If.peek()), 0);

            if (p.token == 208) { //SINO
                p = p.sig;
                insertarNodoPol("A" + (If.peek()), 0);

                Accion();
            }
            if (p.token == 207) { //FIN_SI
                insertarNodoPol("B" + (If.peek()), 0);

                If.pop();
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

                Push_pilaInicial(p.token);
                EToken.push(p.token);
                ELexemas.push(p.lexema);
                p = p.sig;
                Exp_logica();
                Exp_logica_1();
                break;
            case 100://identificador
                lexemaError = p.lexema;
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
            Push_pilaInicial(p.token);
            EToken.push(p.token);
            ELexemas.push(p.lexema);
            p = p.sig;
            exprecion_numerica();
        } else {
            imprimirMensajeError(512);
        }

    }

    private void Exp_logica_1() {
        exprecion_numerica();
        if (p.token >= 201 && p.token <= 202) {//AND, OR
            Push_pilaInicial(p.token);
            EToken.push(p.token);
            ELexemas.push(p.lexema);

            p = p.sig;

            Exp_logica();
            Exp_logica_1();
        }
    }

    private void Escribir() {
        if (p.token == 100) {
            EToken.push(p.token);
            ELexemas.push(p.lexema);
            p = p.sig;
            if (p.token == 116) {
                p = p.sig;

                Escribir();
            }
        } else {
            imprimirMensajeError(601);

        }
    }

    private void Leer() {
        if (p.token == 100) {
            EToken.push(p.token);
            ELexemas.push(p.lexema);
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
                Push_pilaInicial(p.token);
                EToken.push(p.token);
                ELexemas.push(p.lexema);

                exprecion_numerica();
                if (p.token == 115) {
                    Push_pilaInicial(p.token);
                    EToken.push(p.token);
                    ELexemas.push(p.lexema);

                    exprecion_numerica1();
                }
                break;
            case 105://-
                Push_pilaInicial(p.token);

                EToken.push(p.token);
                ELexemas.push(p.lexema);
                exprecion_numerica();
                exprecion_numerica1();
                break;
            case 100:

                Validar_variable_noDeclarada();
                Push_pilaInicial(p.token);
                EToken.push(p.token);
                ELexemas.push(p.lexema);
                p = p.sig;
                exprecion_numerica1();

                break;
            case 203://TRUE
                Push_pilaInicial(221);
                EToken.push(221);
                ELexemas.push(p.lexema);
                p = p.sig;
                Exp_logica_1();
                break;
            case 204://FALSE
                Push_pilaInicial(221);
                EToken.push(221);
                ELexemas.push(p.lexema);
                p = p.sig;
                Exp_logica_1();
                break;
            case 103:
                Push_pilaInicial(p.token);
                EToken.push(p.token);
                ELexemas.push(p.lexema);
                p = p.sig;
                exprecion_numerica1();
                break;
            case 102:
                Push_pilaInicial(p.token);
                EToken.push(p.token);
                ELexemas.push(p.lexema);
                p = p.sig;
                exprecion_numerica1();
                break;
            case 101:
                Push_pilaInicial(p.token);
                EToken.push(p.token);
                ELexemas.push(p.lexema);
                p = p.sig;
                exprecion_numerica1();
                break;
            default:
                break;
        }
    }

    private void exprecion_numerica1() {
        if (p.token >= 104 && p.token <= 107) {
            Push_pilaInicial(p.token);
            EToken.push(p.token);
            ELexemas.push(p.lexema);
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
                    Dec_variable();;
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

    private void Evaluar_Infijo_Post() {
        Invertida.push(115);

        while (!Inicial.empty()) {
            Push_pilaInvertida(Inicial.pop());
        }
        Push_pilaInvertida(114);

        while (!Invertida.empty()) {
            switch (Jerarquias(Invertida.peek())) {
                case 1: //:=
                    Push_pilaOperadores(Invertida.pop());

                    break;
                case 2: //(
                    Push_pilaOperadores(Invertida.pop());
                    break;
                case 3: //)
                    while (!Operadores.peek().equals(114)) {
                        Push_pilaSalidas(Operadores.pop());

                    }
                    Operadores.pop();

                    Invertida.pop();
                    break;
                case 4: //AND OR

                case 5://NOT

                case 6: //Relacionales

                case 7: //+-

                case 8: //*/
                    while (Jerarquias(Operadores.peek()) >= Jerarquias(Invertida.peek())) {
                        Push_pilaSalidas(Operadores.pop());

                    }
                    Push_pilaOperadores(Invertida.pop());
                    break;
                case 9: //token
                    Push_pilaSalidas(Invertida.pop());

                    break;
            }
        }
        Validar_SistemaTipos();
        Salidas.removeAllElements();

    }

    public void Validar_SistemaTipos() {
        boolean error = false;
        int columna = 0;
        int fila = 0;
        int operando1 = 0;
        int operando2 = 0;

        for (int i = 0; i < Salidas.size(); i++) {
            int op = Salidas.get(i);
            if ((op == 104) || (op == 105) || (op == 106) || (op == 107) || (op == 108)
                    || (op == 109) || (op == 110) || (op == 111) || (op == 112) || (op == 113) || (op == 119)
                    || //+, -, *, /, :=, ,< ,>, <=, >=, =, <>, and, or, not
                    (op == 200) || (op == 201) || (op == 202)) {
                if (op == 200) {
                    operando1 = AuxSalida.peek();
                } else {
                    operando1 = AuxSalida.pop();
                    operando2 = AuxSalida.pop();
                }
                switch (operando1) {
                    case 101:
                        columna = 0;
                        break;
                    case 102:
                        columna = 1;
                        break;
                    case 103:
                        columna = 2;
                        break;
                    case 221:
                        columna = 3;
                        break;
                }

                switch (operando2) {
                    case 101:
                        fila = 0;
                        break;
                    case 102:
                        fila = 1;
                        break;
                    case 103:
                        fila = 2;
                        break;
                    case 221:
                        fila = 3;
                        break;
                }
                switch (op) {
                    case 104:
                        if (Tipos.Sumas[fila][columna] == 0) {
                            error = true;
                        } else {
                            AuxSalida.push(Tipos.Sumas[fila][columna]);
                        }
                        break;
                    case 105:
                        if (Tipos.Resta_Multiplicacion[fila][columna] == 0) {
                            error = true;
                        } else {
                            AuxSalida.push(Tipos.Resta_Multiplicacion[fila][columna]);
                        }
                        break;
                    case 106:
                        if (Tipos.Resta_Multiplicacion[fila][columna] == 0) {
                            error = true;
                        } else {
                            AuxSalida.push(Tipos.Resta_Multiplicacion[fila][columna]);
                        }
                        break;
                    case 107:
                        if (Tipos.Divisiones[fila][columna] == 0) {
                            error = true;
                        } else {
                            AuxSalida.push(Tipos.Divisiones[fila][columna]);
                        }
                        break;
                    case 119:
                        if (Tipos.Asingaciones[fila][columna] == false) {
                            error = true;
                        }

                        break;
                    case 110:
                    case 108:
                    case 109:
                    case 111:
                        if (Tipos.Relacionales[fila][columna] == 0) {
                            error = true;
                        } else {
                            AuxSalida.push(Tipos.Relacionales[fila][columna]);
                        }
                        break;
                    case 112:
                    case 113:
                        if (Tipos.Igual_Diferente[fila][columna] == 0) {
                            error = true;
                        } else {
                            AuxSalida.push(Tipos.Igual_Diferente[fila][columna]);
                        }
                        break;
                    case 201://AND OR
                    case 202:
                        if (Tipos.Logicos[fila][columna] == false) {
                            error = true;
                        } else {
                            AuxSalida.push(221);
                        }
                        break;

                    case 200:
                        if (operando1 != 221) {
                            error = true;
                        }
                        break;
                }
            } else {
                AuxSalida.push(op);
            }
            if (error == true) {
                imprimirMensajeError_semantico(520);
                break;
            }

        }

    }

    public void Codigo_IntermedioPolish() {
        ETokenI.push(115);
        ELexemasI.push(")");
        while (!EToken.empty()) {
            ETokenI.push(EToken.pop());
            ELexemasI.push(ELexemas.pop());
        }
        ETokenI.push(114);
        ELexemasI.push("(");

        while (!ETokenI.empty()) {

            switch (Jerarquias(ETokenI.peek())) {
                case 1: //:=
                    ETokenO.push(ETokenI.pop());
                    ELexemasO.push(ELexemasI.pop());

                    break;
                case 2: //(
                    ETokenO.push(ETokenI.pop());
                    ELexemasO.push(ELexemasI.pop());
                    break;
                case 3: //)
                    while (!ETokenO.peek().equals(114)) {
                        insertarNodoPol(ELexemasO.pop(), ETokenO.pop());

                    }
                    ETokenO.pop();
                    ELexemasO.pop();
                    ETokenI.pop();
                    ELexemasI.pop();
                    break;
                case 4: //AND OR

                case 5://NOT

                case 6: //Relacionales

                case 7: //+-

                case 8: //*/
                    while (Jerarquias(ETokenO.peek()) >= Jerarquias(ETokenI.peek())) {
                        insertarNodoPol(ELexemasO.pop(), ETokenO.pop());
                    }
                    ETokenO.push(ETokenI.pop());
                    ELexemasO.push(ELexemasI.pop());

                    break;
                case 9: //token 
                    insertarNodoPol(ELexemasI.pop(), ETokenI.pop());

                    break;
            }
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

    public void CodigoObjetoANDarchivoASM() {
        
        String ruta = "C:\\masm614\\asm\\PROGRAMA.ASM";
           
         HeaderASM();
       
        p_variable = cabeza_variable;
  
        while (p_variable != null) {
            if (p_variable.tipos == 103) {
              
                ASMText += "\n\t\t\t"+p_variable.lexemas_variables +" db "+" ;/provisional, 13,10,'$' ";
               
                Vars.add(p_variable.lexemas_variables);
               
            } else {
                ASMText += "\n\t\t\t" + p_variable.lexemas_variables + " db"+" ?" ;
            }
            p_variable= p_variable.sig;
        }
        ASMText += "\n\t\t\timprimir db ?";
        ASMText += "\n\t\t\t;/Variables";

        ASMText += "\n.CODE\n"
                + ".STARTUP\n"
                + "\t\t\tMOV     AX,@DATA\n"
                + "\t\t\tMOV     DS,AX\n"
                + "\t\t\tCALL    COMPI\n"
                + "\t\t\tMOV AX,4C00H\n"
                + "\t\t\tINT 21H\n"
                + "COMPI PROC\n";;
                
        
       pPol= cabezaPol;
        
        while (pPol != null) {
            if (pPol.token >= 104 && pPol.token < 114 || pPol.token == 119
                    || pPol.token >= 200 && pPol.token <= 202 || pPol.token == 215 || pPol.token == 216 || pPol.token == 0) {
                
                Operador = pPol.lexema_polish;

                switch (Operador) {
                    case "+":
                        operacionesASM("SUMAR");
                        break;
                    case "-":
                        operacionesASM("RESTA");
                        break;
                    case "*":
                        operacionesASM("MULTI");
                        break;
                    case "/":
                        operacionesASM("DIVIDE");
                        break;
                    case ":=":
                        operacionAsignarASM("I_ASIGNAR");
                        break;
                    case "LEER":
                        operacionLeerASM("LEE");
                        break;
                    case "ESCRIBIR":
                        operacionEscribirASM("LEELN","WRITE");
                        break;
                    case "=":
                        operacionesASM("I_IGUAL");
                        break;
                    case ">":
                        operacionesASM("I_MAYOR");
                        break;
                    case "<":
                        operacionesASM("I_MENOR");
                        break;
                    case ">=":
                        operacionesASM("I_MAYORIGUAL");
                        break;
                    case "<=":
                        operacionesASM("I_MENORIGUAL");
                        break;
                    case "<>":
                        operacionesASM("I_DIFERENTES");
                        break;

                }

                SALTOSandETIQUETAS("JF", "JMP");
                
            } else {
                SearchPolishh.push(pPol.lexema_polish);
            }
            pPol = pPol.sig;
        }
        
      FooterASM();
        System.out.println("\n ASM....");
        System.out.println(ASMText);
        
        GeneracionArchivoASM(ruta);
    }

    
    public void GeneracionArchivoASM(String xruta){
        try{
            File file=new File(xruta);
            //si el archivo no existe lo crea
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fw=new FileWriter(file);
            BufferedWriter bw=new BufferedWriter(fw);
            bw.write(ASMText);
            bw.close();
            System.out.println("SE CREO EL ARCHIVO EN: "+xruta);
            
        }catch(Exception e){
            System.out.println("NO SE AH PODIDO CREAR EL ARCHIVO ASM");
        }
    }
    public void operacionesASM(String MacrosOperadores) {
        op2 = SearchPolishh.pop();
        op1 = SearchPolishh.pop();
        ASMText += "\t\t\t"+MacrosOperadores + " "+op1 + "," + op2 + ",Resultado"  + contador +"\n";

        resultadoASM =( "Resultado" + contador);
        SearchPolishh.push(resultadoASM);
        ASMText = Reemplazar(ASMText, ";/Variables", " Resultado" + contador + " db " + " ? " + "\n\t\t\t;/Variables");
    }

    public void operacionAsignarASM(String AsignarMacro) {
        op2 = SearchPolishh.pop();
        op1 = SearchPolishh.pop();
        boolean bandera = false;
        for (int i = 0; i < Vars.size(); i++) {
           
            if (Vars.get(i).equals(op1)) {
       
                ASMText = Reemplazar(ASMText, "" + op1 + ";/provisional ", "\n\t\t\t" + op1 + " db " + op2);
               
                
                bandera = true;
                break;
            }
        }
        if (bandera) {
            System.out.println("LLEGA CEDENA");
        } else {
            ASMText += "\t\t\t"+AsignarMacro + " " +op1  + "," + op2 + "\n";

        }
    }

    public void operacionLeerASM(String LeerMacro) {
        op1 = SearchPolishh.pop();
        boolean bandera = false;
        for (int i = 0; i < Vars.size(); i++) {
            if (Vars.get(i).equals(op1)) {

                bandera = true;
                break;
            }
        }
        if (bandera) {
            String STR = " " + op1 + ";/provisional";
            boolean containerSTR = ASMText.indexOf(STR) >= 0;
            if (containerSTR) {
                ASMText = Reemplazar(ASMText, STR, "\n\t\t\t" + STR);
                ASMText = Reemplazar(ASMText, ";/provisional", " db 0");

            }
        }
        ASMText += "\t\t\t" + LeerMacro + " " + op1 + "\n";

    }

    public void operacionEscribirASM(String LEELN, String WRITE) {
        op1 = SearchPolishh.pop();
        boolean bandera = false;
        for (int i = 0; i < Vars.size(); i++) {
            if (Vars.get(i).equals(op1)) {

                bandera = true;
                break;
            }
        }
        if (bandera) {
            ASMText += "\t\t\t" + LEELN + " " + op1 + "\n";
        } else {
            ASMText += "\t\t\t" + WRITE+ " " + op1 + "\n";
        }
    }

    public void SALTOSandETIQUETAS(String JF, String JMP) {
        if (Operador.startsWith("Brf")) {
            String p = Operador.substring(4, 6);
            int auxCont = contador;
            ASMText += JF +" Resultado" + (auxCont - 1) + "," + p + "\n";
        } else if (Operador.startsWith("Bri")) {
            String p = Operador.substring(4, 6);
            ASMText += JMP +" "+ p + "\n";
        } else if (Operador.startsWith("A") || Operador.startsWith("B")
                || Operador.startsWith("C") || Operador.startsWith("D")) {
            String p = Operador.substring(0, 2);
            ASMText += " " + p + ":\n";

        }
        contador++;
    }

    public static String Reemplazar(String cadena, String buscar, String reemplazar) {
        return cadena.replaceAll(buscar, reemplazar);
    }

    public void FooterASM(){
    
     ASMText+="\t\t\tret\n"+
                "COMPI  ENDP\n"+
                ".EXIT\n"+
                "END";
    }
    public void HeaderASM(){
     ASMText = "\n\nINCLUDE MACROS.MAC\n"
                + ".MODEL SMALL\n"
                + ".586\n"
                + ".STACK 100h\n"
                + ".DATA\n";
          
    }

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

}
