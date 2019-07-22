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
import java.io.*;

abstract public class Subroutine {
	abstract public void genC(PW pw);

	public String getName() {
		return id.getName();
	}

	public void setParamList(ParamList parametros) {
		this.parametros = parametros;
	}

	public int getTamanho() {
		return this.tamanho;
	}

	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}

	public ParamList getParamList() {
		return this.parametros;
	}

	public int getNumParam() {
		if(parametros == null) {
				return 0;
		} else {
    	return parametros.getSize();
		}
  }

	public void setStatList(StatList statlist) {
		this.statlist = statlist;
	}

	public StatList getStatList() {
		return this.statlist;
	}

	protected Variable id;
	private ParamList parametros;
	private StatList statlist;
	private int tamanho;
}
