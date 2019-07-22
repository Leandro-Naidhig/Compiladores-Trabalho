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

import AST.*;
import java.util.*;
import java.lang.Character;
import java.lang.String;
import Error.*;
import Lexer.*;
import java.io.*;

public class Compiler {

  public Program compile( char []input, PrintWriter outError ) {

    error = new CompilerError(outError);
    lexer = new Lexer(input, error);
    error.setLexer(lexer);
    lexer.nextToken();
    return program();
  }

  /* Program ::= Func {Func} - OK*/
  private Program program() {

    ArrayList<Func> arrayFunc = new ArrayList<Func>();
    arrayFunc.add(func());
    lexer.nextToken();
  	while(lexer.token == Symbol.FUNCTION) {
  	   arrayFunc.add(func());
       lexer.nextToken();
  	}
    Program programa = new Program(arrayFunc);
    return programa;
  }

  /* Func ::= "function" Id [ "(" ParamList ")" ] ["->" Type ] StatList - OK*/
  private Func func() {

    ParamList parametros = null;
    String id = "";

  	if(lexer.token == Symbol.FUNCTION) {
      lexer.nextToken();

      if(lexer.token == Symbol.IDENT) {
        id = lexer.getStringValue();
        lexer.nextToken();

      } else {
        error.signal("É esperado um identificador!");
      }

  		if(lexer.token == Symbol.LEFTPAR) {

        lexer.nextToken();
  			parametros = paramlist();

  			if(lexer.token == Symbol.RIGHTPAR) {
  			     lexer.nextToken();
  			} else {
  				error.signal("É esperado ')'");
  			}
  		}

  		if(lexer.token == Symbol.ARROW) {
        lexer.nextToken();
        String tipo = type();
        lexer.nextToken();
        return new Func(parametros, statlist(), id, tipo);

      } else {
        return new Func(parametros, statlist(), id, "void");
      }

  	} else {
  		error.signal("É esperado um 'function'");
  	}
    return null;
  }

  /* ParamList ::= ParamDec {”, ”ParamDec} - OK*/
  private ParamList paramlist() {

    ArrayList<ParamDec> arrayParam = new ArrayList<ParamDec>();
  	arrayParam.add(paramdec());
  	lexer.nextToken();

  	while(lexer.token == Symbol.COMMA) {
      lexer.nextToken();
  		arrayParam.add(paramdec());
  		lexer.nextToken();
  	}
    return new ParamList(arrayParam);
  }

  //Função para compor uma lista de expressao para o FuncCall
  private ExprList exprlist() {

    ArrayList<Expr> arrayExpr = new ArrayList<Expr>();
  	arrayExpr.add(expr());

  	while(lexer.token == Symbol.COMMA) {
      lexer.nextToken();
  		arrayExpr.add(expr());
  	}
    return new ExprList(arrayExpr);
  }

  /* ParamDec ::= Id ":" Type - OK*/
  private ParamDec paramdec() {

    String id = "";
    String tipo = "";

    if(lexer.token == Symbol.IDENT) {
      id = lexer.getStringValue();

    } else {
      error.signal("É esperado um identificador");
    }

    lexer.nextToken();

  	if(lexer.token != Symbol.COLON) {
      error.signal("É esperao ':'");
    }

    lexer.nextToken();
    tipo = type();
    ParamDec parametro = new ParamDec(id, tipo);
    return parametro;
  }

  private String type() {

    String result;

    switch (lexer.token) {
      case INTEGER :
        result = Symbol.INTEGER.toString();
        break;
      case BOOLEAN :
        result = Symbol.BOOLEAN.toString();
        break;
      case STRING :
        result = Symbol.STRING.toString();
        break;
      default :
        error.signal("É esperado um Tipo");
        result = null;
    }
    return result;
  }

  /* StatList ::= "{” {Stat} ”}" - OK*/
  /* Stat ::= AssignExprStat | ReturnStat | VarDecStat | IfStat | WhileStat - OK*/
  private StatList statlist() {

    ArrayList<Stat> arrayStat = new ArrayList<Stat>();

  	if(lexer.token != Symbol.LEFTKEY) {
  		error.signal("É esperado '{'");

    } else {
      lexer.nextToken();
      while(true) {

        Symbol Op = lexer.token;

        if((Op == Symbol.TRUE) || (Op == Symbol.FALSE) || (Op == Symbol.LITERALSTRING) || (Op == Symbol.NUMBER) || (Op == Symbol.IDENT)) {
          arrayStat.add(assignexprstat());
        } else if(Op == Symbol.RETURN) {
          lexer.nextToken();
          arrayStat.add(returnstat());
        } else if(Op == Symbol.VAR) {
          lexer.nextToken();
          arrayStat.add(vardecstat());
        } else if(Op == Symbol.IF) {
          lexer.nextToken();
          arrayStat.add(ifstat());
        } else if(Op == Symbol.WHILE) {
          lexer.nextToken();
          arrayStat.add(whilestat());
        } else if(Op == Symbol.WRITE) {
          lexer.nextToken();
          arrayStat.add(write());
        } else if(Op == Symbol.WRITELN) {
          lexer.nextToken();
          arrayStat.add(writeln());
        } else {
          if(lexer.token != Symbol.RIGHTKEY) {
            error.signal("É esperado '}'");
          }
          break;
        }
      }
    }
    return new StatList(arrayStat);
  }

  /* ReturnStat ::= "return" Expr ";" - OK */
  private ReturnStat returnstat() {

    Expr expressao;
  	expressao = expr();
  	if(lexer.token == Symbol.SEMICOLON) {
      lexer.nextToken();
      ReturnStat retorno = new ReturnStat(expressao);
      return retorno;
    } else {
      error.signal("É esperado ';'");
    }
    return null;
  }

  /* VarDecStat ::= "var" Id ":" Type ";" - OK  */
  private VarDecStat vardecstat() {

    String id = "";
    String tipo = "";

    if(lexer.token == Symbol.IDENT) {
      id = lexer.getStringValue();
      lexer.nextToken();
  	}

  	if(lexer.token != Symbol.COLON) {
    	error.signal("É esperado ':'");
    }
    lexer.nextToken();

    //Recupera o tipo
    tipo = type();
    lexer.nextToken();

    if(lexer.token != Symbol.SEMICOLON) {
      error.signal("É esperado ';'");
    }

    lexer.nextToken();

    VarDecStat variaveldes = new VarDecStat(id, tipo);
    return variaveldes;
  }

  /* IfStat ::= "if" Expr StatList [ "else" StatList ] - OK*/
  private IfStat ifstat() {

    Expr Exp = null;
    StatList Else = null;
    StatList If = null;
    Exp = expr();
    If = statlist();
    lexer.nextToken();

    if ( lexer.token == Symbol.ELSE ) {
      lexer.nextToken();
      Else = statlist();
      lexer.nextToken();
    }
    return new IfStat(Exp, If, Else);
  }

  //WhileStat ::= "while" Expr StatList
  private WhileStat whilestat() {

    Expr expressao = expr();
    StatList corpo = statlist();
    lexer.nextToken();
    return new WhileStat(expressao, corpo);
  }

  /* AssignExprStat ::= Expr [ "=" Expr ] ";" - OK*/
  private AssignExprStat assignexprstat() {

    Expr ExpDir = null;
    Expr ExpEsq = null;

    ExpEsq = expr();

    if (lexer.token == Symbol.ASSIGN) {
      lexer.nextToken();
      ExpDir = expr();
    }

    if(lexer.token != Symbol.SEMICOLON) {
      error.signal("É esperado ';'");
    }

    lexer.nextToken();
    return new AssignExprStat(ExpEsq, ExpDir);
  }

  /* Expr ::= ExprAnd { ”or” ExprAnd } - OK*/
  private Expr expr() {

    Expr ExpEsq, ExpDir;
    ExpEsq = ExprAnd();

    while (lexer.token == Symbol.OR) {
      lexer.nextToken();
      ExpDir = ExprAnd();
      ExpEsq = new CompositeExpr(ExpEsq, Symbol.OR, ExpDir);
    }
    return ExpEsq;
  }

  /* ExprAnd ::= ExprRel { ”and” ExprRel } - OK*/
  private Expr ExprAnd() {

    Expr ExpEsq, ExpDir;
    ExpEsq = ExprRel();

    while (lexer.token == Symbol.AND) {
      lexer.nextToken();
      ExpDir = ExprRel();
      ExpEsq = new CompositeExpr(ExpEsq, Symbol.AND, ExpDir);
    }
    return ExpEsq;
  }

  /* ExprRel ::= ExprAdd [ RelOp ExprAdd ] - OK*/
  private Expr ExprRel() {

    Expr ExpEsq, ExpDir;
    ExpEsq = ExprAdd();
    Symbol Op = lexer.token;

    if(Op == Symbol.EQ || Op == Symbol.NEQ || Op == Symbol.LE || Op == Symbol.LT || Op == Symbol.GE || Op == Symbol.GT ) {
      lexer.nextToken();
      ExpDir = ExprAdd();
      ExpEsq = new CompositeExpr(ExpEsq, Op, ExpDir);
    }
    return ExpEsq;
  }

  /* ExprAdd ::= ExprMult {(” + ” | ” − ”)ExprMult} - OK*/
  private Expr ExprAdd() {

    Symbol Op = null;
    Expr ExpEsq, ExpDir;
    ExpEsq = ExprMult();

    while (lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS) {
      Op = lexer.token;
      lexer.nextToken();
      ExpDir = ExprMult();
      ExpEsq = new CompositeExpr(ExpEsq, Op, ExpDir);
    }
    return ExpEsq;
  }

  /* ExprMult ::= ExprUnary {(” ∗ ” | ”/”)ExprUnary} - OK */
  private Expr ExprMult() {

    Symbol Op = null;
    Expr ExpEsq, ExpDir;
    ExpEsq = ExprUnary();

    while (lexer.token == Symbol.MULT || lexer.token == Symbol.DIV) {
      Op = lexer.token;
      lexer.nextToken();
      ExpDir = ExprUnary();
      ExpEsq = new CompositeExpr(ExpEsq, Op, ExpDir);
    }
    return ExpEsq;
  }

  /* ExprUnary ::= [ ( "+" | "-" ) ] ExprPrimary - OK */
  private Expr ExprUnary() {

    Symbol Op = null;
    Expr ExpDir = null;
    Expr expressao = null;

    if(lexer.token == Symbol.PLUS) {
      Op = Symbol.PLUS;
      lexer.nextToken();
      ExpDir = ExprPrimary();
      expressao = new CompositeExpr(ExpDir, Op, null);

    } else if(lexer.token == Symbol.MINUS){
      Op = Symbol.MINUS;
      lexer.nextToken();
      ExpDir = ExprPrimary();
      expressao = new CompositeExpr(ExpDir, Op, null);
    }

    expressao = ExprPrimary();
    return expressao;
  }

  /* ExprPrimary ::= Id | FuncCall | ExprLiteral */
  private Expr ExprPrimary() {
    switch (lexer.token) {
      case LITERALSTRING :
      String literalstr = lexer.getStringValue();
      lexer.nextToken();
      return new StringExpr(literalstr);
      case NUMBER :
        Expr expr = number();
        lexer.nextToken();
        return expr;
      case TRUE :
        lexer.nextToken();
        return BooleanExpr.True;
      case FALSE :
        lexer.nextToken();
        return BooleanExpr.False;
      case IDENT :
        String nome = lexer.getStringValue();
        Id identificador = new Id(nome);
        lexer.nextToken();
        if(lexer.token == Symbol.LEFTPAR) {
          Expr funcao = funccall(identificador);
          return funcao;
        } else {
          return new IdExpr(identificador);
        }
      default :
        error.signal("É esperado uma expressão");
    }
    return null;
  }

  //Retorna um numero em forma de expressao
  private NumberExpr number() {
    NumberExpr e = null;
    int value = lexer.getNumberValue();
    return new NumberExpr(value);
  }


  /* FuncCall ::= Id "(" [ Expr {”, ”Expr} ] ")" - OK*/
  private Expr funccall(Id identificador) {

    ExprList exprList = null;

  	if (lexer.token == Symbol.LEFTPAR) {
      lexer.nextToken();
      Symbol Op = lexer.token;

      if(Op == Symbol.OR || Op == Symbol.AND || Op == Symbol.EQ ||
         Op == Symbol.NEQ || Op == Symbol.LE || Op == Symbol.LT ||
         Op == Symbol.GE || Op == Symbol.GT || Op == Symbol.MINUS ||
         Op == Symbol.PLUS || Op == Symbol.IDENT || Op == Symbol.TRUE ||
         Op == Symbol.FALSE || Op == Symbol.NUMBER || Op == Symbol.STRING) {

         exprList = exprlist();

  		   if (lexer.token != Symbol.RIGHTPAR) {
  			   error.signal("É esperado ')'");
  		   }
         lexer.nextToken();
         return new FuncCall(identificador, exprList);

      } else {
  		  error.signal("É esperado '('");
  	  }
    }
    return null;
  }

  private Write write() {

    ExprList exprList = null;

  	if (lexer.token == Symbol.LEFTPAR) {
      lexer.nextToken();
      Symbol Op = lexer.token;

      if(Op == Symbol.OR || Op == Symbol.AND || Op == Symbol.EQ ||
         Op == Symbol.NEQ || Op == Symbol.LE || Op == Symbol.LT ||
         Op == Symbol.GE || Op == Symbol.GT || Op == Symbol.MINUS ||
         Op == Symbol.PLUS || Op == Symbol.IDENT || Op == Symbol.TRUE ||
         Op == Symbol.FALSE || Op == Symbol.NUMBER || Op == Symbol.STRING ||
         Op == Symbol.LITERALSTRING) {

         exprList = exprlist();

  		   if (lexer.token != Symbol.RIGHTPAR) {
  			   error.signal("É esperado ')'");
  		   }

         lexer.nextToken();

         if (lexer.token != Symbol.SEMICOLON) {
  			   error.signal("É esperado ';'");
  		   }

         lexer.nextToken();
         return new Write(exprList);

      } else {
  		  error.signal("É esperado '('");
  	  }
    }
    return null;
  }

  private Writeln writeln() {

    ExprList exprList = null;

  	if (lexer.token == Symbol.LEFTPAR) {
      lexer.nextToken();
      Symbol Op = lexer.token;

      if(Op == Symbol.OR || Op == Symbol.AND || Op == Symbol.EQ ||
         Op == Symbol.NEQ || Op == Symbol.LE || Op == Symbol.LT ||
         Op == Symbol.GE || Op == Symbol.GT || Op == Symbol.MINUS ||
         Op == Symbol.PLUS || Op == Symbol.IDENT || Op == Symbol.TRUE ||
         Op == Symbol.FALSE || Op == Symbol.NUMBER || Op == Symbol.STRING ||
         Op == Symbol.LITERALSTRING) {

         exprList = exprlist();

  		   if (lexer.token != Symbol.RIGHTPAR) {
  			   error.signal("É esperado ')'");
  		   }

         lexer.nextToken();

         if (lexer.token != Symbol.SEMICOLON) {
  			   error.signal("É esperado ';'");
  		   }

         lexer.nextToken();
         return new Writeln(exprList);

      } else {
  		  error.signal("É esperado '('");
  	  }
    }
    return null;
  }

  private Lexer lexer;
  private CompilerError error;
}
