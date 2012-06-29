package frontend.functions;

import frontend.*;

public class GreaterThanFunction extends Function {
  public static final String NAME = ">";
  public GreaterThanFunction(){
    super( NAME, new Type[] { Type.IntType, Type.IntType }, 2 );
  }
  public AbstractVariable compile_func( Variable[] args, Statement owner ) throws CompileException{
    return Function.call( LessThanFunction.NAME,
			  new Variable[] { args[1], args[0] },
			  owner );
  }
}