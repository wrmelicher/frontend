package frontend;

import java.util.LinkedList;
import java.util.List;

public class UserFunction extends Function {
  private ExpressionContainer exps;
  private DummyVariable[] func_args;
  private DummyVariable return_var;

  private static UserFunction caller = null;
  private boolean ret_assigned = false;

  private List<DummyVariable> declared =
    new LinkedList<DummyVariable>();
  
  public UserFunction
    ( String name,
      Type[] types,
      ExpressionContainer c,
      DummyVariable[] an_arr ){
    super( name, types, types.length );
    exps = c;
    func_args = an_arr;
    return_var = new DummyVariable( name );
  }

  public UserFunction
    ( String name,
      Type[] types,
      DummyVariable[] an_arr ){
    this( name, types, null, an_arr );
  }

  public void set_body( ExpressionContainer c ){
    exps = c;
  }
  
  public AbstractVariable compile_func
    ( Variable[] args,
      Statement owner ) throws CompileException {
    boolean prev_ret = ret_assigned;
    ret_assigned = false;
    
    for( int i = 0; i < func_args.length; i++){
      func_args[i].push_var( args[i] );
    }

    for( DummyVariable v : declared ){
      v.start_func();
    }
    
    UserFunction prev = caller;
    caller = this;
    exps.compile();
    caller = prev;

    for( int i = 0; i < func_args.length; i++ ){
      func_args[i].pop_var();
    }

    Variable ans = return_var.var();
    
    for( DummyVariable v : declared ){
      v.exit_func();
    }

    ret_assigned = prev_ret;
    
    return ans;
  }

  public void register_defar( DummyVariable v ){
    declared.add( v );
  }
  
  public DummyVariable returning_var(){
    return return_var;
  }

  public static class ReturnExpression extends Statement {
    private Expression val;
    public ReturnExpression( int line, Expression aval ){
      super( line );
      val = aval;
    }
    public void compile() throws CompileException {
      if( caller == null ){
	throw error( "Cannot return when not in a function" );
      }
      val.compile();
      Variable ret_var = val.returnVar().var();
      if( !caller.ret_assigned ){
	caller.ret_assigned = true;
	caller.returning_var().push_var( ret_var );
      }
      caller.returning_var().var().compile_assignment( ret_var, this );
    }
  }
}
