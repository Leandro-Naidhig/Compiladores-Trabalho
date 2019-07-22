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

public class BooleanExpr extends Expr {

	public BooleanExpr( boolean value ) {
		this.value = value;
	}

	public void genC( PW pw ) {
		pw.out.print( value ? "1" : "0" );
	}

	public Type getType() {
		return Type.booleanType;
	}

	public static BooleanExpr True = new BooleanExpr(true);
	public static BooleanExpr False = new BooleanExpr(false);

	private boolean value;
}
