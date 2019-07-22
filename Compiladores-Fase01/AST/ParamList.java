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

public class ParamList {

  public ParamList(ArrayList<ParamDec> arrayParam) {
    this.arrayParam = arrayParam;
	}

  public void genC(PW pw) {

    int i;

    //Percorre cada parametro da funcao
    for(i=0; i<(this.arrayParam.size()-1); i++) {
			this.arrayParam.get(i).genC(pw);
      pw.print(", ");
    }
    //O ultimo parametro nao vai printar a virgula
    this.arrayParam.get(i).genC(pw);
  }

  private ArrayList<ParamDec> arrayParam;
}
