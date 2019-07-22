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

public class ExprList {

  public ExprList(ArrayList<Expr> arrayexpr) {
    this.arrayexpr = arrayexpr;
	}

  public void genC(PW pw) {

    int i;

    //Percorre cada parametro da funcao
    for(i=0; i<(this.arrayexpr.size()-1); i++) {
			this.arrayexpr.get(i).genC(pw);
      pw.print(", ");
    }
    //O ultimo parametro nao vai printar a virgula
    this.arrayexpr.get(i).genC(pw);
  }

  private ArrayList<Expr> arrayexpr;
}
