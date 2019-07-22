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
import Lexer.*;

public class CompositeExpr extends Expr {

	public CompositeExpr( Expr pleft, Symbol poper, Expr pright ) {
		left = pleft;
		oper = poper;
		right = pright;
	}

	public void genC( PW pw ) {

		if(this.left != null) {
			pw.out.print("(");
			left.genC(pw);

			if(this.oper != null) {
				pw.out.print(" "+ oper.toString() + " ");
			} else {
				pw.out.print(")");
			}

			if(this.right != null) {
				right.genC(pw);
				pw.out.print(")");
			}
		}
	}

	public Type getType() {

		if ( oper == Symbol.EQ || oper == Symbol.NEQ || oper == Symbol.LE || oper == Symbol.LT ||
				 oper == Symbol.GE || oper == Symbol.GT || oper == Symbol.AND || oper == Symbol.OR ) {
					 return Type.booleanType;
		} else {
			return Type.integerType;
		}
	}

	private Expr left, right;
	private Symbol oper;
}
