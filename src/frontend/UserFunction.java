package frontend;

import java.util.LinkedList;
import java.util.List;

public class UserFunction extends Function {
  private ExpressionContainer exps;
  private DummyVariable[] func_args;
  private DummyVariable return_var;
  private boolean ret_assigned = false;
  private boolean side_effects = false;

  private List<DummyVariable> declared =
    new LinkedList<DummyVariable>();
  
  private static UserFunction caller = null;
  private static int call_depth = 0;
  
  public UserFunction
    ( String name,
      Type[] types,
      ExpressionContainer c,
      DummyVariable[] an_arr ){
    super( name, types );
    set_body( c );
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
    if( exps != null )
      side_effects = has_side_effects_helper( exps );
  }

  public boolean has_side_effects(){
    return side_effects;
  }

  public boolean has_side_effects_helper( Statement s ){
    if( s instanceof AssignmentExp ){
      AssignmentExp ass = (AssignmentExp)s;
      AbstractVariable dest = ass.dest();
      if( dest != null ){
	if( dest.call_depth() == 0 ) // assignment to a global variable
	  return true;
	for( DummyVariable v : func_args ){
	  // assignment to an argument of this function
	  if( v == dest ){
	    return true;
	  }
	}
      }
    }
    for( Statement child : s.children() ){
      if( has_side_effects_helper(child) ){
	return true;
      }
    }
    return false;
  }

  public static int call_depth(){
    return call_depth;
  }
  
  public AbstractVariable compile_func
    ( Variable[] args,
      Statement owner ) throws CompileException {

    UserFunction.call_depth++;
    
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

    UserFunction.call_depth--;
    
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
