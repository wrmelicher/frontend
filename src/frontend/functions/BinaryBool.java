package frontend.functions;

import frontend.*;
import java.io.PrintStream;

public abstract class BinaryBool extends PrimitiveFunction {
  
  public BinaryBool(String name, String op){
    super( name, new Type[] {Type.BoolType, Type.BoolType}, op );
  }
  public abstract TypeData data_type
    ( TypeData[] types ) throws CompileException {
    return data_out( (BoolData)types[0], (BoolData)types[1] );
  }

  protected abstract BoolData data_out(BoolData a, BoolData b);
}
