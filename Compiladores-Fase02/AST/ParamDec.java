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
import java.util.*;

public class ParamDec {

  public ParamDec(Variable id) {
    this.id = id;
	}

  public void genC(PW pw) {
    pw.print(id.getType().getCname() + " " + id.getName());
  }

  public Type getType(){
	  return this.id.getType();
  }

  public String getName(){
    return this.id.getName();
  }

  private Variable id;
}
