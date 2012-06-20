package frontend;

public class UserFunction extends Function {
  private ExpressionContainer exps;
  private FunctionArgument[] func_args;
  private AbstractVariable return_var;

  private static UserFunction caller = null;
  
  public UserFunction( String name, Type[] types, ExpressionContainer c, FunctionArgument[] an_arr ){
    super( name, types, types.length );
    exps = c;
    func_args = an_arr;
    return_var = new FunctionArgument( name );
  }

  public AbstractVariable compile_func( AbstractVariable[] args, Statement owner ) throws CompileException {
    for( int i = 0; i < func_args.length; i++){
      func_args[i].setVar( args[i] );
    }
    UserFunction prev = caller;
    caller = this;
    exps.compile();
    caller = prev;
    return return_var;
  }

  public AbstractVariable returning_var(){
    return return_var;
  }

  public static class ReturnExpression extends Statement {
    private Expression val;
    public ReturnExpression( int line, Expression aval ){
      super( line );
      val = aval;
    }
    public void compile() throws CompileException {
      if( caller == null ) {
	throw error( "Cannot return when not in a function" );
      }
      val.compile();
      caller.returning_var().compile_assignment( val.returnVar(), this );
    }
  }
}