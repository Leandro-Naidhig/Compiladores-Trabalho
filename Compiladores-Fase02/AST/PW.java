/*
    Bacharelado em Ciência da Computação

    Universidade Federal de São Carlos

    Campus Sorocaba

    Projeto Prático Fase 1 - Compiladores

    Integrantes:

    Nome: [Chady Chaito] (https://github.com/chadychaito) RA: 613697
    Nome: [Gustavo Buoro Branco de Souza] (https://github.com/Gustavobbs/) RA: 726533
    Nome: [José Gabriel Oliveira Santana] (https://github.com/Eetrexx/) RA: 620459
    Nome: [Leandro Naidhig] (https://github.com/Leandro-Naidhig/) RA: 726555

*/
package AST;
import java.lang.System;
import java.io.*;

public class PW {

   public void add() {
      currentIndent += step;
   }
   public void sub() {
      currentIndent -= step;
   }

   public void set( PrintWriter out ) {
      this.out = out;
      currentIndent = 0;
   }

   public void set( int indent ) {
      currentIndent = indent;
   }

   public int get(){
        return this.currentIndent;
   }

   public void print( String s ) {
      out.print( space.substring(0, currentIndent) );
      out.print(s);
   }

   public void println( String s ) {
      out.print( space.substring(0, currentIndent) );
      out.println(s);
   }

   int currentIndent = 0;
   static public final int green = 0, java = 1;
   int mode = green;
   public int step = 3;
   public PrintWriter out;
   static final private String space = "                                                                                                        ";

}
