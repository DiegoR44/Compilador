/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador_diego_autoii;

import java.io.RandomAccessFile;

/**
 *
 * @author DIEKO
 */
public class Analizador_lexico {

    Nodos cabeza = null, p;

    int estado = 0, columna, valorMT, numRenglon = 1;
    int caracter = 0;
    String lexema = "";
    boolean errorEncontrado = false;

    String archivo = "C:\\Users\\Dieko\\Desktop\\Respaldo\\Compilador_Diego_AutoII\\src\\compilador_diego_autoii\\compilador.txt";

    int matriz[][] = {
        //       l	    d	     _	 	  .     '	    +	      -	          *	     /	    >	     <	     =	     (	      )	      ,	     ;  	 :  	{ 	     }	    eb	    tab	    nl	    eof 	oc
        /*0*/{    1,       2,       500,         500,    5,        104,      105,       106,        107,    6,       7,     112,    114, 115, 116, 118, 8, 9, 500, 0, 0, 0, 0, 500
        },
        /*1*/ {   1,        1,        1, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100},
        /*2*/ {101, 2, 101, 3, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101
        },
        /*3*/ {502, 4, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502,},
        /*4*/ {102, 4, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102
        },
        /*5*/ {5, 5, 5, 5, 103, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 503, 5, 5},
        /*6*/ {109, 109, 109, 109, 109, 109, 109, 108, 108, 108, 108, 109, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108, 108},
        /*7*/ {110, 110, 110, 110, 110, 110, 110, 110, 110, 113, 110, 111, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},
        /*8*/ {117, 117, 117, 117, 117, 117, 117, 117, 117, 117, 117, 119, 117, 117, 117, 117, 117, 117, 117, 117, 117, 117, 117, 117},
        /*9*/ {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 0, 9, 9, 501, 9, 9},};

     
    String palReservadas[][] = {
        //     0       1
        /*0*/{"NOT", "200"},
        /*1*/ {"AND", "201"},
        /*2*/ {"OR", "202"},
        /*3*/ {"VERDADERO", "203"},
        /*4*/ {"FALSO", "204"},
        /*5*/ {"SI", "205"},
        /*6*/ {"ENTONCES", "206"},
        /*7*/ {"FIN_SI", "207"},
        /*8*/ {"SINO", "208"},
        /*9*/ {"MIENTRAS", "209"},
        /*10*/ {"HACER", "210"},
        /*11*/ {"FIN_MIENTRAS", "211"},
        /*12*/ {"ALGORITMO", "212"},
        /*13*/ {"INICIO", "213"},
        /*14*/ {"FIN", "214"},
        /*15*/ {"LEER", "215"},
        /*16*/ {"ESCRIBIR", "216"},
        /*17*/ {"ES", "217"},
        /*18*/ {"ENTERO", "218"},
        /*19*/ {"DECIMAL", "219"},
        /*20*/ {"CADENA", "220"},
        /*20*/ {"LOGICO", "221"},};

    
    
    
    String errores[][] = {
        //          0               1
        
        /*0*/{"Simbolo no valido", "500"},
        /*1*/ {"Se espera cierre de comentario", "501"},
        /*2*/ {"Se espera un digito", "502"},
        /*3*/ {"Se espera cierre de cadena", "503"},};

    RandomAccessFile file = null;

    public Analizador_lexico() {
        try {
            file = new RandomAccessFile(archivo, "r");

            while (caracter != -1) {
                caracter = file.read();

                if (Character.isLetter(((char) caracter))) {

                    columna = 0;

                } else if (Character.isDigit((char) caracter)) {
                    columna = 1;
                } else {
                    switch ((char) caracter) {
                        case '_':
                            columna = 2;
                            break;
                        case '\'':
                            columna = 4;
                            break;
                        case '+':
                            columna = 5;
                            break;
                        case '-':
                            columna = 6;
                            break;
                        case '*':
                            columna = 7;
                            break;
                        case '/':
                            columna = 8;
                            break;
                        case '>':
                            columna = 9;
                            break;
                        case '<':
                            columna = 10;
                            break;
                        case '=':
                            columna = 11;
                            break;
                        case '.':
                            columna = 3;
                            break;
                        case '(':
                            columna = 12;
                            break;
                        case ')':
                            columna = 13;
                            break;
                        case ',':
                            columna = 14;
                            break;
                        case ';':
                            columna = 15;
                            break;
                        case ':':
                            columna = 16;
                            break;
                        case '{':
                            columna = 17;
                            break;
                        case '}':
                            columna = 18;
                            break;

                        case ' ':
                            columna = 19;
                            break;
                        case 9://tab
                            columna = 20;
                            break;
                        case 10://nuevalinea
                            columna = 21;
                            numRenglon = numRenglon + 1;
                            break;
                        case 13://retorno de carro
                            columna = 22;
                            break;

                        default:
                            columna = 23;
                            break;
                    }

                }

                valorMT = matriz[estado][columna];

                if (valorMT < 100) {
                    estado = valorMT;

                    if (estado == 0) {
                        lexema = "";
                    } else {
                        lexema = lexema + (char) caracter;
                    }
                } else if (valorMT >= 100 && valorMT < 500) {
                    if (valorMT == 100) {
                        validarSiEsPalabraReservada();
                    }

                    if (valorMT == 100 || valorMT == 101 || valorMT == 102 || valorMT == 108 || valorMT == 110
                            || valorMT == 117 || valorMT == 9 || valorMT >= 200) {

                        file.seek(file.getFilePointer() - 1);
                    } else {
                        lexema = lexema + (char) caracter;
                    }

                    insertarNodo();
                    estado = 0;
                    lexema = "";

                } else {
                    imprimirMensajeError();
                    break;
                }

            }
            imprimirNodos();
          

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {

        }
    }

 
    private void imprimirMensajeError() {
        if (caracter != -1 && valorMT >= 500) {
            for (String[] errore : errores) {
                if (valorMT == Integer.valueOf(errore[1])) {
                    System.out.println(ANSI_RED+"ERROR:"+errore[0]+"error"+valorMT+"caracter"+caracter+"en el renglon" +numRenglon+ANSI_RESET);
                }
            }
            errorEncontrado = true;
        }
    }

    private void insertarNodo() {
        Nodos nodo = new Nodos(lexema, valorMT, numRenglon);

        if (cabeza == null) {
            cabeza = nodo;
            p = cabeza;
        } else {
            p.sig = nodo;
            p = nodo;
        }

    }

    private void imprimirNodos() {
        p = cabeza;
        while (p != null) {
            System.out.println(p.lexema + " " + p.token + " " + p.linea);
            p = p.sig;
        }
    }
 
    private void validarSiEsPalabraReservada() {
        for (String[] palReservada : palReservadas) {
            if (lexema.equals(palReservada[0])) {
                valorMT = Integer.valueOf(palReservada[1]);
            }
        }
    }
public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
}
