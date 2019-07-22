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

public class VarDecStat extends Stat {

  //Construtor da classe
  public VarDecStat(String id, String tipo) {
		this.id = id;
		this.tipo = tipo;
	}

  //Geração do codigo em C
	public void genC( PW pw ) {
		pw.println(tipo + " " + id + ";");
	}

  //Atributos da classe
	private String id;
	private String tipo;
}
