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
import java.io.*;

public class Writeln extends Stat {

  public Writeln(ExprList exprlist) {
    this.exprlist = exprlist;
	}

  public void genC( PW pw ) {

    int i=0;
    int numParam = exprlist.getTamanho();
    Type tipo;

    pw.print("printf(\"");
    for(i=0; (i<numParam-1); i++) {

      tipo = exprlist.getExpr(i).getType();

      if(tipo == Type.stringType && numParam > 1) {
        pw.out.print("%s,");
      } else if(tipo == Type.integerType && numParam > 1) {
        pw.out.print("%d,");
      }
    }

    tipo = exprlist.getExpr(i).getType();

    if(tipo == Type.stringType) {
      pw.out.print("%s\\r\\n\"");
    } else if(tipo == Type.integerType) {
      pw.out.print("%d\\r\\n\"");
    }

    pw.out.print(", ");
    exprlist.genC(pw);
    pw.out.println(");");
  }

  private ExprList exprlist;

}
