/*
    Bacharelado em Ciência da Computação

    Universidade Federal de São Carlos

    Campus Sorocaba

    Projeto Prático Fase 2 - Compiladores

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

  public Program compile( char []input, PrintWriter outError, String arquivo_entrada) {

  	/* Hash table para o analizador semantico */
  	symbolTable = new SymbolTable();
    error = new CompilerError(outError, arquivo_entrada);
    lexer = new Lexer(input, error);
    error.setLexer(lexer);
    lexer.nextToken();
    return program();
  }

  /* Program ::= Func {Func} */
  private Program program() {

    ArrayList<Subroutine> arrayFunc = new ArrayList<Subroutine>();
    num_est = 0;
    numero_retornos = 0;
    arrayFunc.add(func());
    lexer.nextToken();
  	while(lexer.token == Symbol.FUNCTION) {
      num_est = 0;
      numero_retornos = 0;
  	  arrayFunc.add(func());
      lexer.nextToken();
  	}
    Program programa = new Program(arrayFunc);

    /* Analisador semantico: analise de funcao "main" */
    Func mainProc;
    if ((mainProc = (Func) symbolTable.getInGlobal("main")) == null) {
		    error.signal("Código fonte deve ter um procedimento chamado main");
    }

    return programa;
  }

  /* Func ::= "function" Id [ "(" ParamList ")" ] ["->" Type ] StatList */
  private Subroutine func() {

    ParamList parametros = null;
    String identificador = "";
    int mainFlag = 0;

  	if(lexer.token == Symbol.FUNCTION) {
      lexer.nextToken();

      if(lexer.token == Symbol.IDENT) {
        identificador = lexer.getStringValue();
        lexer.nextToken();

      } else {
        error.signal("É esperado um identificador");
      }

      if (identificador.toLowerCase().equals("main")) {
      	mainFlag = 1;
      }

      /*Análise Semantica para verificar se existe uma função com o mesmo nome*/
      if(mainFlag == 1 && symbolTable.get(identificador) != null) {
        error.signal("A função main já foi declarada");

      } else if (symbolTable.get(identificador) != null) {
        error.signal("A função com nome '" + identificador + "' já foi declarada");

      }

      Variable id =  new Variable(identificador);

		if(lexer.token == Symbol.LEFTPAR) {

			lexer.nextToken();
			parametros = paramlist();

			// Verifica se tem algum parametro se tratando da funcao "main"
			if (mainFlag == 1 && parametros.getSize() != 0) {
			error.signal("Não é permitido a passagem de parâmetros na função main");
			}

			if(lexer.token == Symbol.RIGHTPAR) {
				lexer.nextToken();
			} else if(mainFlag == 0){
				error.signal("É esperado ')' fechando a lista de parâmetros");
			}

		} else if(mainFlag == 0) {
			error.signal("É esperado '(' abrindo a lista de parâmetros");
		}

  		if(lexer.token == Symbol.ARROW) {

        /* Verifica se tem tipo de retorno se tratando da funcao "main" */
        if (identificador.toLowerCase().equals("main")) {
  			   error.signal("Não é permitido retorno de tipo na função main");
  			}

	      lexer.nextToken();
        Type tipo = type();
        id.setType(tipo);
        lexer.nextToken();
        Subroutine funcao = funcao_atual = new Func(id);
        symbolTable.putInGlobal(id.getName(), funcao);
        funcao.setParamList(parametros);
        StatList p = statlist();
        int tamanho = p.getTamanho();
        ArrayList<Stat> estados = p.get_estados();
        funcao.setStatList(p);

        /* Verifica se todos os estados da funcao tem retorno ou se no final de seu escopo tem retorno*/
        if(!(estados.get(tamanho-1) instanceof ReturnStat)) {
          if(numero_retornos < num_est) {
            error.signal("A função " + identificador + " deve ter retorno no final do escopo");
          }
        }

        symbolTable.removeLocalIdent(); /* Realiza a limpeza das declarações locais */
	      return funcao;

      } else {

        id.setType(null);
        Subroutine funcao = funcao_atual = new Func(id);
        symbolTable.putInGlobal(id.getName(), funcao);
        funcao.setParamList(parametros);
        funcao.setStatList(statlist());
        symbolTable.removeLocalIdent(); /* Realiza a limpeza das declarações locais */
	      return funcao;
      }

  	} else {
       error.signal("É esperado uma função");
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

  /* Função para compor uma lista de expressao para o FuncCall */
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

    String identificador = "";

    if(lexer.token == Symbol.IDENT) {
      identificador = lexer.getStringValue();
    } else {
      error.signal("É esperado um identificador");
    }

    /*Análise Semantica*/
    if (symbolTable.getInLocal(identificador) != null) {
      error.signal("identificador com nome '" + identificador + "' já foi declarada");
    }

    Variable id = new Variable(identificador);
    symbolTable.putInLocal(identificador, id);
    lexer.nextToken();

    if(lexer.token != Symbol.COLON) {
      error.signal("É esperado ':' após o identificador");
    }

    lexer.nextToken();
    Type tipo = type();
    id.setType(tipo);

    ParamDec parametro = new ParamDec(id);
    return parametro;
  }

  /* StatList ::= "{” {Stat} ”}" */
  /* Stat ::= AssignExprStat | ReturnStat | VarDecStat | IfStat | WhileStat */
  private StatList statlist() {

    ArrayList<Stat> arrayStat = new ArrayList<Stat>();

  	if(lexer.token != Symbol.LEFTKEY) {
  		error.signal("É esperado '{'");

    } else {
      lexer.nextToken();
      while(true) {

        Symbol Op = lexer.token;

        if((Op == Symbol.TRUE) || (Op == Symbol.FALSE) || (Op == Symbol.LITERALSTRING) || (Op == Symbol.LITERALINT) || (Op == Symbol.IDENT) || (Op == Symbol.READINT) || (Op == Symbol.READSTRING)) {
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

  /* ReturnStat ::= "return" Expr ";" */
  private ReturnStat returnstat() {

    Expr expressao;
    numero_retornos++;
  	expressao = expr();

  	if(lexer.token == Symbol.SEMICOLON) {

      lexer.nextToken();

      if(funcao_atual.getName().equals("main")) {
        error.signal("A funcao Main não pode ter retorno");

      } else if(!(expressao instanceof VariableExpr) && !(funcao_atual.getName().equals("main")))  {
          error.signal("O retorno da função não está associado á uma variável");

      } else if (!checkAssignment(funcao_atual.getReturnType(), expressao.getType())) {
        error.signal("Tipo de retorno é " + expressao.getType().getName() + ", enquanto o retorno da função já declarada é " + funcao_atual.getReturnType().getName());
      }

      ReturnStat retorno = new ReturnStat(expressao);
      return retorno;

    } else {
      error.signal("É esperado ';' após a expressão");
    }
    return null;
  }

  /* VarDecStat ::= "var" Id ":" Type ";" - OK  */
  private VarDecStat vardecstat() {

    String identificador = "";

    if(lexer.token == Symbol.IDENT) {
      identificador = lexer.getStringValue();
      lexer.nextToken();

      if (symbolTable.getInLocal(identificador) != null ) {
  		  error.signal("O identificador '" + identificador + "' já foi declarado");
      }

    } else {
      error.signal("Identificador esperado");
      lexer.nextToken();
    }

    Variable id = new Variable(identificador);

  	if(lexer.token != Symbol.COLON) {
    	error.signal("É esperado ':' após o identificador");
    }

    lexer.nextToken();
    Type tipo = type();
    id.setType(tipo);
    lexer.nextToken();

    if(lexer.token != Symbol.SEMICOLON) {
      error.signal("É esperado ';' após o tipo");
    }

    symbolTable.putInLocal(identificador, id);
    lexer.nextToken();
    VarDecStat variaveldes = new VarDecStat(id);
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
    num_est++;

    if (Exp.getType() != Type.booleanType) {
      error.signal("É esperada uma expressão do tipo Boolean");
    }

    if ( lexer.token == Symbol.ELSE ) {
      num_est++;
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

    if (!checkWhileExpr(expressao.getType())) {
      error.signal("É esperada um expressão do tipo Boolean");
    }

    lexer.nextToken();
    return new WhileStat(expressao, corpo);
  }

  /*Método para verificar a expressao no médotodo WhileStat*/
  private boolean checkWhileExpr(Type exprType) {
    if (exprType == Type.undefinedType || exprType == Type.booleanType) {
      return true;
    } else {
      return false;
    }
  }

  /* AssignExprStat ::= Expr [ "=" Expr ] ";" */
  private AssignExprStat assignexprstat() {

    Expr ExpDir = null;
    Expr ExpEsq = null;
    VariableExpr v = null;
    ExpEsq = expr();

    if (lexer.token == Symbol.ASSIGN) {

      if(!(ExpEsq instanceof VariableExpr)) {
        error.signal("É esperado um identificador antes do '='");

      } else {
        v = (VariableExpr) ExpEsq;
      }

      lexer.nextToken();

      if(lexer.token == Symbol.READINT || lexer.token == Symbol.READSTRING) {

        Symbol Op = lexer.token;
        ExpDir = read();

        if(ExpDir != null) {

          if(Op == Symbol.READINT && ExpDir.getType() != Type.integerType) {
            error.signal("Tipo da variável " + v.getName() + " incompátivel com 'readInt'; deve ser do tipo 'Int'");

          } else if(Op == Symbol.READSTRING && ExpDir.getType() != Type.stringType) {
            error.signal("Tipo da variável " + v.getName() + " incompátivel com 'readString'; deve ser do tipo 'String'");
          }

          if(ExpEsq.getType() == Type.integerType && ExpDir.getType() != Type.integerType) {
            error.signal("Função 'readInt' requer parâmetro do tipo Int");
          } else if(ExpEsq.getType() == Type.stringType && ExpDir.getType() != Type.stringType)   {
            error.signal("Função 'readString' requer parâmetro do tipo String");
          }

          lexer.nextToken();
          return new AssignExprStat(ExpEsq, ExpDir);

        } else {
          lexer.nextToken();
        }

      } else {
        ExpDir = expr();
      }
    }

    if(lexer.token != Symbol.SEMICOLON) {
      error.signal("É esperado ';' após a atribuição");
    }
    if (ExpEsq != null && ExpDir != null && !checkAssignment(ExpEsq.getType(), ExpDir.getType())) {
      error.signal("Variaveis possuem tipos incompátiveis para a atribuição");
    }
    lexer.nextToken();
    return new AssignExprStat(ExpEsq, ExpDir);
  }

  /*Método para verificar a expressao no médotodo AssStat*/
  private boolean checkAssignment(Type varType,Type exprType) {

    if (varType == Type.undefinedType || exprType == Type.undefinedType ) {
      return true;
    } else {
      return varType == exprType;
    }
  }

  /* Expr ::= ExprAnd { ”or” ExprAnd } - OK*/
  private Expr expr() {

    Expr ExpEsq, ExpDir;
    ExpEsq = ExprAnd();

    while (lexer.token == Symbol.OR) {
      lexer.nextToken();
      ExpDir = ExprAnd();

      if (!checkBooleanExpr(ExpEsq.getType(), ExpDir.getType())) {
        error.signal("Expressao do tipo 'Boolean' esperada");
      }
      ExpEsq = new CompositeExpr(ExpEsq, Symbol.OR, ExpDir);
    }
    return ExpEsq;
  }

  /*Método para verificar a expressao é booleana*/
  private boolean checkBooleanExpr(Type left, Type right) {
    if (left == Type.undefinedType || right == Type.undefinedType) {
      return true;
    } else {
      return left == Type.booleanType && right == Type.booleanType;
    }
  }

  /* ExprAnd ::= ExprRel { ”and” ExprRel } - OK*/
  private Expr ExprAnd() {

    Expr ExpEsq, ExpDir;
    ExpEsq = ExprRel();

    while (lexer.token == Symbol.AND) {
      lexer.nextToken();
      ExpDir = ExprRel();

      if (!checkBooleanExpr(ExpEsq.getType(), ExpDir.getType())) {
        error.signal("Expressão do tipo 'Boolean' esperada");
      }

      ExpEsq = new CompositeExpr(ExpEsq, Symbol.AND, ExpDir);
    }
    return ExpEsq;
  }

  /*Método para verificar a expressao é booleana*/
  private boolean checkRelExpr(Type left, Type right) {
    if (left == Type.undefinedType || right == Type.undefinedType) {
      return true;
    } else {
      return left == right;
    }
  }

  /* ExprRel ::= ExprAdd [ RelOp ExprAdd ] - OK*/
  private Expr ExprRel() {

    Expr ExpEsq, ExpDir = null;
    ExpEsq = ExprAdd();
    Symbol Op = lexer.token;

    if(Op == Symbol.EQ || Op == Symbol.NEQ || Op == Symbol.LE || Op == Symbol.LT || Op == Symbol.GE || Op == Symbol.GT ) {
      lexer.nextToken();
      ExpDir = ExprAdd();

      if (!checkRelExpr(ExpEsq.getType(), ExpDir.getType())) {
        error.signal("Tipo de expressões incompátiveis para relação");
      }

      ExpEsq = new CompositeExpr(ExpEsq, Op, ExpDir);
    }
    return ExpEsq;
  }

  /* ExprAdd ::= ExprMult {(” + ” | ” − ”)ExprMult} - OK*/
  private Expr ExprAdd() {

    Symbol Op = null;
    Expr ExpEsq, ExpDir = null;
    ExpEsq = ExprMult();

    while (lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS) {
      Op = lexer.token;
      lexer.nextToken();
      ExpDir = ExprMult();

      if (!checkMathExpr(ExpEsq.getType(), ExpDir.getType())) {
        error.signal("Expressão do tipo inteiro esperada para realizar a operação");
      }

      ExpEsq = new CompositeExpr(ExpEsq, Op, ExpDir);
    }
    return ExpEsq;
  }

  private boolean checkMathExpr(Type left, Type right) {
    boolean orLeft = left == Type.integerType || left == Type.undefinedType;
    boolean orRight = right == Type.integerType || right == Type.undefinedType;
    return (orLeft && orRight);
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

      if (!checkMathExpr(ExpEsq.getType(), ExpDir.getType())) {
        error.signal("Expressão do tipo inteiro esperada para realizar a operação");
      }

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

      if (ExpDir.getType() != Type.integerType) {
        error.signal("Expressão do tipo inteiro é esperado para essa operação");
      }

      expressao = new CompositeExpr(ExpDir, Op, ExpDir);
      return expressao;

    } else if(lexer.token == Symbol.MINUS){
      Op = Symbol.MINUS;
      lexer.nextToken();
      ExpDir = ExprPrimary();

      if (ExpDir.getType() != Type.integerType) {
        error.signal("Expressão do tipo inteiro é esperado para essa operação");
      }

      expressao = new CompositeExpr(ExpDir, Op, ExpDir);
      return expressao;
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
      case LITERALINT :
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
        lexer.nextToken();
        if(lexer.token == Symbol.LEFTPAR) {
          Expr funcao = funccall(nome);
          return funcao;
        } else {
          /* Analise semantica para uma variavel*/
          if (symbolTable.getInLocal(nome) == null) {
            error.signal("O identificador '" + nome + "' não foi declarado");
            symbolTable.putInLocal(nome, new Variable(nome, Type.undefinedType));
          }

          Variable id = (Variable) symbolTable.getInLocal(nome);
          return new VariableExpr(id);
        }
      default :
        error.signal("É esperado uma expressão");
    }
    return null;
  }

  /* Retorna um numero em forma de expressao */
  private NumberExpr number() {
    NumberExpr e = null;
    int value = lexer.getNumberValue();
    return new NumberExpr(value);
  }

  /* FuncCall ::= Id "(" [ Expr {”, ”Expr} ] ")" */
  private Expr funccall(String id) {

    ExprList exprList = null;
    int numParam = 0;
    int numParamFunc = 0;
    int i=0;

    /* Analise semantica */
    Func f = (Func) symbolTable.getInGlobal(id);
    if ( f == null ) {
      error.signal("Função '" + id + "' não declarada");
    }

  	if (lexer.token == Symbol.LEFTPAR) {

      lexer.nextToken();
      Symbol Op = lexer.token;

      if(Op == Symbol.OR || Op == Symbol.AND || Op == Symbol.EQ ||
         Op == Symbol.NEQ || Op == Symbol.LE || Op == Symbol.LT ||
         Op == Symbol.GE || Op == Symbol.GT || Op == Symbol.MINUS ||
         Op == Symbol.PLUS || Op == Symbol.IDENT || Op == Symbol.TRUE ||
         Op == Symbol.FALSE || Op == Symbol.LITERALINT || Op == Symbol.LITERALSTRING) {

         exprList = exprlist();

          /* analise semantica */
          if (f != null) {
            numParam = exprList.getTamanho();
            numParamFunc = f.getNumParam();

            if (numParam != numParamFunc) {
              error.signal("Chamada de da função '" + id + "' sendo feita com '" + numParam + "' ao invez de '" + numParamFunc + "' parametro(s)");
            }
          }

  		   if (lexer.token != Symbol.RIGHTPAR) {
  			   error.signal("É esperado ')'");
  		   }

      	 	/* Análise Semântica - Verificar se o tipo passado para a função bate com o tipo esperado */
      		for(i=0; (i<numParam) && (i<numParamFunc); i++){

            Type esperado = f.getParamList().getArray(i).getType();
            Type encontrado = exprList.getExpr(i).getType();

      			if (encontrado.getName() != esperado.getName()) {
      				error.signal("Parâmetro com expressao do tipo '" + esperado.getName() + "' é esperado, porem o tipo encontrado foi '" + encontrado.getName() + "'");
      			}
      		}

         lexer.nextToken();
         return new FuncCall(f, exprList);

      }
    }

    if (lexer.token != Symbol.RIGHTPAR) {
      error.signal("É esperado ')'");

    } else {
      lexer.nextToken();
    }
    return null;
  }

  /* Método para printar uma variavel*/
  private Write write() {

    ExprList exprList = null;

  	if (lexer.token == Symbol.LEFTPAR) {
      lexer.nextToken();
      Symbol Op = lexer.token;

      if(Op == Symbol.OR || Op == Symbol.AND || Op == Symbol.EQ ||
         Op == Symbol.NEQ || Op == Symbol.LE || Op == Symbol.LT ||
         Op == Symbol.GE || Op == Symbol.GT || Op == Symbol.MINUS ||
         Op == Symbol.PLUS || Op == Symbol.IDENT || Op == Symbol.TRUE ||
         Op == Symbol.FALSE || Op == Symbol.LITERALINT || Op == Symbol.LITERALSTRING) {

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

  /* Método para printar uma variavel e pular linha*/
  private Writeln writeln() {

    ExprList exprList = null;

  	if (lexer.token == Symbol.LEFTPAR) {
      lexer.nextToken();
      Symbol Op = lexer.token;

      if(Op == Symbol.OR || Op == Symbol.AND || Op == Symbol.EQ ||
         Op == Symbol.NEQ || Op == Symbol.LE || Op == Symbol.LT ||
         Op == Symbol.GE || Op == Symbol.GT || Op == Symbol.MINUS ||
         Op == Symbol.PLUS || Op == Symbol.IDENT || Op == Symbol.TRUE ||
         Op == Symbol.FALSE || Op == Symbol.LITERALINT || Op == Symbol.LITERALSTRING) {

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

  /* Método para verificar uma variavel */
  private Read read() {

    Variable v = null;
    String nome = "";
    Symbol Op = lexer.token;

    lexer.nextToken();
    if (lexer.token != Symbol.LEFTPAR) {
      error.signal("É esperado (");
    }

    lexer.nextToken();

    if (lexer.token != Symbol.IDENT && Op == Symbol.READINT) {
      error.signal("É esperado um identificador na funcao readInt");

    } else if(lexer.token != Symbol.IDENT && Op == Symbol.READSTRING) {
      error.signal("É esperado um identificador na funcao readString");

    } else {

      nome = (String) lexer.getStringValue();
      v = (Variable) symbolTable.getInLocal(nome);

      if (v == null && Op == Symbol.READINT) {
        error.signal("Identificador com o nome '" + nome + "' não foi declarado na funcao 'readInt'");
        symbolTable.putInLocal(nome, new Variable(nome, Type.undefinedType));

      } else if (v == null && Op == Symbol.READSTRING) {
        error.signal("Identificador com o nome '" + nome + "' não foi declarado na funcao 'readString'");
        symbolTable.putInLocal(nome, new Variable(nome, Type.undefinedType));

      } else if (v.getType() != Type.stringType && v.getType() != Type.integerType && v.getType() != Type.undefinedType) {
        error.signal("O identificador tem que ser do tipo 'String' ou 'Int'");
      }

      lexer.nextToken();
    }

    if (lexer.token != Symbol.RIGHTPAR) {
      error.signal("É esperado )");
      lexer.nextToken();
      return null;
    }

    if(v != null) {
      lexer.nextToken();
      return new Read(v);
    }

    return null;
  }

  private Type type() {
    Type result;

    switch ( lexer.token ) {
      case LITERALINT :
        result = Type.integerType;
        break;
      case LITERALBOOLEAN :
        result = Type.booleanType;
        break;
      case LITERALSTRING :
        result = Type.stringType;
        break;
      default :
        result = null;
        error.signal("É esperado um Tipo válido");
    }
    return result;
  }

  public boolean getFlag() {
    return error.getFlag();
  }

  private SymbolTable symbolTable;
  private Lexer lexer;
  private CompilerError error;
  private Func funcao_atual;
  private int num_est;
  private int numero_retornos;
}
