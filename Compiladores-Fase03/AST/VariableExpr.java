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
import java.io.PrintWriter;

public class VariableExpr extends Expr {
  public VariableExpr(Variable v) {
    this.v = v;
  }

  public void genC(PW pw) {
    pw.out.print(v.getName());
  }

  public Type getType() {
    return v.getType();
  }

  public String getName() {
    return v.getName();
  }

  private Variable v;
}