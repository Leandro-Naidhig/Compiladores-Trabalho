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

public class FuncCall extends Expr {

	public FuncCall(Id identificador, ExprList exprlist) {
		this.identificador = identificador;
    this.exprlist = exprlist;
	}

	public void genC(PW pw) {
		int i;
		pw.out.print(identificador.getName() + "(");
		exprlist.genC(pw);
    pw.out.print(")");
}

	private Id identificador;
  private ExprList exprlist;
}
