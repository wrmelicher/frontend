package frontend;

import java.util.List;
import java.util.LinkedList;

public class LiteralArrayExp extends Expression {
  public LiteralArrayExp
    ( int line,
      List<Expression> vals ){
    super( line );
    for( Expression e : vals ){
      add_child( e );
    }
  }

  public boolean has_side_effects(){
    return false;
  }

  protected ExpSignature.ExpressionType type(){
    return ExpSignature.ExpressionType.LITERALARRAY;
  }
  
  public void compile_exp() throws CompileException {
    List<AbstractVariable> vars
      = new LinkedList<AbstractVariable>();
    for( Statement s : children() ){
      Expression e = (Expression)s;
      e.compile();
      vars.add( e.returnVar() );
    }
    set_ret( ArrayVariable.literal( vars, this ) );
  }
}
