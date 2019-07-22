package Error;

import Lexer.*;
import java.io.*;

public class CompilerError {

  public CompilerError( PrintWriter out, String arquivo_entrada ) {
    this.arquivo = arquivo_entrada;
    this.out = out;
    this.flag = true;
  }

  public void setLexer( Lexer lexer ) {
    this.lexer = lexer;
  }

  public void signal( String strMessage ) {

    this.flag = false;
    out.println(arquivo + " : " + lexer.getLineNumber() + " : " + strMessage);
    out.println(lexer.getCurrentLine());
    out.println();

    if ( out.checkError() ) {
      System.out.println("Error in signaling an error");
    }

  }

  public Boolean getFlag() {
    return flag;
  }

  private Boolean flag;
  private String arquivo;
  private Lexer lexer;
  PrintWriter out;
}
