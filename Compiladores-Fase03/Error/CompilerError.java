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
package Error;

import Lexer.*;
import java.io.*;

public class CompilerError {

  public CompilerError( PrintWriter out, String arquivo_entrada ) {
    this.arquivo = arquivo_entrada;
    this.out = out;
    this.flag = true;
  }

  public void setLexer( Lexer lexer ) {
    this.lexer = lexer;
  }

  public void signal( String strMessage ) {

    this.flag = false;
    out.println(arquivo + " : " + lexer.getLineNumber() + " : " + strMessage);
    out.println(lexer.getCurrentLine());
    out.println();

    if ( out.checkError() ) {
      System.out.println("Error in signaling an error");
    }

  }

  public Boolean getFlag() {
    return flag;
  }

  private Boolean flag;
  private String arquivo;
  private Lexer lexer;
  PrintWriter out;
}
