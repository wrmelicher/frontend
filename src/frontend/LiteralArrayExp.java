package frontend;

import java.util.List;
import java.util.LinkedList;

public class LiteralArrayExp extends Expression {
  private List<Expression> exps;
  public LiteralArrayExp
    ( int line,
      List<Expression> vals ){
    super( line );
    exps = vals;
    for( Expression e : vals ){
      e.setParent( this );
    }
  }

  public boolean has_side_effects(){
    return false;
  }

  public ExpSignature sig(){
    ExpSignature ans = new ExpSignature
      ( ExpSignature.ExpressionType.LITERALARRAY );

    for( Expression e : exps ){
      ans.depends( e );
    }
    return ans;
  }
  
  public void compile_exp() throws CompileException {
    List<AbstractVariable> vars
      = new LinkedList<AbstractVariable>();
    for( Expression e : exps ){
      e.compile();
      vars.add( e.returnVar() );
    }
    set_ret( ArrayVariable.literal( vars, this ) );
  }
}
