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

public class ReturnStat extends Stat {

  //Construtor da classe
  public ReturnStat(Expr expressao) {
		this.expressao = expressao;
	}

  //Geração do codigo em C
	public void genC( PW pw ) {
		pw.print("return ");
    expressao.genC(pw);
    pw.out.println(";");
	}

  //Atributos da classe
	private Expr expressao;
}
