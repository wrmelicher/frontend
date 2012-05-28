package frontend;
import java.io.PrintStream;
public class VariableExp extends Expression {
  Variable var;
  public VariableExp( int line, Variable v ){
    super( line );
    var = v;
  }
  public Variable returnVar(){
    return var;
  }
  public void compile( PrintStream os ) throws CompileException {
    // empty because all computation is already done
  }
  public String outputName(){
    return var.cur_name();
  }
}