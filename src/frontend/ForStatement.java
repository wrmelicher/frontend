package frontend;

import java.util.List;
import java.io.PrintStream;

class ForStatement extends ExpressionContainer {
  private Variable<IntTypeData> loop_var;
  private int from;
  private int to;
  private Expression incExp;

  public ForStatement( int line, int lower, int upper, String loop_name ){
    super( line );
    from = lower;
    to = upper;
    loop_var = new Variable<IntTypeData>( loop_name, new IntTypeData( from ) );
    
    FunctionExp incFuncExp = new FunctionExp
      ( getLine(), Function.IncFunction.NAME, new Expression[] { new VariableExp( getLine(), loop_var ) } );

    incExp = new AssignmentExp( getLine(), loop_var, incFuncExp );
  }
  public Variable<IntTypeData> getLoopVar(){
    return loop_var;
  }
  public void compile() throws CompileException {
    for( int i = from; i < to; i++ ){
      super.compile();
      incExp.compile();
    }
  }

  
}