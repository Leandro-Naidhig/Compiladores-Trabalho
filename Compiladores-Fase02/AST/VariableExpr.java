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
