package frontend;

import java.util.List;
import java.io.PrintStream;
import frontend.functions.IncFunction;

class ForStatement extends ExpressionContainer {
  private DummyVariable loop_var;
  private Expression from;
  private Expression to;
  private Expression incExp;

  public ForStatement( int line, Expression lower, Expression upper, String loop_name ){
    super( line );
    from = lower;
    to = upper;
    loop_var = new DummyVariable( loop_name );
    
    FunctionExp incFuncExp = new FunctionExp
      ( getLine(), IncFunction.NAME, new Expression[] { new VariableExp( getLine(), loop_var ) } );
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
    Variable const_var = new Variable( new IntTypeData( from_val ) );
    loop_var.push_var( const_var );
    
    for( int i = from_val; i < to_val; i++ ){
      super.compile();
      incExp.compile();
    }
  }
}