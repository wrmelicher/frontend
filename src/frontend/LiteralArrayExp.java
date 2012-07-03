package frontend;

import java.util.List;
import java.util.LinkedList;

public class LiteralArrayExp extends Expression {
  private List<Expression> exps;
  private ArrayVariable out;
  public LiteralArrayExp( int line, List<Expression> vals ){
    super( line );
    exps = vals;
  }
  public void compile() throws CompileException {
    List<AbstractVariable> vars = new LinkedList<AbstractVariable>();
    for( Expression e : exps ){
      e.compile();
      vars.add( e.returnVar() );
    }
    out = ArrayVariable.literal( vars, this );
  }
  public AbstractVariable returnVar(){
    return out;
  }
}
