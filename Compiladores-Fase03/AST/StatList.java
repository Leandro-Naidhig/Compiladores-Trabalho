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

public class StatList {

	public StatList(ArrayList<Stat> arraystat) {
		this.arraystat = arraystat;
		this.tamanho = arraystat.size();
	}

	public void genC(PW pw) {
		for(Stat s : arraystat) {
			s.genC(pw);
		}
	}

	public int getTamanho() {
		return tamanho;
	}

	public ArrayList<Stat> get_estados() {
    return arraystat;
  }

	private ArrayList<Stat> arraystat;
	private int tamanho;
}
