package frontend.functions;

import frontend.*;

public class IncFunction extends Function {
  public static final String NAME = "plusone";
  public static final Variable<IntTypeData> ONE =
    new Variable<IntTypeData>( new IntTypeData( 1 ) );
  public IncFunction(){
    super(NAME, new Type[] { Type.IntType } );
  }
  public AbstractVariable compile_func
    ( Variable[] args,
      Statement owner ) throws CompileException {
    return Function.call( AddFunction.NAME,
			  new Variable[]{
			    args[0],
			    ONE
			  },
			  owner );
  }
}
