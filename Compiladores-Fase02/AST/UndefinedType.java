package AST;
import java.io.*;

public class UndefinedType extends Type {
  
  public UndefinedType() {
    super("undefined");
  }

  public String getCname() {
    return "int";
  }
}
