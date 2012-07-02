package frontend;
import java.io.PrintStream;

import java.util.List;
import java.util.LinkedList;

public class FunctionExp extends Expression {
  private String name;
  private AbstractVariable outvar;
  private Expression[] exps;
  public FunctionExp
    ( int line,
      String afunc,
      Expression[] args ){
    super( line );
    name = afunc;
    exps = args;
  }
  public AbstractVariable returnVar(){
    return outvar;
  }
  public void compile() throws CompileException {
    AbstractVariable[] vargs =
      new AbstractVariable[ exps.length ];
    int i = 0;
    for( Expression e : exps ){
      e.compile();
      vargs[i++] = e.returnVar();
    }
    outvar = Function.call( name, vargs, this );
  }

}
