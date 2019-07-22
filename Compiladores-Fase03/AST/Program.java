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

public class Program {
	public Program(ArrayList<Subroutine> arrayFunc) {
		this.arrayFunc = arrayFunc;
	}

	public void genC( PW pw ) {
		pw.out.println("#include <stdio.h>");
		pw.out.println("#include <string.h>");
		pw.out.println();
		for(Subroutine f : arrayFunc) {
			f.genC(pw);
		}
	}

	private ArrayList<Subroutine> arrayFunc;
}
