package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class LessThanFunction extends BinaryInt {
  public static final String NAME = "<";
  public LessThanFunction(){
    super(NAME,"lt");
  }
  public TypeData data_type( IntTypeData a, IntTypeData b ){
    return IntTypeData.lessthan( a, b );
  }
}
