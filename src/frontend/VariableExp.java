package frontend;
import java.io.PrintStream;
public class VariableExp extends Expression {
  AbstractVariable var;
  public VariableExp( int line, AbstractVariable v ){
    super( line );
    var = v;
  }
  public AbstractVariable returnVar(){
    return var;
  }
  public void compile() throws CompileException {
    // empty because all computation is already done
  }
}