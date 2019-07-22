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

public class Read extends Expr{

  public Read(Variable id) {
    this.id = id;
  }

  public void genC( PW pw ) {

    if (id.getType() == Type.stringType ) {
      pw.println("char s[1000];");
      pw.println("gets(s);");
      pw.println("sscanf(s, \"%s\", &" + id.getName() + ");");

    } else if (id.getType() == Type.integerType) {
      pw.println("scanf(\"%d\", &" + id.getName() + ");");
    }
  }

  public Type getType() {
    return id.getType();
  }

  private Variable id;
}
