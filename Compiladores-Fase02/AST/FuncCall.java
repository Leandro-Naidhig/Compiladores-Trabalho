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

	public FuncCall(Func funcao, ExprList exprlist) {
		this.funcao = funcao;
    	this.exprlist = exprlist;
	}

	public void genC(PW pw) {
		int i;
		pw.out.print(funcao.getName() + "(");
		exprlist.genC(pw);
    	pw.out.print(")");
	}

	public Type getType() {
		return Type.undefinedType;
	}

	private Func funcao;
  	private ExprList exprlist;
}
