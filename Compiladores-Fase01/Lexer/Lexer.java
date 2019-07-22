package Lexer;
import java.util.*;
import Error.*;

public class Lexer {

  public Lexer( char []input, CompilerError error ) {
    this.input = input;

    // add an end-of-file label to make it easy to do the lexer
    input[input.length - 1] = '\0';

    // number of the current line
    lineNumber = 1;
    tokenPos = 0;
    this.error = error;
  }

  // contains the keywords
  static private Hashtable<String, Symbol> keywordsTable;

  // this code will be executed only once for each program execution
  static {

    keywordsTable = new Hashtable<String, Symbol>();
    keywordsTable.put( "var", Symbol.VAR );
    keywordsTable.put( "begin", Symbol.BEGIN );
    keywordsTable.put( "end", Symbol.END );
    keywordsTable.put( "if", Symbol.IF );
    keywordsTable.put( "then", Symbol.THEN );
    keywordsTable.put( "else", Symbol.ELSE );
    keywordsTable.put( "endif", Symbol.ENDIF );
    keywordsTable.put( "read", Symbol.READ );
    keywordsTable.put( "write", Symbol.WRITE );
    keywordsTable.put( "writeln", Symbol.WRITELN );
    keywordsTable.put( "Boolean", Symbol.BOOLEAN );
    keywordsTable.put( "function", Symbol.FUNCTION );
    keywordsTable.put( "char", Symbol.CHAR );
    keywordsTable.put( "true", Symbol.TRUE );
    keywordsTable.put( "false", Symbol.FALSE );
    keywordsTable.put( "and", Symbol.AND );
    keywordsTable.put( "or", Symbol.OR );
    keywordsTable.put( "not", Symbol.NOT );
    keywordsTable.put( "Int", Symbol.INTEGER );
    keywordsTable.put( "String", Symbol.STRING );
    keywordsTable.put( "return", Symbol.RETURN );
    keywordsTable.put( "while", Symbol.WHILE );
  }

  public void nextToken() {

    char ch;
    while ( (ch = input[tokenPos]) == ' ' || ch == '\r' || ch == '\t' || ch == '\n') {
      //Conta o numero de linhas (util para mostrar a linha do erro)
      if ( ch == '\n') {
        lineNumber++;
      }
      tokenPos++;
    }

    //Caso for encontrado o final do arquivo
    if ( ch == '\0') {
      token = Symbol.EOF;

    } else {

      //Caso a entrada for um comentario
      if ( input[tokenPos] == '/' && input[tokenPos + 1] == '/' ) {

        //Comentario encontrado
        while ( input[tokenPos] != '\0' && input[tokenPos] != '\n' ) {
          tokenPos++;
        }
        nextToken();

      } else {

        if(ch == '"') {

          StringBuffer palavra = new StringBuffer();
          palavra.append(ch);
          tokenPos++;
          while(input[tokenPos] != '"') {
            palavra.append(input[tokenPos]);
            tokenPos++;
          }

          if(input[tokenPos] == '"') {
            palavra.append('"');
          } else {
            error.signal("Existe algum caracter que não é letra na String");
          }

          tokenPos++;
          stringValue = palavra.toString();
          token = Symbol.LITERALSTRING;

        } else if (Character.isLetter( ch )) {

          //Recupera o identficador ou a letra
          StringBuffer ident = new StringBuffer();
          while (Character.isLetter( input[tokenPos])) {
            ident.append(input[tokenPos]);
            tokenPos++;
          }

          stringValue = ident.toString();

          //Verifica se o idetificador nao é uma palavra chave
          Symbol value = keywordsTable.get(stringValue);

          if (value == null) {
            token = Symbol.IDENT;
          } else {
            token = value;
          }

          //Caso a letra for seguida de um número
          if ( Character.isDigit(input[tokenPos]) ) {
            error.signal("A letra é seguida de um número");
          }
        } else if (Character.isDigit( ch )) { //Caso a entrada for um digito

          //Recupera o numero
          StringBuffer number = new StringBuffer();
          while (Character.isDigit( input[tokenPos])) {
            number.append(input[tokenPos]);
            tokenPos++;
          }
          token = Symbol.NUMBER;

          //Caso for um número que estou o limite de memoria
          try {
            numberValue = Integer.valueOf(number.toString()).intValue();

          } catch ( NumberFormatException e ) {
            error.signal("Foi ultrapassado o limite do número!");
          }

          //Caso ultrapasse o limite estipulado na gramatica
          if (numberValue > MaxValueInteger) {
            error.signal("Foi ultrapassado o limite do número!");
          }

          //Caso seja menor que o numero minimo estipulado na gramatica
          if(numberValue < MinValueInteger) {
            error.signal("O número é menor que o mínimo!");
          }

        //Caso o token não for um digito
        } else {

          tokenPos++;
          switch (ch) {
            case '+' :
              token = Symbol.PLUS;
              break;
            case '-' :
              if (input[tokenPos] == '>') {
                tokenPos++;
                token = Symbol.ARROW;
              } else {
                token = Symbol.MINUS;
              }
              break;
            case '*' :
              token = Symbol.MULT;
              break;
            case '/' :
              token = Symbol.DIV;
              break;
            case '%' :
              token = Symbol.REMAINDER;
              break;
            case '<' :
              if (input[tokenPos] == '=' ) {
                tokenPos++;
                token = Symbol.LE;
              } else if (input[tokenPos] == '>') {
                tokenPos++;
                token = Symbol.NEQ;

              } else {
                token = Symbol.LT;
              }
              break;
            case '>' :
              if (input[tokenPos] == '=') {
                tokenPos++;
                token = Symbol.GE;
              } else {
                token = Symbol.GT;
              }
              break;
            case '=' :
              if ( input[tokenPos] == '=' ) {
                tokenPos++;
                token = Symbol.EQ;
              } else {
                token = Symbol.ASSIGN;
              }
              break;
            case '(' :
              token = Symbol.LEFTPAR;
              break;
            case ')' :
              token = Symbol.RIGHTPAR;
              break;
            case '{' :
              token = Symbol.LEFTKEY;
              break;
            case '}' :
              token = Symbol.RIGHTKEY;
              break;
            case ',' :
              token = Symbol.COMMA;
              break;
            case ';' :
              token = Symbol.SEMICOLON;
              break;
            case ':' :
              token = Symbol.COLON;
              break;
            case '\'' :
              token = Symbol.CHARACTER;
              charValue = input[tokenPos];
              tokenPos++;
              if ( input[tokenPos] != '\'' ) {
                error.signal("Ilegal Caractere Literal" + input[tokenPos-1] );
              }
              tokenPos++;
              break;
            default :
              error.signal("Caracter Inválido: ’" + ch + "’");
          }
        }
      }
    }
    lastTokenPos = tokenPos - 1;
  }

  //Retorna o numero da linha do ultimo last token com getToken()
  public int getLineNumber() {
    return lineNumber;
  }

  public String getCurrentLine() {
    int i = lastTokenPos;
    if ( i == 0 ) {
      i = 1;

    } else {
      if ( i >= input.length ) {
        i = input.length;
      }
    }
    StringBuffer line = new StringBuffer();

    // go to the beginning of the line
    while ( i >= 1 && input[i] != '\n' ) {
      i--;
    }

    if ( input[i] == '\n' ) {
      i++;
    }

    // go to the end of the line putting it in variable line
    while ( input[i] != '\0' && input[i] != '\n' && input[i] != '\r' ) {
      line.append( input[i] );
      i++;
    }
    return line.toString();
  }

  //Caso for uma string, retorna a string
  public String getStringValue() {
    return stringValue;
  }

  //Caso for um numero, retorna o numero
  public int getNumberValue() {
    return numberValue;
  }

  //Caso for um char, retorna o char
  public char getCharValue() {
    return charValue;
  }

  // current token
  public Symbol token;
  private String stringValue;
  private int numberValue;
  private char charValue;
  private int tokenPos;

  // input[lastTokenPos] is the last character of the last token
  private int lastTokenPos;

  // program given as input - source code
  private char []input;

  // number of current line. Starts with 1
  private int lineNumber;
  private CompilerError error;
  private static final int MinValueInteger = 0;
  private static final int MaxValueInteger = 2147483647;
}
