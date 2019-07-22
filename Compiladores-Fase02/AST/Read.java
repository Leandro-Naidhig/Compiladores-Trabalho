package AST;
import java.io.*;

public class Read extends Expr{

  public Read(Variable id) {
    this.id = id;
  }

  public void genC( PW pw ) {

    if (id.getType() == Type.stringType ) {
      pw.println("char s[1000];");
      pw.println("gets(s);");
      pw.println("sscanf(s, \"%s\", &" + id.getName() + ");");

    } else if (id.getType() == Type.integerType) {
      pw.println("scanf(\"%d\", &" + id.getName() + ");");
    }
  }

  public Type getType() {
    return id.getType();
  }

  private Variable id;
}
