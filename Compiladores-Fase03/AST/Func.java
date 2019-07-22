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

public class Func extends Subroutine {

  //Contrutor da Classe
  public Func(Variable id) {
    this.id = id;
	}

  public Type getReturnType() {
    return id.getType();
  }

  public void setNum(int tamanho) {
    this.tamanho = tamanho;
  }

  public int getNum() {
    return tamanho;
  }

  public String getName() {
    return id.getName();
  }

  //Geracao do codigo em C
	public void genC(PW pw) {

    if(id.getType() == Type.undefinedType) {
      pw.out.print("void " + id.getName());
    } else {
      pw.out.print(id.getType().getCname() + " " + id.getName());
    }

    pw.out.print("(");

    if(getParamList() != null) {
      getParamList().genC(pw);
    }

    pw.out.print(")");

    pw.out.println(" {");
    pw.add();
    getStatList().genC(pw);
    pw.sub();
		pw.out.println("}");
    pw.out.println("");
	}

  //Atributos da Classe
  private Variable id;
  private int tamanho;
}
