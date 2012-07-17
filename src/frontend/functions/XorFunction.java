package frontend.functions;

import java.io.PrintStream;
import frontend.*;

public class XorFunction extends BinaryInt {
  public static final String NAME = "^";
  public XorFunction(){
    super( NAME );
  }
  public String op(){
    return "xor";
  }
  public TypeData data_type( IntTypeData a, IntTypeData b ){
    return IntTypeData.xor( a, b );
  }
}
