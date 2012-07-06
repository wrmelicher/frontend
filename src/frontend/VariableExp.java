package frontend;
import java.io.PrintStream;
public class VariableExp extends Expression
  implements Changer {
  
  private AbstractVariable var;
  public VariableExp( int line, AbstractVariable v ){
    super( line );
    var = v;
    var.notify_changes( this );
  }

  protected ExpSignature.ExpressionType type(){
    return ExpSignature.ExpressionType.VARIABLE;
  }  

  public boolean has_side_effects(){
    return false;
  }

  public ExpSignature sig() throws CompileException {
    ExpSignature ans = super.sig();
    ans.depends( var.var().snapshot() );
    return ans;
  }

  public AbstractVariable var(){
    return var;
  }

  public void compile() throws CompileException {
    compile_exp();
  }
  
  public void compile_exp() throws CompileException {
    set_ret( var );
  }
}
