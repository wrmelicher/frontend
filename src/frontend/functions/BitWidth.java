package frontend.functions;

import frontend.*;

public class BitWidth extends Function {
  public static final String NAME = "bit_width";
  public BitWidth(){
    super(NAME,new Type[] {Type.ANYTYPE} );
  }
  public Variable compile_func( Variable[] args, Statement owner ) throws CompileException {
    Variable ans = new Variable
      ( new IntTypeData( args[0].getData().bit_count() ) );
    return ans;
  }
}
