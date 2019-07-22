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

public class IfStat extends Stat {

  //Construtor da classe
  public IfStat(Expr expr, StatList ifPart, StatList elsePart) {
		this.expr = expr;
		this.elsePart = elsePart;
    this.ifPart = ifPart;
	}

  //Geração do codigo em C
	public void genC( PW pw ) {
		pw.print("if (");
		expr.genC(pw);
		pw.out.println(") { ");
    pw.add();
		ifPart.genC(pw);
		pw.sub();
		pw.println("}");

		if (elsePart != null) {
			pw.println("else {");
			pw.add();
			elsePart.genC(pw);
			pw.sub();
			pw.println("}");
    }
	}

  //Atributos da classe
	private Expr expr;
	private StatList elsePart;
  private StatList ifPart;
}
