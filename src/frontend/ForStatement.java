package frontend;

import java.util.List;
import java.io.PrintStream;
import frontend.functions.AddFunction;

class ForStatement extends ExpressionContainer {
  private DummyVariable loop_var;
  private Expression from;
  private Expression to;
  private Expression incExp;
  private Expression step;

  public ForStatement( int line,
		       Expression lower,
		       Expression upper,
		       String loop_name,
		       Expression st ){
    super( line );
    from = lower;
    to = upper;
    step = st;
    loop_var = new DummyVariable( loop_name );
    
    FunctionExp incFuncExp = new FunctionExp
      ( getLine(), AddFunction.NAME,
	new Expression[] {
	new VariableExp( getLine(), loop_var ),
	step 
      } );
    incExp = new AssignmentExp( getLine(), loop_var, incFuncExp );

    add_child( from );
    add_child( to );
    add_child( incExp );
  }
  public AbstractVariable getLoopVar(){
    // TODO: do not allow assignment to the loop variable
    return loop_var;
  }
  public void compile() throws CompileException {
    from.compile();
    to.compile();
    step.compile();
    int from_val = Variable.get_val_from_var( from.returnVar(), this );
    int to_val = Variable.get_val_from_var( to.returnVar(), this );
    int step_val = Variable.get_val_from_var( step.returnVar(), this );
    Variable const_var = new Variable( new IntTypeData( from_val ) );
    loop_var.push_var( const_var );
    for( int i = from_val; i < to_val; i += step_val ){
      super.compile();
      incExp.compile();
    }
  }
}
