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

public class Func {

  //Contrutor da Classe
  public Func(ParamList paramlist, StatList statlist, String id, String tipo) {
		this.parametros = paramlist;
		this.statlist = statlist;
    this.id = id;
    this.tipo = tipo;
	}

  //Geracao do codigo em C
	public void genC(PW pw) {

		pw.out.print(this.tipo + " " + this.id);

    if(parametros != null) {
      pw.out.print("(");
      parametros.genC(pw);
      pw.out.print(")");
    }

    pw.out.println(" {");
    pw.add();
		statlist.genC(pw);
    pw.sub();
		pw.out.println("}");
    pw.out.println("");
	}

  //Atributos da Classe
	private ParamList parametros;
	private StatList statlist;
  private String id;
  private String tipo;
}
