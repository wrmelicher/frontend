package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public abstract class BinaryInt extends Function {
  public BinaryInt(String name, String op ){
    super( name, new Type[] { Type.IntType, Type.IntType}, op );
  }

  public TypeData data_type
    ( TypeData[] types ) throws CompileException {
    return checked_data_type( (IntTypeData) types[0], (IntTypeData) types[1] );
  }

  public abstract TypeData checked_data_type
    ( IntTypeData a, IntTypeData b ) throws CompileException;
}
