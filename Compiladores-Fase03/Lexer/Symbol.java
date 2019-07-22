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
package Lexer;

public enum Symbol {
	EOF("eof"),
	IDENT("Ident"),
	PLUS("+"),
	MINUS("-"),
	MULT("*"),
	DIV("/"),
	LT("<"),
	LE("<="),
	GT(">"),
	GE(">="),
	NEQ("!="),
	EQ("=="),
	ASSIGN("="),
	LEFTPAR("("),
	RIGHTPAR(")"),
	SEMICOLON(";"),
	VAR("var"),
	BEGIN("begin"),
	END("end"),
	IF("if"),
	THEN("then"),
	WHILE("while"),
	ELSE("else"),
	ENDIF("endif"),
	COMMA(","),
	READINT("readInt"),
	READSTRING("readString"),
	WRITELN("writeln"),
	WRITE("write"),
	COLON(":"),
	INTEGER("Int"),
	LITERALBOOLEAN("literalBoolean"),
	LITERALSTRING("literalstring"),
	LITERALINT("literalint"),
	CHAR("char"),
	CHARACTER("character"),
	TRUE("true"),
	FALSE("false"),
	OR("or"),
	AND ("and"),
	REMAINDER("%"),
	NOT("!"),
	FUNCTION("function"),
	RETURN("return"),
	ARROW("->"),
	LEFTKEY("{"),
	INVALID("invalido"),
	RIGHTKEY("}");

	Symbol(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	private String name;
}
