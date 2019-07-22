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
	RIGHTKEY("}");

	Symbol(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	private String name;
}
