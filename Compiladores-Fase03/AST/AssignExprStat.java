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

public class AssignExprStat extends Stat {
	public AssignExprStat(Expr expr1, Expr expr2) {
		this.expr1 = expr1;
		this.expr2 = expr2;
	}

	public void genC( PW pw ) {

		int flag=0;


		if(expr1 != null) {
			pw.print("");

			/*Verificacao para copia de uma string para uma variavel*/
			if(expr1.getType() == Type.stringType && expr2 != null) {
				pw.out.print("strcpy(");
				expr1.genC(pw);
				pw.out.print(", ");
				expr2.genC(pw);
				pw.out.print(")");
				flag =1;
			} else {
				expr1.genC(pw);
			}

			if(expr2 != null && flag != 1) {
				pw.out.print(" = " );
				expr2.genC(pw);
				pw.out.println(";");
			} else {
				pw.out.println(";");
			}
		}
	}

	private Expr expr2;
	private Expr expr1;
}
