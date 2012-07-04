package frontend;
import java.io.PrintStream;
public class VariableExp extends Expression {
  private AbstractVariable var;
  public VariableExp( int line, AbstractVariable v ){
    super( line );
    var = v;
    var.set_changed( this );
  }
  

  public boolean has_side_effects(){
    return false;
  }

  public ExpSignature sig(){
    ExpSignature ans = new ExpSignature
      ( ExpSignature.ExpressionType.VARIABLE );

    ans.depends( var.var().snapshot() );

    return ans;
  }
  
  public void compile_exp() throws CompileException {
    // empty because all computation is already done
    set_ret( var );
  }
}
