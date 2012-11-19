package frontend.functions;

import frontend.*;

public class Negate extends PrimitiveFunction {
  public static final String NAME = "-";
  public Negate(){
    super("-", new Type[] { Type.IntType }, "negate" );
  }
  public abstract TypeData data_type
    ( TypeData[] types ) throws CompileException{
   return IntTypeData.negate( (IntTypeData) types[0] );  
  }
}
