package frontend;
import java.io.PrintStream;

import java.util.List;
import java.util.LinkedList;

public class FunctionExp extends Expression {
  private Function func = null;
  private String name;
  private Expression[] exps;
  public FunctionExp
    ( int line,
      String aname,
      Expression[] args ){
    super( line );
    name = aname;
    exps = args;
    for( Expression e : args ){
      e.setParent( this );
    }
  }
  @Override
  public ExpSignature sig() throws CompileException {
    ExpSignature ans = new ExpSignature
      ( ExpSignature.ExpressionType.FUNCTION );

    ans.depends( func() );
    for( Expression e : exps ){
      ans.depends( e );
    }

    return ans;
  }

  @Override
  public boolean has_side_effects(){
    try{
      return func().has_side_effects();
    } catch( CompileException e ){
      ProgramTree.error.println( e.getMessage() );
      return false;
    }
  }

  private Function func() throws CompileException {
    if( func == null ){
      func = Function.match( name, exps.length, this );
    }
    return func;
  }

  @Override
  public void compile_exp() throws CompileException {
    AbstractVariable[] vargs =
      new AbstractVariable[ exps.length ];
    int i = 0;
    for( Expression e : exps ){
      e.compile();
      vargs[i++] = e.returnVar();
    }
    AbstractVariable v = Function.call( func(), vargs, this );
    set_ret( v );
  }

}
