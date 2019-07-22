package AST;

public class StringType extends Type {

  public StringType() {
    super("char[]");
  }

  public String getCname() {
    return "char[]";
  }
}
