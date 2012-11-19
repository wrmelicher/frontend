package frontend.functions;

import java.math.BigInteger;
import frontend.*;

public class BoolNot extends Function {
  public static final String NAME = "!";
  public BoolNot(){
    super(NAME, new Type[] { Type.BoolType } );
  }
  public abstract TypeData data_type
    ( TypeData[] types ) throws CompileException {
    return BoolData.not( (BoolData)types[0] );
  }
}
