/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador_diego_autoii;

public class SistemaDe_Tipos {
    
    
    
    boolean Asingaciones[][]={
            //          ENTERO   DECIMAL  STRING    LOGICO
            /*ENTERO*/  {true,   false,   false,    false},
            /*DECIMAL*/ {true,   true,    false,    false},
            /*STRING*/  {false,  false,   true,     false},
            /*LOGICO*/  {false,  false,   false,    true}

    };

    int Sumas[][]={
            //          0ENTERO   1DECIMAL  2STRING    3LOGICO
            /*0ENTERO*/  {101,     102,     0,         0},
            /*1DECIMAL*/ {102,     102,     0  ,       0},
            /*2STRING*/  {0,       0,       103,          0},
            /*3LOGICO*/  {0,       0,        0,         0}
    };

    int Resta_Multiplicacion[][]= {
            //          ENTERO   DECIMAL  STRING    LOGICO
            /*ENTERO*/  {101,   102,        0,       0},
            /*DECIMAL*/ {102,   102,        0,       0},
            /*STRING*/  {0,       0,         0,      0},
            /*LOGICO*/  {0,       0,         0,      0}
    };

    int Divisiones[][]={
            //          ENTERO   DECIMAL  STRING    LOGICO
            /*ENTERO*/  {102,   102,        0,       0},
            /*DECIMAL*/ {102,   102,        0,       0},
            /*STRING*/  {0,       0,         0,      0},
            /*LOGICO*/  {0,       0,         0,      0}
    };

    int Relacionales[][]={
            //          ENTERO   DECIMAL  STRING    LOGICO
            /*ENTERO*/  {221,   221,        0,       0},
            /*DECIMAL*/ {221,   221,        0,       0},
            /*STRING*/  {0,       0,         0,      0},
            /*LOGICO*/  {0,       0,         0,      0}
    };

    int Igual_Diferente[][]={
            //          ENTERO   DECIMAL  STRING    LOGICO
            /*ENTERO*/  {221,   221,        0,       0},
            /*DECIMAL*/ {221,   221,        0,       0},
            /*STRING*/  {0,       0,         0,      0},
            /*LOGICO*/  {0,       0,         0,      221}
    };

    boolean Logicos[][]={
            //          ENTERO   DECIMAL  STRING    LOGICO
            /*ENTERO*/  {false,  false,    false,    false},
            /*DECIMAL*/ {false,  false,     false,    false},
            /*STRING*/  {false,  false,    false,    false},
            /*LOGICO*/  {false,  false,    false,    true}

    };

}
