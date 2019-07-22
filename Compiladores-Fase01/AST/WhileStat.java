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

public class WhileStat extends Stat {

  public WhileStat(Expr expr, StatList statlist) {
		this.expr = expr;
    this.statlist = statlist;
	}

	public void genC( PW pw ) {
		pw.print("while (");
		expr.genC(pw);
		pw.out.println(") { ");

		if ( statlist != null ) {
      pw.add();
			statlist.genC(pw);
      pw.sub();
		}

    pw.println("}");
	}

	private Expr expr;
  private StatList statlist;
}
