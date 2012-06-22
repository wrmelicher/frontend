package frontend;

import java.util.List;
import java.io.PrintStream;

class ForStatement extends ExpressionContainer {
  private FunctionArgument loop_var;
  private Expression from;
  private Expression to;
  private Expression incExp;

  public ForStatement( int line, Expression lower, Expression upper, String loop_name ){
    super( line );
    from = lower;
    to = upper;
    loop_var = new FunctionArgument( loop_name );
    
    FunctionExp incFuncExp = new FunctionExp
      ( getLine(), Function.IncFunction.NAME, new Expression[] { new VariableExp( getLine(), loop_var ) } );
    incExp = new AssignmentExp( getLine(), loop_var, incFuncExp );
  }
  public AbstractVariable getLoopVar(){
    // TODO: do not allow assignment to the loop variable
    return loop_var;
  }
  public void compile() throws CompileException {
    from.compile();
    to.compile();
    int from_val = Variable.get_val_from_var( from.returnVar(), this );
    int to_val = Variable.get_val_from_var( to.returnVar(), this );
    loop_var.setVar( new Variable( new IntTypeData( from_val ) ) );
    
    for( int i = from_val; i < to_val; i++ ){
      super.compile();
      incExp.compile();
    }
  }

  
}