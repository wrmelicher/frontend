package frontend.functions;

import java.math.BigInteger;
import frontend.*;

public class NotFunction extends PrimitiveFunction {
  public static final String NAME = "~";
  public NotFunction(){
    super(NAME, new Type[] { Type.IntType }, "not" );
  }
  public abstract TypeData data_type
    ( TypeData[] types ) throws CompileException{
   return IntTypeData.not( (IntTypeData) types[0] );
  }
}
